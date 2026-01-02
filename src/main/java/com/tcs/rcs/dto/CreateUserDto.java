package com.tcs.rcs.dto;

import java.util.List;

public record CreateUserDto(String userName, String password, List<Long> roleIds) {

}
