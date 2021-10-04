package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "channel")
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
