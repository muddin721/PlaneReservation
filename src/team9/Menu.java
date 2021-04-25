package team9;

import java.text.ParseException;
import java.util.InputMismatchException;
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
				NonUserReserveCheckMenu();
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
	        System.out.println("1) 예약    2) 예약 확인    3) 예약 변경    4) 예약 취소    5) 로그아웃");
			
			int input = scanner.nextInt();
			
			scanner.nextLine(); 
			
			switch(input) {
			case 1:
				reserveMenu();
				break;
			case 2: 
				reserveCheckMenu();
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
		
		if(planes.size() == 0) {
			System.out.println("예약 가능한 비행기가 존재하지 않습니다."); 
		    System.out.print("계속하려면 아무키나 입력하세요 : ");
   
		    scanner.nextLine();
		    
		    return;
		}
		
		for(int i = 0; i < keys.length; i++) { 
			Plane plane = planes.get(keys[i]);
			
			System.out.printf("%d) %s ||  %s -> %s  || 출발시간 : %s || 도착시간 : %s \n", i + 1, plane.getID(), plane.getDeparture(), plane.getArrival(), plane.getDepartureTime().toString(), plane.getArrivalTime().toString());
			System.out.println("=========================================================================");
		}
		
		Plane plane;
		
		while(true) {
			System.out.print("예약할 비행기를 선택해주세요 : ");
			
			try { 
				int input = scanner.nextInt() - 1; scanner.nextLine();

				if(0 <= input && input < planes.size()) { 
					plane = planes.get(keys[input]);
					
					break; 
				}
				
				System.out.println("해당 비행기는 존재하지 않습니다.");
			}
			catch(InputMismatchException e) { 
				scanner.nextLine();
				
				System.out.println("올바른 입력을 해주시기 바랍니다.");
			}
		} 

		for(int i = 0 ; i < plane.getClassCount(); i++) {
			System.out.println("=============");
			System.out.println(plane.getClass(i).getName());
			System.out.println("=============");
			
			System.out.print(plane.toString(i, true));
		}
		  
		String seatID;
		Plane.SeatPosition pos; 
		PClass pClass; 
		
		while(true) {
			System.out.print("좌석 번호를 입력해주세요 : ");
			
			seatID = scanner.nextLine();
			
			if(ReservationID.isValidSeatID(seatID)) { 
				
				pos = plane.getSeatPosition(seatID); 
				
				if(pos != null) {
					pClass = plane.getClass(pos.index); 
					
					if(plane.getClass(pos.index).isAvailable(pos.row, pos.col)) {  
						if(pClass.getSeatType(pos.row, pos.col) == SeatType.NONE) {
							break;
						} 
						else {
							System.out.println("이미 예약 완료된 좌석입니다.");
							
							continue;
						}
					}
				} 
				System.out.println("해당 좌석은 존재하지 않습니다.");   
			} 
			else { 
				System.out.println("문법규칙을 지켜 입력해주십시오.");
			}
		}
		
		while(true) {
			System.out.print("승객 나이를 입력해주세요 : ");
			
			try {
				int age = scanner.nextInt(); scanner.nextLine();
				
				if(1 <= age && age < 100) { 
					
					int price = pClass.getPrice();
					
					while(true) {
						System.out.println("총 " + price + "원입니다. 결제하시겠습니까? (y/n)");
						String payment = scanner.nextLine();
						
						if(payment.equals("y")||payment.equals("Y")) {
							System.out.println("결제가 완료되었습니다.");
							
							manager.reserve(rData.currentUser, plane, pos, (age > 11) ? SeatType.ADULT : SeatType.CHILD); 
							 
							rData.currentUser.addMileage((int)Math.round((double)price * 0.1));
							 
							rData.save();
							
							return;
						}
						else if(payment.equals("n")||payment.equals("N")) {
							System.out.println("결제가 취소되었습니다.");
							
							return; 
						}
						else {
							System.out.println("입력이 올바르지 않습니다."); 
						}
					} 		
				}
				else { 
					System.out.println("1~99 사이의 숫자를 입력해주세요"); 
				}  
			}
			catch(InputMismatchException e) { 
				scanner.nextLine();
				
				System.out.println("올바른 입력을 해주시기 바랍니다.");
			}
		}
	}  
    
    private void NonUserReserveCheckMenu() {
    	PlaneManager manager = rData.getPlaneManager();
		
		User found;
		 
		System.out.print("예약 번호 : ");
		String reserveID = scanner.nextLine();
		    
		if(!ReservationID.isValid(reserveID) ) {
			System.out.println("올바른 입력을 해주시기 바랍니다."); 
		    System.out.print("계속하려면 아무키나 입력하세요 : ");
   
		    scanner.nextLine();
		    
		    return;
		}
		    
		if((found = manager.getReservation(reserveID)) != null) {
	    	String planeID = ReservationID.getPlaneID(reserveID);
	    	
	    	Plane plane = manager.planes.get(planeID);
	    	
	    	 System.out.print("1. 이름 :");
	         System.out.println(manager.getReservation(reserveID).getName());
	         
	         System.out.print("2. 목적지 :");
	         System.out.println(plane.getDeparture());
		         
	         System.out.print("3. 좌석번호 :");
	         System.out.println(ReservationID.getSeatID(reserveID));
		         
	         System.out.print("4. 출발 날짜 :");
	         System.out.println(plane.getDepartureTime().toString()); 
	         
	         return;
		}  

	    System.out.println("예약을 찾을 수 없습니다.");
	}

	private void reserveCheckMenu() {
		PlaneManager manager = rData.getPlaneManager();

		User user = rData.currentUser;
		User found;
		 
		System.out.print("예약 번호 : ");
		String reserveID = scanner.nextLine();
		    
		if(!ReservationID.isValid(reserveID)) {
			System.out.println("올바른 입력을 해주시기 바랍니다."); 
		    System.out.print("계속하려면 아무키나 입력하세요 : ");
   
		    scanner.nextLine();
		    
		    return;
		}
		    
		if((found = manager.getReservation(reserveID)) != null) {
			if(user.equals(found)) { 
		    	String planeID = ReservationID.getPlaneID(reserveID);
		    	
		    	Plane plane = manager.planes.get(planeID);
		    	
		    	 System.out.print("1. 이름 :");
		         System.out.println(manager.getReservation(reserveID).getName());
		         
		         System.out.print("2. 목적지 :");
		         System.out.println(plane.getDeparture());
			         
		         System.out.print("3. 좌석번호 :");
		         System.out.println(ReservationID.getSeatID(reserveID));
			         
		         System.out.print("4. 출발 날짜 :");
		         System.out.println(plane.getDepartureTime().toString()); 
		         
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

        
        while (true) {
            System.out.print("이름을 입력하세요 : ");
            name = scanner.nextLine();

            if (User.checkName(name)) break;

            System.out.println("이름 형식에 부합하지 않습니다.");
        }

        while (true) {
            System.out.print("생년월일을 입력하세요 : ");
            birthday = scanner.nextLine();

            try {
                nBirthday = DateTime.parseDate(birthday);

                break;
            } catch (ParseException e) {
                System.out.println("생년월일 형식에 부합하지 않습니다.");
            }
        }
        
        while (true) { 
            System.out.print("아이디를 입력하세요 (8~16 글자의 영문, 숫자로 입력하세요) : ");
            id = scanner.nextLine();

            if (rData.users.containsKey(id)) {
                System.out.println("중복되는 아이디입니다.");

                continue;
            }
            else {
                if (User.checkID(id)) break;
            }

            System.out.println("아이디 형식에 부합하지 않습니다.");
        }

        while (true) {
            String passwordCheck; 
            
            System.out.print("비밀번호를 입력하세요 : ");
            password = scanner.nextLine();

            if (User.checkPassword(password)) {
                System.out.print("비밀번호 확인 : ");
                passwordCheck = scanner.nextLine();
                if (passwordCheck.equals(password)) break;
                else
                {
                    System.out.print("비밀번호 확인 실패했습니다.\n");
                }
            }
            
            System.out.println("비밀번호 형식에 부합하지 않습니다.");
        } 
        
        rData.users.put(id, new User(id, password, name, nBirthday));

        rData.save();

        System.out.println("회원가입이 완료되었습니다.");
    }

    private void loginMenu() {

        System.out.print("아이디 : ");
        String id = scanner.nextLine();
        
        String password;
        int passwordCount = 0; 

        if (rData.users.containsKey(id)) {
            User user = rData.users.get(id);
 
            do {
            	 System.out.print("비밀번호 : ");
                 password = scanner.nextLine();
                 
                 passwordCount += 1;
                 
                 if (passwordCount >= 5) {
                     System.out.print("비밀번호 5회 오류입니다. 메뉴로 돌아갑니다.\n");
                    
                     return;
                 }
                 
            } while(!user.getPassword().equals(password));
            
            if (user.getPassword().equals(password)) {

                rData.currentUser = user;

                if (user.isAdministrator()) {
                    adminMenu();
                } else {
                    userMenu();
                }
            } else {
                System.out.println("비밀번호가 일치하지 않습니다.");
            }
        } else {
            System.out.println("아이디가 일치하지 않습니다.");
        }
    }

    private boolean logoutMenu() {
		
   		while(true)
   		{
   			System.out.println("로그아웃 하시겠습니까?(y/n))");
   			
   	   		String input = scanner.nextLine(); //예외처리		
   	   		char c = input.charAt(0);
   			
			if (c == 'y' || c == 'Y') {
				System.out.println("로그아웃 되었습니다.");

				rData.currentUser = null;
				return true;
			}

			else if (c == 'n' || c == 'N') {
				System.out.println("로그아웃 취소되었습니다.");
				return false;
			}

			else {
				System.out.println("입력이 올바르지 않습니다.");
			}
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
 	    
 	    int input = 0;
 	    
 	    try {
			input = scanner.nextInt() - 1;
			scanner.nextLine();
			if (input < keys.length && input >= 0) {
				Plane plane = planes.get(keys[input]);

				System.out.printf(plane.toString());
			}

			else {
				System.out.println("존재하지 않는 번호입니다.");
			}

			System.out.print("계속하려면 아무키나 입력하세요 : ");

			scanner.nextLine();
 	    }
 	    catch(InputMismatchException e)
 	    {
 	    	scanner.nextLine();
 	    	
 	    	System.out.println("정수를 입력하시길 바랍니다.");
 	    }
    }

    private void userInfoMenu() {
  	   System.out.print("아이디를 입력하세요 : ");
  	   String id = scanner.nextLine();
  	  
  	   var KeySet = rData.users.keySet();
  	   String[] keys = KeySet.toArray(new String[KeySet.size()]);
  	   
  	   int temp=0;
  	   
  	   for(int i=0; i < keys.length; i++)
  	   {
  		   if(rData.users.get(keys[i]).getID().equals(id))
  		   {
  			 User user = rData.users.get(id);
  	  		System.out.printf(user.toString());
  	  		temp++;
  		   }
  	   }
  	   
  	   if(temp == 0) System.out.println("존재하지 않는 아이디입니다."); 
  		 	   
	    System.out.print("계속하려면 아무키나 입력하세요 : ");
	    
	    scanner.nextLine();
     }    
    
}
