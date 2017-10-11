package management.memory.memory_management;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


import management.memory.memory_management.process.Process;
import management.memory.memory_management.process.ProcessPriority;
import management.memory.memory_management.process.ProcessState;

public class ProcessBuilderTest {

	@Test
	public void testUProcessBuildsCorrectly() {
		byte[] data = "Process data".getBytes();
		ProcessState state = ProcessState.IDLE;
		ProcessPriority priority = ProcessPriority.HIGH;
		int id = 1;
		
		Process process = new Process.ProcessBuilder().withData(data)
				.withPriority(priority)
				.withState(state)
				.witId(id)
				.build();
		assertEquals(process.getData(), data);
		assertEquals(process.getProcessId(), id);
		assertEquals(process.getProcessPriority(), priority);
		assertEquals(process.getProcessState(), state);
		
	}
}
