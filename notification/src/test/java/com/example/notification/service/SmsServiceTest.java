package com.example.notification.service;

import org.junit.Test;

public class SmsServiceTest {
    SmsService smsService = new SmsService();

    @Test
    public void testInit() throws Exception {
        smsService.init();
    }

    @Test
    public void testSendSms() throws Exception {
        smsService.sendSms("toPhone", "message");
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme