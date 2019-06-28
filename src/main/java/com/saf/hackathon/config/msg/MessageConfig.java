package com.saf.hackathon.config.msg;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
@SuppressWarnings("deprecation")
@Configuration
@EnableAsync
@PropertySource({"classpath:error.properties" })
public class MessageConfig {
	
}