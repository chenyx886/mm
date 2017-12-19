package com.mm.contract.news;


import com.mm.contract.IBaseMvpView;
import com.mm.data.entity.NodeEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Chenyx
 * 创建时间：2017/3/24 21:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface INodeView extends IBaseMvpView {
    /**
     * 栏目
     *
     * @param list
     */
    void showNode(List<NodeEntity> list);
}
