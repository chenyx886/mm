package com.mm.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Company：苗苗
 * Class Describe：分页实体
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class PageEntity<T> extends BaseResp {

    /**
     * 总条
     */
    public int total;
    /**
     * 当前页
     */
    public int page;
    /**
     * 数据列表
     */
    public List<T> rows = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getRows() {
        return rows;
    }

}
