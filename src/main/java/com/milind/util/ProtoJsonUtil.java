package com.milind.util;

import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Generic ProtoJsonUtil to be used to serialize and deserialize Proto to json
 */
public final class ProtoJsonUtil {

    /**
     * Makes a Json from a given message or builder
     *
     * @param messageOrBuilder is the instance
     * @return The string representation
     * @throws IOException if any error occurs
     */
    public static String toJson(MessageOrBuilder messageOrBuilder) throws IOException {
        return JsonFormat.printer().print(messageOrBuilder);
    }

    /**
     * Makes a new instance of message based on the json and the class
     *
     * @param <T>   is the class type
     * @param json  is the json instance
     * @param clazz is the class instance
     * @return An instance of T based on the json values
     * @throws IOException if any error occurs
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Message> T fromJson(String json, Class<T> clazz) throws IOException {

        Builder builder = null;
        try {
            builder = (Builder) clazz.getMethod("newBuilder").invoke(null);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
        return (T) builder.build();
    }
}
