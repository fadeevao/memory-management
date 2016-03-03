package test.management.memory.memory_management;


import test.management.memory.memory_management.process.Process;

public class ProcessTableEntry {
	private Process process;

	private int baseRegister;
	
	private int limitRegister;
	
	public ProcessTableEntry(Process process) {
		this.process = process;
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public int getBaseRegister() {
		return baseRegister;
	}

	public void setBaseRegister(int baseRegister) {
		this.baseRegister = baseRegister;
	}

	public int getLimitRegister() {
		return limitRegister;
	}

	public void setLimitRegister(int limitRegister) {
		this.limitRegister = limitRegister;
	}
}
