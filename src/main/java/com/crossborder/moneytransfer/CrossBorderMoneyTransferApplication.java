package com.crossborder.moneytransfer;

import com.crossborder.moneytransfer.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
/** Boots the Cross Border Money Transfer API and enables typed JWT settings. */
public class CrossBorderMoneyTransferApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrossBorderMoneyTransferApplication.class, args);
    }
}
