package com.packaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for Smart Returnable Packaging Tracking and Lifecycle Management System (SRPT-LMS)
 * 
 * @SpringBootApplication enables:
 * 1. @Configuration - Marks the class as a source of bean definitions
 * 2. @EnableAutoConfiguration - Tells Spring Boot to configure beans based on classpath dependencies
 * 3. @ComponentScan - Automatically discovers controllers, services, repositories, and entities
 */
@SpringBootApplication
public class PackagingTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackagingTrackingApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("  SRPT-LMS Backend Application Started Successfully on Port 8080 ");
        System.out.println("  API Base URL: http://localhost:8080/api                       ");
        System.out.println("=================================================================");
    }
}
