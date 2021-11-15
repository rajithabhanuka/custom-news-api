package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.comppress.customnewsapi.utils.Transformer;

@Getter
@Setter
public class UserDto implements Transformer {
    private String name;
    private String email;
    private String country;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String listCategoryIds;
    private String listPublisherIds;

}
