package com.driver.services.impl;

import com.driver.Exceptions.CannotMakeException;
import com.driver.model.SpotType;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {


        ParkingLot parkingLot;
        try {
            // Attempt to retrieve the ParkingLot
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            // ParkingLot is found, you can use it here

        } catch (Exception e) {
            // Handle the case where the ParkingLot is not found
            throw new CannotMakeException("Cannot make reservation");
        }
        User user;
        try {
            // Attempt to retrieve the ParkingLot
            user = userRepository3.findById(userId).get();
            // ParkingLot is found, you can use it here
        } catch (Exception e) {
            // Handle the case where the ParkingLot is not found
            throw new CannotMakeException("Cannot make reservation");
        }

        int minimumPrice = Integer.MAX_VALUE;
        Spot reserveSpot = null;
        for (Spot spot : parkingLot.getSpotList()) {
            SpotType spotType = spot.getSpotType();
            int wheels = 0;
            if (spotType.equals(SpotType.TWO_WHEELER)) {
                wheels = 2;
            } else if (spotType.equals(SpotType.FOUR_WHEELER)) {
                wheels = 4;
            } else {
                wheels = Integer.MAX_VALUE;
            }

            if (spot.getOccupied() == false && numberOfWheels <= wheels && spot.getPricePerHour() < minimumPrice) {
                minimumPrice = spot.getPricePerHour();
                reserveSpot = spot;
            }
        }
        if (reserveSpot==null) {
            throw new CannotMakeException("Cannot make reservation");
        }

        reserveSpot.setOccupied(true);
        Reservation reservation = new Reservation();
        reservation.setSpot(reserveSpot);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        userRepository3.save(user);
        user.getReservationList().add(reservation);
        spotRepository3.save(reserveSpot);
        return reservation;
        //finished
    }
}
