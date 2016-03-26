package test.management.memory.memory_management;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.process.MemoryPagingController;
import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessState;

public class MemoryPagingControllerUnitTest {
	private byte[] osData;
	private OperatingSystem operatingSystem;
	
	private ProcessTable processTable;
	
	private MainMemory mainMemory;
	private HardDisk hd;
	
	private MemoryPagingController memoryController;
	
	private Process process; //default process that later can be used for testing
	
	@BeforeMethod
	public void setUpmemoryController() {

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
	public void testDealWithNewProcessEnoughMemoryInMainMemory() {
		memoryController.getPageTable().setPageSize(8);
		process.setData(getRandomProcessData(64));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessState(), ProcessState.NEW);
		assertEquals(memoryController.getPageTable().getPageTable().size(), 1);
		assertEquals(mainMemory.getAvailableSpace(), mainMemory.getCapacity() - process.getData().length);
		ArrayList<Page> processPages = (ArrayList<Page>) memoryController.getPageTable().getProcessPages(process.getProcessId());
		int index = 0;
		for (Page page : processPages) {
			assertEquals(page.getBaseRegisterInMemory(), index);
			assertTrue(page.isResidesInMainMemory());
			index+=8;
		}
		
	}
	
	@Test
	public void testDealWithNewProcessNotEnoughMemoryInMainMemoryNeedToWriteToDisk() {
		memoryController.getPageTable().setPageSize(8);
		mainMemory.setMemoryArray(new byte[10]);
		process.setData(getRandomProcessData(12));
		memoryController.dealWithNewProcess(process);
		assertEquals(process.getProcessState(), ProcessState.NEW);
		assertEquals(memoryController.getPageTable().getPageTable().size(), 1);
		assertEquals(mainMemory.getAvailableSpace(), 2);
		ArrayList<Page> processPages = (ArrayList<Page>) memoryController.getPageTable().getProcessPages(process.getProcessId());
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
	
	private byte[] getRandomProcessData(int length) {
		return Arrays.copyOfRange(RandomStringUtils.random(length*3).getBytes(), 0, length);
	}
	

}
