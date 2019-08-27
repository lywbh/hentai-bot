package com.lyw;

import com.lyw.config.LocalConfig;
import com.lyw.config.SourceConfig;
import com.lyw.modules.YandereModule;
import com.sobte.cqp.jcq.entity.CQDebug;
import com.sobte.cqp.jcq.entity.IMsg;
import com.sobte.cqp.jcq.event.JcqApp;
import com.sobte.cqp.jcq.message.CQCode;

import java.io.File;
import java.io.IOException;

public class Hentaibot extends HentaiAppAbstract {

    private YandereModule yandereModule;

    public int startup() {
        CQ.logInfo("hentai-bot", "初始化图库...");
        yandereModule = new YandereModule(SourceConfig.SOURCE_KONACHAN, LocalConfig.LOCAL_PATH);
        CQ.logInfo("hentai-bot", "初始化完毕");
        return 0;
    }

    public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg, int font) {
        if (yandereModule == null) {
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
