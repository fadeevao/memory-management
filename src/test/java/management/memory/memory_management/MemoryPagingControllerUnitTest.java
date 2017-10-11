package management.memory.memory_management;

import management.memory.memory_management.memory_control.MemoryException;
import management.memory.memory_management.memory_control.MemoryPagingController;
import management.memory.memory_management.memory_type.MainMemory;
import management.memory.memory_management.process.Process;
import management.memory.memory_management.process.ProcessState;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import management.memory.memory_management.memory_type.HardDisk;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

public class MemoryPagingControllerUnitTest {
	private byte[] osData;
	private OperatingSystem operatingSystem;
	
	private ProcessTable processTable;
	
	private MainMemory mainMemory;
	private HardDisk hd;
	
	private MemoryPagingController memoryController;
	
	private Process process; //default process that later can be used for testing
	
	@BeforeMethod
	public void setUpmemoryController() throws MemoryException {

		this.osData = "".getBytes();
		this.operatingSystem = new OperatingSystem(osData);
		
		this.processTable = new ProcessTable();
		this.mainMemory = new MainMemory(processTable, operatingSystem);
		
		this.hd = new HardDisk();
		
		this.memoryController = new MemoryPagingController(hd, mainMemory);
		
		this.process = new Process();
	}

	@Test
	public  void testDivideProcessIntoPagesProcessSizeSameAsPageSize() {
		memoryController.getPageTable().setPageSize(64);
		ArrayList<Page> resultPages = (ArrayList<Page>) memoryController.divideProcessIntoPages(process);
		assertEquals(resultPages.size(), 1);
		assertEquals(resultPages.get(0).getData(), process.getData());
		assertEquals(resultPages.get(0).getData().length, process.getData().length);
	}
	
	/*
	 * Process can be filled into 1 page, the rest of the page is filled with empty bytes
	 */
	@Test
	public  void testDivideProcessIntoPagesProcessSizeSmallerThanPageSize() {
		memoryController.getPageTable().setPageSize(100);
		process.setData(getRandomProcessData(64));
		ArrayList<Page> resultPages = (ArrayList<Page>) memoryController.divideProcessIntoPages(process);
		assertEquals(resultPages.size(), 1);
		assertEquals(resultPages.get(0).getData().length, 100);
		assertEquals(resultPages.get(0).getData()[64], (byte) 0);
	}
	
	
	/*
	 * Process completely fills in some pages
	 */
	@Test
	public  void testDivideProcessIntoPagesProcessSizeGreaterThanPageSizeWithoutPagesNeedingToBeFilledWithEmptyBytes() {
		memoryController.getPageTable().setPageSize(8);
		process.setData(getRandomProcessData(64));
		ArrayList<Page> resultPages = (ArrayList<Page>) memoryController.divideProcessIntoPages(process);
		assertEquals(resultPages.size(), 8);
		assertEquals(resultPages.get(0).getData(), Arrays.copyOfRange(process.getData(), 0, 8));
	}
	
	/*
	 * Some blocks will need to be filled with empty bytes (0) as process data does not lay out nicely in pages,  so there are pages with some space left 
	 */
	@Test
	public void testDivideProcessIntoPagesProcessSizeGreaterThanPageSizeWithPagesNeedingToBeFilledWithEmptyBytes() {
		memoryController.getPageTable().setPageSize(10);
		process.setData(getRandomProcessData(64));
		ArrayList<Page> resultPages = (ArrayList<Page>) memoryController.divideProcessIntoPages(process);
		assertEquals(resultPages.size(), 7);
		assertEquals(resultPages.get(0).getData().length, 10);
		assertEquals(resultPages.get(0).getData(), Arrays.copyOfRange(process.getData(), 0, 10));
		assertEquals(resultPages.get(6).getData()[5], (byte) 0);
	}
	
