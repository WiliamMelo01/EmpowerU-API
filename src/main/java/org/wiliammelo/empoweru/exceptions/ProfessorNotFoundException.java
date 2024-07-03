package org.wiliammelo.empoweru.exceptions;

public class ProfessorNotFoundException extends UserNotFoundException {

    public ProfessorNotFoundException() {
        super("Professor not found");
    }

}
