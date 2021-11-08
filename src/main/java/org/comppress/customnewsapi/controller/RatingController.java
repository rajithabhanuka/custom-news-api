package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.service.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/submit")
    public ResponseEntity<RatingDto> submitRating(@RequestBody SubmitRatingDto submitRatingDto, @RequestHeader(name="Authorization") String token){
        return ratingService.submitRating(submitRatingDto, token);
    }

}
