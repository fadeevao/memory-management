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

	public PageTable getPageTable() {
		return pageTable;
	}

	public void setPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}

	public MemoryPagingController(HardDisk hardDisk, MainMemory mainMemory) {
		super(hardDisk, mainMemory, MemoryManagementStrategy.PAGING);
		pageTable = new PageTable();
		pageTable.setPageSize(hardDisk.getCapacity()/64); //by default then pageSize will be 16 bytes
	}

	@Override
	public void dealWithNewProcess(Process process) {
		process.setProcessState(ProcessState.NEW);
		ArrayList<Page> processPages = (ArrayList<Page>) divideProcessIntoPages(process);
		pageTable.addProcessPages(process.getProcessId(), processPages);
		if (isThereEnoughMemoryForProcess(mainMemory, process)) {
			for(Page page: processPages) {
				page.setBaseRegisterInMemory(mainMemory.getIndex());
				page.setResidesInMainMemory(true);
				mainMemory.write(page.getData());
			}
			
		} else {
			dealWithProcessPaging(process, processPages);
		}
	}
	
	public List<Page> divideProcessIntoPages(Process process) {
		int pageSize = pageTable.getPageSize();
		if (process.getData().length == pageSize) {
			return new ArrayList<Page>(Arrays.asList(new Page(process.getData())));
		}
		
		if (process.getData().length < pageSize) {
			byte[] pageData = new byte[pageSize];
			fillPageWithEmptyBytes(pageData,pageSize);
			for (int i =0; i<process.getData().length; i++) pageData[i] = process.getData()[i];
			return new ArrayList<Page>(Arrays.asList(new Page(pageData)));
		}
		
		byte[] processDataCopy = process.getData();
		List<Page> processPages = new ArrayList<>();
		int index = 0;
		while (index<process.getData().length) {
			processPages.add(new Page(getNextProcessBlock(Arrays.copyOfRange(processDataCopy, index, index+pageSize))));
			index+=pageSize;
		}
		
		
		return processPages;
	} 
	
	private void fillPageWithEmptyBytes(byte[] data, int limit) {
		for (int i =0; i<limit; i++) data[i] = (byte) 0; //fill all page with empty bytes
		
	}
	
	private byte[] getNextProcessBlock(byte[] data) {
		int blockSize = pageTable.getPageSize();
		byte[] processBlock = new byte[blockSize];
		if (data.length >= blockSize) {
			processBlock = Arrays.copyOfRange(data, 0, pageTable.getPageSize());
		} else {
			fillPageWithEmptyBytes(processBlock, blockSize);
			processBlock = Arrays.copyOfRange(data, 0, data.length);
		}
		return processBlock;
	}
	
	private void dealWithProcessPaging(Process process, ArrayList<Page> processPages) {
		//check how many blocks can go to MM
		int numberOfBlocksThatCanGoToMainMemory = 0;
		System.out.println(mainMemory.getAvailableSpace() % pageTable.getPageSize());
//		if (mainMemory.getAvailableSpace() % pageTable.getPageSize() == 0) {
//			numberOfBlocksThatCanGoToMainMemory = mainMemory.getAvailableSpace() / pageTable.getPageSize();
//		} else {
			numberOfBlocksThatCanGoToMainMemory = mainMemory.getAvailableSpace() / pageTable.getPageSize();
		//}
		
		for (int i = 0; i<numberOfBlocksThatCanGoToMainMemory; i++) {
			Page page = processPages.get(i);
			page.setBaseRegisterInMemory(mainMemory.getIndex());
			page.setResidesInMainMemory(true);
			mainMemory.write(page.getData());
		}
		
		int numberOfPagesThatNeedToGoToDisk = processPages.size() - numberOfBlocksThatCanGoToMainMemory;
		for (int k = 0; k<numberOfPagesThatNeedToGoToDisk; k++) {
			Page page = processPages.get(numberOfBlocksThatCanGoToMainMemory+k);
			page.setBaseRegisterInMemory(hardDisk.getIndex());
			page.setResidesInMainMemory(false);
			hardDisk.write(page.getData());
		}
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
