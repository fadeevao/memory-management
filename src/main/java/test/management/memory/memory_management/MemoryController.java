package test.management.memory.memory_management;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.memory_type.Memory;

public class MemoryController {
	
	MemoryManagementStrategy memoryManagementStrategy;

	private HardDisk hardDisk;
	
	private MainMemory mainMemory;
	
	public MemoryController(HardDisk hardDisk, MainMemory mainMemory, MemoryManagementStrategy strategy) {
		this.hardDisk = hardDisk;
		this.mainMemory = mainMemory;
		this.memoryManagementStrategy = strategy;
	}

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
	
	public void allocateMemoryToNewProcess(Process process) {
		checkFreeMemory(mainMemory);
	}
	
	//TODO:  add implementation
	public void checkFreeMemory(Memory memory) {
		
	}
}
