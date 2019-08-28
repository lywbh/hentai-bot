package com.lyw;

import com.lyw.modules.SaucenaoModule;
import com.lyw.modules.YandereModule;
import com.sobte.cqp.jcq.entity.CQDebug;
import com.sobte.cqp.jcq.entity.IMsg;
import com.sobte.cqp.jcq.event.JcqApp;
import com.sobte.cqp.jcq.message.CQCode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Hentaibot extends HentaiAppAbstract {

    private YandereModule yandereModule;

    private SaucenaoModule saucenaoModule;

    public int startup() {
        CQ.logInfo("hentai-bot", "初始化图库插件...");
        yandereModule = new YandereModule();
        saucenaoModule = new SaucenaoModule();
        CQ.logInfo("hentai-bot", "初始化完毕");
        return 0;
    }

    public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg, int font) {
        if (yandereModule == null || saucenaoModule == null) {
            return IMsg.MSG_IGNORE;
        }
        if (msg.startsWith("!色图")) {
            String[] splitMsg = msg.split(" ");
            File pic;
            if (splitMsg.length < 2) {
                pic = yandereModule.randomPic();
            } else {
                pic = yandereModule.randomPic(yandereModule.rating(splitMsg[1]));
            }
            if (pic != null) {
                try {
                    JcqApp.CQ.sendGroupMsg(fromGroup, new CQCode().image(pic));
                } catch (IOException e) {
                    JcqApp.CQ.logError("hentai-bot", e.getMessage());
                }
            }
        } else if (msg.startsWith("!车来")) {
            String imageStr = new CQCode().getImage(msg.substring(3));
            String imgUrl = saucenaoModule.findImgUrl(imageStr);
            Map<String, String> bestMatch = saucenaoModule.bestMatch(imgUrl);
            if (!bestMatch.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                bestMatch.forEach((k, v) -> sb.append(k).append(" --> ").append(v).append("\n"));
                JcqApp.CQ.sendGroupMsg(fromGroup, sb.toString());
            } else {
                JcqApp.CQ.sendGroupMsg(fromGroup, "无匹配源");
            }

        }
        return IMsg.MSG_IGNORE;
    }

    public static void main(String[] args) {
        CQ = new CQDebug();
        Hentaibot demo = new Hentaibot();
        demo.startup();
        demo.enable();
        demo.groupMsg(0, 10006, 3456789012L, 3333333334L, "", "!色图 q", 0);
        demo.exit();
    }

}
