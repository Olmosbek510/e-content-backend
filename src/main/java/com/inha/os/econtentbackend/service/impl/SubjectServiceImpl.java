package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.SubjectCreateRequestDto;
import com.inha.os.econtentbackend.dto.response.SubjectCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.Subject;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectAlreadyExistsException;
import com.inha.os.econtentbackend.repository.SubjectRepository;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
                    .id(subject.getId())
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
}
