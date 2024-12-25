package com.inha.os.econtentbackend.uri;

public interface URIS {
    interface Article {
        String BASE_URI = "/v1/api/articles";
        String DOWNLOAD = "{id}/download";
    }

    interface ELetter {
        String BASE_URI = "/v1/api/e-letters";
        String DOWNLOAD = "{id}/download";
    }

    interface Major {
        String BASE_URI = "/v1/api/majors";
    }

    interface Book {
        String BASE_URI = "/v1/api/books";
        String DOWNLOAD = "/{id}/download";
    }
}
