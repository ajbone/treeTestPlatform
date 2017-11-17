package com.dashu.datashow.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yulei on 16/10/18.
 */
@Component
public class AlertMessage {

    @Scheduled(cron = "0 * * * * ?")
    public void job1() {

        // System.out.println("进行中");

    }
}