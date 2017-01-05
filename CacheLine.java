package simple_cache;

public class CacheLine {
	private CacheTag tag;
	private CacheData data;
	private byte dis;

	CacheLine(byte displacement) {
		dis = (byte) (displacement);
		tag = new CacheTag();
		data = new CacheData(displacement);

	}

	public int getTag() {
		return tag.getTag();
	}

	public boolean checkLineTag(int tagchk) {
		return tag.tagHit(tagchk);
	}

	public int readLineSingle(int addr) {
		return data.returnDataSingle((byte) (addr % dis));
	}

	public int[] readLine() {
		return data.returnDataLine();
	}

	public void writeLineSingle(int Atag, int value, byte disp) {
		tag.setTag(Atag);
		data.writeDataSingle(value, disp);
	}

	public void writeLine(int ATag, int[] values) {
		tag.setTag(ATag);
		data.writeDataLine(values);
	}

	public void dumpLine() {
		System.out.println(tag.getTag() + " : " + data.printData());
	}

}