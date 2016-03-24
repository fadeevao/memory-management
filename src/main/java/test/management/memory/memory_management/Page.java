package test.management.memory.memory_management;

public class Page {
	
	private byte[] data;
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Page(byte[] data) {
		this.data = data;
	}
	
	public byte getDataAtIndex(int index) throws MemoryException {
		if (index >= data.length || index < 0) {
			throw new MemoryException("Trying to access index that's out of bounds");
		}
		return data[index];
	}

}
