package org.comppress.customnewsapi.service.rating;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.comppress.customnewsapi.dto.CriteriaRatingDto;
import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.Criteria;
import org.comppress.customnewsapi.entity.Rating;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.repository.*;
import org.comppress.customnewsapi.service.criteria.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final CriteriaRepository criteriaRepository;
    private final ArticleRepository articleRepository;

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
    public ResponseEntity<RatingDto> submitRating(SubmitRatingDto submitRatingDto, String token) throws Exception {
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
                        .rating(criteriaRating.getRating())
                        .articleId(submitRatingDto.getArticleId())
                        .criteriaId(criteriaRating.getCriteriaId())
                        .userId(user.getId())
                        .build();
                ratingRepository.save(newRating);
            }
        }
        Optional<Article> article = articleRepository.findById(submitRatingDto.getArticleId());
        if(article.isPresent()){
            Integer countRatings = article.get().getCountRatings();
            if(countRatings == null) countRatings = 0;
            countRatings = countRatings + 1;
            article.get().setCountRatings(countRatings);
            articleRepository.save(article.get());
        }else{
            throw new Exception("Article does not Exist! Cant Increment Rating for Article");
        }

        // TODO return State of the DB, or saved state
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @Override
    public void createRandomRatings(int numberRandomRatings) throws Exception {
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWNhcyIsImV4cCI6MTYzNzUyODU4NCwiaWF0IjoxNjM2OTIzNzg0fQ.MdIl2aCajN3fqYBExvHvONgbhP31pXRK2tmO1VMSKQkpTODnvJ6ua9zA_8RTDQwHZwCj2FhGjAZRO30GoJTCPg";
        Random random = new Random();
        for(Article article:articleRepository.retrieveRandomArticles(numberRandomRatings)){
            SubmitRatingDto submitRatingDto = new SubmitRatingDto();
            List<CriteriaRatingDto> criteriaRatingDtoList = new ArrayList<>();
            for(Criteria criteria:criteriaRepository.findAll()){
                CriteriaRatingDto criteriaRatingDto = new CriteriaRatingDto();
                criteriaRatingDto.setRating(random.nextInt(5) + 1);
                criteriaRatingDto.setCriteriaId(criteria.getId());
                criteriaRatingDtoList.add(criteriaRatingDto);
            }
            submitRatingDto.setRatings(criteriaRatingDtoList);
            submitRatingDto.setArticleId(article.getId());
            submitRating(submitRatingDto, token);
        }
    }

}
