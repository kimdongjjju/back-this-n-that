package com.djukim.thisnthat.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper =
                new Jackson2ObjectMapperBuilder()
                        .modules(new JavaTimeModule(), new ParameterNamesModule(), new Jdk8Module())
                        .featuresToDisable(
                                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                                SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                        .serializers(
                                new LocalDateSerializer(DateUtil.FORMAT_DATE_DASH),
                                new LocalDateTimeSerializer(DateUtil.FORMAT_DATETIME_DASH))
                        .deserializers(
                                new LocalDateDeserializer(DateUtil.FORMAT_DATE_DASH),
                                new LocalDateTimeDeserializer(DateUtil.FORMAT_DATETIME_DASH))
                        .serializationInclusion(Include.NON_NULL)
                        .build();
    }

    public static ObjectMapper instance() {
        return objectMapper;
    }

    /**
     * 객체를 JSON 문자열로 직렬화
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String serialize(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON 문자열을 클래스 타입의 객체로 역직렬화
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON 문자열을 클래스 타입의 객체로 역직렬화
     *
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String str, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deepCopy(T source, Class<T> clazz) {
        return deserialize(serialize(source), clazz);
    }

    public static byte[] toJsonBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(String str) {
        try {
            return objectMapper.readTree(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
