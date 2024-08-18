package io.oigres.ecomm.service.orders.web;

import io.oigres.ecomm.service.orders.domain.DomainException;
import io.oigres.ecomm.service.orders.model.CreateCartException;
import io.oigres.ecomm.service.orders.model.StockTimeOutException;
import io.oigres.ecomm.service.products.model.CommonErrorHandlerResponse;
import io.oigres.ecomm.service.products.model.exception.NoStockException;
import io.oigres.ecomm.service.products.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private BasicErrorController basicErrorController;

    public GlobalExceptionHandler(BasicErrorController basicErrorController) {
        super();
        this.basicErrorController = basicErrorController;
    }

    protected ResponseEntity<Object> customHandleException(HttpStatus status, Exception ex, HttpServletRequest request) {
        LOGGER.debug(String.format("Handling exception %s, it will response status %s (%s) ", ex.getClass().getSimpleName(), status.getReasonPhrase(), status.value()));
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value());
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, ex);
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, request.getRequestURI());
        ResponseEntity<Map<String, Object>> error = this.basicErrorController.error(request);
        return super.handleExceptionInternal(ex, error.getBody(), new HttpHeaders(), status, new ServletWebRequest(request));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.BAD_REQUEST, ex, request);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.BAD_REQUEST, ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.info(String.format("Handling exception %s, it will response status %s (%s) ", ex.getClass().getSimpleName(), status.getReasonPhrase(), status.value()));
        List<String> errorList = new ArrayList<>();
        List<ObjectError> exceptionMessage = ex.getBindingResult().getAllErrors();
        for (ObjectError error : exceptionMessage) {
            errorList.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(
                CommonErrorHandlerResponse.builder()
                        .error(status.getReasonPhrase())
                        .status(status.value())
                        .message(errorList.toString())
                        .path("")
                        .build(),
                status);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, JpaObjectRetrievalFailureException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.BAD_REQUEST, ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null && request instanceof ServletWebRequest) {
            return customHandleException(status, ex, ((ServletWebRequest)request).getRequest());
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomainException(DomainException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(NoStockException.class)
    public ResponseEntity<Object> handleNoStockException(NoStockException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.NOT_ACCEPTABLE, ex, request);
    }

    @ExceptionHandler(CreateCartException.class)
    public ResponseEntity<Object> handleNoStockException(CreateCartException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(StockTimeOutException.class)
    public ResponseEntity<Object> handleStockTimeOutException(StockTimeOutException ex, HttpServletRequest request) {
        return customHandleException(HttpStatus.CONFLICT, ex, request);
    }

}
