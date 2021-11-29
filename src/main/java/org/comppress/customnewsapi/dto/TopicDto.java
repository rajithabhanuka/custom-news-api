package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TopicDto {
    @JsonProperty(value = "topic_name")
    private String name;
}
