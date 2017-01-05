package simple_cache;

public class CacheStates {
	public static final byte STALE = 0;
	public static final byte MODIFIED = 1;
	public static final byte OWNED = 2;
	public static final byte EXECLUSIVE = 3;
	public static final byte SHARED = 4;
}
