package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
