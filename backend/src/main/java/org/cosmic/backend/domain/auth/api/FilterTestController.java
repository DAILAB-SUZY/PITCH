package org.cosmic.backend.domain.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterTestController {
    @GetMapping("/example")
    public String protectedEndpoint() {
        return "Access granted to protected url!";
    }
}
