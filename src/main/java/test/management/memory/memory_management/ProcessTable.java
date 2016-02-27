package test.management.memory.memory_management;

import java.util.ArrayList;
import java.util.List;

public class ProcessTable {
	
	List<ProcessTableEntry> processTable;

	public ProcessTable() {
		processTable = new ArrayList<>();
	}
	
	public ProcessTable(List<ProcessTableEntry> table) {
		this.processTable = table;
	}
	
	public List<ProcessTableEntry> getProcessTable() {
		return processTable;
	}

	public void setProcessTable(List<ProcessTableEntry> processTable) {
		this.processTable = processTable;
	}
	
	public void addEntry(ProcessTableEntry entry) {
		processTable.add(entry);
	}
}
