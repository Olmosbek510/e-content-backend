package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.SubjectCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectGetNamesRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.SubjectCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectNameResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectResponseDto;
import com.inha.os.econtentbackend.entity.Subject;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectAlreadyExistsException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionTcp;

import java.util.List;

public interface SubjectService {
    List<SubjectResponseDto> getSubjects(Integer majorId);

    Subject save(Subject operatingSystems) throws SubjectAlreadyExistsException;

    SubjectCreateResponseDto createSubject(SubjectCreateRequestDto requestDto) throws MajorNotFoundException, SubjectAlreadyExistsException;

    List<SubjectNameResponseDto> getSubjectNames(SubjectGetNamesRequestDto subjectGetNamesRequestDto);

    Subject findById(Integer subjectId) throws SubjectNotFoundExceptionHttp;

    String updateSubject(SubjectUpdateRequestDto requestDto) throws SubjectNotFoundExceptionTcp, SubjectAlreadyExistsException;

    String deleteById(Integer subjectId) throws SubjectNotFoundExceptionTcp;
}
