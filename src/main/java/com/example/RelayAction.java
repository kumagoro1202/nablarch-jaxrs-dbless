package com.example;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nablarch.fw.web.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * DBを使わない構成の検証用アクション。
 */
@Path("/relay")
public class RelayAction {

    /** 疎通確認。 */
    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> ping(HttpRequest req) {
        return Map.of("status", "ok");
    }

    /** JSONの受け取りとBean Validation（ドメインバリデーション）の確認。 */
    @POST
    @Path("/echo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Valid
    public EchoForm echo(EchoForm form) {
        return form;
    }

    /** TCPサーバへ1行送信し、1行の応答を返す中継処理。 */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Valid
    public Map<String, String> send(EchoForm form) {
        String host = System.getProperty("relay.tcp.host", "localhost");
        int port = Integer.getInteger("relay.tcp.port", 9999);
        try (Socket socket = new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();
            out.write((form.getMessage() + "\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            return Map.of("reply", in.readLine());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
