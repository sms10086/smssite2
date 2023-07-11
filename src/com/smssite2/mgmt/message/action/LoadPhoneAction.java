package com.smssite2.mgmt.message.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.common.ConfigUtil;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.form.LoadPhoneForm;

public class LoadPhoneAction extends Action{	private String tempPath = ConfigUtil.getProperties("uploadpath");
	private static int total=20;
	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String flag=request.getParameter("flag");
		String txtfilename=request.getParameter("txtfilename");
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		if(flag==null||!flag.equals("1")){
				
				FormFile phoneFile=phoneForm.getPhoneFile();
				if(phoneFile==null||phoneFile.getFileSize()==0){
					request.setAttribute("message", "上传文件为空，请选择文件");
					return mapping.findForward("failure");
				}
				String file = phoneFile.getFileName();
				String extendName=getExtendName(file);
				if(extendName==null){
					request.setAttribute("message", "上传文件格式错误！");
					return mapping.findForward("failure");
				}
				String EId = ((Account)request.getSession().getAttribute("account")).getEId();
				String filepath = createFilePath(EId);
				String filename =System.currentTimeMillis()+request.getSession().getId()+extendName;
				
				byte[] b = phoneForm.getPhoneFile().getFileData();
				File ufile = new File(filepath+filename);
				try{
					if(!ufile.exists()){
						ufile.createNewFile();
					}
				}catch(Exception e){
					e.printStackTrace();
					request.setAttribute("message", "上传文件失败");
					request.setAttribute("errornote", "可能服务器不让创建文件");
					return mapping.findForward("failure");
				}
				OutputStream output = new FileOutputStream(ufile);
				output.write(b);
				output.flush();
				output.close();
				txtfilename=filepath+filename;
				request.setAttribute("txtfilename", txtfilename);
				try{
					if(extendName.equals(".xls")){
						InputStream in =new FileInputStream(filepath+filename); //phoneFile.getInputStream();					
						jxl.Workbook wb = Workbook.getWorkbook(in); 					
						jxl.Sheet st=wb.getSheet("sheet1");
						if(st==null)st=wb.getSheet("Sheet1");
						if(st==null)throw new NoteException("该xls中没有“Sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1” ");
						int rownum=this.getRightRows(st);
						Integer column=st.getColumns();
						List list =new ArrayList();
						
						if(rownum<=total){
							total=rownum;
						}
						for(int i=0;i<total;i++){
							Object[] obj=new Object[column];
							for(int j=0;j<column;j++){
								Cell celltop = st.getCell(j, i);   
								String strTop = celltop.getContents();
								obj[j]=strTop;
								
							}
							list.add(obj);
						}
						wb.close();
						request.setAttribute("list", list);
						request.setAttribute("column", column);
						request.setAttribute("totalnum", rownum);
						request.getSession().removeAttribute("o");
					}else if(extendName.equals(".txt")){
						int num=0;
						String spit=request.getParameter("spit");
						List list=new ArrayList();
						InputStream in =new FileInputStream(filepath+filename);
						BufferedReader br = new BufferedReader( new InputStreamReader(in));
						String str;
						int i=1;
						while((str=br.readLine())!=null){
							num++;
							Object[] o=str.split(spit);
							if(num<=total)
							list.add(o);
							if(o.length>i)i=o.length;
						}
						br.close();
						in.close();
						 
						request.setAttribute("list", list);
						request.setAttribute("column",i);
						request.getSession().removeAttribute("o");
						request.setAttribute("spit", spit);
						request.setAttribute("totalnum", num);
					}
					else if(extendName.equals(".xlsx")){
						XSSFWorkbook xwb = new XSSFWorkbook(filepath+filename);
						
						XSSFSheet sheet=xwb.getSheet("sheet1") ;
						if(sheet==null)sheet=xwb.getSheet("Sheet1");
						if(sheet==null)throw new NoteException("该xlsx中没有“sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1”  ");
						XSSFRow row;
						XSSFCell cell;
						int rows=sheet.getPhysicalNumberOfRows();
						int column=0;
						List list=new ArrayList();
						int c=0;
						for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
							row=null;
							cell=null;
						    row = sheet.getRow(i);
						    if(row==null)continue;
						    if(column<row.getLastCellNum())column=row.getLastCellNum();
						    Object[] o= new Object[row.getLastCellNum()];
						    for (int j = 0; j < row.getLastCellNum(); j++) {
						       cell=row.getCell(j);
						       
						       if(cell==null){
						    	   o[j]="";
						       }else
						       {
						    	   if(cell.getCellType()== HSSFCell.CELL_TYPE_NUMERIC){
								    	  java.text.DecimalFormat   formatter   =   new   java.text.DecimalFormat("########");
								    	  String   str   =   formatter.format(cell.getNumericCellValue());
								    	  o[j]=str;
							    	   }else
							    		   o[j]=cell.toString();
						       }
						    }
						    list.add(o);
						    c++;
						    if(c>=total)break;
						}
						row=null;
						cell=null;
					
						request.setAttribute("list", list);
						request.setAttribute("column", column);
						request.setAttribute("totalnum", rows);
						request.getSession().removeAttribute("o");
					}
				}catch(Exception e){
					e.printStackTrace();
					request.setAttribute("message",e.getMessage());
					return mapping.findForward("failure");
				}
				
