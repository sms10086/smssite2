package com.smssite2.mgmt.common;

import jxl.write.WritableWorkbook;

public class PageUtil {	
	@SuppressWarnings("unchecked")
	public static void writExcel(String name,OutputStream os, String[] names,List list,String dates[],int[] status)
	throws Exception {
		WritableFont HEADER_FONT_STYLE = new WritableFont(
		        WritableFont.TIMES, 15, WritableFont.BOLD);
		WritableCellFormat BODY_FONT_STYLE = new WritableCellFormat(
		        new WritableFont(WritableFont.TIMES,
		        		12));
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet sheet = wwb.createSheet(name, 0);
		WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(HEADER_FONT_STYLE);
		wcfFC.setWrap(true);
		wcfFC.setAlignment(Alignment.CENTRE);
		wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfFC.setBorder(Border.ALL,BorderLineStyle.THIN);
		Label labelC =null;
		if(getTime(dates)!=null)name = name+"("+getTime(dates)+")";
		labelC = new Label(0,0,name,wcfFC);
		sheet.setRowView(0, 400);
		sheet.mergeCells(0, 0, names.length-1, 0);
		sheet.addCell(labelC);
		for(int i=0;i<names.length;i++){
			labelC = new Label(i,1,names[i],wcfFC);
			sheet.addCell(labelC);
			sheet.setColumnView(i, 30);
		}
		if(list!=null){
			for(int i=0;i<list.size();i++){
				Object[] o = (Object[])list.get(i);
				for(int j = 0;j<o.length;j++){
					if(status!=null){
					if(status[i]!=-1){
						if(status[i]==1)
						BODY_FONT_STYLE.setBackground(Colour.GRAY_25);
						if(status[i]==2)BODY_FONT_STYLE.setBackground(Colour.RED);
						if(status[i]==0)BODY_FONT_STYLE.setBackground(Colour.WHITE);
					}else{
						BODY_FONT_STYLE.setBackground(Colour.WHITE);
					}
					}
					WritableCellFormat wcfFC1 = new jxl.write.WritableCellFormat(BODY_FONT_STYLE);
					wcfFC1.setWrap(true);
					wcfFC1.setAlignment(Alignment.LEFT);
					wcfFC1.setBorder(Border.ALL,BorderLineStyle.THIN);
					if(o[j] instanceof Integer){
						Number number = new Number(j,i+2,(Integer)o[j],wcfFC1);
						sheet.addCell(number);
					}else if(o[j] instanceof Short){
						Number number = new Number(j,i+2,(Short)o[j],wcfFC1);
						sheet.addCell(number);
					}else if(o[j] instanceof Long){
						Number number = new Number(j,i+2,(Long)o[j],wcfFC1);
						sheet.addCell(number);
					}
					else if(o[j] instanceof Float){
						Number number = new Number(j,i+2,(Float)o[j],wcfFC1);
						sheet.addCell(number);
					}
					else if(o[j] instanceof Double){
						Number number = new Number(j,i+2,(Double)o[j],wcfFC1);
						sheet.addCell(number);
					}
					else if(o[j] instanceof Timestamp){
						DateFormat format = new DateFormat("yyy-MM-dd");
						WritableCellFormat wc= new jxl.write.WritableCellFormat(format);
						if(status!=null){
							if(status[i]!=-1){
							if(status[i]==1){
								wc.setBackground(Colour.GRAY_25);
							}
							if(status[i]==2){
								wc.setBackground(Colour.RED);
							}
							if(status[i]==0)wc.setBackground(Colour.WHITE);
							}else{
								wc.setBackground(Colour.WHITE);
							}
						}
						wc.setAlignment(Alignment.CENTRE);
						wc.setBorder(Border.ALL, BorderLineStyle.THIN);
						DateTime date = new DateTime(j,i+2,(Timestamp)o[j],wc);
						sheet.addCell(date);
						
					}else{
						labelC = new Label(j,i+2,o[j]+"",wcfFC1);
						sheet.addCell(labelC);
					}
				}
			}
		}
		wwb.write();
		wwb.close();
		}
	
	private static String getTime(String[] dates){
		String time = null;
		if(dates!=null){
			if(!dates[0].equals("")&&!dates[1].equals("")){
				time="从"+dates[0].substring(0, 10)+"到"+dates[1].substring(0,10);
			}else
			if(dates[0].equals("")&&!dates[1].equals("")){
				time="截止到"+dates[1].substring(0,10);
			}else{
				time="从"+dates[0].substring(0,10)+"开始";
			}
		}
		return time;
		}
	
}
