package com.dkg.wsserver.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

@Controller
public class GreetingController {

    @Autowired
    SimpMessagingTemplate template;

    Map<String,String> users = new HashMap<>();

    @MessageMapping("/prequalStarted")
    @SendToUser("/queue/prequalComplete")
    public String greeting(String message, Message messageObject) throws Exception {

        double random = Math.random();

        Executors.newSingleThreadExecutor().submit(new Thread(){
            public void run() {
                async(messageObject, random);
            }
        });

        Thread.sleep(1000); // simulated delay

        return new String("message:" + message + ",task:" + random + ",status:requested");

    }

    private void async(Message messageObject, Double taskId){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String user = (String)messageObject.getHeaders().get("simpSessionId");

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(user);
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(user,"/queue/prequalComplete", taskId+":complete", headerAccessor.getMessageHeaders());

    }

//
//    @EventListener
//    public void handleSubscribeEvent(SessionSubscribeEvent event) {
//        template.convertAndSendToUser(event.getUser().getName(), "/queue/notify", "GREETINGS");
//        System.out.println(event.getUser());
//    }
}