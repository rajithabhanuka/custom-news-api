package org.comppress.customnewsapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecordNotFoundException extends RuntimeException{

    private String message;
    private String variable;

}
