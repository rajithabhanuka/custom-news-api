package org.comppress.customnewsapi.mapper;

import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.entity.Rating;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RatingMapper {

    RatingDto ratingToRatingDto(Rating rating);

}
