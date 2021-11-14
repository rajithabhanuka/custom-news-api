package org.comppress.customnewsapi.service.rating;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.comppress.customnewsapi.dto.CriteriaRatingDto;
import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.entity.Criteria;
import org.comppress.customnewsapi.entity.Rating;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.mapper.RatingMapper;
import org.comppress.customnewsapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final CriteriaRepository criteriaRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, CriteriaRepository criteriaRepository, ArticleRepository articleRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.criteriaRepository = criteriaRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<RatingDto> getRatings() {
        List<Rating> ratingList = ratingRepository.findAll();
        List<RatingDto> ratingDtoList = new ArrayList<>();
        ratingList.stream().map(rating -> ratingDtoList.add(rating.toDto()));
        return ratingDtoList;
    }

    @Override
    public ResponseEntity<RatingDto> submitRating(SubmitRatingDto submitRatingDto, String token) {
        // Get User from JWT?
        UserEntity user = null;
        if(token != null){
            token = token.replace("Bearer", "");
            int i = token.lastIndexOf('.');
            String withoutSignature = token.substring(0, i+1);
            Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
            user = userRepository.findByUsername(untrusted.getBody().getSubject());
            if(user == null) throw new RuntimeException("User is Null");
        }else{
            throw new RuntimeException("JWT Token is Null");
        }

        for(CriteriaRatingDto criteriaRating:submitRatingDto.getRatings()){
            // TODO Why existBy does not work for JPA?
            if(!criteriaRepository.existsById(criteriaRating.getCriteriaId()) ||
            !articleRepository.existsById(submitRatingDto.getArticleId())){
                // TODO Add custom Exception and Logging
                throw new RuntimeException("Wrong Article or Criteria Id");
            }
            Rating rating = ratingRepository.findByUserIdAndArticleIdAndCriteriaId(user.getId(),
                    submitRatingDto.getArticleId(),
                    criteriaRating.getCriteriaId());
            if(rating != null){
                rating.setRating(criteriaRating.getRating());
                ratingRepository.save(rating);
            } else {
                Rating newRating = Rating.builder()
                        .articleId(submitRatingDto.getArticleId())
                        .criteriaId(criteriaRating.getCriteriaId())
                        .userId(user.getId())
                        .build();
                ratingRepository.save(newRating);
            }
        }
        // TODO return State of the DB, or saved state
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
