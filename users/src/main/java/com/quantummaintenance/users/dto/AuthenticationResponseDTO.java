package com.quantummaintenance.users.dto;

import lombok.Data;

import com.quantummaintenance.users.entity.Role;

import lombok.Builder;

@Data
@Builder
public class AuthenticationResponseDTO {
	private String token;
	private Role role;
}
