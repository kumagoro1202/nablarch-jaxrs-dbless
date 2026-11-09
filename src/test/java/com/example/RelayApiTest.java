package com.example;

import nablarch.fw.web.HttpResponse;
import nablarch.test.core.http.SimpleRestTestSupport;
import nablarch.test.junit5.extension.http.SimpleRestTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * DBを使わない構成でのリクエスト単体テスト。
 */
@SimpleRestTest
class RelayApiTest {

    SimpleRestTestSupport support;

    @Test
    void ping() {
        HttpResponse response = support.sendRequest(support.get("/relay/ping"));
        support.assertStatusCode("ping", HttpResponse.Status.OK, response);
        assertThat(response.getBodyString(), containsString("\"status\":\"ok\""));
    }

    @Test
    void echo() {
        HttpResponse response = support.sendRequest(
                support.post("/relay/echo").setContentType("application/json").setBody(Map.of("message", "テスト")));
        support.assertStatusCode("echo", HttpResponse.Status.OK, response);
        assertThat(response.getBodyString(), containsString("テスト"));
    }

    @Test
    void echoValidationError() {
        HttpResponse response = support.sendRequest(
                support.post("/relay/echo").setContentType("application/json").setBody(Map.of("message", "abc")));
        support.assertStatusCode("echo validation error", HttpResponse.Status.BAD_REQUEST, response);
    }
}