				return mapping.findForward("success");
			}
		else
		if(txtfilename!=null){
			String cbcols=request.getParameter("cbcols");
			if(cbcols==null)cbcols="0";
			String cbfields=request.getParameter("cbfields");
			try{
				  if(txtfilename.endsWith(".xlsx")){
					XSSFWorkbook xwb = new XSSFWorkbook(txtfilename);
					XSSFSheet sheet=xwb.getSheet("sheet1") ;
					if(sheet==null)sheet=xwb.getSheet("Sheet1");
					if(sheet==null)throw new NoteException("该xlsx中没有“sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1”  ");
					XSSFRow row=null;
					XSSFCell cell=null;
					int rows=sheet.getPhysicalNumberOfRows();
					int column=0;
					List list=new ArrayList();
					int c=0;
					for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
						row=null;
						cell=null;
					    row = sheet.getRow(i);
					    if(row==null)continue;
					    if(column<row.getLastCellNum())column=row.getLastCellNum();
					    Object[] obj= new Object[row.getLastCellNum()];
					    for (int j = 0; j < row.getLastCellNum(); j++) {
					       cell=row.getCell(j);
					       if(cell==null){
					    	   obj[j]="";
					       }else{
					    	   if(cell.getCellType()== HSSFCell.CELL_TYPE_NUMERIC){
						    	  java.text.DecimalFormat   formatter   =   new   java.text.DecimalFormat("########");
						    	  String   str   =   formatter.format(cell.getNumericCellValue());
						    	  obj[j]=str;
					    	   }else
					    		   obj[j]=cell.toString();
					       }
					    }
					    list.add(obj);
					    c++;
					    if(c>=total)break;
					}
					row=null;
					cell=null;
					Object[] o=(Object[]) request.getSession().getAttribute("o");
					 StringBuffer sb=new StringBuffer();
					if(o!=null){
						for(int n=0;n<o.length;n++){
							sb.append(o[n]);
						}
						if(sb.toString().indexOf("#")<0){
							o=new Object[column];
							o[Integer.parseInt(cbcols)]=cbfields;
						}else{
							o[Integer.parseInt(cbcols)]=cbfields;
						}
					}else{
						o=new Object[column];
						for(int k=0;k<column;k++){
							o[k]="";
						}
						o[Integer.parseInt(cbcols)]=cbfields;
					}
					if(list!=null){
						list.add(0,o);
					}
					
					request.setAttribute("list", list);
					request.setAttribute("column", column);
					request.setAttribute("totalnum", rows);
					request.getSession().setAttribute("o", o);
				}else
				if(txtfilename.endsWith(".xls")){
					InputStream in =new FileInputStream(txtfilename); //phoneFile.getInputStream();					
					jxl.Workbook wb = Workbook.getWorkbook(in); 					
					jxl.Sheet st=wb.getSheet("sheet1");
					if(st==null)st=wb.getSheet("Sheet1");
					if(st==null)throw new NoteException("该xls中没有“Sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1” ");
					int rownum=this.getRightRows(st);
					Integer column=st.getColumns();
					List list =new ArrayList();
					if(rownum<=total){
						total=rownum;
					}
					for(int i=0;i<total;i++){
					 
						Object[] obj=new Object[column];
						for(int j=0;j<column;j++){
							Cell celltop = st.getCell(j, i);   
							String strTop = celltop.getContents();
							obj[j]=strTop;
						}
						list.add(obj);
					}
					Object[] o=(Object[]) request.getSession().getAttribute("o");
					 StringBuffer sb=new StringBuffer();
					if(o!=null){
						for(int n=0;n<o.length;n++){
							sb.append(o[n]);
						}
						if(sb.toString().indexOf("#")<0){
							o=new Object[column];
							o[Integer.parseInt(cbcols)]=cbfields;
						}else{
							o[Integer.parseInt(cbcols)]=cbfields;
						}
					}else{
						o=new Object[column];
						for(int k=0;k<column;k++){
							o[k]="";
						}
						o[Integer.parseInt(cbcols)]=cbfields;
					}
					if(list!=null){
						list.add(0,o);
					}
					request.getSession().setAttribute("o", o);
					request.setAttribute("list", list);
					request.setAttribute("column", column);
					request.setAttribute("totalnum", rownum);
				}else
				if(txtfilename.endsWith(".txt")){
					int num=0;
					String spit=request.getParameter("spit");
					List list=new ArrayList();
					InputStream in =new FileInputStream(txtfilename);
					 
					BufferedReader br = new BufferedReader( new InputStreamReader(in));
					String str;
					int i=1;
					while((str=br.readLine())!=null){
						num++;
						Object[] o=str.split(spit);
						if(num<=total)
						list.add(o);
						if(o.length>i)i=o.length;
					}
					Object[] o=(Object[]) request.getSession().getAttribute("o");
					 StringBuffer sb=new StringBuffer();
					if(o!=null){
						if(i>o.length){
							Object[] o1=new Object[i];
							for(int s=0;s<o.length;s++){
								o1[s]=o[s];
							}
							o=o1.clone();
						}
						for(int n=0;n<o.length;n++){
							sb.append(o[n]);
						}
						if(sb.toString().indexOf("#")<0){
							o=new Object[i];
							o[Integer.parseInt(cbcols)]=cbfields;
						}else{
							o[Integer.parseInt(cbcols)]=cbfields;
							
						}
					}else{
						o=new Object[i];
						for(int k=0;k<i;k++){
							o[k]="";
						}
						o[Integer.parseInt(cbcols)]=cbfields;
					}
					if(list!=null){
						list.add(0,o);
					}
					br.close();
					in.close();
					request.getSession().setAttribute("o", o);
					request.setAttribute("list", list);
					request.setAttribute("column",i);
					request.setAttribute("spit", spit);
					request.setAttribute("totalnum", num);
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
				request.setAttribute("message", e.getMessage());
				return mapping.findForward("failure");
			}
			request.setAttribute("cbfields", cbfields);
			request.setAttribute("cbcols", cbcols);
			request.setAttribute("txtfilename", txtfilename);
			return mapping.findForward("success");
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	private String createFilePath(String companyId) {
		Date dnow = new Date();
		String dir = this.tempPath+File.separator+companyId+File.separator+(dnow.getYear()+1900)+File.separator+(dnow.getMonth()+1)+File.separator+dnow.getDate()+File.separator;
		File f = new File(dir);
		f.mkdirs();
		return dir;
	}

	private String getExtendName(String file){
		file = file.toLowerCase();
	   if(file.endsWith(".xls")){
			return ".xls";
		}else if(file.endsWith(".txt")){
			return ".txt";
		}else if(file.endsWith(".xlsx")){
			return ".xlsx";
		}
		return null;
	}
 
	private int getRightRows(Sheet sheet) {
		int rsCols = sheet.getColumns(); 
		int rsRows = sheet.getRows(); 
		int nullCellNum;
		int afterRows = rsRows;
		for (int i = 1; i < rsRows; i++) { 
		   nullCellNum = 0;
		    for (int j = 0; j < rsCols; j++) {
		        String val = sheet.getCell(j, i).getContents();
		        val = StringUtils.trimToEmpty(val);
		        if (StringUtils.isBlank(val))
		           nullCellNum++;
		    }
		    if (nullCellNum >= rsCols) { 
		     afterRows--;          
		   }
		}
		return afterRows;
		}

}
