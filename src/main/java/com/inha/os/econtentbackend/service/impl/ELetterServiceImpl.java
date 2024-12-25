package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ELetterGetResponseDto;
import com.inha.os.econtentbackend.dto.request.ElettersGetRequestDto;
import com.inha.os.econtentbackend.entity.Article;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.entity.ELetter;
import com.inha.os.econtentbackend.entity.Subject;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.ELetterAlreadyExistsException;
import com.inha.os.econtentbackend.exception.ELetterNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.mapper.ELetterMapper;
import com.inha.os.econtentbackend.repository.ELetterRepository;
import com.inha.os.econtentbackend.service.ContentService;
import com.inha.os.econtentbackend.service.ELetterService;
import com.inha.os.econtentbackend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ELetterServiceImpl implements ELetterService {
    private final ELetterRepository eLetterRepository;
    private final ELetterMapper eLetterMapper;
    private final ContentService contentService;

    @Override
    public Long getTotalElettersCount() {
        return eLetterRepository.countMaterials();
    }

    @Override
    public String createEletter(ELetterCreateRequestDto eLetterCreateRequestDto) throws SubjectNotFoundExceptionHttp, ContentTypeNotFoundExceptionHttp, IOException, ELetterAlreadyExistsException {
        if (eLetterRepository.existsELetterByAuthorAndTitle(eLetterCreateRequestDto.getAuthor(),
                eLetterCreateRequestDto.getTitle())) {
            throw new ELetterAlreadyExistsException("e-letter already exists");
        }
        Content eLetterContent = contentService.createELetterContent(eLetterCreateRequestDto);
        ELetter entity = eLetterMapper.toEntity(eLetterCreateRequestDto);

        entity.setContent(eLetterContent);
        eLetterRepository.save(entity);

        return "e-letter saved successfully";
    }

    @Override
    @Transactional
    public List<ELetterGetResponseDto> getELetters(ElettersGetRequestDto requestDto) {
        List<ELetter> eLetters = eLetterRepository.findBySubjectId(requestDto.getSubjectId());
        return eLetters.stream().map(eLetterMapper::toGetResponseDto).toList();
    }

    @Override
    public String deleteById(Integer eLetterId) {
        eLetterRepository.deleteById(eLetterId);
        return "E-letter deleted successfully";
    }

    @Override
    public ELetter findById(Integer id) throws ELetterNotFoundException {
        Optional<ELetter> optionalELetter = eLetterRepository.findById(id);
        if (optionalELetter.isEmpty()) {
            throw new ELetterNotFoundException("e-letter with id '%s' not found".formatted(id));
        }
        return optionalELetter.get();
    }
}
