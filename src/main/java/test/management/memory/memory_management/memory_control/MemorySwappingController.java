package test.management.memory.memory_management.memory_control;

import java.util.ArrayList;
import java.util.List;

import test.management.memory.memory_management.ProcessTableEntry;
import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessState;

public class MemorySwappingController extends MemoryController {

	public MemorySwappingController(HardDisk hardDisk, MainMemory mainMemory) {
		super(hardDisk, mainMemory, MemoryManagementStrategy.SWAPPING);
	}

	@Override
	public void dealWithNewProcess(Process process) {
		process.setProcessState(ProcessState.NEW);
		if (isThereEnoughMemoryForProcess(mainMemory, process)) {
			mainMemory.write(process, mainMemory.getProcessTable());
		} else {
				dealWithProcessSwapping(process); 
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

	@Override
	public byte[] executeProcess(int id) throws MemoryException {
		Process process = mainMemory.getProcessTable().findProcessById(id);
		if (process == null) {
			return null; // no process with such ID found
		}
		return readProcessDataInSwappingMode(process);
	}
	
	private byte[] readProcessDataInSwappingMode(Process process) throws MemoryException {
		process.setProcessState(ProcessState.RUNNING);
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
	
	

}
