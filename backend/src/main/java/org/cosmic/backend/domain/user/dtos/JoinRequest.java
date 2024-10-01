package org.cosmic.backend.domain.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRequest {

    @NotBlank(message = "이메일을 입력해야합니다.")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "Password confirmation cannot be blank")
    private String checkPassword;

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
