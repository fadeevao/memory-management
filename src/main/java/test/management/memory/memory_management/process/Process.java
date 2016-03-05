package test.management.memory.memory_management.process;


public class Process {
	
	private int processId;
	
	private ProcessState state;
	
	private ProcessPriority priority;
	
	private byte[] data;
	
	public final static int DEFAULT_PROCESS_ID = -1; 	
	
	public static final int DEFAULT_MEMORY_REQUIREMENT = 64;
	
	public Process(int memoryRequirement) {
		this(DEFAULT_PROCESS_ID, memoryRequirement);
	}
	
	public Process() {
		this(DEFAULT_PROCESS_ID, DEFAULT_MEMORY_REQUIREMENT);
	}
	
	public Process(int id, int memoryRequirement) {
		this.processId = id;
		data = new byte[memoryRequirement];
	}
	
	public Process(ProcessBuilder builder) {
		this.data = builder.getData();
		this.priority = builder.getPriority();
		this.processId = builder.getProcessId();
		this.state = builder.getState();
	}
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}
	
	public ProcessState getProcessState() {
		return state;
	}
	
	public void setProcessState(ProcessState state) {
		this.state =  state;
	}
	
	public void setProcessPriority(ProcessPriority priority) {
		this.priority = priority;
	}
	
	public ProcessPriority getProcessPriority() {
		return priority;
	}
	
	public static class ProcessBuilder {

		private int processId;
		
		private ProcessState state;
		
		private ProcessPriority priority;
		
		private byte[] data;
		
		public ProcessBuilder() {}
		
		public ProcessBuilder witId(int id) {
			this.processId = id;
			return this;
		}
		
		public ProcessBuilder withState(ProcessState state) {
			this.state = state;
			return this;
		}
		
		public ProcessBuilder withPriority(ProcessPriority priority) {
			this.priority = priority;
			return this;
		}
		
		public ProcessBuilder withData(byte[] data) {
			this.data = data;
			return this;
		}
		
		public Process build() {
			Process process = new Process(this);
			return process;
		}
		
		public int getProcessId() {
			return processId;
		}

		public ProcessState getState() {
			return state;
		}

		public ProcessPriority getPriority() {
			return priority;
		}

		public byte[] getData() {
			return data;
		}
		
		
	}

}
