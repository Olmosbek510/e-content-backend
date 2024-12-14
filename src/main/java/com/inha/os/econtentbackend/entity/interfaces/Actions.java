package com.inha.os.econtentbackend.entity.interfaces;

public interface Actions {
    interface Majors {
        String GET_MAJORS = "GET_MAJORS";
    }

    interface Student {
        String CREATE_STUDENT = "CREATE_STUDENT";
        String GET_PROFILE = "GET_PROFILE";
        String GET_MAJORS = "GET_MAJORS";
    }
    interface Authorization{
        String LOGIN = "LOGIN";
    }
}
