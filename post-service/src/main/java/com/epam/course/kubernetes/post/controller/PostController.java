package com.epam.course.kubernetes.post.controller;

import javax.validation.Valid;

import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.dto.PostUpdate;
import com.epam.course.kubernetes.post.service.PostService;
import com.epam.course.kubernetes.post.validator.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PostDto> create(@RequestBody @Valid PostDto postDto) {
        return userService.save(postDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PostDto> getById(@PathVariable @ValidId Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable @ValidId Long id) {
        return userService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PostDto> update(@PathVariable @ValidId Long id, @RequestBody @Valid PostUpdate postUpdate) {
        return userService.update(id, postUpdate);
    }
}
