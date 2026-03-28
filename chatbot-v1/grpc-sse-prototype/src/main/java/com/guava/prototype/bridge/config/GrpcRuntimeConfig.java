package com.guava.prototype.bridge.config;

import com.guava.prototype.bridge.grpc.FakeLlmBridgeService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({GrpcBridgeProperties.class, OpenAiMockProperties.class})
public class GrpcRuntimeConfig {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    Server grpcServer(GrpcBridgeProperties properties, FakeLlmBridgeService fakeLlmBridgeService) {
        return ServerBuilder.forPort(properties.port())
                .addService(fakeLlmBridgeService)
                .build();
    }

    @Bean(destroyMethod = "shutdownNow")
    ManagedChannel managedChannel(GrpcBridgeProperties properties) {
        return ManagedChannelBuilder.forAddress(properties.host(), properties.port())
                .usePlaintext()
                .build();
    }

    @Bean
    WebClient openAiWebClient(OpenAiMockProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
