package org.example.eoullimback._common.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record PageDTO<T>(
        List<T> content,
        int number,
        int size,
        int totalPages,
        Long totalElements,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious,

        Integer nextPageNumber,
        Integer previousPageNumber,
        List<PageLink> pageLinks
) {
    public static <T, E> PageDTO<T> from(Page<E> page, Function<E, T> mapper) {
        return new PageDTO<>(
                page.getContent().stream()
                        .map(mapper)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                page.hasNext() ? page.getNumber() : null,
                page.hasPrevious() ? page.getNumber() + 2 : null,
                generatePageLinks(page)
        );
    }

    public static <E> List<PageLink> generatePageLinks(Page<E> page) {
        List<PageLink> links = new ArrayList<>();

        int currentPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();
        int pageLinkSize = 5;

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
            links.add(new PageLink(i, i == currentPage));
        }

        return links;
    }
}
