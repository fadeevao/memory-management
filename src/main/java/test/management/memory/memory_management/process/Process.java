package test.management.memory.memory_management.process;


public class Process {
	
	private int processId;
	
	private ProcessState state;
	
	private ProcessPriority priority;
	
	private byte[] memoryNeeded;
	
	private static int DEFAULT_ID = -1; 	
	
	private static final int DEFAULT_MEMORY_REQUIREMENT = 64;
	
	public Process(int memoryRequirement) {
		this(DEFAULT_ID, memoryRequirement);
	}
	
	public Process() {
		this(DEFAULT_ID, DEFAULT_MEMORY_REQUIREMENT);
	}
	
	public Process(int id, int memoryRequirement) {
		this.processId = id;
		memoryNeeded = new byte[memoryRequirement];
	}
	
	public byte[] getMemoryNeeded() {
		return memoryNeeded;
	}
	public void setMemoryNeeded(byte[] memoryNeeded) {
		this.memoryNeeded = memoryNeeded;
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
}
