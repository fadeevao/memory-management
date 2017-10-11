package management.memory.memory_management.memory_control;

import management.memory.memory_management.memory_type.HardDisk;
import management.memory.memory_management.memory_type.MainMemory;
import management.memory.memory_management.memory_type.Memory;
import management.memory.memory_management.process.Process;

import java.util.List;

public abstract class MemoryController {
	
	MemoryManagementStrategy memoryManagementStrategy;

	protected HardDisk hardDisk;
	
	protected MainMemory mainMemory;
	
	public MemoryController(HardDisk hardDisk, MainMemory mainMemory, MemoryManagementStrategy strategy) {
		this.hardDisk = hardDisk;
		this.mainMemory = mainMemory;
		this.memoryManagementStrategy = strategy;
	}
	
	public abstract void dealWithNewProcess(Process process) throws MemoryException, MemoryException;
	
	/*
	 * "Runs" the process.  We assume that memory of the process is accessed sequentially and execution returns the data that belongs to the process.
	 * @param id of the process to be run
	 */
	public abstract byte[] executeProcess(int id) throws MemoryException;

	public MemoryManagementStrategy getMemoryManagementStrategy() {
		return memoryManagementStrategy;
	}

	public void setMemoryManagementStrategy(
			MemoryManagementStrategy memoryManagementStrategy) {
		this.memoryManagementStrategy = memoryManagementStrategy;
	}
	
	public HardDisk getHardDisk() {
		return hardDisk;
	}

	public void setHardDisk(HardDisk hardDisk) {
		this.hardDisk = hardDisk;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}
	
	protected boolean enoughMemoryForProcess(Memory memory, Process process) {
		return memory.getAvailableSpace() >= process.getData().length;
	}
	
	protected byte[] transferListToArrayOfBytes(List<Byte> list) {
		byte[] array = new byte[list.size()];
		for (int i =0; i<list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}
