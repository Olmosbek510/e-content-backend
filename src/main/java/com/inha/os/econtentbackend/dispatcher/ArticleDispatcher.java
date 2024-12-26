package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ArticleResponseDto;
import com.inha.os.econtentbackend.dto.request.SubjectsGetRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.ArticleNotFoundException;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.service.ArticleService;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleDispatcher {
    private final ArticleService articleService;
    private final AuthService authService;
    private final Gson gson;
    private final JwtUtil jwtUtil;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, BookNotFoundException, ArticleNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Article.GET_ARTICLES)) {
            return handleGetArticles(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Article.CREATE_ARTICLE)) {
            return handleCreateArticle(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Article.DELETE_ARTICLE)) {
            return handleDeleteArticle(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleDeleteArticle(String dataNode, String token) throws ArticleNotFoundException {
        ArticleResponseDto articleDto = JsonUtil.getObject(ArticleResponseDto.class, dataNode);
        String message = articleService.deleteById(articleDto);

        Map<String, String> data = new HashMap<>();
        data.put("message", message);

        BaseResponse response = BaseResponse.builder()
                .data(gson.toJson(data))
                .status(ResponseStatus.SUCCESS)
                .action(Actions.Article.DELETE_ARTICLE)
                .entity(Entity.ARTICLE)
                .build();

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    private String handleCreateArticle(String dataNode, String token) {
        ArticleCreateRequestDto requestDto = JsonUtil.getObject(ArticleCreateRequestDto.class, dataNode);
        Map<String, String> data = new HashMap<>();

        data.put("message", "article '%s' created successfully".formatted(requestDto.getPath()));

        BaseResponse baseResponse = BaseResponse.builder()
                .entity(Entity.ARTICLE)
                .action(Actions.Article.CREATE_ARTICLE)
                .status(ResponseStatus.SUCCESS)
                .data(gson.toJson(data))
                .build();

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();
        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleGetArticles(String dataNode, String token) {
        SubjectsGetRequestDto requestDto = JsonUtil.getObject(SubjectsGetRequestDto.class, dataNode);
        List<ArticleResponseDto> subjects = articleService.getSubjects(requestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder()
                .data(gson.toJson(subjects))
                .status(ResponseStatus.SUCCESS)
                .entity(Entity.ARTICLE)
                .action(Actions.Article.GET_ARTICLES)
                .build();
        optionalRole.ifPresent(response::setRole);

        return gson.toJson(response);
    }
}
