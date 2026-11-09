package com.example;

import nablarch.core.validation.ee.Domain;
import nablarch.core.validation.ee.Required;

/**
 * 検証用フォーム。
 */
public class EchoForm {

    @Required
    @Domain("userKanjiName")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
