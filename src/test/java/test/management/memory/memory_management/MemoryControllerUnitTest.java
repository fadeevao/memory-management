package test.management.memory.memory_management;

import org.testng.annotations.Test;

import test.management.memory.memory_management.memory_type.HardDisk;
import test.management.memory.memory_management.memory_type.MainMemory;
import test.management.memory.memory_management.memory_type.Memory;
import test.management.memory.memory_management.process.Process;
import static org.testng.Assert.assertEquals;

public class MemoryControllerUnitTest {

	@Test
	public void testAllocateMemoryToNewProcessWhenTherIsEnoughMemory() {
		byte[] data = "".getBytes();
		OperatingSystem os = new OperatingSystem(data);
		
		ProcessTable processTable = new ProcessTable();
		MainMemory mainMemory = new MainMemory(processTable, os);
		
		HardDisk hd = new HardDisk();
		
		MemoryController memoryController = new MemoryController(hd, mainMemory, MemoryManagementStrategy.SWAPPING);
		
		Process process = new Process();
		
		memoryController.allocateMemoryToNewProcess(process);
		assertEquals(mainMemory.getAvailableSpace(), Memory.DEFAULT_SIZE - process.DEFAULT_MEMORY_REQUIREMENT);
		assertEquals(mainMemory.getIndex(), process.DEFAULT_MEMORY_REQUIREMENT);

		assertEquals(mainMemory.getProcessTable().getEntries().size(), 1);
		
		ProcessTableEntry processTableEntry = mainMemory.getProcessTable().getEntries().get(0);
		assertEquals(processTableEntry.getProcess(), process);
		assertEquals(processTableEntry.getBaseRegister(), 0);
		assertEquals(processTableEntry.getLimitRegister(), 63);
		
	}

}