	@Test
	public void testDealWithNewProcessEnoughMemoryInMainMemory() throws MemoryException {
		memoryController.getPageTable().setPageSize(8);
		process.setData(getRandomProcessData(64));
		memoryController.dealWithNewProcess(process);
		Assert.assertEquals(process.getProcessState(), ProcessState.NEW);
		assertEquals(memoryController.getPageTable().getPageTable().size(), 1);
		assertEquals(mainMemory.getAvailableSpace(), mainMemory.getCapacity() - process.getData().length);
		ArrayList<Page> processPages = (ArrayList<Page>) memoryController.getPageTable().getProcessPages(process);
		int index = 0;
		for (Page page : processPages) {
			assertEquals(page.getBaseRegisterInMemory(), index);
			assertTrue(page.isResidesInMainMemory());
			index+=8;
		}
		
	}
	
	@Test
	public void testDealWithNewProcessNotEnoughMemoryInMainMemoryNeedToWriteToDisk() throws MemoryException {
		memoryController.getPageTable().setPageSize(8);
		mainMemory.setMemoryArray(new byte[10]);
		process.setData(getRandomProcessData(12));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessState(), ProcessState.NEW);
		assertEquals(memoryController.getPageTable().getPageTable().size(), 1);
		assertEquals(mainMemory.getAvailableSpace(), 2);
		ArrayList<Page> processPages = (ArrayList<Page>) memoryController.getPageTable().getProcessPages(process);
		assertEquals(processPages.get(0).getBaseRegisterInMemory(), 0);
		assertTrue(processPages.get(0).isResidesInMainMemory());
		
		assertEquals(processPages.get(1).getBaseRegisterInMemory(), 0);
		assertFalse(processPages.get(1).isResidesInMainMemory());
		assertEquals(hd.getDataAtIndex(0), process.getData()[8]);
		assertEquals(hd.getDataAtIndex(1), process.getData()[9]);
		assertEquals(hd.getDataAtIndex(2), process.getData()[10]);
		assertEquals(hd.getDataAtIndex(3), process.getData()[11]);
		assertEquals(hd.getAvailableSpace(), hd.getCapacity() - memoryController.getPageTable().getPageSize());
	}
	
	@Test
	public void testDealWithNewProcessNotEnoughMemoryInMainMemoryNeedToWriteToDiskAllProcess() throws MemoryException {
		memoryController.getPageTable().setPageSize(8);
		mainMemory.setMemoryArray(new byte[2]);
		process.setData(getRandomProcessData(12));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessState(), ProcessState.NEW);
		assertEquals(memoryController.getPageTable().getPageTable().size(), 1);
		assertEquals(mainMemory.getAvailableSpace(), 2);
		ArrayList<Page> processPages = (ArrayList<Page>) memoryController.getPageTable().getProcessPages(process);
		assertEquals(processPages.size(), 2);
		assertEquals(processPages.get(0).getBaseRegisterInMemory(), 0);
		assertFalse(processPages.get(0).isResidesInMainMemory());
		
		assertEquals(processPages.get(1).getBaseRegisterInMemory(), 8);
		assertFalse(processPages.get(1).isResidesInMainMemory());
		assertEquals(hd.getAvailableSpace(), hd.getCapacity() - memoryController.getPageTable().getPageSize()*2);
	}
	
	@Test
	public void testExecuteProcessWithNonExistentId() throws MemoryException {
		assertNull(memoryController.executeProcess(100));
	}
	
	@Test
	public void testExecuteProcessThatWasAllWrittenToMainMemory() throws MemoryException {
		memoryController.getPageTable().setPageSize(8);
		process.setData(getRandomProcessData(64));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessId(), 1);
		byte[] processDataReadFromMemory = memoryController.executeProcess(process.getProcessId());
		assertEquals(processDataReadFromMemory.length, process.getData().length);
		assertEquals(processDataReadFromMemory, process.getData());
	}

	
	@Test
	public void testExecuteProcessThatWasHalfWrittenToDiskAndHalfToMemory() throws MemoryException {
		memoryController.getPageTable().setPageSize(8);
		mainMemory.setMemoryArray(new byte[10]);
		process.setData(getRandomProcessData(12));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessId(), 1);
		byte[] processDataReadFromMemory = memoryController.executeProcess(process.getProcessId());
		assertEquals(processDataReadFromMemory.length, process.getData().length);
		assertEquals(processDataReadFromMemory, process.getData());
	}
	
	private byte[] getRandomProcessData(int length) {
		return Arrays.copyOfRange(RandomStringUtils.random(length*3).getBytes(), 0, length);
	}
	

}
