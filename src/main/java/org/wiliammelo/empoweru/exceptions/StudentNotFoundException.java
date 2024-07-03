package org.wiliammelo.empoweru.exceptions;

public class StudentNotFoundException extends UserNotFoundException {

    public StudentNotFoundException() {
        super("Student not found");
    }

}
