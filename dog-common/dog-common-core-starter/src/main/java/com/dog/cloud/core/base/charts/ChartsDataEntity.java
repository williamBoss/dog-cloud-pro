package com.dog.cloud.core.base.charts;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 图表数据
 *
 * @author KING
 * @version V1.0
 * @date 2021/2/26
 **/
@Data
@ApiModel(value = "图表数据", description = "图表数据")
public class ChartsDataEntity implements Serializable {
    /**
     * 数据项名称
     */
    private String name;

    /**
     * 数据值
     */
    private Object value;
}
