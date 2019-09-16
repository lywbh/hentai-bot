package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

    public CQImage randomPic() {
        return randomPic(Rating.SAFE, null);
    }

    public CQImage randomPic(Rating rating) {
        return randomPic(rating, null);
    }

    public CQImage randomPic(String tags) {
        return randomPic(Rating.SAFE, tags);
    }

    public CQImage randomPic(Rating rating, String tags) {
        return randomPic(rating, tags, 100);
    }

    private CQImage randomPic(Rating rating, String tags, int bound) {
        if (bound == 0) {
            return null;
        }
        if (rating == Rating.UNKNOWN) {
            rating = Rating.SAFE;
        }
        if (tags == null) {
            tags = "";
        }
        int page = RandomUtils.getRandomInt(bound) + 1;
        String url = SourceConfig.SOURCE_KONACHAN + "?limit=100&page=" + page + "&tags=" + tags + "%20rating:" + rating.name().toLowerCase();
        String response = HttpUtils.get(url);
        if (response == null) {
            return null;
        }
        JSONArray respArray = JSON.parseArray(response);
        if (respArray.isEmpty()) {
            return randomPic(rating, tags, bound / 2);
        }
        int rc = RandomUtils.getRandomInt(respArray.size());
        JSONObject respJson = respArray.getJSONObject(rc);
        try {
            return new CQImage(respJson.getString("sample_url"));
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        }
    }

}
