package com.epam.course.kubernetes.user.controller;

import javax.validation.Valid;

import com.epam.course.kubernetes.user.dto.UserDto;
import com.epam.course.kubernetes.user.dto.UserUpdate;
import com.epam.course.kubernetes.user.model.PostAmountStatus;
import com.epam.course.kubernetes.user.service.UserService;
import com.epam.course.kubernetes.user.validator.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> create(@RequestBody @Valid UserDto userDto) {
        return userService.save(userDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> getById(@PathVariable("id") @ValidId Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable @ValidId Long id) {
        return userService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> update(@PathVariable @ValidId Long id, @RequestBody @Valid UserUpdate userUpdate) {
        return userService.update(id, userUpdate);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> update(@PathVariable @ValidId Long id,
            @RequestParam("postAmountStatus") PostAmountStatus postAmountStatus) {
        return userService.update(id, postAmountStatus);
    }
}
