package management.memory.memory_management;


import management.memory.memory_management.memory_control.MemoryException;

public class Page {
	
	private byte[] data;
	
	private int baseRegisterInMemory;
	
	private boolean residesInMainMemory;
	
	public boolean isResidesInMainMemory() {
		return residesInMainMemory;
	}

	public void setResidesInMainMemory(boolean residesInMainMemory) {
		this.residesInMainMemory = residesInMainMemory;
	}

	public int getBaseRegisterInMemory() {
		return baseRegisterInMemory;
	}

	public void setBaseRegisterInMemory(int baseRegisterInMemory) {
		this.baseRegisterInMemory = baseRegisterInMemory;
	}

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
