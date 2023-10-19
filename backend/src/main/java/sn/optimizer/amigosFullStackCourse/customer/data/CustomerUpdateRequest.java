package sn.optimizer.amigosFullStackCourse.customer.data;

public record CustomerUpdateRequest(String email, Object patch, String type) {
}
