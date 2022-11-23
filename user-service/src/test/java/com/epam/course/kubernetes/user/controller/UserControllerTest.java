package com.epam.course.kubernetes.user.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.epam.course.kubernetes.user.configuration.DbSchemaInitOnStartup;
import com.epam.course.kubernetes.user.dto.UserDto;
import com.epam.course.kubernetes.user.dto.UserUpdate;
import com.epam.course.kubernetes.user.model.PostAmountStatus;
import com.epam.course.kubernetes.user.service.UserService;
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
class UserControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/users";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    private UserDto userDto;

    @MockBean
    private DbSchemaInitOnStartup dbSchemaInitOnStartup;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1111L)
                .username("Inna")
                .amountOfPosts("1")
                .build();
    }

    @Test
    void create() {
        // given
        when(userService.save(any())).thenReturn(Mono.just(userDto));

        UserDto request = UserDto.builder()
                .username("inna")
                .amountOfPosts("0")
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
        when(userService.getById(id)).thenReturn(Mono.just(userDto));

        final var expectedJson = IOUtils
                .toString(Objects.requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("users.json")), StandardCharsets.UTF_8);

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
        String amountOfPosts = "2";
        userDto.setAmountOfPosts(amountOfPosts);
        when(userService.update(id, PostAmountStatus.DECREASE)).thenReturn(Mono.just(userDto));

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
    void update_user() {
        // given
        Long id = 1L;
        String username = "anna";
        UserUpdate userUpdate = UserUpdate.builder()
                .username(username)
                .build();
        when(userService.update(id, userUpdate)).thenReturn(Mono.just(userDto));

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

    @Test
    void update_partial_user() {
        // given
        Long id = 1L;
        String amountOfPosts = "2";
        userDto.setAmountOfPosts(amountOfPosts);
        when(userService.update(id, PostAmountStatus.INCREASE)).thenReturn(Mono.just(userDto));

        // when
        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder.path(URL_TEMPLATE + "/{id}")
                        .queryParam("postAmountStatus", PostAmountStatus.INCREASE)
                        .build(id))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus()
                .isOk();
    }
}
