package org.cosmic.backend.domain.auth.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterTestController {
    @GetMapping("/api/example")
    public String protectedEndpoint() {
        return "Access granted to protected url!";
    }
}
