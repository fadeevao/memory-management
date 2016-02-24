package test.management.memory.memory_management;

public class Process {
	
	private int baseRegister;
	
	private int limitRegister;
	
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
