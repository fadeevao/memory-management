package test.management.memory.memory_management;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.process.MemoryPagingController;
import test.management.memory.memory_management.process.Process;

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
	
	private byte[] getRandomProcessData(int length) {
		return Arrays.copyOfRange(RandomStringUtils.random(100).getBytes(), 0, length-1);
	}

}
