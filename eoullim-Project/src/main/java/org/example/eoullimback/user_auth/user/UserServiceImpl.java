package org.example.eoullimback.user_auth.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback._common.util.FileUtil;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
     */
    @Override
    @Transactional
    public void updateProfile(Long id, UserRequest.@Valid UpDateDTO update) {

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

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
}
