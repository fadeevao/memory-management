package test.management.memory.memory_management.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.management.memory.memory_management.MemoryController;
import test.management.memory.memory_management.MemoryManagementStrategy;
import test.management.memory.memory_management.Page;
import test.management.memory.memory_management.PageTable;
import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;

public class MemoryPagingController extends MemoryController{
	
	private PageTable pageTable;

	public MemoryPagingController(HardDisk hardDisk, MainMemory mainMemory) {
		super(hardDisk, mainMemory, MemoryManagementStrategy.PAGING);
	}

	@Override
	public void dealWithNewProcess(Process process) {
		process.setProcessState(ProcessState.NEW);
		pageTable.addProcessPages(process.getProcessId(), divideProcessIntoPages(process));
		
	}
	
	private List<Page> divideProcessIntoPages(Process process) {
		if (process.getData().length <= pageTable.getPageSize()) {
			return new ArrayList<Page>(Arrays.asList(new Page(process.getData())));
		}
		
		return null;
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
