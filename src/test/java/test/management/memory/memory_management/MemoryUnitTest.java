package test.management.memory.memory_management;

import org.testng.annotations.Test;
import test.management.memory.memory_management.memory_control.MemoryException;
import test.management.memory.memory_management.memory_type.Memory;

import static org.testng.Assert.assertEquals;

public class MemoryUnitTest {
	
	@Test
	public void testReadMemory() throws MemoryException {
		Memory memory = new Memory();
		byte[] data = "HereIsTheData".getBytes();
		memory.write(data);
		memory.setIndex(0);
		for (int i = 0; i<data.length; i++) {
			assertEquals(memory.readMemory(), data[i]);
		}
		
	}

}
