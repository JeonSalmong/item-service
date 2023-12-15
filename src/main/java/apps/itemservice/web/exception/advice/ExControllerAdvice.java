package apps.itemservice.web.exception.advice;

import lombok.extern.slf4j.Slf4j;
import apps.itemservice.platform.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

// Target all Controllers annotated with @RestController
//@ControllerAdvice(annotations = RestController.class)
// Target all Controllers within specific packages
//@ControllerAdvice("org.example.controllers")
// Target all Controllers assignable to specific classes
//@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
@Slf4j
@ControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ModelAndView exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ModelAndView("error/500");
    }

    @ExceptionHandler(UserException.class)
    public ModelAndView ex(UserException e) {
        log.info("exception e", e);
        return new ModelAndView("error/404");
    }
}
