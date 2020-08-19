package com.milind.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.milind.grpc.LineItems;
import com.milind.grpc.Order;
import com.milind.grpc.OrderConfirmation;
import com.milind.grpc.OrderConfrimationServiceGrpc;
import com.milind.util.ProtoJsonUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Client to invoke gRPC call and construct the order
 */
@Component
public class GRPCReactiveClient {


    @Value("${app.grpc.server.hostname:localhost}")
    private String gRPCServerName;
    @Value("${app.grpc.server.port:8090}")
    private int gRPCServerPort;
    private OrderConfrimationServiceGrpc.OrderConfrimationServiceFutureStub futureStub;
    private ManagedChannel channel = null;


    @PostConstruct
    public void init() {

        channel = ManagedChannelBuilder.forAddress(gRPCServerName, gRPCServerPort)
                .usePlaintext()
                .build();
        futureStub = OrderConfrimationServiceGrpc.newFutureStub(channel);
    }

    @PreDestroy
    public void destroy() {
        channel.shutdownNow();
    }


    /**
     * This is the reactive method which will call the gRPC server in reactive way.
     *
     * @param order
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletableFuture<String> confirmOrder(Order order) throws ExecutionException,
            InterruptedException {
        System.out.println(Thread.currentThread() + " --- In confirmorder client");
        ListenableFuture<OrderConfirmation> future = futureStub.confrim(order);
        CompletableFuture<String> completableFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println(Thread.currentThread() + " --- In Completable future");
                        return ProtoJsonUtil.toJson(finalizeTheOrder(order, future.get()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        System.out.println("Before Returing the gRPC Call");
        return completableFuture;
    }

    /**
     * This method will finalize the order from order confirmation
     *
     * @param order
     * @param orderConfirmation
     * @return
     */
    private Order finalizeTheOrder(Order order, OrderConfirmation orderConfirmation) {

        List<LineItems> tempList = new ArrayList<>(3);
        orderConfirmation.getConfirmedLineItemsList().stream().forEach(
                line -> {
                    Optional<LineItems> lineItems = getLineItemFromOder(line.getSku(), order);
                    lineItems.ifPresent(l -> {
                        tempList.add(l.toBuilder().setQuantity(line.getConfirmQuantity()).build());
                    });
                }
        );
        //Removing unconfirmed line items
        Order.Builder builder = order.toBuilder();
        builder.clearLineItems().addAllLineItems(tempList);
        return builder.build();
    }

    /**
     * This will get lineItem for the sku
     *
     * @param sku
     * @param order
     * @return
     */
    private Optional<LineItems> getLineItemFromOder(String sku, Order order) {

        return order.getLineItemsList().stream().filter(lineItems ->
                lineItems.getSku().equalsIgnoreCase(sku)).findFirst();
    }
}
