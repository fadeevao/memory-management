package test.management.memory.memory_management;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import test.management.memory.memory_management.memory_type.Memory;

public class MemoryUnitTest {
	
	@Test
	public void testReadMemory(){
		Memory memory = new Memory();
		byte[] data = "HereIsTheData".getBytes();
		memory.write(data);
		memory.setIndex(0);
		for (int i = 0; i<data.length; i++) {
			assertEquals(memory.readMemory(), data[i]);
		}
		
	}

}
