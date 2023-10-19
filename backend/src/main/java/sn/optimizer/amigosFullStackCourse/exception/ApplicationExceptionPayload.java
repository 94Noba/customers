package sn.optimizer.amigosFullStackCourse.exception;

import java.time.LocalDateTime;

public record ApplicationExceptionPayload(String message, ErrorCode exception, int code,
                                          LocalDateTime date, String path) {}
