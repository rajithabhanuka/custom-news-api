package org.comppress.customnewsapi.service.rating;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.entity.Rating;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.mapper.RatingMapper;
import org.comppress.customnewsapi.repository.RatingRepository;
import org.comppress.customnewsapi.repository.UserRepository;
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
    private final MapstructMapper mapstructMapper;
    private final RatingMapper ratingMapper;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, MapstructMapper mapstructMapper, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.mapstructMapper = mapstructMapper;
        this.ratingMapper = ratingMapper;
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

        // user should not be null
        Rating rating = ratingRepository.retrieveByUserNameAndArticleId(user.getUsername(),submitRatingDto.getArticleId());
        if(rating != null){
            // Todo update Rating and Save
            return ResponseEntity.status(HttpStatus.CREATED).body(mapstructMapper.ratingToRatingDto(rating));
        } else {
            rating = mapstructMapper.submitRatingDtoToRating(submitRatingDto);
            rating.setUserId(user.getId());
            rating = ratingRepository.save(rating);
        }

        RatingDto ratingDto = ratingMapper.ratingToRatingDto(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingDto);
    }

}
