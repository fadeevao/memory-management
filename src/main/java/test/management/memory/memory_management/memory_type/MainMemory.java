package test.management.memory.memory_management.memory_type;

import test.management.memory.memory_management.ProcessTable;

public class MainMemory extends Memory{

	private ProcessTable processTable;
	
	public MainMemory(ProcessTable table) {
		this.processTable = table;
	}
	
	public MainMemory() {
		this.processTable = new ProcessTable();
	}

	public ProcessTable getProcessTable() {
		return processTable;
	}

	public void setProcessTable(ProcessTable processTable) {
		this.processTable = processTable;
	}
}
