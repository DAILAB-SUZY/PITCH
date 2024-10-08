package org.cosmic.backend.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    public static UserDto createUserDto(Long userId) {
        return  UserDto.builder()
                .userId(userId)
                .build();
    }
}
