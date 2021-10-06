package org.comppress.customnewsapi.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RootDto {
    @JsonProperty("Publishers")
    public List<PublisherDto> publisherDtos;
}

