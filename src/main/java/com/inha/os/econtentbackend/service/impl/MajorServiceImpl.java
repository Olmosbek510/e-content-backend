package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.response.MajorResponseDto;
import com.inha.os.econtentbackend.dto.response.MajorsGetResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.mapper.MajorMapper;
import com.inha.os.econtentbackend.repository.MajorRepository;
import com.inha.os.econtentbackend.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;
    private final Base64.Encoder encoder;

    @Override
    public MajorsGetResponseDto getMajors() {
        List<Major> majors = majorRepository.findAll();
        List<MajorResponseDto> majorResponseDtos = setUpMajorDTOS(majors);
        MajorsGetResponseDto.builder()
                .majors(majorResponseDtos)
                .status(ResponseStatus.SUCCESS)
                .build();
        return new MajorsGetResponseDto();
    }

    private List<MajorResponseDto> setUpMajorDTOS(List<Major> majors) {
        List<MajorResponseDto> majorResponseDtos = new LinkedList<>();
        for (Major major : majors) {
            MajorResponseDto majorResponseDTO = majorMapper.toMajorResponseDTO(major);
            byte[] content = major.getPhoto().getContent();
            if (content != null) {
                majorResponseDTO.setPhoto(encoder.encodeToString(content));
            }
            majorResponseDtos.add(
                    majorResponseDTO
            );
        }
        return majorResponseDtos;
    }
}
