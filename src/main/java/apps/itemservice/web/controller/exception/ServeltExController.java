package apps.itemservice.web.controller.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import apps.itemservice.core.exception.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServeltExController {

    @GetMapping("/error/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");
    }

    @GetMapping("/error/error-user")
    public void errorUser() {
        throw new UserException("사용자 오류");
    }

    @GetMapping("/error/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }

    @GetMapping("/error/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
