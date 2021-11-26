package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private int pageNumber;
    @JsonProperty("current_page_size")
    private int size;
    @JsonProperty("data")
    private List<T> data;

}
