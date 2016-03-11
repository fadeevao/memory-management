package test.management.memory.memory_management;

import java.util.ArrayList;
import java.util.List;

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
		process.setProcessState(ProcessState.NEW);
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
		if (process.getData().length <= mainMemory.getCapacity()) {
			while (process.getData().length > mainMemory.getAvailableSpace()) {
				Process processToMoveToDisk = mainMemory.getProcessTable()
						.getLowestPriorityProcess();
				if (processToMoveToDisk != null) {
					mainMemory.moveProcessToDisk(hardDisk, processToMoveToDisk);
				}
			}

			mainMemory.write(process, mainMemory.getProcessTable());
		} else {
			hardDisk.write(process, hardDisk.getProcessTable());
		}
	}
	
	/*
	 * "Runs" the process.  We assume that memory of the process is accessed sequentially and execution returns the data that belongs to the process.
	 * @param id of the process to be run
	 */
	public byte[] executeProcess(int id) {
		Process process = mainMemory.getProcessTable().findProcessById(id);
		if (process == null) {
			return null; // no process with such ID found
		}
		process.setProcessState(ProcessState.RUNNING);
		if (memoryManagementStrategy.equals(MemoryManagementStrategy.SWAPPING)) {
			return runProcessInSwapMode(process);
		} else if (memoryManagementStrategy.equals(MemoryManagementStrategy.PAGING)) {
			return runProcessInPagingMode(process);
		}
		
		return null; //should never really reach this
	}
	
	private byte[] runProcessInSwapMode(Process process) {
		ProcessTableEntry entry = mainMemory.getProcessTable().findProcessEntry(process);
		int base = entry.getBaseRegister();
		int limit = entry.getLimitRegister(); 
		
		mainMemory.setIndex(base);
		
		List<Byte> dataReadAtExecution = new ArrayList<Byte>();
		for (int i = base; i<=limit; i++) {
			dataReadAtExecution.add(mainMemory.readMemory());
		}
		return transferListToArrayOfBytes(dataReadAtExecution);
	}
	
	private byte[] runProcessInPagingMode(Process process) {
		//TODO: implement
		return null;
	}
	
	private void dealWithProcessPaging(Process process) {
		//TODO: implement
	}
	
	private boolean isThereEnoughMemoryForProcess(Memory memory, Process process) {
		return memory.getAvailableSpace() >= process.getData().length;
	}
	
	private byte[] transferListToArrayOfBytes(List<Byte> list) {
		byte[] array = new byte[list.size()];
		for (int i =0; i<list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}
