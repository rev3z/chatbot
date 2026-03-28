package com.guava.prototype.bridge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prototype.grpc")
public record GrpcBridgeProperties(String host, int port) {
}
