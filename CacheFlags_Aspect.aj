package simple_cache;

privileged public aspect CacheFlags_Aspect {
	//InterType Declarations
	//State defaults to stale
	private byte CacheLine.state = CacheStates.STALE;
	//Setters for the state
	public void CacheLine.setModified(){
		state = CacheStates.MODIFIED;
	}
	public void CacheLine.setOwned(){
		state = CacheStates.OWNED;
	}
	public byte CacheLine.checkState(){
		//returns the current state of the system
		return state;
	}
	
	//Flag Pointcuts
	//When a value is modified (Dirty Bit)
	pointcut modValue(Cache C, int addr, int value, int index): target(C) && args(addr, value, index) 
																&& (call(* CacheWriter.*(..)) || execution(* CacheWriter.*(..)));
	//When a cache location is stale (Should only happen at the start)
	pointcut hitStaleCheck(Cache C, int addr): target(C) && args(addr) && call(* *cacheHit(..));
	
	//Flag advice
	//Sets the Flag modified after the cache line is written to
	after(Cache C, int addr, int value, int index): modValue(C,addr,value,index){
		C.L1[index].setModified();
	}
	//On a cache hit check returns -1 (invalid) if the state is stale
	int around(Cache C, int addr): hitStaleCheck(C, addr){
		int index = proceed(C,addr);
		if(index != -1){
			if(C.L1[index].checkState() == CacheStates.STALE){
				return (-1);
			}
		}
		return index;
	}

	
	/*if(L1[index].checkState() == CacheStates.STALE){
	L1[index].setOwned();
	return -1;
	}*/
	

	public void CacheLine.setExeclusive(){
		state = CacheStates.EXECLUSIVE;
	}
	
	public void CacheLine.setShared(){
		state = CacheStates.SHARED;
	}
	

	
}
