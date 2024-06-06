package org.cosmic.backend.domain.mail.utils;

import org.apache.commons.math3.random.RandomDataGenerator;

public class ApacheMathRandomCodeGenerator implements RandomCodeGenerator{
    private static final RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    private static final int DEFAULT_LENGTH = 6;

    @Override
    public String randomCode() {
        return randomCode(DEFAULT_LENGTH);
    }

    @Override
    public String randomCode(int length) {
        return randomDataGenerator.nextSecureHexString(length);
    }
}
