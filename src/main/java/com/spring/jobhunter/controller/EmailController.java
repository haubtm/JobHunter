package com.spring.jobhunter.controller;

import com.spring.jobhunter.service.EmailService;
import com.spring.jobhunter.service.SubscriberService;
import com.spring.jobhunter.util.annotation.ApiMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/email")
    @ApiMessage("Send Email")
    //@Scheduled(cron = "*/60 * * * * *")
    //@Transactional
    public String sendEmail() {
        //emailService.sendEmail();
        //emailService.sendEmailFromTemplateSync("haubuidoi111@gmail.com", "Test Email", "job");
        subscriberService.sendSubscribersEmailJobs();
        return "Email sent successfully";
    }
}
