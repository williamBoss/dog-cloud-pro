package com.dog.cloud.core.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页返回数据
 *
 * @author KING
 */
@Data
@ApiModel("分页返回数据")
public class PageResult<T> {

    @ApiModelProperty("列表，分页中的数据")
    private List<T> records;

    @ApiModelProperty("总数目")
    private Long total;

    @ApiModelProperty("当前第几页")
    private Long pageNum;

    @ApiModelProperty("每页的元素的数量")
    private Long pageSize;

    public static <T> BaseResult<PageResult<T>> result(Page<?> page, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        return BaseResult.<PageResult<T>>success().data(pageResult);
    }

}