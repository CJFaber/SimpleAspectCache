package simple_cache;

//Interface for the cache writing procedures
public interface CacheWriter {
	void writeValue (int addr, int value, int index);
	void writeBlock (int addr, int[] block, int index);
}
