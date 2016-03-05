package test.management.memory.memory_management.memory_type;

import test.management.memory.memory_management.ProcessTable;

public class HardDisk extends Memory {
	
	private ProcessTable processTable;

	public HardDisk(int capacity) {
		super(capacity);
		this.processTable = new ProcessTable();
	}
	
	public HardDisk() {
		super(DEFAULT_SIZE);
		this.processTable = new ProcessTable();
	}

	public ProcessTable getProcessTable() {
		return processTable;
	}

	public void setProcessTable(ProcessTable processTable) {
		this.processTable = processTable;
	}
}
