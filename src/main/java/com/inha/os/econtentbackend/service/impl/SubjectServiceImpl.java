package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.SubjectCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectGetNamesRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.SubjectCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectNameResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.Subject;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectAlreadyExistsException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionTcp;
import com.inha.os.econtentbackend.repository.SubjectRepository;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final MajorService majorService;

    @Override
    public List<SubjectResponseDto> getSubjects(Integer majorId) {
        List<Subject> subjects = subjectRepository.findAllByMajorId(majorId);
        List<SubjectResponseDto> subjectResponseDtos = new LinkedList<>();

        for (Subject subject : subjects) {
            subjectResponseDtos.add(SubjectResponseDto.builder()
                    .subjectId(subject.getId())
                    .name(subject.getName())
                    .totalMaterialsCount(subjectRepository.getTotalMaterialsById(subject.getId()))
                    .build());
        }
        return subjectResponseDtos;
    }

    @Override
    public Subject save(Subject subject) throws SubjectAlreadyExistsException {
        boolean existsSubjectByName = subjectRepository.existsSubjectByName(subject.getName());
        if (existsSubjectByName) {
            throw new SubjectAlreadyExistsException("subject '%s' already exists");
        }
        return subjectRepository.save(subject);
    }

    @Override
    public SubjectCreateResponseDto createSubject(SubjectCreateRequestDto requestDto) throws
            MajorNotFoundException,
            SubjectAlreadyExistsException {
        if (subjectRepository.existsSubjectByName(requestDto.getName())) {
            throw new SubjectAlreadyExistsException("Subject '%s' already exists".formatted(requestDto.getName()));
        }
        Major major = majorService.getById(requestDto.getMajorId());
        Subject subject = Subject.builder()
                .major(major)
                .name(requestDto.getName())
                .build();
        Subject savedSubject = subjectRepository.save(subject);
        return SubjectCreateResponseDto.builder()
                .name(savedSubject.getName())
                .id(savedSubject.getId())
                .build();
    }

    @Override
    public List<SubjectNameResponseDto> getSubjectNames(SubjectGetNamesRequestDto subjectGetNamesRequestDto) {
        List<Subject> subjects = subjectRepository.findAllByMajorId(subjectGetNamesRequestDto.getMajorId());
        List<SubjectNameResponseDto> subjectNameResponseDtos = new LinkedList<>();
        for (Subject subject : subjects) {
            subjectNameResponseDtos.add(SubjectNameResponseDto.builder()
                    .name(subject.getName())
                    .id(subject.getId())
                    .build());
        }
        return subjectNameResponseDtos;
    }

    @Override
    public Subject findById(Integer subjectId) throws SubjectNotFoundExceptionHttp {
        Optional<Subject> subjectById = subjectRepository.findSubjectById(subjectId);
        if (subjectById.isEmpty()) {
            throw new SubjectNotFoundExceptionHttp("Subject with id '%s' not found".formatted(subjectById));
        }
        return subjectById.get();
    }

    @Override
    @Transactional
    public String updateSubject(SubjectUpdateRequestDto requestDto) throws SubjectNotFoundExceptionTcp, SubjectAlreadyExistsException {
        Optional<Subject> optionalSubject = subjectRepository.findById(requestDto.getSubjectId());
        if (optionalSubject.isEmpty()) {
            throw new SubjectNotFoundExceptionTcp("Subject with id '%s' not found".formatted(requestDto.getSubjectId()));
        }
        Subject subject = optionalSubject.get();
        if (subjectRepository.existsSubjectByName(requestDto.getName()) && !subject.getName().equals(requestDto.getName())) {
            throw new SubjectAlreadyExistsException("Subject with name '%s' already exists".formatted(requestDto.getName()));
        }
        subject.setName(requestDto.getName());
        subjectRepository.save(subject);
        return "Subject updated successfully";
    }

    @Override
    public String deleteById(Integer subjectId) throws SubjectNotFoundExceptionTcp {
        if (!subjectRepository.existsById(subjectId)) {
            throw new SubjectNotFoundExceptionTcp("subject not found");
        }
        subjectRepository.deleteById(subjectId);
        return "Subject deleted successfully";
    }
}
