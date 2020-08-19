package com.milind.service;

import com.milind.client.GRPCReactiveClient;
import com.milind.grpc.Order;
import com.milind.util.ProtoJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * This will confirm the order by calling gRPC service
 */
@Component
public class OrderConfirmationService {
    @Autowired
    GRPCReactiveClient grpcReactiveClient;

    /**
     * This will confirm the order and return JSON String
     *
     * @param json
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Mono<String> confirmProcess(String json) throws IOException, ExecutionException,
            InterruptedException {
        System.out.println(Thread.currentThread() + " --- In confirm process");
        Order order = ProtoJsonUtil.fromJson(json, Order.class);
        return Mono.fromFuture(grpcReactiveClient.confirmOrder(order));
    }
}
