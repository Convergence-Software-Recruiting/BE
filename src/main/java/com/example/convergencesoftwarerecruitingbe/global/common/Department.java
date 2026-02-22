package com.example.convergencesoftwarerecruitingbe.global.common;

import lombok.Getter;

@Getter
public enum Department {

    PLANNING("기획국"),
    EXTERNAL_COOPERATION("대외협력국"),
    WELFARE("복지국"),
    SECRETARIAT("사무국"),
    PUBLIC_RELATIONS("홍보국");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    public String getCode() {
        return name();
    }
}