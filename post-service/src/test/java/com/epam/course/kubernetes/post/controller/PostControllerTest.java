package com.epam.course.kubernetes.post.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

import com.epam.course.kubernetes.post.configuration.DbSchemaInitOnStartup;
import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.dto.PostUpdate;
import com.epam.course.kubernetes.post.service.PostService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/posts";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PostService postService;

    private PostDto postDto;

    @MockBean
    private DbSchemaInitOnStartup dbSchemaInitOnStartup;

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
    void create() {
        // given
        when(postService.save(any())).thenReturn(Mono.just(postDto));

        PostDto request = PostDto.builder()
                .authorId(1111L)
                .text("Hi, today I post top of my favorite anime!")
                .build();

        // when
        webTestClient.post()
                .uri(URL_TEMPLATE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus()
                .isCreated();
    }

    @Test
    void getById() throws IOException {
        // given
        Long id = 1L;
        when(postService.getById(id)).thenReturn(Mono.just(postDto));

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("posts.json")), StandardCharsets.UTF_8);

        // when
        webTestClient.get()
                .uri(URL_TEMPLATE + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJson);
    }

    @Test
    void delete() {
        // given
        Long id = 1L;
        when(postService.delete(id)).thenReturn(Mono.empty());

        // when
        webTestClient.delete()
                .uri(URL_TEMPLATE + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus()
                .isNoContent();
    }

    @Test
    void update() {
        // given
        Long id = 1L;
        String text = "Hi, today I post top of my favorite manga!";
        PostUpdate userUpdate = PostUpdate.builder()
                .text(text)
                .build();
        when(postService.update(id, userUpdate)).thenReturn(Mono.just(postDto));

        // when
        webTestClient.put()
                .uri(URL_TEMPLATE + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userUpdate)
                .exchange()
                // then
                .expectStatus()
                .isOk();
    }
}
