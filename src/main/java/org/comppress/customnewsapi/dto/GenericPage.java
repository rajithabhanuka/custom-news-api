package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GenericPage<T> {

    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_elements")
    private long totalElements;
    @JsonProperty("is_first")
    private boolean isFirst;
    @JsonProperty("is_last")
    private boolean isLast;
    @JsonProperty("current_page")
    private int number;
    @JsonProperty("current_page_size")
    private int numberOfElements;
    @JsonProperty("data")
    private List<T> data;

}
