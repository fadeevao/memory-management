package management.memory.memory_management.memory_type;

import management.memory.memory_management.ProcessTable;

public class HardDisk extends Memory {
	
	private ProcessTable processTable;

	public HardDisk(int capacity) {
		super(capacity);
		this.processTable = new ProcessTable();
	}
	
	public HardDisk() {
		super(DEFAULT_SIZE*2); // greater than main memory twice
		this.processTable = new ProcessTable();
	}

	public ProcessTable getProcessTable() {
		return processTable;
	}
}
