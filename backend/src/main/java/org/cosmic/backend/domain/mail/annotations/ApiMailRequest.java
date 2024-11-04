package org.cosmic.backend.domain.mail.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.cosmic.backend.domain.mail.dtos.EmailAddress;
import org.springframework.http.MediaType;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "랜덤코드 메일 전송 API", description = "사용자 이메일로 랜덤코드를 발송합니다.")
@ApiResponse(responseCode = "200", content = {
    @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE, implementation = EmailAddress.class))
})
@ApiResponse(responseCode = "401", description = "Email is already exist or interval is too short")
public @interface ApiMailRequest {

}
