package org.wiliammelo.empoweru.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("admin"),
    STUDENT("student"),
    PROFESSOR("professor");

    private final String role;
}
