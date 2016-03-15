package test.management.memory.memory_management.process;

import test.management.memory.memory_management.MemoryController;
import test.management.memory.memory_management.MemoryManagementStrategy;
import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;

public class MemoryPagingController extends MemoryController{

	public MemoryPagingController(HardDisk hardDisk, MainMemory mainMemory) {
		super(hardDisk, mainMemory, MemoryManagementStrategy.PAGING);
	}

	@Override
	public void dealWithNewProcess(Process process) {
		process.setProcessState(ProcessState.NEW);
		if (isThereEnoughMemoryForProcess(mainMemory, process)) {
			mainMemory.write(process, mainMemory.getProcessTable());
		} else {
				dealWithProcessPaging(process);
		}
	}
	
	private void dealWithProcessPaging(Process process) {
		//TODO: implement
	}

	@Override
	public byte[] executeProcess(int id) {
		Process process = mainMemory.getProcessTable().findProcessById(id);
		if (process == null) {
			return null; // no process with such ID found
		}
		
		return readProcessDataInPagingMode(process);
	}
	
	//TODO implement this 
	private byte[] readProcessDataInPagingMode(Process process) {
		return null;
	}

}
