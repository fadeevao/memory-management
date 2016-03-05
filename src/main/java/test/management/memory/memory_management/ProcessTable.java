package test.management.memory.memory_management;

import test.management.memory.memory_management.process.Process;

import java.util.ArrayList;
import java.util.List;

import test.management.memory.memory_management.process.ProcessState;

public class ProcessTable {
	
	List<ProcessTableEntry> entries;

	public ProcessTable() {
		entries = new ArrayList<>();
	}
	
	public ProcessTable(List<ProcessTableEntry> table) {
		this.entries = table;
	}
	
	public List<ProcessTableEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProcessTableEntry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(ProcessTableEntry entry) {
		entries.add(entry);
	}
	
	public test.management.memory.memory_management.process.Process getProcess(ProcessState state) {
		for (ProcessTableEntry entry: entries) {
			if (entry.getProcess().getProcessState().equals(state)) {
				return entry.getProcess();
			} 
		}
		
		return null;
	}
	
	public ProcessTableEntry findProcessEntry(Process process) {
		for (ProcessTableEntry entry: entries) {
			if (entry.getProcess().equals(process)) {
				return entry;
			}
		}
		return null;
	}
	
	public  void removeEntry(ProcessTableEntry entry) {
		entries.remove(entry);
	}
	
	public int getSize() {
		return entries.size();
	}
	
}
