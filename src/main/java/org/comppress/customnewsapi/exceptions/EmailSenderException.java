package org.comppress.customnewsapi.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailSenderException extends Exception{

    private String message;

}
