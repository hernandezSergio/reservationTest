package com.reservation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.reservation.model.Reservation;
import com.reservation.utils.Utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class ReservationSerivce {

	private Utils utils;

	private final static Logger logg = Logger.getLogger(ReservationSerivce.class.getName());
	private static Map<String, Reservation> list = new LinkedHashMap<String, Reservation>();

	public ReservationSerivce(Utils utils) {

		this.utils = utils;
	}

	@PostConstruct
	private void init() {
		try {
			File file = new File(getClass().getResource("/reservations.json").getFile());

			List<Reservation> initReservations = Arrays
					.asList(utils.customMapper().readValue(file, Reservation[].class));
			buildInitReservatations(initReservations);
		} catch (IOException e) {
			logg.warning(e.getMessage());
		}

	}

	public List<Reservation> getList() {

		return new ArrayList<Reservation>(list.values());
	}

	public ResponseEntity<String> addValue(Reservation reservation) {

		if (list.containsKey("Reservation " + reservation.getId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration already booked");
		}

		if (isRoomTaken(reservation)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration has rooms taken");

		}

		list.put("Reservation " + reservation.getId(), reservation);
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	public ResponseEntity<String> updateValue(Reservation reservation, String id) {
		if (!list.containsKey("Reservation " + id))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("registration not found");

		Reservation reservationUpdated = buildObject(reservation, id);
		if (null == reservationUpdated)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration has rooms taken");

		list.put("Reservation " + id, reservationUpdated);

		return ResponseEntity.status(HttpStatus.OK).body("");

	}

	private Reservation buildObject(Reservation reservation, String id) {
		Reservation temp = list.get("Reservation " + id);
		
		boolean isUpdatable = true;

		if (null != reservation.getRoomNumber()) {

			if (null == reservation.getReservations()) {

				reservation.setReservations(temp.getReservations());

				if (isRoomTaken(reservation)) {
					isUpdatable = false;
					return null;
				}
			}
			

		}
		
		for (Reservation item : list.values()) {
			System.out.println("");
		}

		if (null != reservation.getReservations()) {
			if (isRoomTakenPatch(reservation))
				return null;
			temp.setRoomNumber(reservation.getRoomNumber());
			temp.setReservations(reservation.getReservations());

		}

		if (null != reservation.getFullName() && isUpdatable) {
			temp.setFullName(reservation.getFullName());

		}

		return temp;

	}

	private void buildInitReservatations(List<Reservation> initReservations) {
		for (Reservation item : initReservations) {
			if (!list.containsKey("Reservation " + item.getId())) {
				list.put("Reservation " + item.getId(), item);
			}
		}

	}

	public boolean isRoomTaken(Reservation reservation) {

		for (Reservation item : list.values()) {
			if (reservation.getRoomNumber().equals(item.getRoomNumber())) {
				for (int i = 0; i < item.getReservations().size(); i++) {
					for (int j = 0; j < reservation.getReservations().size(); j++) {
						if (!item.getReservations().get(i).equals((reservation.getReservations().get(j)))) {
							return true;
						}

					}
				}
			}
		}

		return false;
	}

	public boolean isRoomTakenPatch(Reservation reservation) {

		for (Reservation item : list.values()) {
			for (int i = 0; i < item.getReservations().size(); i++) {
				for (int j = 0; j < reservation.getReservations().size(); j++) {
					if (item.getReservations().get(i).equals((reservation.getReservations().get(j)))) {
						if (reservation.getRoomNumber().equals(item.getRoomNumber()))
							return true;
					}

				}
			}
		}

		return false;
	}

	@PreDestroy
	public void preDestroy() {

		File file = new File(getClass().getResource("/reservations.json").getFile());

		try {
			Files.write(file.toPath(), utils.customMapper().writeValueAsString(list.values()).getBytes(),
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			logg.warning(e.getMessage());
		}

	}

}
