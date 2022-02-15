package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.dto.response.ResponseDto;
import org.comppress.customnewsapi.entity.Criteria;
import org.comppress.customnewsapi.service.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value ="/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ResponseDto> submitRating(@RequestBody SubmitRatingDto submitRatingDto, @RequestParam(required = false) String guid) throws Exception {
        return ratingService.submitRating(submitRatingDto,guid);
    }

    @GetMapping("/generate")
    public ResponseEntity<String> createRandomRatings(@RequestParam(defaultValue = "100")int numberRandomRatings) throws Exception {
        ratingService.createRandomRatings(100);
        return ResponseEntity.ok().body("Successfully generated " + numberRandomRatings + " random ratings");
    }
}
