package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyw.config.LocalConfig;
import com.lyw.utils.HttpUtils;
import com.sobte.cqp.jcq.event.JcqApp;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SaucenaoModule {

    public Map<String, String> bestMatch(String imgUrl) {
        Map<String, String> result = new HashMap<>();
        try {
            String url = "https://saucenao.com/search.php?db=999&output_type=2&testmode=1&url="
                    + URLEncoder.encode(imgUrl, "UTF-8");
            String response = HttpUtils.get(url);
            JSONArray respJson = JSON.parseObject(response).getJSONArray("results");
            if (respJson.isEmpty()) {
                return result;
            }
            JSONObject dataJson = respJson.getJSONObject(0).getJSONObject("data");
            dataJson.forEach((k, v) -> {
                String value;
                if (v instanceof JSON) {
                    value = ((JSON) v).toJSONString();
                } else {
                    value = v.toString();
                }
                result.put(k, value);
            });
            return result;
        } catch (UnsupportedEncodingException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
        }
        return result;
    }

    public String findImgUrl(String imgStr) {
        FileReader fr = null;
        BufferedReader bf = null;
        try {
            File imgFile = new File(LocalConfig.LOCAL_PATH + "\\" + imgStr + ".cqimg");
            fr = new FileReader(imgFile);
            bf = new BufferedReader(fr);
            String line;
            while ((line = bf.readLine()) != null) {
                if (line.startsWith("url=")) {
                    return line.substring(4);
                }
            }
            return null;
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                JcqApp.CQ.logError("hentai-bot", e.getMessage());
            }
        }
    }

}
