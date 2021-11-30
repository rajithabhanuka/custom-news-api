package org.comppress.customnewsapi.service.topic;

import org.comppress.customnewsapi.dto.CustomRatedArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.TopicDto;
import org.comppress.customnewsapi.entity.Topic;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.TopicRepository;
import org.comppress.customnewsapi.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(ArticleRepository articleRepository, TopicRepository topicRepository) {
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public ResponseEntity<GenericPage> getTopic(int page, int size, Long topicId, String lang, String fromDate, String toDate) {
        Page<ArticleRepository.CustomRatedArticle> articlePage = articleRepository.retrieveRatedArticlesByLangAndTopicId(lang, topicId, DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate), PageRequest.of(page,size));
        List<CustomRatedArticleDto> articleDtoList = new ArrayList<>();
        articlePage.forEach(a -> {
            CustomRatedArticleDto customRatedArticleDto = new CustomRatedArticleDto();
            getTopicsFromArticle(a, topicRepository, customRatedArticleDto);
            articleDtoList.add(customRatedArticleDto);
        });
        GenericPage genericPage = new GenericPage();
        BeanUtils.copyProperties(articlePage,genericPage);
        genericPage.setData(articleDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(genericPage);
    }

    public static void getTopicsFromArticle(ArticleRepository.CustomRatedArticle a, TopicRepository topicRepository, CustomRatedArticleDto customRatedArticleDto) {
        BeanUtils.copyProperties(a, customRatedArticleDto);
        List<Topic> list = topicRepository.retrieveByArticleId(a.getArticle_id());
        List<TopicDto> topicDtoList = new ArrayList<>();
        list.forEach(t -> {
            TopicDto topicDto = new TopicDto();
            BeanUtils.copyProperties(t,topicDto);
            topicDtoList.add(topicDto);
        });
        customRatedArticleDto.setTopicDtoList(topicDtoList);
    }
}
