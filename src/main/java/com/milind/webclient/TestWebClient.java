package com.milind.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.sound.midi.Soundbank;

@Component
public class TestWebClient {

    /**
     * This will test the reactive component
     */
    public void testReactive(){

        String json ="{\n" +
                "    \"currency\": \"US\",\n" +
                "    \"totalAmount\": 800.0999755859375,\n" +
                "    \"orderId\": \"O-2989\",\n" +
                "    \"emailAddress\": \"mail@gmail.com\",\n" +
                "    \"lineItems\": [\n" +
                "        {\n" +
                "            \"sku\": \"sk99001\",\n" +
                "            \"name\": \"salt\",\n" +
                "            \"category\": \"foodItem-daily\",\n" +
                "            \"unitPrice\": 1.899999976158142,\n" +
                "            \"salePrice\": 1.9900000095367432,\n" +
                "            \"quantity\": 10,\n" +
                "            \"totalPrice\": 10.989999771118164\n" +
                "        },\n" +
                "\t\t {\n" +
                "            \"sku\": \"SKU01\",\n" +
                "            \"name\": \"Chips\",\n" +
                "            \"category\": \"foodItem-daily\",\n" +
                "            \"unitPrice\": 20,\n" +
                "            \"salePrice\": 15,\n" +
                "            \"quantity\": 25,\n" +
                "            \"totalPrice\": 710.989999771118164\n" +
                "        },\n" +
                "\t\t {\n" +
                "            \"sku\": \"SKU99\",\n" +
                "            \"name\": \"soap\",\n" +
                "            \"category\": \"grocessry\",\n" +
                "            \"unitPrice\": 19.8,\n" +
                "            \"salePrice\": 18.9900000095367432,\n" +
                "            \"quantity\": 10,\n" +
                "            \"totalPrice\": 100.989999771118164\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Mono<String>  testWeb = WebClient.create().post().uri("http://localhost:8080/confirmorder")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(json),String.class).retrieve().bodyToMono(String.class);
        System.out.println("After calling the WebClient");
        testWeb.subscribe(System.out::println);

    }
}
