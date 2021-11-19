package org.comppress.customnewsapi.utils;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PageHolderUtils {

    public static ResponseEntity getResponseEntityGenericPage(int page, int size, List list) {
        PagedListHolder pagedListHolder = new PagedListHolder(list);
        pagedListHolder.setPageSize(size);  // number of items per page
        pagedListHolder.setPage(page);      // set to first page

        return ResponseEntity.status(HttpStatus.OK).body(
                GenericPage.builder()
                        .totalPages(pagedListHolder.getPageCount())
                        .totalElements(pagedListHolder.getSource().size())
                        .isFirst(pagedListHolder.isFirstPage())
                        .isLast(pagedListHolder.isLastPage())
                        .pageNumber(pagedListHolder.getPage())
                        .pageNumberOfElements(pagedListHolder.getPageSize())
                        .data(pagedListHolder.getPageList())
                        .build());
    }

}
