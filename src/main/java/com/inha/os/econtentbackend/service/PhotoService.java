package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.entity.Photo;

public interface PhotoService {
    Photo create(String base64Photo, String photoName);
}
