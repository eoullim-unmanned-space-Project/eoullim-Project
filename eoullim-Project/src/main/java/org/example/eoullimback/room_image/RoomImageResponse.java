package org.example.eoullimback.room_image;

import lombok.Data;

public class RoomImageResponse {

    @Data
    public static class DetailDTO{
        String originalName;
        String storedName;
        String contentType;
        Long fileSize;
        String filePath;
        int displayOrder;

        public DetailDTO(RoomImage image) {
            this.originalName = image.getOriginalName();
            this.storedName = image.getStoredName();
            this.contentType = image.getContentType();
            this.fileSize = image.getFileSize();
            this.filePath = image.getFilePath();
            this.displayOrder = image.getDisplayOrder();
        }
    }
}
