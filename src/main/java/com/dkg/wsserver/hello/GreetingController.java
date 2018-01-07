package com.dkg.wsserver.hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class GreetingController {

    SimpleMessagingTemplate brokerMessagingTemplate;

    @MessageMapping("/prequalStarted")
    @SendToUser("/queue/prequalComplete")
    public String greeting(String message) throws Exception {
        Thread.sleep(5000); // simulated delay
        return new String(message + ":complete");
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/prequalGo")
    public String process() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });
        return new String("submitted");
    }

}