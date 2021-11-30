package org.comppress.customnewsapi.service.twitter;

import org.comppress.customnewsapi.dto.CustomRatedArticleDto;
import org.comppress.customnewsapi.dto.TwitterArticleDto;
import org.springframework.http.ResponseEntity;
import twitter4j.TwitterException;

public interface TwitterService {

    ResponseEntity<TwitterArticleDto> getTwitterArticle(Long id) throws TwitterException;
    void getTweetDetails();
    void setReplyCount(CustomRatedArticleDto articleDto);

}
