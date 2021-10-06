package org.comppress.customnewsapi.mapper;

import org.comppress.customnewsapi.dto.xml.ItemDto;
import org.comppress.customnewsapi.entity.Article;
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

}
