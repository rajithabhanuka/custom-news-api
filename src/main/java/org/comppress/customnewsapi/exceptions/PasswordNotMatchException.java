package org.comppress.customnewsapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PasswordNotMatchException extends RuntimeException{

    private String message;
    private String variable;

}
