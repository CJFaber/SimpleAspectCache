package simple_cache;

//WriteBack Aspect depends of CacheFlags_Aspect
privileged public aspect WB_Aspect {
	private int[][] Cache.wb_buffer; // Write Back buffer (currently 1 value)
	private int [] Cache.wb_addr;	 // These are added to the Memory system
	static final byte WB_SIZE = 1;
	
	//Pointcut to wrap Memory to intialize WB buffer
	pointcut initWB(Memory M, int memorySize, int L, int C, int m): this(M) && args(memorySize, L, C, m) && execution(Memory.new(int, int, int, int));
	
	//Write back after Cache replacement
	pointcut memWB(Memory M, int addr): target(M) && args(addr) 
										  && (call(* ReadMemory.*(..)) || execution(* ReadMemory.*(..)));
	//Adds the value to the WB buffer
	pointcut addWB(Cache C, int addr, int[] blk, int index): target(C) && args(addr, blk, index) 
														   && (call(* CacheWriter.*(..)) || execution(* CacheWriter.*(..)));
	
	//Applies around advice to the single write back for memory - will be handled by block write now
	pointcut mmSingleValueWB(MainMemory MM, int addr, int value): target(MM) && args(addr, value) 
																&& call(void MainMemory.writeValue(int, int));
	
	//Initialize advice with memory - Called after Memory creation
	after (Memory M, int memorySize, int L, int C, int m): initWB(M, memorySize, L, C, m){
		M.cache.wb_buffer = new int [WB_SIZE][];
		M.cache.wb_addr = new int[WB_SIZE];
	}
	//Actual WriteBack Action - Called after write operations
	//Could be changed further dependent on CPU/Memory architecture
	after (Memory M, int addr): memWB(M, addr){
		if(M.cache.wb_buffer[0] != null){
			M.mainMem.writeBlock(M.cache.wb_addr[0], M.cache.wb_buffer[0]);
			M.cache.wb_addr[0] = 0;
			M.cache.wb_buffer[0] = null;
		}
	}	
	//WB advice - adds the evicted cache line to the write buffer
	void around(Cache C, int addr, int[] blk, int index): addWB(C,addr,blk,index){
		if((C.L1[index].getTag() != 0) && (C.L1[index].checkState() == CacheStates.MODIFIED)){
			C.wb_addr[0] = ( (C.L1[index].getTag() << (C.i_index + C.t_dis)) + (index << C.t_dis) );
			C.wb_buffer[0] = C.L1[index].readLine().clone();
			C.L1[index].setOwned();
		}
		proceed(C, addr, blk, index);
	}
	//Skips the singular write through
	void around(MainMemory MM, int addr, int value): mmSingleValueWB(MM, addr, value){
		return;
	}







after (Memory M, int addr, int value): memWriteWB(M, addr, value){
	if(M.cache.wb_buffer[0] != null){
		M.mainMem.writeBlock(M.cache.wb_addr[0], M.cache.wb_buffer[0]);
		M.cache.wb_addr[0] = 0;
		M.cache.wb_buffer[0] = null;
	}
}
//Write back after Memory transaction
	pointcut memWriteWB(Memory M, int addr, int value): target(M) && args(addr, value) 
												 	  && (call(* WriteMemory.*(..)) || execution(* WriteMemory.*(..)));
}