package club.gpn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({IllegalParameterException.class})
    public ResponseEntity<ErrorDto> handleIllegalParameter(Exception e) {
        return new ResponseEntity<>(new ErrorDto(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }
}
