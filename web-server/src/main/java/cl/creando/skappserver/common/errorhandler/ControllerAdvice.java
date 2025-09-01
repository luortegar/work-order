package cl.creando.skappserver.common.errorhandler;

import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.exception.TokenRefreshException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.Date;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(value = SKException.class)
    public ResponseEntity<ErrorMessage> handleSKException(SKException ex, WebRequest request) {
        logger.warn("SKException caught: {} - Path: {} - Status: {}",
                ex.getMessage(),
                request.getDescription(false),
                ex.getHttpStatus());

        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorMessage(
                ex.getHttpStatus().value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)));
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        logger.warn("ExpiredJwtException caught: {} - Path: {} - Status: {}",
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        logger.warn("TokenRefreshException caught: {} - Path: {}",
                ex.getMessage(),
                request.getDescription(false));

        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleSQLException(SQLException ex, WebRequest request) {
        logger.error("SQLException caught: {} - Path: {} - SQL State: {} - Error Code: {}",
                ex.getMessage(),
                request.getDescription(false),
                ex.getSQLState(),
                ex.getErrorCode(),
                ex);

        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Database error occurred",
                request.getDescription(false));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        logger.warn("BadCredentialsException caught: {} - Path: {}",
                ex.getMessage(),
                request.getDescription(false));

        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Invalid login credentials.",
                request.getDescription(false));
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleThrowable(Throwable ex, WebRequest request) {
        logger.error("Unexpected error caught: {} - Path: {} - Exception Type: {}",
                ex.getMessage(),
                request.getDescription(false),
                ex.getClass().getSimpleName(),
                ex);

        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "An unexpected error occurred",
                request.getDescription(false));
    }
}