package team9.Data;

import java.util.HashMap;

import team9.Data.PClass.SeatType;

public class PlaneManager {

	public HashMap<String, Plane> planes = new HashMap<String, Plane>();
	
	private HashMap<String, User> reservation = new HashMap<String, User>(); 
	
	public User getReservation(String reservationId) {
		return reservation.get(reservationId);
	}
	
	public void reserve(User user, Plane plane, SeatPosition pos, SeatType type) {
		String reservationID = ReservationID.generateID(plane, pos, type);
		
		if(!user.reservationID.contains(reservationID)) { 
			user.reservationID.add(reservationID);
		}
		
		plane.getClass(pos.index).setSeatType(pos.row, pos.col, type); 
	} 
	
	public void reserve(User user, String reservationId) {
		Plane plane = planes.get(ReservationID.getPlaneID(reservationId));
		
		reserve(user, plane, plane.getSeatPosition(ReservationID.getSeatID(reservationId)), ReservationID.getSeatType(reservationId));
	} 
	
	public void cancel(String reservationId) {   
		User user = reservation.get(reservationId);
		
		Plane plane = planes.get(ReservationID.getPlaneID(reservationId));
		
		SeatPosition pos = plane.getSeatPosition(ReservationID.getSeatID(reservationId));
		
		user.reservationID.remove(reservationId);
		  
		plane.getClass(pos.index).setSeatType(pos.row, pos.col, SeatType.NONE);
		
		reservation.remove(reservationId); 
	}  
}
