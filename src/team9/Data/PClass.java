package team9.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
 
// 비행기 클래스(등급)의 객체를 생성하기 위한 클래스(원형)
public class PClass { 
	public enum SeatType{
		NONE, ADULT, CHILD
	}
	
	private enum ElementType{
		EXIT, LAV, WIN
	}

	private static class Element{
		private int index;
		private ElementType type;
		
		public int getIndex() {
			return index;
		}

		public ElementType getType() {
			return type;
		}

		public Element(int index, ElementType type) {
			this.index = index;
			this.type = type;
		}
	}

	//***************************
	//		   VARIABLEs
	//
	//***************************
	
	private String name;
	private int price;
	
	private boolean[][] isAvailable;
	private SeatType[][] seatType; 
	private List<Element> element = new ArrayList<Element>(); 
	
	//***************************
	//			GETTER
	//
	//***************************
	
	/** 비행기 클래스의 이름을 반환합니다. */
	public String getName() {
		return name;
	}
 
	public int getPrice() {
		return price;
	}
	
	/** 행의 수를 반환합니다. */
	public int getRowCount() {
		return isAvailable.length;
	}
	
	/** 열의 수를 반환합니다. */
	public int getColCount() {
		return isAvailable[0].length;
	}
	
	/** 해당 위치에 좌석이 존재하는지를 반환합니다(예약 정보와 상관 없이). */
	public boolean isAvailable(int row, int col) {
		return isAvailable[row][col];
	}
	
	/** 해당 좌석의 예약 정보를 반환합니다. */
	public SeatType getSeatType(int row, int col) {
		return seatType[row][col];
	} 
	
	//***************************
	//			SETTER
	//
	//***************************
	 
	protected void setSeatType(int row, int col, SeatType type) {
		this.seatType[row][col] = type;
	} 
	
	//***************************
	//	      CONSTRUCTOR
	//
	//***************************
	
	private PClass() {
		super();
	}
 
	private PClass(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	//***************************
	//		 public METHODs
	//
	//***************************

	public String toString(int startIndex, boolean alsoReservation) {  
		StringBuilder sb = new StringBuilder();
		
		sb.append("  ");
		for(int i = 0; i < getColCount(); i++) {
			sb.append(String.format("%2d", i + 1));
		}
		sb.append("\n");  
		
		int index = 0;
		
		for(int i = 0; i <= getRowCount(); i++) { 
			
			boolean isWIN = false;
			while(index < element.size() && element.get(index).getIndex() <= i) { 
				switch(element.get(index).getType()) {
				case EXIT: 
					sb.append("<EXIT\n");
					break;
				case LAV:
					sb.append("화장실\n");
					break;
				case WIN:
					isWIN = true;
					break; 
				} 
				index++;
			}
			
			if(i == getRowCount()) {
				break;
			}
			
			// 현재 행이 창문석인 경우 문자 'ㅣ'를 출력합니다.
			if(isWIN) { 
				sb.append(String.format("%cㅣ", 'a' + startIndex + i));  
			}
			else { 
				sb.append(String.format("%-2c", 'a' + startIndex + i ));  
			} 
			
			for(int j = 0; j < getColCount(); j++) {
				if(alsoReservation) {
					if(isAvailable[i][j]) {
						if(seatType[i][j] == SeatType.NONE) {
							sb.append(String.format("%2c", 'o'));
							}
						else {
							sb.append(String.format("%2c", (seatType[i][j] == SeatType.CHILD) ? 'c' : 'x')); 
						}
					}
					else {
						sb.append(String.format("%2c", ' '));
					}
				}
				else { 
					sb.append(String.format("%2c", isAvailable[i][j] ? 'o' : ' '));
				}
			}
			
			// 현재 행이 창문석인 경우 문자 'ㅣ'를 출력합니다.
			if(isWIN) sb.append(" ㅣ"); 
			
			sb.append("\n");
		}
		
		return sb.toString();
	} 

	//***************************
	//		    METHODs
	//
	//***************************

	/** 비행기 좌석 데이터 요소의 문자열을 파싱하여 객체로 만듭니다. */
	protected static PClass parse(String name, int price, String str) {
		PClass result = new PClass(name, price);
		
		List<boolean[]> seat = new ArrayList<boolean[]>();
				
		try (BufferedReader reader = new BufferedReader(new StringReader(str))){
			String line;
			
			for(int i = 0; (line = reader.readLine()) != null; i++) { 
				 if(line.isBlank() || line.contains(" ")) {
					throw new ParseException("파싱 오류", i);
				 }
				 
				 if(line.length() == 1) {
					switch(line.charAt(0)) {
					case 'E' :
						result.element.add(new Element(seat.size(), ElementType.EXIT));
						continue;
					case 'L' :
						result.element.add(new Element(seat.size(), ElementType.LAV));
						continue;
					} 
				 } 
				 
				 boolean isWIN;
				 
				 if(isWIN = line.endsWith(":")) {
					 result.element.add(new Element(seat.size(), ElementType.WIN));
				 }
				 
				 boolean[] row = new boolean[line.length() - (isWIN ? 1 : 0)];
				 
				 if(seat.size() != 0 && seat.get(0).length != row.length) {
					 // 오류 발생
				 }
				 
				 for(int index = 0; index < row.length; index++) {
					 char c = line.charAt(index);
					
					 if(c == 'O' || c == 'X') {
						 row[index] = (c == 'O') ? true : false;
					 }
					 else {
						 // 오류 발생
					 }
				 }
				 
				 seat.add(row);
			} 
			 
		} catch (IOException e) {
			 
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		result.isAvailable = seat.toArray(new boolean[seat.size()][]);
		result.seatType = new SeatType[result.getRowCount()][result.getColCount()];
		
		for(int i = 0; i < result.getRowCount(); i++) {
			for(int j = 0; j < result.getColCount(); j++) {
				result.seatType[i][j] = SeatType.NONE;
			}
		}
		
		return result; 
	}  
}
