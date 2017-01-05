package simple_cache;
import java.util.*;

//Class of main memory - Giant array
public class MainMemory {
	private int [] memSpace;
	private int memSize;
	private int L; //block size
	private int t_dis;
	
	MainMemory(int memSize, int L){
		this.memSize = memSize;
		this.L = L;
		t_dis = (int)(Math.log(this.L) / Math.log(2));
		if(this.memSize > 0){
			memSpace = new int [this.memSize];
		}
		for (int i = 0; i < this.memSize; ++i){
			memSpace[i] = new Random().nextInt(Integer.MAX_VALUE);
		}
	}
	
	public void writeBlock(int addr, int[] value){
		for(int i = 0; i < value.length; ++i){
			memSpace[addr+i] = value[i];
		}
	}
	
	public int[] readBlock(int addr){
		int block [] = new int [L];
		addr = addr >> t_dis; //Shift out displacement
		addr = addr << t_dis; //Shift in zeros
		for(int i = 0; i < L; ++i){
			block[i] = memSpace[addr + i];
		}
		return block;
	}
	
	public void writeValue(int addr, int value){
		memSpace[addr] = value;
	}
	
	public int readValue(int addr){
		return memSpace[addr];
	}
	
}
