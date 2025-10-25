package com.manager.shared.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> items, int page, int size, long totalElements, int totalPages, boolean last) {
    public static <T> PageResponse<T> of(Page<T> p) {
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.isLast());
    }
}
