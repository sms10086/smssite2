package com.note.common.page;


public class PageBarUtil {
	public static String makeBar(PageBar page) {
		
		int halfPages = page.getShowIndexes() >> 1;
		int startPage = page.getPageIndex() - halfPages;
		if (startPage < 0) startPage = 0;
		int endPage = startPage + page.getShowIndexes() - 1;
		if (endPage > page.getTotalPages() - 1) {
			endPage = page.getTotalPages() - 1;
			startPage = endPage - page.getShowIndexes() + 1;
			if (startPage < 0) startPage = 0;
		}
		int currentPage = 0;
		if(page.getTotalItems()!=0)
			currentPage = page.getPageIndex() + 1;
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td class=\"page_Num\" nowrap>&nbsp;&nbsp;共有 ")
		.append(page.getTotalItems()).append(" 条记录，当前第 ").
		append(currentPage).append("/").append(page.getTotalPages()).
		append(" 页，每页<input name=\"pageSize\" type=\"text\" class=\"input\" onkeydown=\"if(event.keyCode==13) javascript:goPage(").append(currentPage).append(");\" value=\"").append(page.getPageSize()).append("\" style=\"height:15px; width:40px; border:1px solid #999999;\" size=\"4\">行 </td>" +
				"<td><table border=\"0\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\">"+
				"<tr>"+
				"<td width=\"40\"><img src=\"../../../images/first.gif\" width=\"37\" height=\"15\" onclick=\"javascript:goPage(1);\"/></td>"+
				"<td width=\"45\"><img src=\"../../../images/back.gif\" width=\"43\" height=\"15\" onclick=\"javascript:goPage(").append(currentPage-1).append(");\"/></td>"+
				"<td width=\"45\"><img src=\"../../../images/next.gif\" width=\"43\" height=\"15\" onclick=\"javascript:goPage(").append(currentPage+1).append(");\"/></td>"+
				"<td width=\"40\"><img src=\"../../../images/last.gif\" width=\"37\" height=\"15\" onclick=\"javascript:goPage(").append(page.getTotalPages()).append(");\"/></td>"+
				"<td  nowrap>转到第"+
				" <input name=\"pageIndex\" type=\"text\" class=\"input\" id=\"pageIndex\" size=\"6\" style=\"height:15px; width:40px; border:1px solid #999999;\" onkeydown=\"if(event.keyCode==13) javascript:goPage(").append(-1).append(");\"  value=\"").append(currentPage).append("\"/>"+
				"页 </td>"+
				"<td width=\"40\"><img src=\"../../../images/go.gif\" width=\"37\" height=\"15\" onclick=\"javascript:goPage(").append(-1).append(");\"/></td>"+
				"</tr> </table></td></tr>");
			
		sb.append("</tr>");
		return sb.toString();
	}
	
}
