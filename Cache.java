package simple_cache;

//import java.util.Queue;
//import java.util.LinkedList;

//Defaults to Direct Mapped cache without aspects
public class Cache implements CacheReader, CacheWriter {
	//Lines (C)		  : 1
	//Line Size (L)   :	4 Bytes  
	//Associativity(m): C (fully associative)	 
	//Displacement (t): log(L) = 2
	//Index (i)		  : log(C/m) = 0
	//Tag (T)		  : 32 - 2 - 1 = 30
	
	public static final byte ADDR_SIZE = 32;
	private int L ;	       // Line Size (How many data values are stored in a line)
	private int C ;	       // Number of Cache lines
	private int m ;        // Associativity of tags - How many groups for the chache lines 
	private byte i_index ; // # of bits needed for index related to m and C
	private byte t_dis ;   // # of bits needed for cache offset (related to L)
	
	private CacheLine[] L1; // Cache Lines
	
	
	
	//public Queue<Integer> LRU_index;
	
	Cache(int L, int C, int m){
		this.L = L; // Line size
		this.C = C; // how many cache lines
		this.m = m; // Associativity of tags (how many groups) // m==1 Direct mapped, m==C fully associative
		t_dis = (byte) (Math.log(this.L) / Math.log(2));  //displacement how many memory values in a line
		i_index = (byte) (Math.log(this.C/this.m) / Math.log(2)); //index bits
		L1 = new CacheLine[C];
		//LRU_index = new LinkedList<Integer>();
		for(int i = 0; i < this.C; ++i){
			L1[i] = new CacheLine((byte)L);
		}
	}
	
	//Could be changed by aspects depending on the policy
	public int createIndex(int addr){
		//returns the index of the line
		//if index bits are zero then return zero
		if(i_index !=0 ){
			 //shift left to remove tag (index and displacement still valid)
			int index = addr << ADDR_SIZE - (i_index+t_dis);
			//shift right to return index (displacement is shifted out)
			index = index >>> (ADDR_SIZE - i_index);
			return index;
		}
		return 0;
	}
	
	public int createTag(int addr){
		//shift right to remove index and displacement
		return ( addr >>> (i_index + t_dis));
	}

	public int createOffset(int addr){
		if(t_dis != 0){
			//shift left to remove tag and index
			int offset = addr << (ADDR_SIZE - t_dis);
			//return to original
			return offset >>> (ADDR_SIZE-t_dis);
		}
		return 0;
	}
	
	
	public int cacheHit(int addr){
		int index = createIndex(addr);
		if( L1[index].checkLineTag(createTag(addr))){
			return index;
		}
		return (-1);
	}
	
	
	//handle write back with aspects - Handle replacements with aspect
	@Override
	public void writeBlock(int addr, int[] block, int index) {
		L1[index].writeLine(createTag(addr),block);
	}

	//handle set-associative in advice
	@Override
	public int[] readBlock(int addr, int index) {
		return L1[index].readLine();
	}

	//Advice should change how the cache is accessed for set associative
	//Should already be in cache when used
	@Override
	public void writeValue(int addr, int value, int index) {
		L1[index].writeLineSingle(createTag(addr), value, (byte)createOffset(addr));
	}
	
	//call cache hit before calling
	//Advice should change how the cache is accessed for set associative
	@Override
	public int readValue(int addr, int index) {
		return L1[index].readLineSingle(addr);
	}
	
	public void cacheDump(){
		System.out.println("Tag : Flag : Data");
		for(int i = 0; i < C; ++i){
			L1[i].dumpLine();
		}
	}

}

/*if(C==m){
//Fully associative need to check all tags
for(int i = 0; i < C; ++i){
	if(L1[i].checkLineTag(createTag(addr))){
		return i;
	}
}
}
else{
if(m == 1){
	//Direct mapped 
	//Find index and return
	if( L1[createIndex(addr)].checkLineTag(createTag(addr)) ){
		return createIndex(addr);
	}
}
//Set associative need to check tags based on grouping - handled by aspects
}
return (-1);
*/
