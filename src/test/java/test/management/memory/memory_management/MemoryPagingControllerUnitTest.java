package test.management.memory.memory_management;

import java.util.ArrayList;

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
	
	@Test
	public  void testDivideProcessIntoPagesProcessSizeSmallerThanPageSize() {
		memoryController.getPageTable().setPageSize(100);
		ArrayList<Page> resultPages = (ArrayList<Page>) memoryController.divideProcessIntoPages(process);
		assertEquals(resultPages.size(), 1);
		assertEquals(resultPages.get(0).getData().length, 100);
		assertEquals(resultPages.get(0).getData()[64], (byte) 0);
	}
	
	@Test
	public  void testDivideProcessIntoPagesProcessSizeGreaterThanPageSize() {
		
	}

}
