package org.comppress.customnewsapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.comppress.customnewsapi.dto.SubmitRatingDto;

@Builder
@Data
public class CreateRatingResponseDto implements ResponseDto{
    private String message;
    @JsonProperty(value = "submit_rating")
    private SubmitRatingDto submitRatingDto;
}
