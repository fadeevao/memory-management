package management.memory.memory_management;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

import management.memory.memory_management.memory_control.MemoryException;
import management.memory.memory_management.memory_type.HardDisk;
import management.memory.memory_management.memory_type.MainMemory;
import management.memory.memory_management.process.Process;

public class MainMemoryUnitTest {
	
	private static final int DEFAULT_MEMORY_SIZE = 1024;
	
	@Test
	public void testConstructorInitialisation() throws MemoryException {
		byte[] data = "OS data".getBytes();
		OperatingSystem os = new OperatingSystem(data);
		
		ProcessTable processTable = new ProcessTable();
		MainMemory mainMemory = new MainMemory(processTable, os);
		assertEquals(mainMemory.getIndex(), 7);
		assertEquals(Arrays.copyOfRange(mainMemory.getMemoryArray(), 0, 7), data);
		assertEquals(mainMemory.getCapacity(), DEFAULT_MEMORY_SIZE);
	}
	
	@Test
	public void testMoveProcessToDisk() throws MemoryException {

		byte[] data = "".getBytes();
		OperatingSystem os = new OperatingSystem(data);
		
		ProcessTable processTable = new ProcessTable();
		MainMemory mainMemory = new MainMemory(processTable, os);
		
		Process process = new Process();
		HardDisk hd = new HardDisk();
		
		mainMemory.write(process, mainMemory.getProcessTable());
		
		assertEquals(mainMemory.getAvailableSpace(), DEFAULT_MEMORY_SIZE - process.getData().length);
		
		mainMemory.moveProcessToDisk(hd, process);
		assertEquals(mainMemory.getProcessTable().getSize(), 0);
		assertEquals(hd.getProcessTable().getSize(), 1);
		assertEquals(hd.getAvailableSpace(), hd.getCapacity()-process.getData().length);
		assertEquals(hd.getProcessTable().findProcessEntry(process).getBaseRegister(), 0);
		assertEquals(hd.getProcessTable().findProcessEntry(process).getLimitRegister(), process.getData().length-1);

	}
	

}
