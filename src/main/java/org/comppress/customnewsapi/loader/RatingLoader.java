package org.comppress.customnewsapi.loader;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.Rating;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.RatingRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class RatingLoader implements ApplicationRunner {

    private final RatingRepository ratingRepository;
    private final ArticleRepository articleRepository;

    public RatingLoader(RatingRepository ratingRepository, ArticleRepository articleRepository) {
        this.ratingRepository = ratingRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Article> articleList;
        if(articleRepository.findAll() == null || articleRepository.findAll().isEmpty()){
            log.info("No Articles in the Database, exiting RatingLoader now");
            return;
        } else {
            articleList = articleRepository.findAll();
        }
        int numberRandomRatings = 50;
        Random random = new Random();
        for (int i = 0; i < numberRandomRatings; i++) {
            Article article = articleList.get(random.nextInt(articleList.size()));
            Rating rating = new Rating().builder()
                    .rating1(random.nextInt(5) + 1)
                    .rating2(random.nextInt(5) + 1)
                    .rating3(random.nextInt(5) + 1)
                    .articleId(article.getId())
                    .build();

            ratingRepository.save(rating);
        }
    }
}
