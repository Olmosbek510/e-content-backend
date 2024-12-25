package com.inha.os.econtentbackend.entity.interfaces;

public interface Actions {
    interface ELetter {
        String CREATE_E_LETTER = "CREATE_E_LETTER";
        String GET_E_LETTERS = "GET_E_LETTERS";
        String DELETE_E_LETTER = "DELETE_E_LETTER";
    }

    interface Article {
        String GET_ARTICLES = "GET_ARTICLES";
        String CREATE_ARTICLE = "CREATE_ARTICLE";
        String DELETE_ARTICLE = "DELETE_ARTICLE";
    }

    interface Books {
        String CREATE_BOOK = "CREATE_BOOK";
        String GET_BOOK_NAMES = "GET_BOOK_NAMES";
        String GET_BOOKS = "GET_BOOKS";
        String DELETE_BOOK = "DELETE_BOOK";
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
        String GET_SUBJECT_NAMES = "GET_SUBJECT_NAMES";
        String UPDATE_SUBJECT = "UPDATE_SUBJECT";
        String DELETE_SUBJECT = "DELETE_SUBJECT";
    }
}
