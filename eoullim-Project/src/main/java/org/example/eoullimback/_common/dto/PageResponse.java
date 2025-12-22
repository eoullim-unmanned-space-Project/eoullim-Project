package org.example.eoullimback._common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PageResponse {

    @Data
    public static class PageDTO<T, E> {
        private List<E> content;
        private int number;
        private int size;
        private int totalPages;
        private Long totalElements;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;

        Integer nextPageNumber;
        Integer previousPageNumber;
        List<PageLink> pageLinks;

        public PageDTO(Page<T> page, Function<T, E> mapper) {
            this.content = page.getContent().stream()
                    .map(mapper)
                    .toList();
            this.number = page.getNumber();
            this.size = page.getSize();
            this.totalPages = page.getTotalPages();
            this.totalElements = page.getTotalElements();
            this.first = page.isFirst();
            this.last = page.isLast();
            this.hasNext = page.hasNext();
            this.hasPrevious = page.hasPrevious();

            this.previousPageNumber = page.hasPrevious() ? page.getNumber() : null;
            this.nextPageNumber = page.hasNext() ? page.getNumber() + 2 : null;

            this.pageLinks = generatePageLinks(page);
        }

        private List<PageLink> generatePageLinks(Page<T> page) {
            List<PageLink> links = new ArrayList<>();

            int currentPage = page.getNumber() + 1;
            int totalPages = page.getTotalPages();
            int pageLinkSize = 5;

            //  현재 페이지 번호 5 인 상태
            //       3 4 [5] 6 7
            int startPage = currentPage - 2;
            int endPage = currentPage + 2;

            if (startPage < 1) {
                startPage = 1;
                endPage = Math.min(pageLinkSize, totalPages);
            }

            if (endPage > totalPages) {
                endPage = totalPages;
                startPage = Math.max(1, totalPages - pageLinkSize + 1);
            }

            for (int i = startPage; i <= endPage; i++) {
                PageLink link = new PageLink();
                link.setDisplayNumber(i);
                link.setActive(i == currentPage);
                links.add(link);
            }

            return links;
        }
    }

    @Data
    public static class PageLink {
        private int displayNumber;
        private boolean active;
    }
}