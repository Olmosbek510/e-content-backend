package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.MajorCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorDeleteRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.*;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.exception.MajorAlreadyExistsException;
import com.inha.os.econtentbackend.exception.MajorNameAlreadyExistsException;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;

import java.util.List;

public interface MajorService {
    List<MajorResponseDto> getMajors();

    Major getById(Integer id) throws MajorNotFoundException;

    Long getTotalMajorsCount();

    Major save(Major build) throws MajorAlreadyExistsException;

    MajorCreateResponseDto createMajor(MajorCreateRequestDto majorCreateDto) throws MajorAlreadyExistsException;

    MajorUpdateResponseDto updateMajor(MajorUpdateRequestDto majorUpdateRequestDto) throws MajorNotFoundException, MajorNameAlreadyExistsException;

    List<MajorNameResponseDto> getMajorNames();

    MajorDeleteResponseDto deleteMajor(MajorDeleteRequestDto deleteRequestDto) throws MajorNotFoundException;
}
