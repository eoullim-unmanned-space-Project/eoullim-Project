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
    public User getMyProfile(Long userId) {
        return  userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 기능: 프로필 수정
     */
    @Override
    @Transactional
    public void updateProfile(Long userId, UserRequest.@Valid UpDateDTO upDateDTO) {

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        UserRequest.UpDateDTO finalDTO = upDateDTO;

        if (upDateDTO.userProfile() != null && !upDateDTO.userProfile().isEmpty()) {
            if (!FileUtil.isImageFile(upDateDTO.userProfile())) {
                throw new Exception400(ErrorCode.ONLY_FILE_IMG);
            }
            try {
                String newFileName = FileUtil.saveFile(upDateDTO.userProfile());
                finalDTO = UserRequest.UpDateDTO.of(upDateDTO, newFileName); // 파일명 채워서 새로 생성
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
    public void deleteProfileImage(Long userId) {

        User userEntity = userRepository.findById(userId)
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
