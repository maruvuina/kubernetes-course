package com.epam.course.kubernetes.post.service;

import java.time.Instant;

import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.dto.PostUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostService postService;

    private PostDto postDto;

    @BeforeEach
    void setUp() {
        postDto = PostDto.builder()
                .id(1111L)
                .authorId(111L)
                .text("Hi, today I post top of my favorite anime!")
                .postedAt(Instant.parse("2022-11-11T11:11:22.022915900Z"))
                .build();
    }

    @Test
    void save() {
        // given
        when(postService.save(any())).thenReturn(Mono.just(postDto));

        // when
        Mono<PostDto> actual = postService.save(any());

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected-> {
                    assertNotNull(expected);
                    assertThat(expected.getId(), is(postDto.getId()));
                    assertThat(expected.getAuthorId(), is(postDto.getAuthorId()));
                    assertThat(expected.getText(), is(postDto.getText()));
                    assertThat(expected.getPostedAt(), is(postDto.getPostedAt()));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void getById() {
        // given
        Long id = 1L;
        when(postService.getById(id)).thenReturn(Mono.just(postDto));
        //when(webClient.patch()).thenReturn();

        // when
        Mono<PostDto> actual = postService.getById(id);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected-> {
                    assertNotNull(expected);
                    assertThat(expected.getId(), is(postDto.getId()));
                    assertThat(expected.getAuthorId(), is(postDto.getAuthorId()));
                    assertThat(expected.getText(), is(postDto.getText()));
                    assertThat(expected.getPostedAt(), is(postDto.getPostedAt()));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void delete() {
        // given
        when(postService.delete(any())).thenReturn(Mono.empty());

        // when
        Mono<Void> actual = postService.delete(any());

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void update() {
        // given
        Long id = 1L;
        String text = "Hi, today I post top of my favorite manga!";
        PostUpdate userUpdate = PostUpdate.builder()
                .text(text)
                .build();
        postDto.setText(text);
        when(postService.update(id, userUpdate)).thenReturn(Mono.just(postDto));

        // when
        Mono<PostDto> actual = postService.update(id, userUpdate);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected -> {
                    assertNotNull(expected);
                    assertThat(expected.getText(), is(text));
                })
                .expectComplete()
                .verify();
    }
}
