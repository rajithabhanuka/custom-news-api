package org.comppress.customnewsapi.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SubmitRatingDto {

    private Integer rating1;
    private Integer rating2;
    private Integer rating3;
    private String articleId;

}
