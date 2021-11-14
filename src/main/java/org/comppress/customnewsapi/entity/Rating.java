package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.comppress.customnewsapi.dto.RatingDto;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends AbstractEntity{

    private Long criteriaId;
    private Long userId;
    private Long articleId;
    private Integer rating;

    public RatingDto toDto(){
        RatingDto ratingDto = new RatingDto();
        BeanUtils.copyProperties(this, ratingDto);
        return ratingDto;
    }

}
