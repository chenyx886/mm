package com.mm.data;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Company：苗苗
 * Class Describe：JsonRequestBody
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class JsonRequestBody extends RequestBody {
    @Override
    public MediaType contentType() {
        return MediaType.parse("application/json; charset=utf-8");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

    }

    public static RequestBody createJsonBody(Map<String, Object> param) {
        Gson gson = new Gson();
        String jsonParam = gson.toJson(param);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
    }
}
