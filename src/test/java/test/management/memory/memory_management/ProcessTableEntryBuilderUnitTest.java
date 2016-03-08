package test.management.memory.memory_management;

import org.testng.annotations.Test;

import test.management.memory.memory_management.process.Process;
import static org.testng.Assert.assertEquals;


public class ProcessTableEntryBuilderUnitTest {

	@Test
	public void testProcessTableEntryBuilder() {
		Process process = new Process();
		int baseReg = 10;
		int limitReg = 20;
		
		ProcessTableEntry entry = new ProcessTableEntry.ProcessTableEntryBuilder().withBaseRegister(baseReg)
				.withLimitRegister(limitReg)
				.withProcess(process)
				.build();
		assertEquals(entry.getBaseRegister(), baseReg);
		assertEquals(entry.getLimitRegister(), limitReg);
		assertEquals(entry.getProcess(), process);
		
	}
}
