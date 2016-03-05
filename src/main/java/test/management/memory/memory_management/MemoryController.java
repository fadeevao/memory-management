package test.management.memory.memory_management;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.memory_type.Memory;
import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessState;

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

	
	public void dealWithNewProcess(Process process) {
		if (isThereEnoughMemoryForProcess(mainMemory, process)) {
			mainMemory.write(process, mainMemory.getProcessTable());
		} else {
			//depending on the management strategy deal with the situation
			if (memoryManagementStrategy.equals(MemoryManagementStrategy.SWAPPING)) {
				dealWithProcessSwapping(process);
			} else if (memoryManagementStrategy.equals(MemoryManagementStrategy.PAGING)) {
				dealWithProcessPaging(process);
			}
		}
	}
	
	private void dealWithProcessSwapping(Process process) {
		Process processToMoveToDisk = mainMemory.getProcessTable().getProcess(ProcessState.IDLE);
		if (processToMoveToDisk == null) {
			return;
		}
		mainMemory.moveProcessToDisk(hardDisk, processToMoveToDisk);
	}
	
	private void dealWithProcessPaging(Process process) {
		//TODO: implement
	}
	
	private boolean isThereEnoughMemoryForProcess(Memory memory, Process process) {
		return memory.getAvailableSpace() >= process.getData().length;
	}
}
