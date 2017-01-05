package simple_cache;


//import java.io.*;
import java.util.*;

public class simCache {

	/*public void dumpCache(){
		System.out.println("Tag: Data");
		for (int i = 0; i < C; ++i ){
			System.out.println(String.format("0x%08X",(tags[i]))+" : "+
					String.format("0x%08X",(data[i])));
		}
	}
	*/
	
	
	public void run(Memory mem){
		int addr = 0;
		String command = "NULL";
		int memoryMax = 128;
		Scanner inp = new Scanner(System.in);
		System.out.println("Welcome to the Basic Memory Simulator!");
		System.out.println("Current Address is: " + addr);
		System.out.println("Write commands are in the form: w 0xADDR 0xValue");
		System.out.println("Read commands are in the form: r 0xADDR");
		System.out.println("Dump the cache by typing: c");
		System.out.println("Current Max address is: "+ Integer.toHexString(memoryMax));
		while(!command.equals("EXIT")){
			System.out.println("Type EXIT to quit");
			System.out.println("Please enter a command:");
			command = inp.nextLine();
			String[] commandSplit = command.split("\\s+");
			if(commandSplit.length > 3){
				System.out.println("Invalid Command!");
				break;
			}
			switch (commandSplit[0]){
				case "w": 
						if( (addr = Integer.decode(commandSplit[1])) == -1){
							System.out.println("Invalid Address!");
							break;
						}
						else{
							if((addr >= 0) && (addr <= memoryMax)){
								mem.writeMemory(addr, Integer.decode(commandSplit[2]));
							}
							else{
								System.out.println("Invalid Address!");
								break;
							}
						}
						System.out.println("Data("+commandSplit[2]+") written to Memory address:"+commandSplit[1]);
						break;
						
				case "r":
						if( (addr = Integer.decode(commandSplit[1])) == -1){
							System.out.println("Invalid Address!");
							break;
						}
						else{
							if((addr >= 0) && (addr <= memoryMax)){
								System.out.println("Data at Address:" + addr + " : " + mem.readMemory(addr));
							}
							else{
								System.out.println("Invalid Address!");
								break;
							}
						}
						
						break;
				case "c":
					mem.dumpCache();
					break;
				
				case "EXIT":
					break;
					
				default:
					System.out.println("Malformed Instruction!");
					break;
			}
		}
		
		System.out.println("Exiting cache simulation!");
		inp.close();
	}
	
	
	public static void main(String [] args) {
		Memory sysMem = new Memory(128,2,2,1);
		new simCache().run(sysMem);
	}
}
