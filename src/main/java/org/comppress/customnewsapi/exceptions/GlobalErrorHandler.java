package org.comppress.customnewsapi.exceptions;

import org.comppress.customnewsapi.constant.ErrorCodes;
import org.comppress.customnewsapi.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .variable(ex.getVariable())
                .errorCode(ErrorCodes.CNA_001)
                .build());
    }

    @ExceptionHandler(CriteriaDoesNotExistException.class)
    public ResponseEntity<ErrorResponseDto> handleCriteriaDoesNotExistException(CriteriaDoesNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .variable(ex.getVariable())
                .errorCode(ErrorCodes.CNA_002)
                .build());
    }

    @ExceptionHandler(ArticleDoesNotExistException.class)
    public ResponseEntity<ErrorResponseDto> handleArticleDoesNotExistException(ArticleDoesNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .variable(ex.getVariable())
                .errorCode(ErrorCodes.CNA_003)
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //return super.handleNoHandlerFoundException(ex, headers, status, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto.builder()
                .message(ex.getMessage())
                .variable(ex.getLocalizedMessage())
                .errorCode(ErrorCodes.CNA_999)
                .build());
    }
}