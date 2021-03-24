package com.dog.cloud.core.base.domain;

import org.mapstruct.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * 实体类转换基类
 *
 * @author KING
 */
@MapperConfig(componentModel = "spring")
public interface BaseConvertMapping<SOURCE, TARGET> {

    /**
     * 映射同名属性
     */
    TARGET sourceToTarget(SOURCE var);

    /**
     * 反向，映射同名属性
     */
    @InheritInverseConfiguration(name = "sourceToTarget")
    SOURCE targetToSource(TARGET var);

    /**
     * 映射同名属性，集合形式
     */
    @InheritConfiguration(name = "sourceToTarget")
    List<TARGET> sourceToTarget(List<SOURCE> var);

    /**
     * 反向，映射同名属性，集合形式
     */
    @InheritConfiguration(name = "targetToSource")
    List<SOURCE> targetToSource(List<TARGET> var);

    /**
     * 映射同名属性，集合流形式
     */
    List<TARGET> sourceToTarget(Stream<SOURCE> stream);

    /**
     * 反向，映射同名属性，集合流形式
     */
    List<SOURCE> targetToSource(Stream<TARGET> stream);

}
