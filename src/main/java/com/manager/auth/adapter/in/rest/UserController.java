package com.manager.auth.adapter.in.rest;

import com.manager.auth.adapter.dto.UserDto;
import com.manager.auth.adapter.in.security.AuthenticatedUserProvider;
import com.manager.auth.adapter.out.persistence.mapper.UserMapper;
import com.manager.auth.model.users.User;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserMapper userMapper;

    public UserController(AuthenticatedUserProvider authenticatedUserProvider, UserMapper userMapper) {
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDto>> getCurrentUser() {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), userMapper.toUserDto(authenticatedUser)));
    }
}
