package test.management.memory.memory_management;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.memory_type.Memory;
import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessPriority;
import test.management.memory.memory_management.process.ProcessState;

public class MemoryControllerUnitTest {
	
	private byte[] osData;
	private OperatingSystem operatingSystem;
	
	private ProcessTable processTable;
	
	private MainMemory mainMemory;
	private HardDisk hd;
	
	private MemoryController memoryController;
	
	private Process process; //default process that later can be used for testing
	
	@BeforeMethod
	public void setUpmemoryController() {

		this.osData = "".getBytes();
		this.operatingSystem = new OperatingSystem(osData);
		
		this.processTable = new ProcessTable();
		this.mainMemory = new MainMemory(processTable, operatingSystem);
		
		this.hd = new HardDisk();
		
		this.memoryController = new MemorySwappingController(hd, mainMemory);
		
		this.process = new Process();
	}

	@Test
	public void testAllocateMemoryToNewProcessWhenTherIsEnoughMemorySwapping() {
		memoryController.dealWithNewProcess(process);
		
		assertEquals(mainMemory.getAvailableSpace(), Memory.DEFAULT_SIZE - Process.DEFAULT_MEMORY_REQUIREMENT);
		assertEquals(mainMemory.getIndex(), Process.DEFAULT_MEMORY_REQUIREMENT);

		assertEquals(mainMemory.getProcessTable().getEntries().size(), 1);
		
		ProcessTableEntry processTableEntry = mainMemory.getProcessTable().getEntries().get(0);
		assertEquals(processTableEntry.getProcess(), process);
		assertEquals(processTableEntry.getBaseRegister(), 0);
		assertEquals(processTableEntry.getLimitRegister(), 63);
	}
	
	@Test
	public void AllocateMemoryToNewProcessWhenThereIsNotEnoughMemoryInMainMemorySwapping() {
		process.setData(new byte[1025]);
		memoryController.dealWithNewProcess(process);

		assertEquals(mainMemory.getAvailableSpace(), Memory.DEFAULT_SIZE.intValue());	
		assertEquals(hd.getAvailableSpace(), hd.getCapacity()-1025);	
		assertEquals(process.getProcessId(), 0);
	}
	
	/*
	 * Test for the case when there is still available space in main memory for new process
	 */
	@Test
	public void allocateMemoryToNewProcessWithCoupleOfProcessesAlreadyInMemorySwapping() {
		Process process0 = new Process.ProcessBuilder().withData(new  byte[1]).build();
		Process process1 = new Process.ProcessBuilder().withData(new  byte[1]).build();
		Process process2 = new Process.ProcessBuilder().withData(new  byte[1]).build();
		
		memoryController.dealWithNewProcess(process0);
		memoryController.dealWithNewProcess(process1);
		memoryController.dealWithNewProcess(process2);
		
		
		process.setData(new byte[1]);
		memoryController.dealWithNewProcess(process);

		assertEquals(mainMemory.getAvailableSpace(), Memory.DEFAULT_SIZE - 4);
		assertEquals(process.getProcessId(), 3);
	}
	
	@Test
	public void allocateMemoryToNewProcessWithCoupleOfProcessesAlreadyInMemoryNeedToMovProcessToDiskSwapping() {
		Process process0 = new Process.ProcessBuilder().withData(new  byte[1]).build();
		process0.setProcessPriority(ProcessPriority.LOW);
		process0.setProcessState(ProcessState.IDLE);
		
		memoryController.dealWithNewProcess(process0);
		
		process.setData(new byte[Memory.DEFAULT_SIZE]); // there won't be enough space for this process
		memoryController.dealWithNewProcess(process);

		assertEquals(mainMemory.getAvailableSpace(), 0);
		assertEquals(process.getProcessId(), 0);
		assertEquals(hd.getAvailableSpace(), hd.getCapacity()-1);
	}

	
	@Test
	public void TestExecuteProcessNonExistentIdSwapping() {
		process.setProcessId(456);

		byte[] readData = memoryController.executeProcess(process.getProcessId());
		assertNull(readData);
	}
	
	@Test
	public void testExecuteProcessWithExistingIdSwapping() {
		memoryController.dealWithNewProcess(process);

		byte[] readData = memoryController.executeProcess(process.getProcessId());
		assertEquals(readData, process.getData());
	}
}
