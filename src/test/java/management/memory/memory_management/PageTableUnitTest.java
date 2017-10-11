package management.memory.memory_management;

import management.memory.memory_management.process.Process;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class PageTableUnitTest {
	
	private PageTable pageTable;
	
	@BeforeMethod
	public void setUp() {
		this.pageTable = new PageTable();
	}
	@Test
	public void testGetProcessByIdWithExistentId() {
		Process process = new Process();
		process.setProcessId(3);
		pageTable.addProcessPages(process, null);
		assertEquals(pageTable.getProcessFromId(3), process);
	}

	@Test
	public void testGetProcessByIdWIthNonExistentId() {
		assertNull(pageTable.getProcessFromId(12));
	}
	
}
