package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.OAuthProvider;
import org.example.eoullimback._common.enums.user.Role;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.*;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${tenco.key}")
    private String tencoKey;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    @Override
    @Transactional
    public User kakaoSocialLogin(String code) {
        UserResponse.OAuthToken oAuthToken = getKakaoAccessToken(code);

        UserResponse.KakaoProfile kakaoProfile = getKakaoProfile(oAuthToken.getAccessToken());

        User user = getOrCreateKakaoUser(kakaoProfile);

        return user;
    }

    /**
     * 카카오 엑세스 토큰 발급
     */
    private UserResponse.OAuthToken getKakaoAccessToken(String code) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", clientId);
        tokenParams.add("redirect_uri", "http://localhost:8080/user/kakao");
        tokenParams.add("code", code);
        tokenParams.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);

        ResponseEntity<UserResponse.OAuthToken> tokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                UserResponse.OAuthToken.class
        );
        UserResponse.OAuthToken oAuthToken = tokenResponse.getBody();

        return oAuthToken;
    }

    /**
     * 카카오프로필조회
     */
    private UserResponse.KakaoProfile getKakaoProfile(String accessToken) {

        RestTemplate profileRt = new RestTemplate();

        HttpHeaders  profileHeaders = new HttpHeaders();

        profileHeaders.add("Authorization",
                "Bearer " + accessToken);
        profileHeaders.add("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<UserResponse.KakaoProfile> profileResponse =
                profileRt.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.POST,
                        profileRequest,
                        UserResponse.KakaoProfile.class
                );

        UserResponse.KakaoProfile kakaoProfile = profileResponse.getBody();

        return kakaoProfile;
    }

    // 카카오사용자생성또는조회
    @Transactional
    public User getOrCreateKakaoUser(UserResponse.KakaoProfile kakaoProfile) {

        String kakaoId = kakaoProfile.getId().toString();
        String loginId = "KAKAO_" + kakaoId;
        String nickname = kakaoProfile.getProperties().getNickname();
        String email = loginId + "@kakao.com";
        String password = passwordEncoder.encode(tencoKey);

        User userOrigin = userRepository.findByLoginId(loginId).orElse(null);

        if (userOrigin == null) {

            User newUser = User.builder()
                    .loginId("KAKAO_" + kakaoId)
                    .name(nickname)
                    .email(email)
                    .password(password)
                    .provider(OAuthProvider.KAKAO)
                    .status(Status.ACTIVE)
                    .build();

            String profileImage = kakaoProfile.getProperties().getProfileImage();

            if (profileImage != null && !profileImage.isEmpty()) {
                newUser.setProfileImage(profileImage);
            }

            signupSocialUser(newUser);
            userOrigin = newUser;
        }

        return userOrigin;
    }

    /**
     * 화면: 프로필 정보
     */
    @Override
    public User getMyProfile(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 기능: 프로필 수정
     *
     * @return
     */
    @Override
    @Transactional
    public User updateProfile(Long id, UserRequest.UpDateDTO update) {

        update.validate();

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (!userEntity.canUpdateProfile()) {
            throw new Exception403(ErrorCode.SOCIAL_USER_CANNOT_UPDATE);
        }

        UserRequest.UpDateDTO finalDTO = update;

        if (update.getUserProfile() != null && !update.getUserProfile().isEmpty()) {
            if (!FileUtil.isImageFile(update.getUserProfile())) {
                throw new Exception400(ErrorCode.ONLY_FILE_IMG);
            }
            try {
                String newFileName = FileUtil.saveFile(update.getUserProfile());
                finalDTO = UserRequest.UpDateDTO.of(update, newFileName); // 파일명 채워서 새로 생성
            } catch (IOException e) {
                throw new Exception500(ErrorCode.INTERNAL_ERROR);
            }
        }

        userEntity.update(finalDTO);
        return userEntity;
    }

    /**
     * 기능: 프로필 삭제
     */
    @Override
    @Transactional
    public void deleteProfileImage(Long id) {

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));


        String profileImage = userEntity.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                FileUtil.deleteFile(profileImage);
            } catch (Exception e) {
                throw new Exception403(ErrorCode.ACCESS_DENIED);
            }
        }

        userEntity.clearProfileImage();
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * 기능: 회원 탈퇴
     */
    @Override
    @Transactional
    public void leaveUser(Long userId) {

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        userEntity.userWithdrawn(Status.WITHDRAWN);
    }

    public User getUsername(String name) {
        // 사용자 이름 여부만 조회하는 기능
        return userRepository.findByName(name).orElse(null);
    }

    public void signupSocialUser(User user) {
        userRepository.save(user);
        user.addRole(Role.USER);
    }

    /**
     * 비밀번호 검증
     *
     * @return
     */
    @Override
    public boolean verifyPassword(Long id, String password) {

        System.out.println(id);
        System.out.println(password);

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }

        return true;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public String findLoginIdByNameAndEmail(String name, String email) {

        User user = userRepository.findByNameAndEmailAndStatus(name, email, Status.ACTIVE)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (!user.isLocalUser()) {
            throw new Exception400(ErrorCode.SOCIAL_USER_CANNOT_FIND_LOGIN_ID);
        }

        return user.getLoginId();
    }

    @Override
    public User findByUserIdAndEmail(String userId, String email) {

        return userRepository.findByLoginIdAndEmail(userId, email)
                .orElse(null);
    }

    @Override
    @Transactional
    public void updatePassword(String userId, String newPassword) {
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));

    }

    @Override
    public List<UserResponse.AdminListDTO> getUserList() {

        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(UserResponse.AdminListDTO::new)
                .toList();
    }

    @Override
    public User findById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void suspendUser(Long userId, String reason) {

        User user = findById(userId);

        if (user.getStatus() == Status.WITHDRAWN) {
            throw new Exception400(ErrorCode.INVALID_INPUT);
        }

        if (user.getStatus() == Status.SUSPENDED) {
            return;
        }

        user.suspend(reason);
    }

    @Override
    @Transactional
    public void restoreUser(Long userId) {
        User user = findById(userId);

        if (user.getStatus() == Status.ACTIVE) {
            return;
        }

        user.restore();
    }
}
