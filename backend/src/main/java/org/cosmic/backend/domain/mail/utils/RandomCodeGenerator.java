package org.cosmic.backend.domain.mail.utils;

public interface RandomCodeGenerator {
    String randomCode();
    String randomCode(int length);
}
