package test.management.memory.memory_management;

import java.util.HashMap;
import java.util.List;

public class PageTable {

	private HashMap<Integer, List<Page>> pageTable;

	private int pageSize;

	public PageTable() {
		pageTable = new HashMap<>();
	}
	
	public HashMap<Integer, List<Page>> getPageTable() {
		return pageTable;
	}


	public void setPageTable(HashMap<Integer, List<Page>> pageTable) {
		this.pageTable = pageTable;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void addProcessPages(int processId, List<Page> processPages) {
		pageTable.put(processId, processPages);
	}

}
