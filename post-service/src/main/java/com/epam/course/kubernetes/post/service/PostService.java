package com.epam.course.kubernetes.post.service;

import java.time.Instant;

import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.dto.PostUpdate;
import com.epam.course.kubernetes.post.exception.PostNotFoundException;
import com.epam.course.kubernetes.post.exception.UserServiceBadRequestException;
import com.epam.course.kubernetes.post.exception.UserServiceNotFoundException;
import com.epam.course.kubernetes.post.exception.UserServiceServerErrorException;
import com.epam.course.kubernetes.post.mapper.PostMapper;
import com.epam.course.kubernetes.post.model.Post;
import com.epam.course.kubernetes.post.model.PostAmountStatus;
import com.epam.course.kubernetes.post.repository.PostRepository;
import com.epam.course.kubernetes.post.validator.PostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private static final String EXTERNAL_USER_SERVICE_BASE_PATH = "api/v1/users";

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final WebClient webClient;

    private final PostValidator postValidator;

    public Mono<PostDto> save(PostDto postDto) {
        postValidator.validateText(postDto.getText());
        Post post = postMapper.fromDto(postDto);
        post.setPostedAt(Instant.now());
        Long authorId = post.getAuthorId();
        return updateUserPostAmountValue(authorId, PostAmountStatus.INCREASE)
                .flatMap(voidResponseEntity -> postRepository.save(post))
                .map(postMapper::toDto);
    }

    public Mono<PostDto> getById(Long id) {
        return getPostById(id).map(postMapper::toDto);
    }

    public Mono<Void> delete(Long id) {
        Mono<Post> postMono = getPostById(id);
        return postMono.flatMap(post -> updateUserPostAmountValue(post.getAuthorId(), PostAmountStatus.DECREASE)
                .flatMap(voidResponseEntity -> postRepository.delete(post)))
                .doOnSuccess(unused -> log.info("Post deleted with id = {}", id));
    }

    public Mono<PostDto> update(Long id, PostUpdate postUpdate) {
        postValidator.validateText(postUpdate.getText());
        Mono<Post> postMono = getPostById(id);
        return postMono.flatMap(post -> {
            post.setText(postUpdate.getText());
            post.setPostedAt(Instant.now());
            return postRepository.save(post);
        }).map(postMapper::toDto);
    }

    private Mono<Post> getPostById(Long id) {
        return postRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> {
                    log.error("There is no post with id = {}", id);
                    throw new PostNotFoundException("Post doesn’t exist with given id");
                }));
    }

    private Mono<ResponseEntity<Void>> updateUserPostAmountValue(Long authorId, PostAmountStatus postAmountStatus) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(EXTERNAL_USER_SERVICE_BASE_PATH + "/{id}")
                        .queryParam("postAmountStatus", postAmountStatus)
                        .build(authorId))
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(UserServiceServerErrorException::new))
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(UserServiceBadRequestException::new))
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(String.class).map(UserServiceNotFoundException::new))
                .toBodilessEntity()
                .doOnSuccess(v -> log.info("Updated user post amount value"));
    }
}
