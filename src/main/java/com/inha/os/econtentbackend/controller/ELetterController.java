package com.inha.os.econtentbackend.controller;


import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.entity.ELetter;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.ELetterAlreadyExistsException;
import com.inha.os.econtentbackend.exception.ELetterNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.service.ELetterService;
import com.inha.os.econtentbackend.uri.URIS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(URIS.ELetter.BASE_URI)
@RequiredArgsConstructor
public class ELetterController {
    private final ELetterService eLetterService;

    @PostMapping
    public ResponseEntity<String> createELetter(
            @ModelAttribute ELetterCreateRequestDto eLetterCreateRequestDto
    ) throws SubjectNotFoundExceptionHttp, ContentTypeNotFoundExceptionHttp, ELetterAlreadyExistsException, IOException {
        String eletter = eLetterService.createEletter(eLetterCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(eletter);
    }

    @GetMapping(URIS.ELetter.DOWNLOAD)
    public ResponseEntity<byte[]> downloadArticle(@PathVariable Integer id) throws ELetterNotFoundException {
        ELetter eLetter = eLetterService.findById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + eLetter.getTitle() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(eLetter.getContent().getData());
    }
}
