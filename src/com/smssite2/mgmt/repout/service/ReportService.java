package com.smssite2.mgmt.repout.service;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.StringUtil;

public class ReportService {	private static final Log LOG = LogFactory.getLog(ReportService.class);
	private PrintWriter pw;

	public PrintWriter getPw() {
		return pw;
	}

	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}

	@SuppressWarnings("unchecked")
	public void getReportInfo(String topStr,List list) throws Exception {
		if (list != null && list.size()!=0) {
			try {
				this.pw.write(topStr+"\n");
				int count=0;
				for(int i=0;i<list.size();i++) {
					List reportList = (List) list.get(i);
					Object[] os = null;
					if(reportList!=null)
						os = reportList.toArray();
					getInfo(os);
					count ++;
					if ((count & 0xff) == 0)
						pw.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}
	public void getReportInfo(String topStr, ResultSet rs) throws Exception{
		if(rs!=null){
			try{
				this.pw.write(topStr+"\n");
				int count=0;
				while(rs.next()){
					Object[] os = new Object[12];
					
					os[0]=rs.getString("id");
					os[1]=rs.getString("staffid");
					os[2]=rs.getTimestamp("sendtime");
					os[3]=rs.getString("phone");
					os[4]=rs.getString("name");
					
					if(rs.getInt("result")==0){
						os[5]="成功";
					}else{
						os[5]="已发送";
					}
					if(rs.getString("status")==null||rs.getString("status").trim().length()==0||rs.getString("status").equals("10004")){
						os[6]="发出";
					}else
					if(rs.getString("status").equals("0")){
						os[6]="收到";
					}else{
						os[6]="etc";
					}
					if(!StringUtil.isEmpty(rs.getString("content")))
						os[7]=rs.getString("content").replace("\r\n", "").replace(",", "，");
					else os[7]=rs.getString("content");
					os[8]=rs.getString("smsnum");
					os[9]=rs.getString("smssign");
					os[10]=1;
					os[11]=rs.getTimestamp("addtime");
					 
					getInfo(os);
					count ++;
					if ((count & 0xff) == 0)
						pw.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
		}
	}
	public void getInfo(Object[] os) {
		if (os == null)
			return;
		for(int i=0;i<os.length;i++){
			if(os[i]!=null){
				pw.write(os[i].toString());
			}
			if(i!=os.length-1)
				pw.write(',');
		}
		pw.write("\n");
		os=null;
	}

	
	
}
