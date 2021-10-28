package org.comppress.customnewsapi.entity;

import lombok.Data;
import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.RatingDto;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

@Data
@Entity
public class Rating extends AbstractEntity{

    private Integer rating1;
    private Integer rating2;
    private Integer rating3;
    private Long userId;
    private Long articleId;

    public RatingDto toDto(){
        RatingDto ratingDto = new RatingDto();
        BeanUtils.copyProperties(this, ratingDto);
        return ratingDto;
    }

}
