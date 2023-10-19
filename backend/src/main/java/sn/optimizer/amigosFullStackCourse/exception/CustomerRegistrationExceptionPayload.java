package sn.optimizer.amigosFullStackCourse.exception;

import sn.optimizer.amigosFullStackCourse.customer.validator.ValidationResult;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerRegistrationExceptionPayload(String message, ErrorCode exception,
                                                   int code, List<ValidationResult> validationResult,
                                                   LocalDateTime date, String path) {
}
