package com.bootdang.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerYzm {
    private static final Timer timer = new Timer();
    public static void  sendTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                ShiroUtils.getSubjct().getSession().removeAttribute("passcode");//120秒后清理session
            }
        },120000);
    }
    public static void timeClose(){
        timer.cancel();
    }
}
