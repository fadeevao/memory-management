package management.memory.memory_management;

public class OperatingSystem {

	private byte[] data;
	
	public static final byte[] DEFAULT_OPERATING_SYSTEM_DATA = new byte[32];
	
	public OperatingSystem(byte[] data) {
		this.data = data;
	}
	
	public OperatingSystem() {
		this(DEFAULT_OPERATING_SYSTEM_DATA);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
