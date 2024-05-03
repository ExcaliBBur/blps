package com.example.lab.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;

    private String consumerGroup;

    private Integer partitionsNumber;

    private Short replicationFactor;

    private String outboundTopic;

    private String inboundTopic;

    private Short isr;

}
