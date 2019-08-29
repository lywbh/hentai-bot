package com.lyw.utils;

import com.lyw.config.LocalConfig;
import com.sobte.cqp.jcq.event.JcqApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LocalUtils {

    public static String findImgUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }
        FileReader fr = null;
        BufferedReader bf = null;
        try {
            File imgFile = new File(LocalConfig.IMAGE_PATH + imageName + ".cqimg");
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
