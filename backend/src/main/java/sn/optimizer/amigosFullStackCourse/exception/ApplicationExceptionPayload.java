package sn.optimizer.amigosFullStackCourse.exception;


public record ApplicationExceptionPayload(String message, ErrorCode exception, int code,
                                          Object date, String path) {}
