package org.cosmic.backend.domainsTest.mail;

import org.cosmic.backend.domain.mail.utils.ApacheMathRandomCodeGenerator;
import org.cosmic.backend.domain.mail.utils.RandomCodeGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class RandomCodeGeneratorTest {
    private final RandomCodeGenerator randomCodeGenerator = new ApacheMathRandomCodeGenerator();

    @Test
    public void generateDuplication(){
        Set<String> codes = new HashSet<>();
        int VALIDATION_NUMBER = 1000;
        for (int i = 0; i < VALIDATION_NUMBER; i++) {
            String newRandomCode = randomCodeGenerator.randomCode();
            Assertions.assertFalse(codes.contains(newRandomCode));
            codes.add(newRandomCode);
        }
    }
}
