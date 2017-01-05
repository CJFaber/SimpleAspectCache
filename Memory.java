package simple_cache;

public class Memory implements ReadMemory, WriteMemory{
	private Cache cache;
	private MainMemory mainMem;
	
	Memory(int memorySize, int L, int C, int m){
		//Set values for memory here;
		mainMem = new MainMemory(memorySize, L);
		cache = new Cache(L,C,m);
	}
	@Override
	public int readMemory(int addr) {
		int index = cache.cacheHit(addr);
		if(index == -1){
			index = cache.createIndex(addr);
			cache.writeBlock(addr, mainMem.readBlock(addr), index);
			return cache.readValue(addr,index);
		}
		return cache.readValue(addr, index);
	}
	@Override
	public void writeMemory(int addr, int value) {
		int index = cache.cacheHit(addr);
		if(index == -1){
			index = cache.createIndex(addr);
			cache.writeBlock(addr, mainMem.readBlock(addr), index);
		}
		mainMem.writeValue(addr, value);
		cache.writeValue(addr, value, index);
	}
	
	public void dumpCache(){
		cache.cacheDump();
	}

}

