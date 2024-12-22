package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.Photo;
import com.inha.os.econtentbackend.repository.PhotoRepository;
import com.inha.os.econtentbackend.service.PhotoService;
import com.inha.os.econtentbackend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {
    private final Base64.Decoder decoder;
    private final PhotoRepository photoRepository;

    @Override
    public Photo create(String base64Photo, String photoName) {
        String fileType = FileUtil.getFileExtension(photoName);
        Photo photo = Photo.builder()
                .content(decoder.decode(base64Photo))
                .type(fileType)
                .build();
        return photoRepository.save(photo);
    }
}
