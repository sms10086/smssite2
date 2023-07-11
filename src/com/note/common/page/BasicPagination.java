package com.note.common.page;


public abstract class BasicPagination extends AbstractPagination implements PageBar {	
	public int getShowIndexes() {
		return 9;
	}
	
	public int getTotalPages() {
		return (getTotalItems() + this.getPageSize() - 1) / this.getPageSize();
	}
	
	protected int total;

	public int getTotalItems() {
		return total;
	}
	
	public void setTotalItems(int total){
		this.total=total;
	};
	
	public String getPageBar() {
		return PageBarUtil.makeBar(this);
	}

	public void adjustPageIndex() {
		int totalPage = this.getTotalPages();
		this.pageIndex = this.pageIndex - 1;
		if (this.pageIndex >= totalPage) this.pageIndex = totalPage - 1;
		else if (this.pageIndex < 0) this.pageIndex = 0;
	}
}
