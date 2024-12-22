package com.inha.os.econtentbackend.entity.interfaces;

public interface Actions {
    interface Books {
        String CREATE_BOOK = "CREATE_BOOK";
    }

    interface Statistics {
        String GET_STATISTICS = "GET_STATISTICS";
    }

    interface Majors {
        String GET_MAJORS = "GET_MAJORS";
        String ADD_MAJOR = "ADD_MAJOR";
        String UPDATE_MAJOR = "UPDATE_MAJOR";
        String GET_MAJOR_NAMES = "GET_MAJOR_NAMES";
        String DELETE_MAJOR = "DELETE_MAJOR";
    }

    interface Student {
        String CREATE_STUDENT = "CREATE_STUDENT";
        String GET_PROFILE = "GET_PROFILE";
        String GET_MAJORS = "GET_MAJORS";
    }

    interface Authorization {
        String LOGIN = "LOGIN";
    }

    interface Subject {
        String GET_SUBJECTS = "GET_SUBJECTS";
        String ADD_SUBJECT = "ADD_SUBJECT";
    }
}
