package org.comppress.customnewsapi.dto.xml;

import lombok.Data;

@Data
public class RssDto {

    public ChannelDto channel;
    public String content;
    public double version;
    public String text;

}
