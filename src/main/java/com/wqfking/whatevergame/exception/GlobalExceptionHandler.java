package com.wqfking.whatevergame.exception;

import java.net.URI;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Converts API and MVC exceptions to the project's Problem Details format. */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> handleApiException(ApiException ex, HttpServletRequest request) {
        ProblemDetail problem = problem(ex.getStatus(), ex.getMessage(), ex.getCode(), request);
        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex, HttpServletRequest request) {
        ProblemDetail problem = problem(
                HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误", "INTERNAL_ERROR", request);
        log.error("Unhandled API error, traceId={}", problem.getProperties().get("traceId"), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = body instanceof ProblemDetail detail
                ? detail
                : ProblemDetail.forStatusAndDetail(status, "请求无法处理");
        if (status.is5xxServerError()) {
            problem.setDetail("服务器内部错误");
        }
        problem.setProperty("code", codeFor(ex, status));
        String traceId = UUID.randomUUID().toString();
        problem.setProperty("traceId", traceId);
        if (request instanceof ServletWebRequest servletRequest) {
            problem.setInstance(URI.create(servletRequest.getRequest().getRequestURI()));
        }
        if (status.is5xxServerError()) {
            log.error("Spring MVC error, traceId={}", traceId, ex);
        }
        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private static String codeFor(Exception ex, HttpStatusCode status) {
        if (ex instanceof MethodArgumentNotValidException
                || ex instanceof HandlerMethodValidationException) {
            return "VALIDATION_FAILED";
        }
        if (ex instanceof HttpMessageNotReadableException) {
            return "MALFORMED_JSON";
        }
        if (status.value() == 404) {
            return "RESOURCE_NOT_FOUND";
        }
        return status.is5xxServerError() ? "INTERNAL_ERROR" : "INVALID_REQUEST";
    }

    private static ProblemDetail problem(
            HttpStatusCode status, String detail, String code, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", code);
        problem.setProperty("traceId", UUID.randomUUID().toString());
        return problem;
    }
}
