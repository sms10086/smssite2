package com.note.common.page;


public class AbstractPageBar implements PageBar {	
	protected int pageIndex;
	protected int pageSize;
	protected int totalItems;
	protected int showIndexes;
	protected int totalPages;

	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getShowIndexes() {
		return showIndexes;
	}
	public void setShowIndexes(int showIndexes) {
		this.showIndexes = showIndexes;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
	
}
