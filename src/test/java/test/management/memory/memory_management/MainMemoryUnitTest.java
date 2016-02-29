package test.management.memory.memory_management;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

import test.management.memory.memory_management.memory_type.MainMemory;

public class MainMemoryUnitTest {
	
	private static final int DEFAULT_MEMORY_SIZE = 1024;
	
	@Test
	public void testConstructorInitialisation() {
		byte[] data = "OS data".getBytes();
		OperatingSystem os = new OperatingSystem(data);
		
		ProcessTable processTable = new ProcessTable();
		MainMemory mainMemory = new MainMemory(processTable, os);
		assertEquals(mainMemory.getIndex(), 7);
		assertEquals(Arrays.copyOfRange(mainMemory.getMemoryArray(), 0, 7), data);
		assertEquals(mainMemory.getCapacity(), DEFAULT_MEMORY_SIZE);
	}
	
	

}
