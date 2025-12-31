package com.example.convergencesoftwarerecruitingbe.domain.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ApplicationResultCodeGenerator {

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 4;
    private static final int MAX_RETRY = 50;

    private final ApplicationRepository applicationRepository;
    private final Random random = new SecureRandom();

    public String generate(Long formId) {
        for (int i = 0; i < MAX_RETRY; i++) {
            String candidate = randomCode();
            if (isAllDigits(candidate)) {
                continue;
            }
            if (isSameCharRepeated(candidate) || isSequential(candidate) || isAlternatingPattern(candidate)) {
                continue;
            }
            if (!applicationRepository.existsByFormIdAndResultCode(formId, candidate)) {
                return candidate;
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "결과 코드 생성에 실패했습니다");
    }

    private String randomCode() {
        char[] buffer = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            buffer[i] = ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length()));
        }
        return new String(buffer);
    }

    private boolean isAllDigits(String code) {
        for (char c : code.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSameCharRepeated(String code) {
        char first = code.charAt(0);
        for (int i = 1; i < code.length(); i++) {
            if (code.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }

    private boolean isSequential(String code) {
        for (int i = 1; i < code.length(); i++) {
            int prevIndex = ALLOWED_CHARACTERS.indexOf(code.charAt(i - 1));
            int currentIndex = ALLOWED_CHARACTERS.indexOf(code.charAt(i));
            if (prevIndex == -1 || currentIndex == -1) {
                return false;
            }
            int diff = currentIndex - prevIndex;
            if (diff != 1 && diff != -1) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlternatingPattern(String code) {
        return code.charAt(0) == code.charAt(2) && code.charAt(1) == code.charAt(3);
    }
}
