package org.comppress.customnewsapi.dto;

import lombok.Data;

@Data
public class TopNewsFeedDto {

    private String url;
    private Long publisherId;
    private String lang;

}
