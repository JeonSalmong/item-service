package apps.itemservice.web.exception.advice;

import lombok.extern.slf4j.Slf4j;
import apps.itemservice.web.dto.ErrorResult;
import apps.itemservice.platform.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Target all Controllers annotated with @RestController
//@ControllerAdvice(annotations = RestController.class)
// Target all Controllers within specific packages
//@ControllerAdvice("org.example.controllers")
// Target all Controllers assignable to specific classes
//@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
@Slf4j
@RestControllerAdvice
public class ExRestControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(606, e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult(909, e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(505, "내부 오류");
    }
}
