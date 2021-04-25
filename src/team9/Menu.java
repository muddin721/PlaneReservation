package team9;

import java.text.ParseException;
import java.util.Scanner;

import team9.Data.*;
import team9.Data.PClass.SeatType;

public class Menu { 

	Scanner scanner = new Scanner(System.in);
	
	RunData rData;
	
	public Menu(RunData rData) {
		this.rData = rData;
	}
	
	public void show() {
		mainMenu();
	}
	
	private void mainMenu() { 
		
		LOOP : while(true) {
			System.out.println("1) 회원가입    2) 로그인    3) 예약 확인    4) 프로그램 종료");
			
			int input = scanner.nextInt();
			
			scanner.nextLine(); 
			
			switch(input) {
			case 1:
				registerMenu();
				break;
			case 2:
				loginMenu();
				break;
			case 3:

				break;
			case 4:
				break LOOP;
			default:
				System.out.println("입력이 올바르지 않습니다.");
			}
			
		} 
	}

    private void userMenu() {

		LOOP : while(true) {
	        System.out.println("1) 예약    2) 예약 확인    3) 예약 변경    4) 예약 취소    5)로그아웃");
			
			int input = scanner.nextInt();
			
			scanner.nextLine(); 
			
			switch(input) {
			case 1:
				reserveMenu();
				break;
			case 2: 

				break;
			case 3:
				reserveChangeMenu();
				break;
			case 4: 
				reserveCancelMenu();
				break;
			case 5:
				if(logoutMenu()) { 
					break LOOP;
				}
			default:
				System.out.println("입력이 올바르지 않습니다.");
			}
			
		}  
    } 
    
    private void adminMenu() {

		LOOP : while(true) {
		 	System.out.println("1) 비행정보확인    2) 회원관리    3) 로그아웃");
			
			int input = scanner.nextInt();
			
			scanner.nextLine(); 
			
			switch(input) {
			case 1:
 				planeInfoMenu(); 
 				break;
 			case 2:
 				userInfoMenu(); 
 				break;
 			case 3:
 				if(logoutMenu()) { 
 	 				break LOOP;
 				}
			default:
				System.out.println("입력이 올바르지 않습니다.");
			} 
		}   
    }
    
	private void reserveMenu() {
		PlaneManager manager = rData.getPlaneManager();
		
		var planes = manager.planes;
		var keySet = planes.keySet();
		
		String[] keys = keySet.toArray(new String[keySet.size()]);
		
		for(int i = 0; i < keys.length; i++) { 
			Plane plane = planes.get(keys[i]);
			
			System.out.printf("%d) %s || 목적지 : %s || 출발시간 : %s || 도착시간 : %s \n", i + 1, plane.getName(), plane.getDeparture(), plane.getDepartureTime().toString(), plane.getArrivalTime().toString());
			System.out.println("=========================================================================");
		}
		
		System.out.print("예약할 비행기를 선택해주세요 : ");
		int input = scanner.nextInt() - 1; scanner.nextLine();
		
		Plane plane = planes.get(keys[input]);
		
		System.out.println("비행기 클래스를 선택해주세요 : ");
		for(int i = 0 ; i < plane.getClassCount(); i++) {
			System.out.println("=============");
			System.out.println(plane.getClass(i).getName());
			System.out.println("=============");
			
			System.out.print(plane.toString(i, true));
		} 

		System.out.print("좌석 번호를 입력해주세요 : ");
		
		String seatID = scanner.nextLine();
		
		var pos = plane.getSeatPosition(seatID);
		
		System.out.print("승객 나이를 입력해주세요 : ");
		int age = scanner.nextInt(); scanner.nextLine();
		 
		manager.reserve(rData.currentUser, plane, pos, (age > 11) ? SeatType.ADULT : SeatType.CHILD); 	
		
		rData.save();
	}
	
	private void reserveCheck() {
		PlaneManager manager = rData.getPlaneManager();
		
		User user = rData.currentUser;
		User found;
		
	    System.out.print("예약 번호 : ");
	    String reserveID = scanner.nextLine();
	      
	    if((found = manager.getReservation(reserveID)) != null) {
	    	if(user.equals(found)) { 
		    	String planeID = ReservationID.getPlaneID(reserveID);
		    	
		    	Plane plane = manager.planes.get(planeID);
		    	
		    	 System.out.println("1. 이름 :");
		         System.out.println(user.getName());
		         
		         System.out.println("2. 목적지 :");
		         System.out.println(plane.getDeparture());
		         
		         System.out.println("3. 좌석번호 :");
		         System.out.println(ReservationID.getSeatID(reserveID));
		         
		         System.out.println("4. 출발 날짜 :");
		         plane.getDepartureTime().toString(); 
		         
		         return;
	    	} 
	    }
	  
	    System.out.println("예약을 찾을 수 없습니다.");
	   }
	
