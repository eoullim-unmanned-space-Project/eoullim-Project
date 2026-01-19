package org.example.eoullimback.qna;

import lombok.Data;
import org.example.eoullimback._common.util.DateTimeUtil;

import java.util.List;

public class QnaResponse {

    @Data
    public static class DetailDTO{

        private final Long id;
        private final String title;
        private final String content;
        private final Long userId;
        private final String name;
        private final Long viewCount;
        private final String createdAt;

        public DetailDTO(Qna qna) {
            this.id = qna.getId();
            this.title = qna.getTitle();
            this.content = qna.getContent();
            this.userId = qna.getUser() != null ? qna.getUser().getId() : null;
            this.name = qna.getUser() != null ? qna.getUser().getName() : null;
            this.viewCount = qna.getViewCount();
            this.createdAt = DateTimeUtil.toKstString(qna.getCreatedAt());
        }
    }

    @Data
    public static class ListDTO{
        private Long id;
        private String title;
        private String name;
        private Long viewCount;
        private String createdAt;

        public ListDTO(Qna qna) {
            this.id = qna.getId();
            this.title = qna.getTitle();
            this.name = qna.getUser() != null ? qna.getUser().getName() : null;
            this.viewCount = qna.getViewCount();
            this.createdAt = DateTimeUtil.toKstString(qna.getCreatedAt());
        }
    }

    @Data
    public static class ListPageDTO {
        private final List<ListDTO> list;

        public ListPageDTO(List<ListDTO> list) {
            this.list = list;
        }
    }

    @Data
    public static class UpdateFormDTO{
        private Long id;
        private String title;
        private String content;
        private String name;
        private String updatedAt;

        public UpdateFormDTO(Qna qna) {
            this.id = qna.getId();
            this.title = qna.getTitle();
            this.content = qna.getContent();
            this.name = qna.getUser() != null ? qna.getUser().getName() : null;
            this.updatedAt = DateTimeUtil.toKstString(qna.getUpdatedAt());
        }
    }
}
