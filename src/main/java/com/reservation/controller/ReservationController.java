package com.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.model.Reservation;
import com.reservation.service.ReservationSerivce;

@RestController
public class ReservationController {
	
	private ReservationSerivce reservationService;

	public ReservationController(ReservationSerivce reservationService) {

		this.reservationService = reservationService;
	}

	@GetMapping("/get")
	public List<Reservation> getList() {
		return reservationService.getList();
	}

	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody Reservation reservation) {
		return reservationService.addValue(reservation);
	}

	@PatchMapping("/{id}/update")
	public ResponseEntity<String> update(@RequestBody Reservation reservation, @PathVariable String id) {
		return reservationService.updateValue(reservation, id);
	}

}