	private boolean reserveCancelMenu() {
		PlaneManager manager = rData.getPlaneManager();
		 
		User user = rData.currentUser;
		User found;
		
		System.out.println("사용자의 예약번호 리스트입니다.");
		
		for(int i =0;i < user.getReservationIDCount();i++) {
			user.getReservationID(i);
		}
		
		System.out.print("예약번호 : ");
		String reserveID= scanner.nextLine();
		
		boolean hasReserveNum = false;
		
		if((found = manager.getReservation(reserveID)) != null) {
			 if(user.equals(found)) {
					System.out.println("예약이 취소되었습니다.");
					manager.cancel(reserveID);
					
					hasReserveNum = true;
					
					rData.save(); 
			 }
		}  
		
		if(hasReserveNum == false) {
			System.out.println("취소할 비행이 없습니다. 예약 번호를 다시 확인해주세요"); 
		}

		return hasReserveNum;
	}
	
	private void reserveChangeMenu() {
		if(reserveCancelMenu()) {
			System.out.println("예약 화면으로 넘어갑니다.");
			reserveMenu();
		}		
	}

    private void registerMenu() { 
	        String name, id, password, birthday;
	        DateTime nBirthday;
	        
			while(true) {
				System.out.print("아이디를 입력하세요 : ");
				id = scanner.nextLine();
				
				if(rData.users.containsKey(id)) { 
					System.out.println("중복되는 아이디입니다.");
					
					continue;
				}
				else { 
					if(User.checkID(id)) break; 
				}
				
				System.out.println("아이디 형식에 부합하지 않습니다.");
			}
			
			while(true) {
				System.out.print("비밀번호를 입력하세요 : ");
				password = scanner.nextLine();
				
				if(User.checkPassword(password)) break; 
				
				System.out.println("비밀번호 형식에 부합하지 않습니다.");
			}
			
			System.out.print("이름을 입력하세요 : ");
			name = scanner.nextLine(); 
			
			while (true) {
                System.out.print("생년월일을 입력하세요 : ");
                birthday = scanner.nextLine();
                
            	try {
            		nBirthday = DateTime.parseDate(birthday);
            		
            		break;
            	} catch (ParseException e){
            	    System.out.println("생년월일 형식에 부합하지 않습니다.");
        		} 
            }
  
			rData.users.put(id, new User(id, password, name, nBirthday));
			
			rData.save();
			
			System.out.println("회원가입이 완료되었습니다.");
    }

    private void loginMenu(){ 
   	  System.out.print("아이디 : ");
   	  
   	  String id = scanner.nextLine();
   	  
   	  System.out.print("비밀번호 : ");
   	  
   	  String password = scanner.nextLine();
   	  
   	  if(rData.users.containsKey(id)) {
   		  User user = rData.users.get(id);
   		  
   		  if(user.getPassword().equals(password)) {
   			  
   			rData.currentUser = user;
   			  
   			if(user.isAdministrator()) {
   				adminMenu();
   			}
   			else {
   				userMenu();
   			}
   		  }
   		  else {
   			  System.out.println("비밀번호가 일치하지 않습니다.");
   		  }
   	  }
   	  else {
   		  System.out.println("아이디가 일치하지 않습니다.");
   	  }
	}

    private boolean logoutMenu() {
     	System.out.println("로그아웃 하시겠습니까?(y/n))");
     	
   		String input = scanner.nextLine(); //예외처리
   		
   		char c = input.charAt(0);
   		
   		if(c == 'y' || c == 'Y') {
   			System.out.println("로그아웃 되었습니다.");
   			
   			rData.currentUser = null;
   			
   			return true;
   		}
   		else {
   			System.out.println("메인으로 돌아갑니다.");
   			
  			return false;
   		}
   	} 
    
    private void planeInfoMenu() {
    	PlaneManager manager = rData.getPlaneManager();
		
		var planes = manager.planes;
		var keySet = planes.keySet(); 
		
		String[] keys = keySet.toArray(new String[keySet.size()]);
		
 	    for(int i = 0; i < keys.length; i++) {
 	       System.out.printf("%d) %s \n", i + 1, planes.get(keys[i]).getName());
 	       System.out.println("=========================================================================");
 	    }
 	    
 	    System.out.print("비행기를 선택해주세요 : ");
 	    int input = scanner.nextInt() - 1; scanner.nextLine();
 	    
 	    Plane plane = planes.get(keys[input]);
 	    
 	    System.out.printf(plane.toString());
 	    
 	    System.out.print("계속하려면 아무키나 입력하세요 : ");
 	    
	    scanner.nextLine();
    }

    private void userInfoMenu() {
  	   System.out.print("아이디를 입력하세요 : ");
  	   String id = scanner.nextLine();
  	   
  	   User user = rData.users.get(id);
  
  	   System.out.printf(user.toString());
	    
	    System.out.print("계속하려면 아무키나 입력하세요 : ");
	    
	    scanner.nextLine();
     }    
}
