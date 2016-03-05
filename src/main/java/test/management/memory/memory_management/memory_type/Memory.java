package test.management.memory.memory_management.memory_type;

import test.management.memory.memory_management.ProcessTable;
import test.management.memory.memory_management.ProcessTableEntry;
import test.management.memory.memory_management.process.Process;

public class Memory {

	private byte[] memoryArray;

	private int availableSpace;

	private int size;

	private int index;

	public static final Integer DEFAULT_SIZE = 1024;

	public Memory(int capacity) {
		memoryArray = new byte[capacity];
		for (int i = 0; i < capacity; i++)
			memoryArray[i] = 0;
		size = capacity;
		availableSpace = capacity;
		index = 0;
	}

	public Memory() {
		this(DEFAULT_SIZE);
	}
	

	public byte[] getMemoryArray() {
		return memoryArray;
	}

	public void setMemoryArray(byte[] memoryArray) {
		this.memoryArray = memoryArray;
	}

	public int getCapacity() {
		return size;
	}

	public int getAvailableSpace() {
		return availableSpace;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public byte readMemory() {
		if (index < memoryArray.length && index > 0) {
			return memoryArray[index % memoryArray.length + 1];
		} else {
			throw new IndexOutOfBoundsException("Index is out of bounds");
		}

	}

	public void resetIndex() {
		index = 0;
	}

	public void write(byte[] data) {
		for (byte b : data) {
			if (index < this.size) {
				memoryArray[index++] = b;
				availableSpace--;
			} else {
				throw new IndexOutOfBoundsException("Index is out of bounds");
			}
		}
	}
	
	public void write(Process process, ProcessTable processTable) {
		ProcessTableEntry entry = new ProcessTableEntry(process);
		entry.setBaseRegister(getIndex());
		write(process.getData());
		entry.setLimitRegister(getIndex() - 1);
		processTable.addEntry(entry);
	}
	
	public void deleteDataAtIndex(int dataIndex) {
		if (dataIndex > 0 && dataIndex < size) {
			memoryArray[dataIndex] = 0;
			availableSpace++;
		}
	}

}
