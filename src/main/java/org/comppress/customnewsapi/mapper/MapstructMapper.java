package org.comppress.customnewsapi.mapper;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.comppress.customnewsapi.dto.RatingDto;
import org.comppress.customnewsapi.dto.SubmitRatingDto;
import org.comppress.customnewsapi.dto.xml.ItemDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapstructMapper {

    @Mapping(source = "link", target = "url")
    @Mapping(source = "enclosure.url", target = "urlToImage")
    @Mapping(source = "encoded", target = "content")
    @Mapping(source = "pubDate", target = "publishedAt")
    Article itemDtoToArticle(ItemDto itemDto);

    Rating submitRatingDtoToRating(SubmitRatingDto submitRatingDto);
    RatingDto ratingToRatingDto(Rating rating);
}
