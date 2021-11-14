package org.comppress.customnewsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingSumDto {
    Long articleId;
    double criteriaRatingSum1;
    double criteriaRatingSum2;
    double criteriaRatingSum3;
    double ratingSum;
}