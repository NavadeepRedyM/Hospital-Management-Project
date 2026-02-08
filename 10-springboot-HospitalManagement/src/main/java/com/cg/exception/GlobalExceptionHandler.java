import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        logger.error("Resource not found: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/resourceNotFound"; // Thymeleaf view name
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Model model) {
        logger.error("Validation error: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/validationError"; // Thymeleaf view name
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        logger.error("Runtime exception occurred: " + ex.getMessage());
        model.addAttribute("errorMessage", "An internal error occurred. Please try again later.");
        return "error/internalError"; // Thymeleaf view name
    }
}