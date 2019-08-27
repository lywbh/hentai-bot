package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyw.utils.HttpUtils;
import com.lyw.utils.RandomUtils;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.event.JcqApp;

import java.io.File;
import java.io.IOException;

public class YandereModule {

    public YandereModule(String url, String localPath) {
        this.url = url;
        this.localPath = localPath;
    }

    private String url;
    private String localPath;

    public String rating(String rating) {
        switch (rating) {
            case "s":
                return "safe";
            case "q":
                return "questionable";
            case "e":
                return "explicit";
            default:
                return rating;
        }
    }

    public File randomPic() {
        return randomPic(null);
    }

    public File randomPic(String rating) {
        int page = RandomUtils.getRandomInt(1000) + 1;
        String finalUrl = url + "?limit=1&page=" + page;
        if (rating != null) {
            finalUrl += "&tags=rating:" + rating;
        }
        String response = HttpUtils.get(finalUrl);
        if (response == null) {
            return null;
        }
        JSONObject respJson = JSON.parseArray(response).getJSONObject(0);
        try {
            CQImage cqImage = new CQImage(respJson.getString("sample_url"));
            return cqImage.download(localPath, "hentai_" + respJson.getInteger("id"));
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        }
    }

}
