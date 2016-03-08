package test.management.memory.memory_management;

import test.management.memory.memory_management.process.Process;
import test.management.memory.memory_management.process.ProcessPriority;

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
	
	/*
	 * Returns a process from the process table that is in a specified state
	 */
	public Process getProcess(ProcessState state) {
		for (ProcessTableEntry entry: entries) {
			if (entry.getProcess().getProcessState().equals(state)) {
				return entry.getProcess();
			} 
		}
		
		return null;
	}
	
	
	/*
	 * Key method that decides which process is to be removed from the main memory when  processes are moved to disk. 
	 * Precedence of the priorities is following: if process is terminated, then it can be moved to disk first.  We DO NOT want to
	 * move process that is of high priority and is running to disk. 
	 */
	public Process getLowestPriorityProcess() {
		Process process = null;
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.TERMINATED, ProcessPriority.LOW);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.TERMINATED, ProcessPriority.HIGH);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.IDLE, ProcessPriority.LOW);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.IDLE, ProcessPriority.HIGH);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.NEW, ProcessPriority.LOW);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.NEW, ProcessPriority.HIGH);
		if (process != null) {
			return process;
		} 
		
		process = getProcessOfSpecifiedStateAndPriority(ProcessState.RUNNING, ProcessPriority.LOW);
		if (process != null) {
			process.setProcessState(ProcessState.IDLE);
			return process;
		} 
		
		return process;
	}
	
	public ProcessTableEntry findProcessEntry(Process process) {
		for (ProcessTableEntry entry: entries) {
			if (entry.getProcess().equals(process)) {
				return entry;
			}
		}
		return null;
	}
	
	
	private Process getProcessOfSpecifiedStateAndPriority(ProcessState state, ProcessPriority priority) {
		Process process = null;
		for (ProcessTableEntry entry: entries) {
			process = entry.getProcess();
			if (process.getProcessState().equals(state) && process.getProcessPriority().equals(priority)) {
				return process;
			}
		}
		return process;
	}
	
	
	
	public  void removeEntry(ProcessTableEntry entry) {
		entries.remove(entry);
	}
	
	public int getSize() {
		return entries.size();
	}
	
}
