package com.dkg.wsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${wsserver.relay.host}")
    String relayHost;

    @Value("${wsserver.relay.port}")
    int relayPort;

    @Autowired
    Environment env;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("external")) {
            config.enableStompBrokerRelay("/queue")
                    .setRelayHost(relayHost)
                    .setRelayPort(relayPort)
                    .setSystemLogin("admin")
                    .setSystemPasscode("admin");
        } else {
            config.enableSimpleBroker("/queue");
        }
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/astronaut-websocket").setAllowedOrigins("*").withSockJS();
    }
}
