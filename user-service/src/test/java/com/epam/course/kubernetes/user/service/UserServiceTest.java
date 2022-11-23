package com.epam.course.kubernetes.user.service;

import com.epam.course.kubernetes.user.dto.UserDto;
import com.epam.course.kubernetes.user.dto.UserUpdate;
import com.epam.course.kubernetes.user.model.PostAmountStatus;
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
class UserServiceTest {

    @Mock
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1111L)
                .username("Inna")
                .amountOfPosts("1")
                .build();
    }

    @Test
    void save() {
        // given
        when(userService.save(any())).thenReturn(Mono.just(userDto));

        // when
        Mono<UserDto> actual = userService.save(any());

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected-> {
                    assertNotNull(expected);
                    assertThat(expected.getId(), is(userDto.getId()));
                    assertThat(expected.getUsername(), is(userDto.getUsername()));
                    assertThat(expected.getAmountOfPosts(), is(userDto.getAmountOfPosts()));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void getById() {
        // given
        Long id = 1L;
        when(userService.getById(id)).thenReturn(Mono.just(userDto));

        // when
        Mono<UserDto> actual = userService.getById(id);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected-> {
                    assertNotNull(expected);
                    assertThat(expected.getId(), is(userDto.getId()));
                    assertThat(expected.getUsername(), is(userDto.getUsername()));
                    assertThat(expected.getAmountOfPosts(), is(userDto.getAmountOfPosts()));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void delete() {
        // given
        when(userService.delete(any())).thenReturn(Mono.empty());

        // when
        Mono<Void> actual = userService.delete(any());

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void update_user() {
        // given
        Long id = 1L;
        String username = "anna";
        UserUpdate userUpdate = UserUpdate.builder()
                .username(username)
                .build();
        userDto.setUsername(username);
        when(userService.update(id, userUpdate)).thenReturn(Mono.just(userDto));

        // when
        Mono<UserDto> actual = userService.update(id, userUpdate);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected -> {
                    assertNotNull(expected);
                    assertThat(expected.getUsername(), is(username));
                })
                .expectComplete()
                .verify();
    }

    @Test
    void update_partial_user() {
        // given
        Long id = 1L;
        String amountOfPosts = "2";
        userDto.setAmountOfPosts(amountOfPosts);
        when(userService.update(id, PostAmountStatus.INCREASE)).thenReturn(Mono.just(userDto));

        // when
        Mono<UserDto> actual = userService.update(id, PostAmountStatus.INCREASE);

        // then
        StepVerifier
                .create(actual)
                .expectSubscription()
                .assertNext(expected -> {
                    assertNotNull(expected);
                    assertThat(expected.getAmountOfPosts(), is(amountOfPosts));
                })
                .expectComplete()
                .verify();
    }
}
