package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyw.config.SourceConfig;
import com.lyw.utils.HttpUtils;
import com.lyw.utils.RandomUtils;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.event.JcqApp;

import java.io.IOException;

public class YandereModule {

    public enum Rating {
        UNKNOWN, SAFE, QUESTIONABLE, EXPLICIT
    }

    public Rating rating(String rating) {
        rating = rating.trim();
        switch (rating) {
            case "s":
            case "safe":
                return Rating.SAFE;
            case "q":
            case "questionable":
                return Rating.QUESTIONABLE;
            case "e":
            case "explicit":
                return Rating.EXPLICIT;
            default:
                return Rating.UNKNOWN;
        }
    }

    public CQImage randomPic(Rating rating) {
        if (rating == Rating.UNKNOWN) {
            rating = Rating.SAFE;
        }
        int page = RandomUtils.getRandomInt(20000) + 1;
        String url = SourceConfig.SOURCE_KONACHAN + "?limit=1&page=" + page + "&tags=rating:" + rating.name().toLowerCase();
        String response = HttpUtils.get(url);
        if (response == null) {
            return null;
        }
        JSONObject respJson = JSON.parseArray(response).getJSONObject(0);
        try {
            return new CQImage(respJson.getString("sample_url"));
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        }
    }

}
