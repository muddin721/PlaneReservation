package team9.Data; 

import java.text.ParseException;  


public class Plane {	 
	public static class SeatPosition {
		public int index, row, col;
		
		public SeatPosition(int index, int row, int col) {
			this.index = index;
			this.row = row;
			this.col = col;
		}
	}	
	
	//***************************
	//		   VARIABLESs
	//
	//***************************

	private String name;
	private String id;
	private String departure;
	private String arrival;
	private DateTime departureTime;
	private DateTime arrivalTime;

	private PClass[] pClass;
	
	//***************************
	//			GETTER
	//
	//***************************
	
	public String getName() {
		return name;
	}

	public String getID() {
		return id;
	}

	public DateTime getDepartureTime() {
		return departureTime;
	}

	public DateTime getArrivalTime() {
		return arrivalTime;
	}

	public String getDeparture() {
		return departure;
	}

	public String getArrival() {
		return arrival;
	}
	
	public PClass getClass(int index) {
		return pClass[index];
	}
	
	public int getClassCount() {
		return pClass.length;
	}

	//***************************
	//	      CONSTRUCTOR
	//
	//***************************
	
	private Plane() {
		super();
	}	
 
	//***************************
	//		 public METHODs
	//
	//***************************
	
	/** 좌석 번호로부터 좌석의 위치를 얻어 반환합니다. */
	public SeatPosition getSeatPosition(String seatID) {
		int index = 0, row = seatID.charAt(0) - 'a', col = seatID.charAt(1) - '1';
	 
	
		while(row >= pClass[index].getRowCount()) { 
			row -= pClass[index++].getRowCount();
			
			if(index >= pClass.length) {
				return null;
			}
		}
		 
		var pos = new SeatPosition(index, row, col);
	
		if((0 <= pos.row && pos.row < pClass[pos.index].getRowCount()) &&
				(0 <= pos.col && pos.col < pClass[pos.index].getColCount())) { 
			return pos;
		}
		else return null;
	}
	
	public String getSeatID(int index, int row, int col) {
		return getSeatID(new SeatPosition(index, row, col));
	} 
	
	public boolean checkSeatID(String seatID) { 
		return getSeatPosition(seatID) == null;
	}
	
	/** 지정된 좌석의 좌석 번호를 반환합니다.*/
	public String getSeatID(SeatPosition pos) {
		int total = 0;
		
		for(int i = 0; i <= pos.index; i++) {
			if(i == pos.index) {
				total += pos.row;
				
				break;
			}
			total += pClass[i].getRowCount();
		}
		
		return String.format("%c%d", 'a' + total, pos.col + 1);
	}

	@Override
	public boolean equals(Object o) {
		return id.equals(((Plane)o).id); 
	}

	public String toString(int index, boolean alsoReservation) {
		int total = 0;
		
		for(int i = 0; i <= index; i++) {
			if(i == index) {  
				return pClass[index].toString(total, alsoReservation);
			}
			total += pClass[i].getRowCount();
		}
		
		return null;
	} 
	
	public String toString(boolean alsoReservation) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("1) 출발지 : %s\n", this.departure));
		sb.append(String.format("2) 도착지 : %s\n", this.arrival));
		sb.append(String.format("3) 출발시간 : %s\n", this.departureTime.toString()));
		sb.append(String.format("4) 도착시간 : %s\n", this.arrivalTime));
		
		sb.append("좌석배치\n");
		
		for(int i = 0; i < pClass.length; i++) {
			sb.append(pClass[i].getName());
			sb.append("\n"); 
			sb.append(toString(i, alsoReservation));
		}
		
		return sb.toString();
	} 
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	//***************************
	//		    METHODs
	//
	//***************************
	
	/** 비행기 ID가 유효한지를 반환합니다. */
	private static boolean checkID(String id) {
		return true;
	}
	
	/** 데이터로부터 비행기 정보를 읽고 객체를 생성합니다. */ 
	protected static Plane parse(Data data) {
		Plane result = new Plane(); 
		
		try {			
			result.name = data.get("name");
			
			String id = data.get("id");
			
			if(checkID(id)) { 
				result.id = id;
			}
			else {
				throw new ParseException("파싱 오류", -1);
			}

			result.departure = data.get("departure");
			result.arrival = data.get("arrival"); 
 
			result.departureTime = DateTime.parseDateTime(data.get("departure_time"));
			result.arrivalTime = DateTime.parseDateTime(data.get("arrival_time"));
			
			result.pClass = new PClass[Integer.parseInt(data.get("class_count"))]; 
			String[] className = data.get("class_name").split("\n");
			String[] classPrice = data.get("class_price").split("\n");
			
			for(int i = 0; i < result.pClass.length; i++) {
				result.pClass[i] = PClass.parse(className[i], Integer.parseInt(classPrice[i]), data.get(String.format("class_%d", i)));
			} 
		}
		catch(KeyNotFoundException e) { 
			System.out.println("오류 : 키가 존재하지 않습니다.");
		}
		catch(NumberFormatException e) { 
			System.out.println("오류 : 숫자 입력이 잘못되었습니다.");
		} 
		catch (ParseException e) { 
			System.out.println("오류 : 날짜 입력이 잘못되었습니다.");
		}
		catch(IndexOutOfBoundsException e) {  
			System.out.println("오류 : 인덱스가 범위를 벗어났습니다.");
		} 
		
		return result; 
	}
}
