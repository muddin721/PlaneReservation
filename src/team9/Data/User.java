package team9.Data;

import java.text.ParseException;
import java.util.*;

public class User {
	
	//***************************
	//		   VARIABLESs
	//
	//***************************

	private String id;
	private String password;
	private String name;
	private DateTime birthday;
	private int mileage = 0;
	
	private boolean isAdministrator; 
	
	protected List<String> reservationID = new ArrayList<String>();
	
	//***************************
	//			GETTER
	//
	//***************************
	
	public String getID() {
		return id;
	}

	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMileage() {
		return mileage;
	}
 
	public DateTime getBirthday() {
		return birthday;
	}
	
	public boolean isAdministrator() {
		return isAdministrator;
	}

	public String getReservationID(int index) {
		return reservationID.get(index);
	}
	
	public int getReservationIDCount() {
		return reservationID.size();
	}

	//***************************
	//	      CONSTRUCTOR
	//
	//***************************
	
	private User() {  
		super();
	} 
	
	public User(String id, String password, String name, DateTime birthday) {
		this(id, password, name, birthday, false);
	}  
	
	public User(String id, String password, String name, DateTime birthday,  boolean isAdministrator ) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		
		this.isAdministrator = isAdministrator;
	} 
	
	//***************************
	//		    METHODs
	//
	//***************************
  
	/** 아이디가 형식에 부합하는지의 여부를 반환합니다. */
	public static boolean checkID(String id) {
		if(id.length() >= 1){
			return true;
		}
		return false;
	}
	
	/** 비밀번호가 형식에 부합하는지의 여부를 반환합니다. */
	public static boolean checkPassword(String password) {
		if(8 <= password.length() && password.length()<= 16){
			return true;
		}
		return false;
	} 
	
	/** 데이터로부터 유저 정보를 읽고 객체를 생성합니다. */
	protected static User parse(Data data) {
		User result = new User(); 
		
		try {
			result.id = data.get("id");  
			result.password = data.get("password");
			result.name = data.get("name");
			result.birthday = DateTime.parseDate(data.get("birthday")); 
			result.mileage = Integer.parseInt(data.get("mileage"));
			result.isAdministrator = data.get("admin").equals("true"); 
	
			String[] rID = data.get("reservationID").split("\n");
			
			for(int i = 0; i < rID.length; i++) { 
				if(rID[i].isBlank()) {
					break;	
				}
				
				result.reservationID.add(rID[i]); 
			}
		}
		catch(KeyNotFoundException e) { 
			e.printStackTrace();
		}
		catch(NumberFormatException e) { 
			e.printStackTrace();
		} 
		catch (ParseException e) { 
			e.printStackTrace();
		}
		
		return result; 
	}
	 
	protected static Data parseToData(User user) {
		Data result = new Data();
		
		result.set("id", user.id);  
		result.set("password", user.password);  
		result.set("name", user.name);  
		result.set("birthday", user.birthday.toString());
		result.set("mileage", Integer.toString(user.mileage));  
		result.set("isAdministrator", user.isAdministrator ? "true" : "false");

		  StringBuffer sb = new StringBuffer();
	      for(int i = 0; i < user.reservationID.size(); i++) {
	         sb.append(user.reservationID.get(i));
	         sb.append("\n");
	      }
	      String value = sb.toString();
		
		result.set("reservationID", value); 
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("아이디 : %s\n", id));
		sb.append(String.format("비밀번호 : %s\n", password));
		sb.append(String.format("이름 : %s\n", name));
		sb.append(String.format("생년월일 : %s\n", birthday.toString(true))); 
		sb.append("예약번호\n");
	  
		for(int i = 0; i < reservationID.size(); i++) {
			sb.append(reservationID.get(i));
			sb.append("\n");
	  	}  
		
		return sb.toString();
		
	} 
	
	@Override
	public boolean equals(Object o) {
		return id.equals(((User)o).id); 
	}
}
