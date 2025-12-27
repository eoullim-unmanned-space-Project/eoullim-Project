package org.example.eoullimback._common.util;

import org.example.eoullimback.file.FileInfo;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room_image.RoomImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {

    public static String IMAGES_DIR = "images/";
    public static String ROOM_FILE_DIR = IMAGES_DIR + "roomImages/";

    public static String saveFile(MultipartFile file) throws IOException {
        return saveFile(file, IMAGES_DIR);
    }

    public static FileInfo saveRoomFile(MultipartFile file) throws IOException {
        return saveRoomFile(file, ROOM_FILE_DIR);
    }

    public static FileInfo saveRoomFile(MultipartFile file, String roomDir) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(roomDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("파일명이 존재하지 않습니다.");
        }

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + "_" + originalFilename;

        Path filePath = uploadPath.resolve(saveFileName);

        Files.copy(file.getInputStream(), filePath);

        return FileInfo.builder()
                .originalName(originalFilename)
                .storedName(saveFileName)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .filePath(filePath)
                .build();
    }

    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(IMAGES_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("파일명이 존재하지 않습니다.");
        }

        String uuid = UUID.randomUUID().toString();
        String savedFileName = uuid + "_" + originalFilename;

        Path filePath = uploadPath.resolve(savedFileName);

        Files.copy(file.getInputStream(), filePath);

        return savedFileName;
    }

    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();

        return  contentType != null && contentType.startsWith("image/");
    }

    public static void deleteFile(String filename) throws IOException {
        deleteFile(filename, IMAGES_DIR);
    }

    public static void deleteFile(String filename, String uploadDir) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        Path filePath = Paths.get(uploadDir, filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
