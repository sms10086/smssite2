package com.smssite2.mgmt.message.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.note.NoteException;
import com.note.common.ConfigUtil;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.patch.FormatResult;
import com.smssite2.mgmt.patch.ImportUtil;
import com.smssite2.mgmt.patch.PhoneItem;

public class LoadPhoneService {	private static Log log = LogFactory.getLog(LoadPhoneService.class);
	String unicom = ConfigUtil.getProperties("unicom");
	String mobile = ConfigUtil.getProperties("mobile");
	String ctc = ConfigUtil.getProperties("ctc");

	public List<PhoneBean> LoadPhone(String newfn, String spit)
			throws Exception {
		if (newfn == null)
			throw new Exception("导入的文件不存在");
		String[] str = newfn.split(";");

		String filename = str[0];
		String phonecol = str[1];
		String namecol = str[2];
		String contentcol = str[3];
		if (unicom == null)
			throw new Exception("配置文件中联通号码段出错！");
		if (mobile == null)
			throw new Exception("配置文件中移动号码段出错！");
		Integer phoneType = null;
		if (filename == null)
			throw new Exception("导入的文件不存在");
		if (phonecol.equals("999"))
			throw new Exception("请按窗口上的提示方法, 正确选择<手机号码>列");

		if (filename.endsWith(".xls")) {
			InputStream in = new FileInputStream(filename);
			jxl.Workbook wb = Workbook.getWorkbook(in); 
			jxl.Sheet st = wb.getSheet(0); 
			int rownum = st.getRows(); 
			int column = st.getColumns();
			if (column < Integer.parseInt(phonecol))
				throw new Exception("导入文件错误！");
			List<PhoneBean> list = new ArrayList<PhoneBean>();
			for (int i = 0; i < rownum; i++) {
				if (this.isNullCell(st.getRow(i)))
					continue;
				String phone = st.getCell(Integer.parseInt(phonecol), i)
						.getContents().trim();
				phoneType = IsPhone(phone);
				if (phoneType < 0)
					continue;
				PhoneBean pb = new PhoneBean();
				pb.setPhone(phone);
				if (column < Integer.parseInt(namecol)) {
					pb.setName("");
				} else {
					pb.setName(st.getCell(Integer.parseInt(namecol), i)
							.getContents());
				}
				if (column < Integer.parseInt(contentcol)) {
					pb.setContent("");
				} else {
					pb.setContent(st.getCell(Integer.parseInt(contentcol), i)
							.getContents());
				}
				pb.setPhoneType(phoneType);
				list.add(pb);
			}
			return list;
		} else if (filename.endsWith(".txt")) {
			List<PhoneBean> list = new ArrayList<PhoneBean>();
			InputStream in = new FileInputStream(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String txt = "";
			int i = 0;
			while ((txt = br.readLine()) != null) {
				i++;
				Object[] o = txt.split(spit);

				if (o.length < Integer.parseInt(phonecol) + 1)
					continue;

				String phone = (String) o[Integer.parseInt(phonecol)];
				phoneType = IsPhone(phone);
				if (phoneType < 0)
					continue;
				PhoneBean pb = new PhoneBean();
				pb.setPhone(phone);
				if (o.length < Integer.parseInt(namecol)) {
					pb.setName("");
				} else {
					pb.setName((String) o[Integer.parseInt(namecol)]);
				}
				if (o.length < Integer.parseInt(contentcol)) {
					pb.setContent("");
				} else {
					pb.setContent((String) o[Integer.parseInt(contentcol)]);
				}
				pb.setPhoneType(phoneType);
				list.add(pb);
			}
			return list;
		} else {
			throw new Exception("导入文件类型错误！");
		}
	}

	public String LoadPhones(String newfn, String split, String sessionId,
			String eid, String staffid, String taskID) throws Exception {
		if (newfn == null)
			throw new Exception("导入的文件不存在");
		String[] str = newfn.split(";");
		String filename = str[0];
		String phonecol = str[1];
		String namecol = str[2];
		String contentcol = str[3];
		int phoneIndex = Integer.parseInt(phonecol);
		int nameIndex = Integer.parseInt(namecol);
		int contentIndex = Integer.parseInt(contentcol);
		ObjectInputStream ois = null;
		DbBean db = new DbBean();
		Connection conn = null;
		PreparedStatement prep = null;
		Object obj = null;
		FormatResult fr = null;
		String message=null;
		try {
			if (filename.endsWith("xls")) {
				fr = ImportUtil.formatXLS(filename, phoneIndex, nameIndex,
						contentIndex);
			} else if (filename.endsWith("xlsx")) {
				fr = ImportUtil.formatXLSX(filename, phoneIndex, nameIndex,
						contentIndex);
			} else if (filename.endsWith("txt")) {
				fr = ImportUtil.formatText(filename, split, phoneIndex,
						nameIndex, contentIndex);
			}

			if (fr == null)
				throw new NoteException("导入文件失败！");
			conn = db.getConnection();
			conn.setAutoCommit(false);
			prep = conn
					.prepareStatement("insert into phones(phone,name,content,phonetype,sessionid,eid,staffID,bornDate,taskID) values(?,?,?,?,'"
							+ sessionId
							+ "','"
							+ eid
							+ "','"
							+ staffid
							+ "',?,?)");
			if (fr.flag) {
				ois = new ObjectInputStream(new FileInputStream(fr.file));
				int i = 0;
				try {
					while (true) {
						try {
							obj = ois.readObject();
						} catch (IOException ioe) {
							break;
						}
						PhoneItem phoneItem = (PhoneItem) obj;
						prep.setString(1, phoneItem.phone);
						prep.setString(2, phoneItem.name);
						prep.setString(3, phoneItem.content);
						prep.setInt(4, phoneItem.phoneType);
						prep.setTimestamp(5, TimeUtil.now());
						prep.setString(6, taskID);
						prep.addBatch();
						i++;
						if (i >= 100) {
							prep.executeBatch();
							prep.clearBatch();
							i=0;
							System.out.println(">>>>>>insert into phones 100tiao"+TimeUtil.now());
						}
					}
					prep.executeBatch();
					prep.clearBatch();
					conn.commit();
					conn.setAutoCommit(true);
				} catch (Exception e1) {
					conn.rollback();
					e1.printStackTrace();
				}
				message="成功导入"+fr.real+"条号码";
				if(fr.err>0)message=message+" 有"+fr.err+"号码错误";
				int a=fr.total-fr.real-fr.err;
				if(a>0)message=message+" 有"+a+"号码重复";
				fr.file.delete();
			}
		} finally{
			try{
				if(ois!=null)ois.close();
				db.close(prep, conn);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return message;
	}

	 

	public int IsPhone(String phone) {
		if (phone == null)
			return -1;
		if (phone.length() != 11 && phone.length() != 8 || !Isnumber(phone))
			return -1;
		if (phone.trim().length() == 8)
			return 2;
		if (unicom.indexOf(phone.substring(0, 3)) >= 0) {
			return 1;
		} else if (mobile.indexOf(phone.substring(0, 3)) >= 0) {
			return 0;
		} else if (ctc.indexOf(phone.substring(0, 3)) >= 0) {
			return 2;
		} else {
			return -1;
		}
	}

	public String getPhone(String source) {
		if (StringUtil.isEmpty(source))
			return null;
		source = source.trim();
		if (source.startsWith("+86"))
			source = source.substring(3);
		else if (source.startsWith("86") && source.length() != 8) {
			source = source.substring(2); 
		}
		if (IsPhone(source) != -1)
			return source;
		else
			return null;
	}

	public boolean Isnumber(String str1) {
		String str2 = "0123456789";
		if (str1.length() < 1)
			return false;
		for (int i1 = 0; i1 < str1.length(); i1++) {
			if (str2.indexOf(str1.substring(i1, i1 + 1)) < 0)
				return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public String LoadLinkman(String newfn, String spit, String groupid,
			String Eid, String staffid) throws NoteException {
		if (newfn == null)
			throw new NoteException("导入的文件不存在");
		String[] str = newfn.split(";");
		String filename = str[0];
		String phonecol = str[1];
		String namecol = str[2];
		String contentcol = str[3];
		String postcol = str[4];
		String sexcol = str[5];
		String memocol = str[6];
		String brithdaycol = str[7];
		String phone = "";
		String name = "";
		String content = "";
		String post = "";
		String sex = "";
		String memo = "";
		String brithday = null;
		Integer phoneType = null;
		if (filename == null)
			throw new NoteException("导入的文件不存在");

		int count = 0;
		int err = 0;
		DbBean db = new DbBean();
		Connection conn = null;
		PreparedStatement ps = null;
		try { 
			conn = db.getConnection();
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("insert into linkman(name,phone,sex,post,birthday,userMemo,optionalContent,eid,staffid,groupid,phoneType,status) values(?,?,?,?,?,?,?,?,?,?,?,'1')");

			if (filename.endsWith(".xls")) {
				InputStream in = new FileInputStream(filename); // phoneFile.getInputStream();
				jxl.Workbook wb = Workbook.getWorkbook(in); 
				jxl.Sheet st = wb.getSheet("sheet1");
				if (st == null)
					st = wb.getSheet("Sheet1");
				if (st == null)
					throw new NoteException(
							"该xls中没有“Sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1”");
				int rownum = st.getRows(); 
				Integer column = st.getColumns();
				for (int i = 0; i < rownum; i++) {
					if (this.isNullCell(st.getRow(i)))
						continue;
					if (column >= Integer.parseInt(namecol)) {
						name = st.getCell(Integer.parseInt(namecol), i)
								.getContents();
						if (name.indexOf("姓名") >= 0) {
							err++;
							continue;
						}
					}
					if (column >= Integer.parseInt(phonecol)) {
						phone = getPhone(st.getCell(Integer.parseInt(phonecol),
								i).getContents());

					}
					phoneType = IsPhone(phone);
					if (phoneType < 0) {
						err++;
						continue;
					}
					if (column >= Integer.parseInt(contentcol)) {
						content = st.getCell(Integer.parseInt(contentcol), i)
								.getContents();
					}
					if (column >= Integer.parseInt(postcol)) {
						post = st.getCell(Integer.parseInt(postcol), i)
								.getContents();
					}
					if (column >= Integer.parseInt(sexcol)) {
						sex = st.getCell(Integer.parseInt(sexcol), i)
								.getContents();
					}
					if (column >= Integer.parseInt(memocol)) {
						memo = st.getCell(Integer.parseInt(memocol), i)
								.getContents();
					}
					if (column >= Integer.parseInt(brithdaycol)) {
						brithday = st.getCell(Integer.parseInt(brithdaycol), i)
								.getContents();
					}

					if (name != null && name.getBytes().length > 50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制。");
					if (phone != null && phone.getBytes().length > 20)
						throw new NoteException("某行手机号超出20位的限制。");
					if (sex != null && sex.getBytes().length > 50)
						throw new NoteException("某行性别超出50个字符(25个汉字)");
					if (post != null && post.getBytes().length > 30)
						throw new NoteException("某行职位超出30个字符(15个汉字)长度限制。");
					if (brithday != null && brithday.getBytes().length > 50)
						throw new NoteException("某行生日超出50个字符长度限制");
					if (content != null && content.getBytes().length > 250)
						throw new NoteException("某行可选短信内容超出250个字符(125个汉字)长度限制");
					count++;
					ps.setString(1, name);
					ps.setString(2, phone);
					ps.setString(3, sex);
					ps.setString(4, post);
					ps.setString(5, brithday);
					ps.setString(6, memo);
					ps.setString(7, content);
					ps.setString(8, Eid);
					ps.setString(9, staffid);
					ps.setString(10, groupid);
					ps.setInt(11, phoneType);
					ps.addBatch();
					if (count % 100 == 0) {
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
			} else if (filename.endsWith(".xlsx")) {
				XSSFWorkbook xwb = new XSSFWorkbook(filename);
				XSSFSheet sheet = xwb.getSheet("sheet1");
				if (sheet == null)
					sheet = xwb.getSheet("Sheet1");
				if (sheet == null)
					throw new NoteException(
							"该xlsx中没有“sheet1”这张工作表，注意要导入表为“sheet1”或“Sheet1”");
				XSSFRow row;
				XSSFCell cell;
				for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
					row = null;
					cell = null;
					row = sheet.getRow(i);
					if (row == null) {
						err++;
						continue;
					}
					if (row.getLastCellNum() < Integer.parseInt(phonecol)) {
						err++;
						continue;
					}

					cell = row.getCell(Integer.parseInt(phonecol));
					if (cell == null) {
						err++;
						continue;
					} else {
						if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							java.text.DecimalFormat formatter = new java.text.DecimalFormat(
									"########");
							String sx = formatter.format(cell
									.getNumericCellValue());
							phone = getPhone(sx);
						} else
							phone = getPhone(cell.toString());
					}

					phoneType = IsPhone(phone);
					if (phoneType < 0) {
						err++;
						continue;
					}

					if (row.getLastCellNum() < Integer.parseInt(namecol) + 1) {
						name = "";
					} else {
						cell = row.getCell(Integer.parseInt(namecol));
						if (cell == null) {
							name = "";
						} else
							name = cell.toString();
					}
					if (row.getLastCellNum() < Integer.parseInt(contentcol) + 1) {
						content = "";
					} else {
						cell = row.getCell(Integer.parseInt(contentcol));
						if (cell == null)
							content = "";
						else
							content = cell.toString();
					}
					if (row.getLastCellNum() < Integer.parseInt(sexcol) + 1) {
						sex = "";
					} else {
						cell = row.getCell(Integer.parseInt(sexcol));
						if (cell == null)
							sex = "";
						else
							sex = cell.toString();
					}
					if (row.getLastCellNum() < Integer.parseInt(postcol) + 1) {
						post = "";
					} else {
						cell = row.getCell(Integer.parseInt(postcol));
						if (cell == null)
							post = "";
						else
							post = cell.toString();
					}
					if (row.getLastCellNum() < Integer.parseInt(memocol) + 1) {
						memo = "";
					} else {
						cell = row.getCell(Integer.parseInt(memocol));
						if (cell == null)
							memo = "";
						else
							memo = cell.toString();
					}
					if (row.getLastCellNum() < Integer.parseInt(brithdaycol) + 1) {
						brithday = "";
					} else {
						cell = row.getCell(Integer.parseInt(brithdaycol));
						if (cell == null)
							brithday = "";
						else
							brithday = cell.toString();
					}
					if (name != null && name.getBytes().length > 50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制。");
					if (phone != null && phone.getBytes().length > 20)
						throw new NoteException("某行手机号超出20位的限制。");
					if (sex != null && sex.getBytes().length > 50)
						throw new NoteException("某行性别超出50个字符(25个汉字)");
					if (post != null && post.getBytes().length > 30)
						throw new NoteException("某行职位超出30个字符(15个汉字)长度限制。");
					if (brithday != null && brithday.getBytes().length > 50)
						throw new NoteException("某行生日超出50个字符长度限制");
					if (content != null && content.getBytes().length > 250)
						throw new NoteException("某行可选短信内容超出250个字符(125个汉字)长度限制");
					count++;
					ps.setString(1, name);
					ps.setString(2, phone);
					ps.setString(3, sex);
					ps.setString(4, post);
					ps.setString(5, brithday);
					ps.setString(6, memo);
					ps.setString(7, content);
					ps.setString(8, Eid);
					ps.setString(9, staffid);
					ps.setString(10, groupid);
					ps.setInt(11, phoneType);
					ps.addBatch();
					if (count % 100 == 0) {
						try {
							ps.executeBatch();
							ps.clearBatch();
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							conn.rollback();
						}
					}
				}
				row = null;
				cell = null;
				try {
					ps.executeBatch();
					ps.clearBatch();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					conn.rollback();
				}
				conn.commit();
				conn.setAutoCommit(true);
			} else if (filename.endsWith(".txt")) {
				InputStream in = new FileInputStream(filename);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String txt = "";
				int i = 0;
				while ((txt = br.readLine()) != null) {
					phone = "";
					i++;
					Object[] o = txt.split(spit);
					if (o.length >= (Integer.parseInt(phonecol) + 1)) {
						phone = getPhone((String) o[Integer.parseInt(phonecol)]);
					}
					phoneType = IsPhone(phone);
					if (phoneType < 0) {
						err++;
						continue;
					}
					if (o.length >= Integer.parseInt(namecol)) {
						name = (String) o[Integer.parseInt(namecol)];
					}
					if (o.length >= Integer.parseInt(contentcol)) {
						content = (String) o[Integer.parseInt(contentcol)];
					}
					if (o.length >= Integer.parseInt(sexcol)) {
						sex = (String) o[Integer.parseInt(sexcol)];
					}
					if (o.length >= Integer.parseInt(postcol)) {
						post = (String) o[Integer.parseInt(postcol)];
					}
					if (o.length >= Integer.parseInt(memocol)) {
						memo = (String) o[Integer.parseInt(memocol)];
					}
					if (o.length >= Integer.parseInt(brithdaycol)) {
						brithday = (String) o[Integer.parseInt(brithdaycol)];
					}
					if (name != null && name.getBytes().length > 50)
						throw new NoteException("某行姓名超出50个字符(25个汉字)长度限制。");
					if (phone != null && phone.getBytes().length > 20)
						throw new NoteException("某行手机号超出20位的限制。");
					if (sex != null && sex.getBytes().length > 50)
						throw new NoteException("某行性别超出50个字符(25个汉字)");
					if (post != null && post.getBytes().length > 30)
						throw new NoteException("某行职位超出30个字符(15个汉字)长度限制。");
					if (brithday != null && brithday.getBytes().length > 50)
						throw new NoteException("某行生日超出50个字符长度限制");
					if (content != null && content.getBytes().length > 250)
						throw new NoteException("某行可选短信内容超出250个字符(125个汉字)长度限制");
					count++;
					ps.setString(1, name);
					ps.setString(2, phone);
					ps.setString(3, sex);
					ps.setString(4, post);
					ps.setString(5, brithday);
					ps.setString(6, memo);
					ps.setString(7, content);
					ps.setString(8, Eid);
					ps.setString(9, staffid);
					ps.setString(10, groupid);
					ps.setInt(11, phoneType);
					ps.addBatch();
					if (count % 100 == 0) {
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
			} else {
				throw new NoteException("导入文件类型错误！");
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NoteException(e.getMessage());
		} finally {

			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		String message = null;
		if (count > 0)
			message = "成功导入" + count + "条记录.";
		else
			message = "导入不成功。";
		return message;
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

	private boolean isNullCell(Cell[] cells) {
		boolean result = false;
		if (cells == null)
			result = true;
		else {
			int nullcellnum = 0;

			for (int i = 0; i < cells.length; i++) {
				if (StringUtil.isEmpty(cells[i].getContents())) {
					nullcellnum++;
				}
			}
			if (nullcellnum >= cells.length)
				result = true;
		}
		return result;
	}
}
