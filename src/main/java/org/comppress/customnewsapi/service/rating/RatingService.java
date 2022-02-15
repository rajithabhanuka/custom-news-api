package org.comppress.customnewsapi.service.rating;

import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingService {
    List<RatingDto> getRatings();
    ResponseEntity<ResponseDto> submitRating(SubmitRatingDto submitRatingDto, String guid) throws Exception;
    void createRandomRatings(int numberRandomRatings) throws Exception;
}
