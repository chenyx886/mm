package com.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.mm.R;
import com.mm.data.Constant;


/**
 * 类描述：分享工具类
 * 创建人：Chenyx
 * 创建时间：2017/5/21 22:35
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ShareUtils {


    /**
     * 会话
     */
    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneSession;

    /**
     * 朋友圈
     */
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneTimeline;

    private static IWXAPI mWXApi;


    public static void shareWebPage(Context context, String webpageUrl, String title, String description, int type) {

        mWXApi = WXAPIFactory.createWXAPI(context, Constant.AppID);
        mWXApi.registerApp(Constant.AppID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if (thumb == null) {
            Toast.makeText(context, "图片不能为空", Toast.LENGTH_SHORT).show();
        } else {
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();

        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        mWXApi.sendReq(req);
    }



    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
