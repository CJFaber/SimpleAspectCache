package simple_cache;

//Interface for reading to the cache
public interface CacheReader {
	int readValue (int addr, int index);
	int[] readBlock (int addr, int index);
}
