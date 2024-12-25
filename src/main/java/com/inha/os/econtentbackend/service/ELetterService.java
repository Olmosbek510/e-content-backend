package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ELetterGetResponseDto;
import com.inha.os.econtentbackend.dto.request.ElettersGetRequestDto;
import com.inha.os.econtentbackend.entity.Article;
import com.inha.os.econtentbackend.entity.ELetter;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.ELetterAlreadyExistsException;
import com.inha.os.econtentbackend.exception.ELetterNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;

import java.io.IOException;
import java.util.List;

public interface ELetterService {
    Long getTotalElettersCount();

    String createEletter(ELetterCreateRequestDto eLetterCreateRequestDto) throws SubjectNotFoundExceptionHttp, ContentTypeNotFoundExceptionHttp, IOException, ELetterAlreadyExistsException;

    List<ELetterGetResponseDto> getELetters(ElettersGetRequestDto requestDto);

    String deleteById(Integer eLetterId);

    ELetter findById(Integer id) throws ELetterNotFoundException;
}
