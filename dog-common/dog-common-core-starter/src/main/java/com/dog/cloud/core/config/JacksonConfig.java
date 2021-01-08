package com.dog.cloud.core.config;

import com.dog.cloud.core.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson配置
 *
 * @author gourd.hu
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {

    /**
     * Jackson全局转化long类型为String，解决jackson序列化时long类型缺失精度问题
     *
     * @return Jackson2ObjectMapperBuilderCustomizer 注入的对象
     */
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.locale(Locale.CHINA);
            jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            jacksonObjectMapperBuilder.simpleDateFormat(DateUtils.DATETIME_FORMATTER);
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(long.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

}