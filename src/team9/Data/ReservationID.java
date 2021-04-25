package team9.Data;

import java.text.SimpleDateFormat;

import team9.Data.PClass.SeatType;

public class ReservationID {  
	
	private ReservationID() {
		super();
	}

	//***************************
	//		 public METHODs
	//
	//*************************** 
	
	public static String getPlaneID(String reservationID) {
		return reservationID.substring(0, 2);
	} 
	
	public static String getSeatID(String reservationID) {
		return reservationID.substring(10, 12);
	} 

	public static SeatType getSeatType(String reservationID) {
		return (reservationID.charAt(13) == 'a') ? SeatType.ADULT : SeatType.CHILD ;
	} 
	
	public static String generateID(Plane plane, int index, int row, int col, SeatType type) {
		return generateID(plane, new Plane.SeatPosition(index, row, col), type);
	}
	
	/** 지정된 좌석의 예약 번호를 반환합니다.*/
	public static String generateID(Plane plane, Plane.SeatPosition pos, SeatType type) {
		if(type == SeatType.NONE) {
			return null;
		}
  
		return String.format("%s-%s-%s-%c", plane.getID(), new SimpleDateFormat("yyMMdd").format(plane.getDepartureTime()),
				plane.getSeatID(pos), (type == SeatType.ADULT) ? 'a' : 'c');
	} 
	
	public static boolean isValid(String reservationID) {
		return reservationID.matches("^[A-Z][0-9]-[0-9]{6}-[a-z][1-9]-[ac]$");
	}
	
	public static boolean isValidSeatID(String SeatID) {
		return SeatID.matches("^[a-z][1-9]$");
	}
}
