package org.comppress.customnewsapi.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PublisherDto {
    @JsonProperty("Publisher")
    public String publisher;
    @JsonProperty("Category")
    public String category;
    @JsonProperty("RSS Feed")
    public String rSSFeed;
}
