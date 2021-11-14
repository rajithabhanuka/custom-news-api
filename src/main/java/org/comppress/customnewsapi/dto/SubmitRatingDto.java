package org.comppress.customnewsapi.dto;

import lombok.Data;
import org.comppress.customnewsapi.entity.Criteria;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class SubmitRatingDto {
    private Long articleId;
    private List<CriteriaRatingDto> ratings;
}
