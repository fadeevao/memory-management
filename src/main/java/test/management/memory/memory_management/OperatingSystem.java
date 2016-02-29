package test.management.memory.memory_management;

public class OperatingSystem {

	private byte[] data;
	
	public OperatingSystem(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
