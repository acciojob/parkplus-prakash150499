package com.driver.services.impl;

import com.driver.model.PaymentMode;
import com.driver.model.Payment;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        String paymentMode=mode.toLowerCase();
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        if(!paymentMode.equals("cash")&&!paymentMode.equals("card")&&!paymentMode.equals("upi"))
        {
            throw new Exception("Payment mode not detected");
        }

        int billAmount=reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();
        if(amountSent<billAmount)
        {
            throw new Exception("Insufficient Amount");
        }
        Payment payment=new Payment();
        if(mode.equals("cash"))
        {
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if(mode.equals("card"))
        {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else
        {
            payment.setPaymentMode(PaymentMode.CASH);
        }
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
return payment;
    }
}
