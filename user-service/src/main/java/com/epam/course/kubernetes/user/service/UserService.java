package com.epam.course.kubernetes.user.service;

import com.epam.course.kubernetes.user.dto.UserDto;
import com.epam.course.kubernetes.user.dto.UserUpdate;
import com.epam.course.kubernetes.user.exception.UserNotFoundException;
import com.epam.course.kubernetes.user.exception.UserUpdateException;
import com.epam.course.kubernetes.user.exception.UserValidationException;
import com.epam.course.kubernetes.user.mapper.UserMapper;
import com.epam.course.kubernetes.user.model.PostAmountStatus;
import com.epam.course.kubernetes.user.model.User;
import com.epam.course.kubernetes.user.repository.UserRepository;
import com.epam.course.kubernetes.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserValidator userValidator;

    public Mono<UserDto> save(UserDto userDto) {
        String username = userDto.getUsername();
        userValidator.validateUsername(username);

        return isUserExistsByUsername(username)
                .flatMap(bool -> {
                    User user = userMapper.fromDto(userDto);
                    user.setAmountOfPosts(0);
                    return userRepository.save(user)
                            .map(userMapper::toDto);
                });
    }

    private Mono<Boolean> isUserExistsByUsername(String username) {
        return userRepository.existsUserByUsername(username)
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(() -> {
                    log.error("User already exists with such username = {}", username);
                    throw new UserValidationException("User already exists with such username");
                }));
    }

    public Mono<UserDto> getById(Long id) {
        return getUserById(id).map(userMapper::toDto);
    }

    public Mono<Void> delete(Long id) {
        Mono<User> userMono = getUserById(id);
        return userMono.flatMap(userRepository::delete)
                .doOnSuccess(unused -> log.info("User deleted with id = {}", id));
    }

    public Mono<UserDto> update(Long id, UserUpdate userUpdate) {
        userValidator.validateUsername(userUpdate.getUsername());
        Mono<User> userMono = getUserById(id);

        return userMono.<User>handle((user, sink) ->
                        validateUsernameForUpdating(userUpdate, user, sink))
                .flatMap(user -> {
                    user.setUsername(userUpdate.getUsername());
                    return userRepository.save(user);
                }).map(userMapper::toDto);
    }

    private void validateUsernameForUpdating(UserUpdate userUpdate, User user, SynchronousSink<User> sink) {
        if (user.getUsername().equals(userUpdate.getUsername())) {
            log.error("User already exists with such username = {}", user.getId());
            sink.error(new UserUpdateException("User already exists with such username"));
        } else {
            sink.next(user);
        }
    }

    public Mono<UserDto> update(Long id, PostAmountStatus postAmountStatus) {
        Mono<User> userMono = getUserById(id);
        return userMono.<User>handle((user, sink) ->
                        validateLimitValueOfAmountOfPostsOfUser(postAmountStatus, user, sink))
                .flatMap(user -> {
                    Integer actualAmountOfPosts = user.getAmountOfPosts();
                    if (postAmountStatus.equals(PostAmountStatus.INCREASE)) {
                        user.setAmountOfPosts(actualAmountOfPosts + 1);
                    } else {
                        user.setAmountOfPosts(actualAmountOfPosts - 1);
                    }
                    return userRepository.save(user);
                })
                .map(userMapper::toDto);
    }

    private Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> {
                    log.error("There is no user with id = {}", id);
                    throw new UserNotFoundException("User doesn’t exist with given id");
                }));
    }

    private void validateLimitValueOfAmountOfPostsOfUser(PostAmountStatus postAmountStatus, User user, SynchronousSink<User> sink) {
        if (user.getAmountOfPosts() == 0 && postAmountStatus.equals(PostAmountStatus.DECREASE)) {
            log.error("Cannot decrease amount of posts with given user id = {} cause amount = 0", user.getId());
            sink.error(new UserUpdateException("Cannot decrease amount of posts with given user id cause amount = 0"));
        } else {
            sink.next(user);
        }
    }

}
