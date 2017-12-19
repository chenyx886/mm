package com.mm.contract.find;

import com.mm.contract.IBaseMvpView;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/30 下午1:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IFindAddView extends IBaseMvpView {
    /**
     * 返回压缩图片路径列表
     *
     * @param filePaths
     */
    void returnImage(List<String> filePaths);
}
