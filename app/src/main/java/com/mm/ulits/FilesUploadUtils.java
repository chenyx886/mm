package com.mm.ulits;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 类描述：文件上传工具
 *
 */
public class FilesUploadUtils {


    /**
     * 构建多文件上传  List<MultipartBody.Part>
     *
     * @param files
     * @param fileTypes
     * @return
     */
    public static List<MultipartBody.Part> multiPartFiles(List<File> files, List<String> fileTypes) {

        List<MultipartBody.Part> multiPartFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i) != null && files.get(i).exists()) {
                if (files.get(i).isFile()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse(fileTypes.get(i)), files.get(i));
                    MultipartBody.Part part = MultipartBody.Part.createFormData("imagefile[]", files.get(i).getName(), requestBody);
                    multiPartFiles.add(part);
                }
            }
        }
        return multiPartFiles;
    }

    /**
     * 构建多文件上传  Map<String, RequestBody>
     *
     * @param files
     * @param fileTypes
     * @return
     */
    public static Map<String, RequestBody> filesToMapRequestBody(List<File> files, List<String> fileTypes) {

        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i) != null && files.get(i).exists()) {
                if (files.get(i).isFile()) {
                    map.put("file" + i + "\";filename=\"" + files.get(i).getName(), RequestBody.create(MediaType.parse(fileTypes.get(i)), files.get(i)));
                }
            }
        }
        return map;
    }
}
