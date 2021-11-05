package org.comppress.customnewsapi.mapper;

import org.comppress.customnewsapi.dto.TwitterArticleDto;
import org.comppress.customnewsapi.entity.TwitterTweet;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TwitterMapper {

    TwitterArticleDto twitterArticleToTwitterArticleDto(TwitterTweet twitterTweet);

}
