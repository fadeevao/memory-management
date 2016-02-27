package test.management.memory.memory_management.memory_type;

public class Memory {
	
	private byte[] memoryArray;
	
	private int availableSpace;
	
	private static final Integer DEFAULT_SIZE = 1024;
	
	public Memory(int capacity) {
		memoryArray = new byte[capacity];
	}
	
	public Memory() {
		memoryArray = new byte[DEFAULT_SIZE];
	} 

	public byte[] getMemoryArray() {
		return memoryArray;
	}

	public void setMemoryArray(byte[] memoryArray) {
		this.memoryArray = memoryArray;
	}

	public int getCapacity() {
		return memoryArray.length;
	}
	
	public int getAvailableSpace() {
		return availableSpace;
	}
	
	public void setAvailableSpace(int availableSpace) {
		this.availableSpace = availableSpace;
	}
}
