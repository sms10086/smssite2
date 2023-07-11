package com.smssite2.mgmt.staff;

import java.util.ArrayList;
import java.util.List;

public class GetPageList {	@SuppressWarnings("unchecked")
	public static List getPages(List list,int startIndex,int pageSize){
		if(list==null)return new ArrayList();
		if(pageSize<=0)pageSize=16;
		if(startIndex<=0)startIndex=0;
		List list1=new ArrayList();
		if(list.size()>=startIndex+pageSize){
			for(int i=0;i<pageSize;i++){
				list1.add(list.get(startIndex+i));
			}
		}else{
			for(int i=0;i<list.size()-startIndex;i++){
				list1.add(list.get(startIndex+i));
			}
		}
		return list1;
	}
}
