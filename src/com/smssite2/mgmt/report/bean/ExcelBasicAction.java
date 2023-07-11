package com.smssite2.mgmt.report.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class ExcelBasicAction extends Action {	private static final Log LOG = LogFactory.getLog(ExcelBasicAction.class);
	
	public static final String CSV = "csv";
	public static final String XLS = "xls";
	public static final String TXT = "txt";
	public  ActionForm form;
	public HttpSession session;
	private String fileType = "xls";
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.form=form;
		this.session=request.getSession();
		response.setContentType("application/vnd.ms-excel");
		String filename = this.getFileName();
		filename = new String(filename.getBytes(), "utf-8");
		response.setHeader("Location", filename);
		response.setHeader("Cache-Control", "max-age=10000");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);
		response.setCharacterEncoding("gb2312");
		
		try {
			if(this.fileType.equalsIgnoreCase(this.XLS)){
				ServletOutputStream os = response.getOutputStream();
				response.setContentType("application/vnd.ms-excel");
				writeExcel(getTitle(),os,getColumnNames(),getDatas());
			}else if(this.fileType.equalsIgnoreCase(this.CSV)){
				PrintWriter pw = response.getWriter();
				response.setContentType("application/vnd.ms-excel");
				writeCVS(getTitle(),pw,getColumnNames(),getDatas());
			}else{
				PrintWriter pw = response.getWriter();
				response.setContentType("text/plain");
				writeCVS(getTitle(),pw,getColumnNames(),getDatas());
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return null;
	}
	
	public abstract String getTitle();

	public abstract String getFileName();

	public abstract ArrayList<String> getColumnNames();

	public abstract List<Object[]> getDatas();

	public void setFileType(String type){
		this.fileType = type;
	}

	private void writeCVS(String title, PrintWriter pw,
			ArrayList<String> columnNames, List<Object[]> datas) throws IOException {
		
		if(title!=null&&title.length()!=0){
			pw.write(title);
			pw.write("\n");
		}
		if(columnNames!=null&&columnNames.size()!=0){
			for(int i=0; i<columnNames.size(); i++){
				pw.write(columnNames.get(i));
				if(i<columnNames.size()-1)
					pw.write(",");
			}
			pw.write("\n");
		}
		
		if(datas!=null&&datas.size()!=0){
			for(int i=0; i<datas.size(); i++){
				Object[] rows = datas.get(i);
				for(int j=0; j<rows.length; j++){
					Object v = rows[j];
					if(v instanceof String){
						pw.write((String)v);
					}else if(v instanceof Integer){
						pw.write(Integer.toString((Integer)v));
					}else if (v instanceof Short) {
						pw.write(Short.toString((Short)v));
					} else if (v instanceof Long) {
						pw.write(Long.toString((Long)v));
					} else if (v instanceof Float) {
						pw.write(Float.toString((Float)v));
					} else if (v instanceof Double) {
						pw.write(Double.toString((Double)v));
					} else if (v instanceof Timestamp) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						pw.write(format.format((Timestamp)v));
					} else {
						pw.write(v.toString());
					}
					if(j<rows.length-1)
						pw.write(",");
				}
				pw.write("\n");
			}
		}
		pw.flush();
		pw.close();
	}

	public static void writeExcel(String title, OutputStream os, List<String> columns,
			List<Object[]> datas) throws Exception {
		final WritableFont HEADER_FONT_STYLE = new WritableFont(WritableFont.TIMES,
				15, WritableFont.BOLD);
		final WritableCellFormat BODY_FONT_STYLE = new WritableCellFormat(
				new WritableFont(WritableFont.TIMES, 12));

		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet sheet = wwb.createSheet(title, 0);
		WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
				HEADER_FONT_STYLE);
		wcfFC.setWrap(true);
		wcfFC.setAlignment(Alignment.CENTRE);
		wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN); 
		Label labelC = null;
		labelC = new Label(0, 0, title, wcfFC);
		sheet.setRowView(0, 400);
		sheet.mergeCells(0, 0, columns.size() - 1, 0);
		sheet.addCell(labelC);
		for (int i = 0; i < columns.size(); i++) {
			labelC = new Label(i, 1, columns.get(i), wcfFC);
			sheet.addCell(labelC);
			sheet.setColumnView(i, 20);
		}
		if (datas != null) {
			int total=0;
			for (int i = 0; i < datas.size(); i++) {
				if(total>0&&total%65000==0){
					sheet=wwb.createSheet(title+i/65000, i/65000);
					labelC = new Label(0, 0, title, wcfFC);
					sheet.setRowView(0, 400);
					sheet.mergeCells(0, 0, columns.size() - 1, 0);
					sheet.addCell(labelC);
					for (int n = 0; n < columns.size(); n++) {
						labelC = new Label(n, 1, columns.get(n), wcfFC);
						sheet.addCell(labelC);
						sheet.setColumnView(n, 20);
					}
					total=0;
				}
				Object[] o = (Object[]) datas.get(i);
				for (int j = 0; j < o.length; j++) {
					BODY_FONT_STYLE.setBackground(Colour.WHITE);
				
					WritableCellFormat wcfFC1 = new jxl.write.WritableCellFormat(
							BODY_FONT_STYLE);
					wcfFC1.setWrap(true);
					wcfFC1.setAlignment(Alignment.LEFT);
					wcfFC1.setBorder(Border.ALL, BorderLineStyle.THIN);
					if (o[j] instanceof Integer) {
						Number number = new Number(j, total + 2, (Integer) o[j],
								wcfFC1);
						sheet.addCell(number);
					} else if (o[j] instanceof Short) {
						Number number = new Number(j, total + 2, (Short) o[j],
								wcfFC1);
						sheet.addCell(number);
					} else if (o[j] instanceof Long) {
						Number number = new Number(j, total + 2, (Long) o[j],
								wcfFC1);
						sheet.addCell(number);
					} else if (o[j] instanceof Float) {
						Number number = new Number(j, total + 2, (Float) o[j],
								wcfFC1);
						sheet.addCell(number);
					} else if (o[j] instanceof Double) {
						Number number = new Number(j, total + 2, (Double) o[j],
								wcfFC1);
						sheet.addCell(number);
					} else if (o[j] instanceof Timestamp) {
						DateFormat format = new DateFormat("yyy-MM-dd");
						WritableCellFormat wc = new jxl.write.WritableCellFormat(
								format);
						wc.setBackground(Colour.WHITE);
						wc.setAlignment(Alignment.CENTRE);
						wc.setBorder(Border.ALL, BorderLineStyle.THIN);
						DateTime date = new DateTime(j, total + 2,
								(Timestamp) o[j], wc);
						sheet.addCell(date);

					} else {
						labelC = new Label(j, total + 2, o[j] + "", wcfFC1);
						sheet.addCell(labelC);
					}
				}
				total++;
			}
		}

		wwb.write();

		wwb.close();

	}

	public ActionForm getForm() {
		return form;
	}

	public void setForm(ActionForm form) {
		this.form = form;
	}
	public Object getNullValue(Object o) {
		return o != null ? o : "";
	}
}
