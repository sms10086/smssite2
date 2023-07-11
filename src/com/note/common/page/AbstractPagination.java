package com.note.common.page;

import java.util.ArrayList;
import org.apache.struts.action.ActionForm;

public abstract class AbstractPagination extends ActionForm {	
	public static final int DEFAULT_PAGE_SIZE = 16;
	
	protected int pageIndex;
	protected int pageSize;
	
	public int getPageIndex() {
		if (pageIndex < 0)
			return 0;
		return pageIndex;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}



	public int getPageSize() {
		if (pageSize <= 0)
			return DEFAULT_PAGE_SIZE;
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ArrayList getPage() {
		return getPage(null);
	}

	public ArrayList getPage(Class clazz) {
		throw new RuntimeException("not implements");
	}

	public int getStartIndex() {
		return getPageSize() * getPageIndex();
	}

	public int getEndIndex() {
		return getStartIndex() + getPageSize();
	}
}
