package com.mm.contract.mine;

import com.mm.contract.IBaseMvpView;
import com.mm.data.entity.ImageEntity;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/18 上午2:00
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IUserSignView extends IBaseMvpView {

    void returnImage(List<String> filePaths);

}
