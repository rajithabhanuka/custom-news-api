package org.comppress.customnewsapi.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailSenderException extends Exception {

    private final String message;

}
