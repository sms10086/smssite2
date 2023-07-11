package com.smssite2.mgmt.patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jxl.Cell;
import jxl.Workbook;
import com.note.NoteException;
import com.note.common.StringUtil;

public class ImportUtil {	
	private static boolean isNullRow(Cell[] cells){
		if(cells == null)
			return true;
		int nullCount = 0;
		for(int i = 0; i < cells.length; i++ ) {
			if(StringUtil.isEmpty(cells[i].getContents()))
				nullCount ++;
		}
		return (nullCount >= cells.length);
	}
	
	private static void close(FileInputStream fi, FileOutputStream fo) {
		try {
			if ( fi != null ) fi.close();
		} catch (Throwable thr) {}
		try {
			if ( fo != null ) fo.close();
		} catch (Throwable thr) {}
	}
	
	public static FormatResult formatXLS( String filename, int phoneIndex, int nameIndex, int contentIndex ) throws Exception {
		
		FormatResult fr = new FormatResult();
		
		FileOutputStream fos = new FileOutputStream( fr.file );
		ObjectOutputStream obj = new ObjectOutputStream( fos );

		FileInputStream fis = new FileInputStream(filename);
		try {
			
			jxl.Workbook wb = Workbook.getWorkbook( fis );
			
			jxl.Sheet st = wb.getSheet("sheet1");
			if(st == null) st = wb.getSheet("Sheet1");
			if(st == null) throw new NoteException("该xls中没有“Sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1” ");
			
			int row = st.getRows();
			int col = st.getColumns();
			if ( col < phoneIndex ) throw new NoteException("导入文件错误！");
			
			
			
			PhoneFilter pf = new PhoneFilter(row);
			
			for(int i = 0; i < row; i++ ) {
				Cell[] cells = st.getRow(i);
				if( isNullRow( cells ) ) continue;
				PhoneItem item = new PhoneItem();
				item.phone = MobileUtil.format( st.getCell(phoneIndex, i).getContents() );
				item.phoneType = MobileUtil.getType( item.phone );
				if( item.phoneType < 0 ) {
					fr.err++;
					continue;
				}
				fr.total ++;
				if ( pf.isExist(Long.parseLong(item.phone)))
					continue;
				fr.real ++;
				
				if( col > nameIndex ) {
					item.name = st.getCell(nameIndex, i).getContents();
					if (item.name != null && item.name.getBytes().length > 50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制，");
				} else {
					item.name = "";
				}
				
				if(col > contentIndex ) {
					item.content = st.getCell(contentIndex, i).getContents();
					if (item.content != null && item.content.getBytes().length >2000)
						throw new NoteException("某行内容超出2000个字符(1000个汉字)长度限制，");
				} else {
					item.content = "";
				}
				obj.writeObject( item );
				
			}
			obj.flush();
			fr.flag = true;
			return fr;
		}
		finally {
			close( fis, fos );
			if (! fr.flag )
				fr.file.delete();
		}
	}
	
	private static String getCellString(XSSFRow row, int index) {
		if (row.getLastCellNum() < index) {
			return null;
		}
		XSSFCell cell = row.getCell( index );
		if (cell == null)
			return null;
		if(cell.getCellType()== HSSFCell.CELL_TYPE_NUMERIC){
			java.text.DecimalFormat formatter = new java.text.DecimalFormat("########");
	    	return formatter.format(cell.getNumericCellValue());
  	   	}
		return cell.toString();
	}
	
	public static FormatResult formatXLSX( String filename, int phoneIndex, int nameIndex, int contentIndex ) throws Exception {
		
		FormatResult fr = new FormatResult();
		
		FileOutputStream fos = new FileOutputStream( fr.file );
		ObjectOutputStream obj = new ObjectOutputStream( fos );

		try {
			XSSFWorkbook xwb = new XSSFWorkbook(filename);
			
			XSSFSheet sheet = xwb.getSheet("sheet1") ;
			if( sheet == null) sheet = xwb.getSheet("Sheet1");
			if( sheet == null ) throw new NoteException("该xlsx中没有“sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1”  ");
			
			XSSFRow row;
			XSSFCell cell;
			
			
			PhoneFilter pf = new PhoneFilter(sheet.getLastRowNum() - sheet.getFirstRowNum() + 1);
			
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++ ) {
				PhoneItem item = new PhoneItem();
				row = sheet.getRow(i);
				if( row == null ) {
			    	fr.err++;
			    	continue;
			    }
				item.phone = MobileUtil.format( getCellString(row, phoneIndex) );
				item.phoneType = MobileUtil.getType( item.phone );
				if( item.phoneType < 0 ) {
					fr.err++;
					continue;
				}
				fr.total ++;
				if ( pf.isExist(Long.parseLong(item.phone)))
					continue;
				fr.real ++;
				
				
				item.name = getCellString(row, nameIndex);
				if (item.name != null ) {
					if (item.name.getBytes().length > 50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制，");
				} else {
					item.name = "";
				}
				
				item.content = getCellString(row, contentIndex);
				if(item.content != null ) {
					if (item.content.getBytes().length > 2000 )
						throw new NoteException("某行内容超出2000个字符(1000个汉字)长度限制，");
				} else {
					item.content = "";
				}
				obj.writeObject( item );
			}
			obj.flush();
			fr.flag = true;
			return fr;
		}
		finally {
			close( null, fos );
			if (! fr.flag )
				fr.file.delete();
		}
	}
	
	public static FormatResult formatText( String filename, String split, int phoneIndex, int nameIndex, int contentIndex  ) throws Exception {
		
		FormatResult fr = new FormatResult();
		
		File f = new File(filename);
		PhoneFilter pf = new PhoneFilter( (int)(f.length() / 13) + 1 );
		
		FileInputStream fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader( new InputStreamReader(fis) );
		
		FileOutputStream fos = new FileOutputStream( fr.file );
		ObjectOutputStream obj = new ObjectOutputStream( fos );

		String txt;
		
		
		try {
			while( (txt = br.readLine()) != null ) {
				PhoneItem item = new PhoneItem();
			 
				String[] o = txt.split(split);
				if(o.length <= phoneIndex ) continue;
				item.phone = MobileUtil.format( o[ phoneIndex] );
			 
				item.phoneType = MobileUtil.getType( item.phone );
				if( item.phoneType < 0 ) {
					fr.err++;
					continue;
				}
				fr.total++;
				if ( pf.isExist(Long.parseLong(item.phone)))
					continue;
				fr.real ++;
				
				if(o.length <= nameIndex) {
					item.name = "";
				} else {
					item.name = o[nameIndex];
					if (item.name != null && item.name.getBytes().length>50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制，");
				}
	
				if(o.length<= contentIndex) {
					item.content = "";
				} else {
					item.content = o[contentIndex];
					if (item.content != null && item.content.getBytes().length>2000 )
						throw new NoteException("某行内容超出2000个字符(1000个汉字)长度限制，");
				}
				obj.writeObject(item);
			}
			obj.flush();
			fr.flag = true;
			return fr;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			close(fis, fos);
			if (! fr.flag )
				fr.file.delete();
		}
		
	}
}
