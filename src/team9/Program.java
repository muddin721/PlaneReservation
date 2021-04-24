package team9;
  
import team9.Data.*;

public class Program { 
	
	public static void main(String[] args) {
		RunData rData = new RunData(); 
		
		Menu menu = new Menu(rData);
		
		menu.show();
		
		rData.save(); 
	}
	
}
