package test.management.memory.memory_management;


import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessPriority;
import test.management.memory.memory_management.process.ProcessState;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class ProcessTableUnitTest {

	@Test
	public void testGetLowestPriorityProcess() {
		List<ProcessTableEntry> entries = new ArrayList<>();
		Process process1 = new Process.ProcessBuilder().withPriority(ProcessPriority.HIGH)
				.withState(ProcessState.NEW).build();
		
		Process process2 = new Process.ProcessBuilder().withPriority(ProcessPriority.LOW)
				.withState(ProcessState.TERMINATED).build();
		
		Process process3 = new Process.ProcessBuilder().withPriority(ProcessPriority.LOW)
				.withState(ProcessState.NEW).build();
		
		Process process4 = new Process.ProcessBuilder().withPriority(ProcessPriority.HIGH)
				.withState(ProcessState.IDLE).build();
		
		Process process5 = new Process.ProcessBuilder().withPriority(ProcessPriority.LOW)
				.withState(ProcessState.RUNNING).build();
		
		Process process6 = new Process.ProcessBuilder().withPriority(ProcessPriority.LOW)
				.withState(ProcessState.IDLE).build();
		
		Process process7 = new Process.ProcessBuilder().withPriority(ProcessPriority.HIGH)
				.withState(ProcessState.RUNNING).build();
		
		Process process8 = new Process.ProcessBuilder().withPriority(ProcessPriority.HIGH)
				.withState(ProcessState.TERMINATED).build();
		
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process1).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process2).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process3).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process4).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process5).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process6).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process7).build());
		entries.add(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process8).build());
		
		ProcessTable processTable = new ProcessTable(entries);
		assertEquals(processTable.getLowestPriorityProcess(), process2);
		assertEquals(processTable.getLowestPriorityProcess(), process8);
		assertEquals(processTable.getLowestPriorityProcess(), process6);
		assertEquals(processTable.getLowestPriorityProcess(), process4);
		assertEquals(processTable.getLowestPriorityProcess(), process3);
		assertEquals(processTable.getLowestPriorityProcess(), process1);
		assertEquals(processTable.getLowestPriorityProcess(), process5);
		assertEquals(processTable.getLowestPriorityProcess(), null);
		assertEquals(processTable.getEntries().size(), 1);
		
	}
	
	@Test
	public void addAndRemoveEntryIndexing() {
		Process process0 = new Process();
		Process process1 = new Process();
		Process process2 = new Process();
		Process process3 = new Process();
		
		ProcessTable processTable = new ProcessTable();
		processTable.addEntry(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process0).build());
		processTable.addEntry(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process1).build());
		processTable.addEntry(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process2).build());
		processTable.addEntry(new ProcessTableEntry.ProcessTableEntryBuilder().withProcess(process3).build());
		
		
		assertEquals(process0.getProcessId(), 0);
		assertEquals(process1.getProcessId(), 1);
		assertEquals(process2.getProcessId(), 2);
		assertEquals(process3.getProcessId(), 3);
		
		processTable.removeEntryForProcess(process1);
		assertEquals(process0.getProcessId(), 0);
		//IDs have shifted
		assertEquals(process2.getProcessId(), 1);
		assertEquals(process3.getProcessId(), 2);
		
		
		
		
		
	}
	
}
