package management.memory.memory_management;


import management.memory.memory_management.process.Process;

public class ProcessTableEntry {
	private Process process;

	private int baseRegister;
	
	private int limitRegister;
	
	public ProcessTableEntry(Process process) {
		this.process = process;
	}
	
	public ProcessTableEntry(ProcessTableEntryBuilder builder) {
		this.process = builder.getProcess();
		this.baseRegister = builder.getBaseRegister();
		this.limitRegister = builder.getLimitRegister();
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
	
	public static class ProcessTableEntryBuilder {
		
		private Process process;

		private int baseRegister;
		
		private int limitRegister;
		
		public ProcessTableEntryBuilder() {}
		
		public ProcessTableEntryBuilder withProcess(Process process) {
			this.process = process;
			return this;
		}
		
		public ProcessTableEntryBuilder withBaseRegister(int baseReg) {
			this.baseRegister = baseReg;
			return this;
		}
		
		public ProcessTableEntryBuilder withLimitRegister(int limitReg) {
			this.limitRegister = limitReg;
			return this;
		}
		
		public ProcessTableEntry build() {
			return new ProcessTableEntry(this);
		}
		
		public Process getProcess() {
			return process;
		}

		public int getBaseRegister() {
			return baseRegister;
		}

		public int getLimitRegister() {
			return limitRegister;
		}
		
	}
}
