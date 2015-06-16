package com.badlydone.aparto.sms;

import java.util.EventListener;

public interface aPartoSMSListener extends EventListener {
    public void SmsSent(aPartoSMSResult result);
}