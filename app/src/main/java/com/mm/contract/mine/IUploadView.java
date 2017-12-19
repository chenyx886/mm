package com.mm.contract.mine;

import com.mm.contract.IBaseMvpView;
import com.mm.data.entity.ImageEntity;
import com.mm.data.entity.VersionEntity;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：上传文件
 * Create Person：Chenyx
 * Create Time：2017/11/29 下午6:20
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IUploadView extends IBaseMvpView {

    void compressImgs(List<String> filePaths);

    void UploadSuccess(List<ImageEntity> list);

    void checkVersion(VersionEntity data);

    void UpdateSucces();
}
