package management.memory.memory_management;

import management.memory.memory_management.process.Process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PageTable {

	private HashMap<Process, List<Page>> pageTable;

	private int pageSize;

	public PageTable() {
		pageTable = new HashMap<>();
	}
	
	public HashMap<Process, List<Page>> getPageTable() {
		return pageTable;
	}


	public void setPageTable(HashMap<Process, List<Page>> pageTable) {
		this.pageTable = pageTable;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void addProcessPages(Process process, List<Page> processPages) {
		pageTable.put(process, processPages);
	}
	
	public List<Page> getProcessPages(Process process) {
		if (pageTable.containsKey(process)) {
			return pageTable.get(process);
		}
		return  null;
	}
	
	public Process getProcessFromId(int processId) {
		Iterator<Process> iterator = pageTable.keySet().iterator();
		while (iterator.hasNext()) {
			Process process = iterator.next();
			if (process.getProcessId() == processId) {
				return process;
			}
		}
		return null;
	}

}
