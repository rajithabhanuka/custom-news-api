package org.comppress.customnewsapi.xmlModel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Date;
import java.util.List;

public class ChannelXmlModel {
    public String title;
    public String link;
    public String description;
    public String language;
    public Date pubDate;
    public Date lastBuildDate;
    public ImageXmlModel image;
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<ItemXmlModel> item;
}
