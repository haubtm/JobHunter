package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Subscriber;

public interface SubscriberService {
    public boolean isExistsByEmail(String email);
    public Subscriber create(Subscriber subs);
    public Subscriber update(Subscriber subsDB, Subscriber subsRequest);
    public Subscriber findById(long id);
}
