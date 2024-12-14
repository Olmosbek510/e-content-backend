package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.response.MajorsGetResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.mapper.MajorMapper;
import com.inha.os.econtentbackend.repository.MajorRepository;
import com.inha.os.econtentbackend.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
    private MajorRepository majorRepository;
    private MajorMapper majorMapper;
    @Override
    public MajorsGetResponseDto getMajors() {
        List<Major> majors = majorRepository.findAll();
        return new MajorsGetResponseDto();
    }

//    private List<MajorResponseDto> setUpMajorDTOS(List<Major> majors) {
//        List<MajorResponseDto> majorResponseDtos = new LinkedList<>();
//        for (Major major : majors) {
//            majorResponseDtos.add(
//
//            )
//        }
//    }
}
