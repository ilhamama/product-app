package com.ilham.products_app.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {
    @Min(value = 0)
    @Builder.Default
    private int page = 0;

    @Min(value = 1)
    @Max(value = 100)
    @Builder.Default
    private int size = 10;

    @Builder.Default
    private Sort.Direction sort = Sort.Direction.ASC;
}
