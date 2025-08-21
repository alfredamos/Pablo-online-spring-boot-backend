package com.alfredamos.springbootbackend.orders;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {
    private String privateKey;
    private String websiteUrl;
    private String webhookSecretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = privateKey;
    }

}

