package org.comppress.customnewsapi.service.rating;

import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingService {

    List<RatingDto> getRatings();
    ResponseEntity<RatingDto> submitRating(SubmitRatingDto submitRatingDto, String token);

}
