package com.smssite2.mgmt.message.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import jxl.Workbook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.ConfigUtil;
import com.note.common.DbBean;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;

public class LoadPhoneBean {
	private static Log log = LogFactory.getLog(LoadPhoneService.class);
	String unicom=ConfigUtil.getProperties("unicom");
	String mobile=ConfigUtil.getProperties("mobile");
	String ctc = ConfigUtil.getProperties("ctc");
	private static int total=0;
	private String newfn;
	private String spit;
	private List<PhoneBean> list=new ArrayList();
	
	public static int getTotal() {
		return total;
	}
	public static void setTotal(int total) {
		LoadPhoneBean.total = total;
	}
	public List<PhoneBean> getList() {
		return list;
	}
	public void setList(List<PhoneBean> list) {
		this.list = list;
	}
	public LoadPhoneBean(String newfn,String spit){
		this.newfn=newfn;
		this.spit=spit;
	}
	
	public LoadPhoneBean instance(LoadPhoneForm phoneForm){
		
		return null;
	}
	public List<PhoneBean> LoadPhone(int startindex,int pageSize) throws Exception{
		if(newfn==null)throw new Exception("导入的文件不存在");
		String[] str = newfn.split(";");
		String filename=str[0];
		String phonecol=str[1];
		String namecol=str[2];
		String contentcol=str[3];
		if(unicom==null)throw new Exception("配置文件中联通号码段出错！");
		if(mobile==null)throw new Exception("配置文件中移动号码段出错！");
		Integer phoneType=null;
		if(filename==null)throw new Exception("导入的文件不存在");
		if(phonecol.equals("999"))throw new Exception("请按窗口上的提示方法, 正确选择<手机号码>列");
	
		if(filename.endsWith(".xls")){
			InputStream in =new FileInputStream(filename); //phoneFile.getInputStream();
			jxl.Workbook wb = Workbook.getWorkbook(in); 					
			jxl.Sheet st = wb.getSheet(0);									
			int rownum=st.getRows();										
			int column=st.getColumns();
			if(column<Integer.parseInt(phonecol))throw new Exception("导入文件错误！");
			List<PhoneBean> list =new ArrayList<PhoneBean>();
			for(int i=0;i<rownum;i++){
				String phone=st.getCell(Integer.parseInt(phonecol), i).getContents().trim();
				phoneType=IsPhone(phone);
				if(phoneType<0)continue;
				total++;
				if(total>=startindex&&total<(startindex+pageSize)){
					PhoneBean pb=new PhoneBean();
					pb.setPhone(phone);
					if(column<Integer.parseInt(namecol)){
						pb.setName("");
					}else{
						pb.setName(st.getCell(Integer.parseInt(namecol), i).getContents());
					}
					if(column<Integer.parseInt(contentcol)){
						pb.setContent("");
					}else{
						pb.setContent(st.getCell(Integer.parseInt(contentcol), i).getContents());
					}
					pb.setPhoneType(phoneType);
					list.add(pb);
				}
			}
			return list;
		}else
		if(filename.endsWith(".txt")){
			List<PhoneBean> list=new ArrayList<PhoneBean>();
			InputStream in =new FileInputStream(filename);
			BufferedReader br = new BufferedReader( new InputStreamReader(in));
			String txt="";
			int i=0;
			while((txt=br.readLine())!=null){i++;
				Object[] o=txt.split(spit);
				
				if(o.length<Integer.parseInt(phonecol)+1)continue;
				
				String phone=(String) o[Integer.parseInt(phonecol)];
				phoneType=IsPhone(phone);
				if(phoneType<0)continue;
				total++;
				if(total>=startindex&&total<(startindex+pageSize)){
					PhoneBean pb=new PhoneBean();
					pb.setPhone(phone);
					if(o.length<Integer.parseInt(namecol)){
						pb.setName("");
					}else{
						pb.setName((String) o[Integer.parseInt(namecol)]);
					}
					if(o.length<Integer.parseInt(contentcol)){
						pb.setContent("");
					}else{
						pb.setContent((String)o[Integer.parseInt(contentcol)]);
					}
					pb.setPhoneType(phoneType);
					list.add(pb);
				}
			}
			return list;
		}else{
			throw new Exception("导入文件类型错误！");
		}
	}
	public int IsPhone(String phone){
		if(phone==null) return -1;
		if(phone.startsWith("+86")){
			phone=phone.substring(3);
		}else if(phone.startsWith("86")&&phone.length()!=8){
			phone=phone.substring(2); 
		}
		phone = phone.trim();
		if(phone.length()!=11&&phone.length()!=8||!Isnumber(phone))return -1;
		if(phone.trim().length()==8)return 2;
		if(unicom.indexOf(phone.substring(0, 3))>=0){
			return 1;
		}else if(mobile.indexOf(phone.substring(0,3))>=0){
			return 0;
		}else if(ctc.indexOf(phone.substring(0,3))>=0){
			return 2;
		}else {
			return -1;
		}
	}
 public	boolean Isnumber(String str1){
		 String str2="0123456789";
		  if(str1.length()<1) return false;
		  for(int i1=0;i1<str1.length();i1++){
		     if(str2.indexOf(str1.substring(i1,i1+1))<0) return false;
		  }
		  return true;
		  }
	@SuppressWarnings("unchecked")
	public String LoadLinkman(String newfn, String spit, String groupid,String Eid,String staffid) throws Exception {
		if(newfn==null)throw new Exception("导入的文件不存在");
		String[] str = newfn.split(";");
		String filename=str[0];
		String phonecol=str[1];
		String namecol=str[2];
		String contentcol=str[3];
		String postcol=str[4];
		String sexcol=str[5];
		String memocol=str[6];
		String brithdaycol=str[7];
		String phone="";
		String name="";
		String content="";
		String post="";
		String sex="";
		String memo="";
		String brithday=null;
		Integer phoneType=null;
		if(filename==null)throw new Exception("导入的文件不存在");
	
		
		int count=0;
		DbBean db= new DbBean();
		Connection conn = null;
		PreparedStatement  ps = null;
		try{		
					conn=db.getConnection();
					conn.setAutoCommit(false);
					ps=conn.prepareStatement("insert into linkman(name,phone,sex,post,birthday,userMemo,optionalContent,eid,staffid,groupid,phoneType,status) values(?,?,?,?,?,?,?,?,?,?,?,'1')");
					
					if(filename.endsWith(".xls")){
						InputStream in =new FileInputStream(filename); //phoneFile.getInputStream();
						jxl.Workbook wb = Workbook.getWorkbook(in); 					
						jxl.Sheet st = wb.getSheet(0);									
						int rownum=st.getRows();										
						Integer column=st.getColumns();
						for(int i=0;i<rownum;i++){
						
							if(column>=Integer.parseInt(namecol)){
								name=st.getCell(Integer.parseInt(namecol), i).getContents();
								if(name.indexOf("姓名")>=0)continue;
							}
							if(column>=Integer.parseInt(phonecol)){
								phone=st.getCell(Integer.parseInt(phonecol),i).getContents();
								 
							}
							phoneType=IsPhone(phone);
							if(phoneType<0)continue;
							if(column>=Integer.parseInt(contentcol)){
								content=st.getCell(Integer.parseInt(contentcol),i).getContents();
							}
							if(column>=Integer.parseInt(postcol)){
								post=st.getCell(Integer.parseInt(postcol),i).getContents();
							}
							if(column>=Integer.parseInt(sexcol)){
								sex=st.getCell(Integer.parseInt(sexcol),i).getContents();
								if(sex.length()>1)continue;
							}
							if(column>=Integer.parseInt(memocol)){
								memo=st.getCell(Integer.parseInt(memocol),i).getContents();
							}
							if(column>=Integer.parseInt(brithdaycol)){
								brithday=st.getCell(Integer.parseInt(brithdaycol),i).getContents();
							}
							count++;
							ps.setString(1, name);
							ps.setString(2, phone);
							ps.setString(3, sex);
							ps.setString(4, post);
							ps.setString(5, brithday);
							ps.setString(6,memo);
							ps.setString(7,content);
							ps.setString(8, Eid);
							ps.setString(9, staffid);
							ps.setString(10, groupid);
							ps.setInt(11, phoneType);
							ps.addBatch();
							if(count%100==0){
								try {
									ps.executeBatch();
									ps.clearBatch();
								} catch (Exception e) {
									log.error(e.getMessage(), e);
								}
							}
						}
						try {
							ps.executeBatch();
							ps.clearBatch();
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}else  if(filename.endsWith(".txt")){
						InputStream in =new FileInputStream(filename);
						BufferedReader br = new BufferedReader( new InputStreamReader(in));
						String txt="";
						int i=0;
						while((txt=br.readLine())!=null){
							phone="";
							i++;
							Object[] o=txt.split(spit);
							if(o.length>=(Integer.parseInt(phonecol)+1)){
								phone=(String)o[Integer.parseInt(phonecol)];
							}
							phoneType=IsPhone(phone);
							if(phoneType<0)continue;
							if(o.length>=Integer.parseInt(namecol)){
								name=(String)o[Integer.parseInt(namecol)];
							}
							if(o.length>=Integer.parseInt(contentcol)){
								content=(String)o[Integer.parseInt(contentcol)];
							}
							if(o.length>=Integer.parseInt(sexcol)){
								sex=(String)o[Integer.parseInt(sexcol)];
							}
							if(o.length>=Integer.parseInt(postcol)){
								post=(String)o[Integer.parseInt(postcol)];
							}
							if(o.length>=Integer.parseInt(memocol)){
								memo=(String)o[Integer.parseInt(memocol)];
							}
							if(o.length>=Integer.parseInt(brithdaycol)){
								brithday=(String)o[Integer.parseInt(brithdaycol)];
							}
							count++;
							ps.setString(1, name);
							ps.setString(2, phone);
							ps.setString(3, sex);
							ps.setString(4, post);
							ps.setString(5, brithday);
							ps.setString(6,memo);
							ps.setString(7,content);
							ps.setString(8, Eid);
							ps.setString(9, staffid);
							ps.setString(10, groupid);
							ps.setInt(11, phoneType);
							ps.addBatch();
							if(count%100==0){
								try {
									ps.executeBatch();
									ps.clearBatch();
								} catch (Exception e) {
									log.error(e.getMessage(), e);
								}
							}
						}
						try {
							ps.executeBatch();
							ps.clearBatch();
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}else{
						throw new Exception("导入文件类型错误！");
					}
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			try{
				if(ps!=null)ps.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
				log.error(e.getMessage(), e);
			}
		}
		return "成功导入"+count+"条记录";
	}
}
