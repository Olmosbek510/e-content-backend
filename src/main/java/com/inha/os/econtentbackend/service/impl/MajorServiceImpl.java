package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.MajorCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorDeleteRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.*;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.exception.MajorAlreadyExistsException;
import com.inha.os.econtentbackend.exception.MajorNameAlreadyExistsException;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.mapper.MajorMapper;
import com.inha.os.econtentbackend.repository.MajorRepository;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;
    private final Base64.Encoder encoder;
    private final PhotoService photoService;
    private final Base64.Decoder decoder;

    @Override
    public List<MajorResponseDto> getMajors() {
        List<Major> majors = majorRepository.findAll();
        return setUpMajorDTOS(majors);
    }

    @Override
    public Major getById(Integer id) throws MajorNotFoundException {
        Optional<Major> optionalMajor = majorRepository.findMajorById(id);
        if (optionalMajor.isEmpty()) {
            throw new MajorNotFoundException("Major with id '%d' not found".formatted(id));
        }
        return optionalMajor.get();
    }

    @Override
    public Long getTotalMajorsCount() {
        return majorRepository.count();
    }

    @Override
    public Major save(Major major) throws MajorAlreadyExistsException {
        boolean existsMajorByName = majorRepository.existsMajorByName(major.getName());
        if (existsMajorByName) {
            throw new MajorAlreadyExistsException("major '%s' already exists".formatted(major.getName()));
        }
        return majorRepository.save(major);
    }

    @Override
    public MajorCreateResponseDto createMajor(MajorCreateRequestDto majorCreateDto) throws MajorAlreadyExistsException {
        Major major = Major.builder()
                .name(majorCreateDto.getName())
                .build();
        if (majorRepository.existsMajorByName(majorCreateDto.getName())) {
            throw new MajorAlreadyExistsException("Major '%s' already exists".formatted(majorCreateDto.getName()));
        }
        Major savedMajor = majorRepository.save(major);
        return MajorCreateResponseDto.builder().majorName(savedMajor.getName()).build();
    }

    @Override
    @Transactional
    public MajorUpdateResponseDto updateMajor(MajorUpdateRequestDto majorUpdateRequestDto) throws MajorNotFoundException, MajorNameAlreadyExistsException {
        Optional<Major> major = majorRepository.findById(majorUpdateRequestDto.getMajorId());
        if (major.isEmpty()) {
            throw new MajorNotFoundException("major with ID '%s' not found".formatted(majorUpdateRequestDto.getMajorId()));
        }
        Major oldMajor = major.get();
        if (!majorUpdateRequestDto.getName().isBlank() && !majorUpdateRequestDto.getName().equals(oldMajor.getName()) &&
                majorRepository.existsMajorByName(majorUpdateRequestDto.getName())
        ) {
            throw new MajorNameAlreadyExistsException("major name '%s' already exists".formatted(majorUpdateRequestDto.getName()));
        }
        if (!majorUpdateRequestDto.getName().isBlank()) {
            oldMajor.setName(majorUpdateRequestDto.getName());
        }
        Major updatedMajor = majorRepository.save(oldMajor);
        return MajorUpdateResponseDto.builder()
                .name(updatedMajor.getName())
                .build();
    }

    @Override
    public List<MajorNameResponseDto> getMajorNames() {
        List<Major> majors = majorRepository.findAll();
        List<MajorNameResponseDto> majorNameResponseDtos = new LinkedList<>();
        for (Major major : majors) {
            majorNameResponseDtos.add(majorMapper.toMajorNameResponseDto(major));
        }
        return majorNameResponseDtos;
    }

    @Override
    public MajorDeleteResponseDto deleteMajor(MajorDeleteRequestDto deleteRequestDto) throws MajorNotFoundException {
        Optional<Major> optionalMajor = majorRepository.findById(deleteRequestDto.getMajorId());
        if (optionalMajor.isEmpty()) {
            throw new MajorNotFoundException("Major with id '%s' not found".formatted(deleteRequestDto.getMajorId()));
        }
        MajorDeleteResponseDto response = MajorDeleteResponseDto.builder()
                .id(deleteRequestDto.getMajorId())
                .build();
        majorRepository.deleteById(deleteRequestDto.getMajorId());
        return response;
    }

    private List<MajorResponseDto> setUpMajorDTOS(List<Major> majors) {
        List<MajorResponseDto> majorResponseDtos = new LinkedList<>();
        for (Major major : majors) {
            MajorResponseDto majorResponseDTO = majorMapper.toMajorResponseDTO(major);
            majorResponseDtos.add(
                    majorResponseDTO
            );
        }
        return majorResponseDtos;
    }
}
