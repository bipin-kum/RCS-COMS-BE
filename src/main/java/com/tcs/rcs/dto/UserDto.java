package com.tcs.rcs.dto;

import java.util.Set;

public record UserDto(Long id, String userName, boolean enabled, Set<String> roles) {

}
