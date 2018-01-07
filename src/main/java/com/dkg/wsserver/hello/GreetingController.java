package com.dkg.wsserver.hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/prequalStarted")
    @SendToUser("/queue/prequalComplete")
    public String greeting(String message) throws Exception {
        Thread.sleep(5000); // simulated delay
        return new String(message + ":complete");
    }

}