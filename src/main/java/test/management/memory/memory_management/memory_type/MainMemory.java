package test.management.memory.memory_management.memory_type;

import test.management.memory.memory_management.OperatingSystem;

import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.ProcessTable;
import test.management.memory.memory_management.ProcessTableEntry;

public class MainMemory extends Memory{

	private ProcessTable processTable;
	
	private OperatingSystem operatingSystem;

	public MainMemory(int capacity, ProcessTable table, OperatingSystem operatingSystem) {
		super(capacity);
		this.processTable = table;
		this.operatingSystem = operatingSystem;
		write(this.operatingSystem.getData());
	}
	
	public MainMemory(ProcessTable table, OperatingSystem operatingSystem) {
		this(DEFAULT_SIZE, table, operatingSystem);
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
	
	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	
	public void write(Process process) {
		ProcessTableEntry entry = new ProcessTableEntry(process);
		entry.setBaseRegister(getIndex());
		write(process.getData());
		entry.setLimitRegister(getIndex() - 1);
		processTable.addEntry(entry);
	}
}
