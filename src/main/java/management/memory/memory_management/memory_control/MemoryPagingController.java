package management.memory.memory_management.memory_control;

import management.memory.memory_management.Page;
import management.memory.memory_management.process.Process;
import management.memory.memory_management.process.ProcessState;
import management.memory.memory_management.PageTable;
import management.memory.memory_management.memory_type.HardDisk;
import management.memory.memory_management.memory_type.MainMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryPagingController extends MemoryController{
	
	private PageTable pageTable;

	public PageTable getPageTable() {
		return pageTable;
	}

	public MemoryPagingController(HardDisk hardDisk, MainMemory mainMemory) {
		super(hardDisk, mainMemory, MemoryManagementStrategy.PAGING);
		pageTable = new PageTable();
		pageTable.setPageSize(hardDisk.getCapacity()/64); //by default then pageSize will be 16 bytes
	}

	public void dealWithNewProcess(Process process) throws MemoryException {
		process.setProcessState(ProcessState.NEW);
		process.setProcessId(pageTable.getPageTable().size()+1);
		ArrayList<Page> processPages = (ArrayList<Page>) divideProcessIntoPages(process);
		pageTable.addProcessPages(process, processPages);
		if (enoughMemoryForProcess(mainMemory, process)) {
			processPages.forEach(page -> {
				page.setBaseRegisterInMemory(mainMemory.getIndex());
				page.setResidesInMainMemory(true);
				try {
					mainMemory.write(page.getData());
				} catch (MemoryException e) {
					e.printStackTrace();
				}
			});
		} else {
			dealWithProcessPaging(processPages);
		}
	}
	
	public List<Page> divideProcessIntoPages(Process process) {
		int pageSize = pageTable.getPageSize();
		if (process.getData().length == pageSize) {
			return new ArrayList<Page>(Arrays.asList(new Page(process.getData())));
		}
		
		if (process.getData().length < pageSize) {
			byte[] pageData = new byte[pageSize];
			fillPageWithEmptyBytes(pageData, pageSize);
			for (int i =0; i<process.getData().length; i++) pageData[i] = process.getData()[i];
			return new ArrayList<>(Arrays.asList(new Page(pageData)));
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
	
	private void dealWithProcessPaging(ArrayList<Page> processPages) throws MemoryException {
		//check how many blocks can go to MM
		int numberOfBlocksThatCanGoToMainMemory = mainMemory.getAvailableSpace() / pageTable.getPageSize();
		
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
	public byte[] executeProcess(int id) throws MemoryException {
		Process process = pageTable.getProcessFromId(id);
		if (process == null) {
			return null; // no process with such ID found
		}
		
		return readProcessDataInPagingMode(process);
	}
	
	
	private byte[] readProcessDataInPagingMode(Process process) throws MemoryException {
		List<Byte> processDataList = new ArrayList<>();
		ArrayList<Page> processPages = (ArrayList<Page>) pageTable.getProcessPages(process);
		for (Page page : processPages) {
			if (page.isResidesInMainMemory()) {
				mainMemory.setIndex(page.getBaseRegisterInMemory());
				for (int i = 0; i<pageTable.getPageSize(); i++) {
					processDataList.add(mainMemory.readMemory());
				}
			} else {
				hardDisk.setIndex(page.getBaseRegisterInMemory());
				for (int i = 0; i<pageTable.getPageSize(); i++) {
					byte byteToAdd = hardDisk.readMemory();
					//we don't want to add empty bytes as process data
					if (byteToAdd != (byte) 0) { 
						processDataList.add(byteToAdd);
					}
				}
			}
		}
		return transferListToArrayOfBytes(processDataList);
	}

}
