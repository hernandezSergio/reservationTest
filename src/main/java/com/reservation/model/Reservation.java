package com.reservation.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Reservation implements Serializable {

	private static final long serialVersionUID = -6645096007400516233L;
	
	private int id;
	private String fullName;
	private Integer roomNumber;

	@JsonProperty("reservations")
	private List<LocalDate> reservations;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<LocalDate> getReservations() {
		return reservations;
	}

	public void setReservations(List<LocalDate> reservations) {
		this.reservations = reservations;
	}

}
