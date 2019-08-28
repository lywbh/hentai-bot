package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyw.config.LocalConfig;
import com.lyw.config.SourceConfig;
import com.lyw.utils.HttpUtils;
import com.lyw.utils.RandomUtils;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.event.JcqApp;

import java.io.File;
import java.io.IOException;

public class YandereModule {

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
        return randomPic("safe");
    }

    public File randomPic(String rating) {
        int page = RandomUtils.getRandomInt(20000) + 1;
        String url = SourceConfig.SOURCE_KONACHAN + "?limit=1&page=" + page + "&tags=rating:" + rating;
        String response = HttpUtils.get(url);
        if (response == null) {
            return null;
        }
        JSONObject respJson = JSON.parseArray(response).getJSONObject(0);
        try {
            CQImage cqImage = new CQImage(respJson.getString("sample_url"));
            return cqImage.download(LocalConfig.LOCAL_PATH, "hentai_" + respJson.getInteger("id"));
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        }
    }

}
