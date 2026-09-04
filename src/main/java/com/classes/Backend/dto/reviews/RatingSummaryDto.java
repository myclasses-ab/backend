package com.classes.Backend.dto.reviews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummaryDto {
    private BigDecimal averageRating;
    private Long totalReviews;
    private BigDecimal googleRating;
    private Long googleRatingCount;
}
