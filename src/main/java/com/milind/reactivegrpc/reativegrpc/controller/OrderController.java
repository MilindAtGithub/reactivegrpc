package com.milind.reactivegrpc.reativegrpc.controller;

import com.milind.service.OrderConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class OrderController {

    @Autowired
    OrderConfirmationService orderConfirmationService;

    /**
     * Controller which takes JSON and produce JSON
     * @param orderJson
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @PostMapping(value = "/confirmorder", consumes = "application/json", produces = "application/json")
    public Mono<String> getConfirmOrder(@RequestBody String orderJson)
            throws IOException, InterruptedException,
            ExecutionException {
        System.out.println(Thread.currentThread() + " --- In Controller");
        return orderConfirmationService.confirmProcess(orderJson);
    }
}
