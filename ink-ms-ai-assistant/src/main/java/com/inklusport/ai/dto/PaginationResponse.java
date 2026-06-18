package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Boolean first;
    private Boolean last;

    public static <T> PaginationResponse<T> of(
            List<T> content, 
            Integer page, 
            Integer size, 
            Long totalElements) {
        return PaginationResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .first(page == 0)
                .last(page >= (int) Math.ceil((double) totalElements / size) - 1)
                .build();
    }
}