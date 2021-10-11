package org.comppress.customnewsapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class FileImportException extends RuntimeException{

    private final String message;
    private final String variable;

}
