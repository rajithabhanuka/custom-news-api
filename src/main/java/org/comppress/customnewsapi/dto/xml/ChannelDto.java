package org.comppress.customnewsapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChannelDto {
    public String title;
    public String link;
    public String description;
    public String language;
    public Date pubDate;
    public Date lastBuildDate;
    public ImageDto image;
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<ItemDto> item;
}
