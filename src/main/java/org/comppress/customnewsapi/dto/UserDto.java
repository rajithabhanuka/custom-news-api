package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.comppress.customnewsapi.utils.Transformer;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto implements Transformer {
    private String name;
    private String email;
    private String username;
    @Size(min = 8, message = "Confirm password must be at least 8 characters")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+"),
            @Pattern(regexp = "(?=.*[a-z]).+"),
            @Pattern(regexp = "(?=.*[A-Z]).+"),
            //@Pattern(regexp = "(?=.[!@#\\$%\\^&\\*]).+"),
            @Pattern(regexp = "(?=\\S+$).+")
    })
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(value = "category_ids")
    private String listCategoryIds;
    @JsonProperty(value = "publisher_ids")
    private String listPublisherIds;

}
