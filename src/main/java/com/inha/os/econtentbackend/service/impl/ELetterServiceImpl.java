package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.repository.ELetterRepository;
import com.inha.os.econtentbackend.service.ELetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ELetterServiceImpl implements ELetterService {
    private final ELetterRepository eLetterRepository;

    @Override
    public Long getTotalElettersCount() {
        return eLetterRepository.countMaterials();
    }
}
