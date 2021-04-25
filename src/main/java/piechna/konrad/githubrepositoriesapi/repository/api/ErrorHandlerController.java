package piechna.konrad.githubrepositoriesapi.repository.api;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}