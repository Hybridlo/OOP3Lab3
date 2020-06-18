package com.lab3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotNull(message = "Login is required")
    private String login;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Type is required")
    private String type;
}
