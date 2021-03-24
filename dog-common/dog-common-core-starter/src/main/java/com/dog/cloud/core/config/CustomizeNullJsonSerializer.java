package com.dog.cloud.core.config;

import com.dog.cloud.core.annotation.NullNotJoinToCustomizeJsonSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

/**
 * 自定义null值序列化处理器
 *
 * @author KING
 */
public class CustomizeNullJsonSerializer extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
        List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        beanProperties.forEach(beanProperty -> {
            // 带有该注解的字段为null时 不参与自定义格式化 使用Jackson全局NON_NULL设置 隐藏该字段
            NullNotJoinToCustomizeJsonSerializer filterSerialize =
                beanProperty.getAnnotation(NullNotJoinToCustomizeJsonSerializer.class);
            if (filterSerialize == null) {
                // 判断字段的类型，如果是array，list，set则注册nullSerializer
                if (isArrayType(beanProperty)) {
                    //给writer注册一个自己的nullSerializer
                    beanProperty.assignNullSerializer(new NullArrayJsonSerializer());
                } else if (isNumberType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullNumberJsonSerializer());
                } else if (isBooleanType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullBooleanJsonSerializer());
                } else if (isStringType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullStringJsonSerializer());
                } else if (isLocalDateType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullDateJsonSerializer());
                } else if (isLocalTimeType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullDateJsonSerializer());
                } else if (isLocalDateTimeType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullDateJsonSerializer());
                } else if (isObjectType(beanProperty)) {
                    beanProperty.assignNullSerializer(new NullObjectJsonSerializer());
                }
            }
        });
        return beanProperties;
    }

    /**
     * 是否是数组
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是string
     */
    private boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是int
     */
    private boolean isNumberType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Number.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是boolean
     */
    private boolean isBooleanType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Boolean.class);
    }

    /**
     * 是否是LocalDate
     */
    private boolean isLocalDateType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(LocalDate.class);
    }

    /**
     * 是否是LocalDateTime
     */
    private boolean isLocalDateTimeType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(LocalDateTime.class);
    }

    /**
     * 是否是LocalTime
     */
    private boolean isLocalTimeType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(LocalTime.class);
    }

    /**
     * 判断是否是对象类型
     */
    private boolean isObjectType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return !clazz.isPrimitive() && !clazz.equals(String.class) && clazz.isAssignableFrom(Object.class);
    }

    /**
     * 处理数组集合类型的null值
     */
    public static class NullArrayJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    }

    /**
     * 处理字符串类型的null值
     */
    public static class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
            jsonGenerator.writeString("");
        }
    }

    /**
     * 处理数值类型的null值
     */
    public static class NullNumberJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
            jsonGenerator.writeNumber(0);
        }
    }

    /**
     * 处理boolean类型的null值
     */
    public static class NullBooleanJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
            jsonGenerator.writeBoolean(false);
        }
    }

    /**
     * 时间类型为null转成""
     */
    public static class NullDateJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider)
            throws IOException {
            jsonGenerator.writeObject("");
        }
    }

    /**
     * 处理实体对象类型的null值
     */
    public static class NullObjectJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeEndObject();
        }
    }

}

