package com.dog.cloud.core.base.charts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图表基础实体对象
 *
 * @author KING
 * @version V1.0
 * @date 2021/2/26
 **/
@Data
@ApiModel(value = "图表基础实体对象", description = "统计图表基础实体对象")
public class BaseChartsVO implements Serializable {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 图例
     */
    @ApiModelProperty(value = "图例")
    private String[] legendData;

    /**
     * x轴类目数据
     */
    @ApiModelProperty(value = "x轴类目数据")
    private String[] xAxisDatas;

    /**
     * 图表类型
     */
    @ApiModelProperty(value = "图表类型")
    private List<ChartsSeriesVO> series;
}
