import java.util.ArrayList;
import java.util.List;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.report.bean.EveSendBean;
import com.smssite2.mgmt.report.bean.ParagraphBean;
import com.smssite2.mgmt.report.bean.SmsReceiveBean;
import com.smssite2.mgmt.report.bean.SmsSendBean;
import com.smssite2.mgmt.report.dao.IReportDao;
import com.smssite2.mgmt.report.form.ReportBaseForm;

public class ReportDao implements IReportDao{		
	
	
	public Integer countOfSmsSendWithPagination(ReportBaseForm form, String routeType) {
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		String indicators=form.getIndicators();
		StringBuffer sbc;
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		
		sbc=new StringBuffer()
			.append("(select staffId,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A ")
			.append(" where ")
			 
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"' ");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<='"+end+"' ");
			}
			if(indicators!=null&&!indicators.trim().equalsIgnoreCase("")){
				sbc.append(" and status='"+indicators+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and staffId like '%"+staffId+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select count(*)")
				.append(" from (")
				.append("   select distinct staffId")
				.append("   from "+sbc+"as C")
				.append(" ) as T");
		System.out.println(sb.toString());
		return DbBean.getInt(sb.toString(), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<SmsSendBean> selectSmsSendWithPagination(ReportBaseForm form,String routeType) {
	System.out.println(TimeUtil.now());
		form.setTotalItems(countOfSmsSendWithPagination(form,routeType));
		System.out.println(TimeUtil.now());
		form.adjustPageIndex();
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		String indicators=form.getIndicators();
		StringBuffer sbc;
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		sbc=new StringBuffer()
			.append("(select staffId,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A")
			.append(" where ")
			.append("1=1  ");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"' ");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<='"+end+"' ");
			}
			if(indicators!=null&&!indicators.trim().equalsIgnoreCase("")){
				sbc.append(" and status='"+indicators+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and staffId like '%"+staffId+"%'");
			}
			sbc.append(" and A.EID='"+form.getEid()+"' ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select staffId,")
				.append("       (select count(*) from "+sbc+"as C1 where result='0' and C1.staffId=C.staffId   ) as sucTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where result='1' and C1.staffId=C.staffId   ) as faiTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=0 and result='0' and C1.staffId=C.staffId) as sucTotalMobile,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=0 and result='1' and C1.staffId=C.staffId) as faiTotalMobile,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=1 and result='0' and C1.staffId=C.staffId) as sucTotalUnicom,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=1 and result='1' and C1.staffId=C.staffId) as faiTotalUnicom,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=2 and result='0' and C1.staffId=C.staffId) as sucTotalTelecom,")
				.append("       (select count(*) from "+sbc+"as C1 where phoneType=2 and result='1' and C1.staffId=C.staffId) as faiTotalTelecom")
				.append(" from  (select distinct staffId")
				.append("        from ("+sbc+")as T) as C")
				.append(" group by staffId")
				.append(" order by staffId");
		List<SmsSendBean> list=null;
		System.out.println(sb.toString());
		list=DbBean.select(sb.toString(), null, form.getStartIndex(), form.getPageSize(), SmsSendBean.class);
		System.out.println(TimeUtil.now());
		ArrayList<SmsSendBean> sendList = new ArrayList<SmsSendBean>(0);
		if(list==null||list.size()==0)
			return null;
		else{
			
		}
		return list;
		
	}

	public Float getPrencent(Integer in1,Integer in2){
		float i2=0;
		float i1=0;
		if (in2==null)i2=0;
		else i2=in2.intValue();
		if(in1==null)i1=0;
		else i1=in1.intValue();
		if(i1+i2<=0)return Float.valueOf(0);
		return Float.valueOf(i2/(i1+i2));
		}

	public Integer countOfParagraphWithPagination(ReportBaseForm form, String routeType){
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		String indicators=form.getIndicators();
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		
		StringBuffer sbc;
		sbc=new StringBuffer()
			.append("(select subString(phone,1,3) as phone3,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A ")
			.append(" where ")
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"' ");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<='"+end+"' ");
			}
			if(indicators!=null&&!indicators.trim().equalsIgnoreCase("")){
				sbc.append(" and status='"+indicators+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and staffId like '%"+staffId+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select count(*)")
				.append(" from (")
				.append("   select distinct phone3")
				.append("   from "+sbc+"as C")
				.append(" ) as T");
			
			
		return DbBean.getInt(sb.toString(), null);
		}
	@SuppressWarnings("unchecked")
	public List<ParagraphBean> selectParagraphWithPagination(ReportBaseForm form,String routeType){
		form.setTotalItems(countOfParagraphWithPagination(form,routeType));
		form.adjustPageIndex();
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		String indicators=form.getIndicators();
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		StringBuffer sbc;
		sbc=new StringBuffer()
			.append("(select subString(phone,1,3) as phone3,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A ")
			.append(" where ")
			
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"' ");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<='"+end+"' ");
			}
			if(indicators!=null&&!indicators.trim().equalsIgnoreCase("")){
				sbc.append(" and status='"+indicators+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and staffId like '%"+staffId+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select phone3,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.phone3=C.phone3 and result='0')as sucTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.phone3=C.phone3 and result='1')as faiTotal ")
				.append(" from (select distinct phone3")
				.append("       from ("+sbc+")as T)as C")
				.append(" order by phone3");
			
			
			List<ParagraphBean> list=DbBean.select(sb.toString(), null, form.getStartIndex(), form.getPageSize(), ParagraphBean.class);
			ArrayList<ParagraphBean> sendList = new ArrayList<ParagraphBean>(0);
			if(list==null||list.size()==0)
				return null;
			else{
				for(Object o:list){
					ParagraphBean bean = (ParagraphBean) o;
					Float prencent=getPrencent(bean.getFaiTotal(),bean.getSucTotal());
					bean.setPercent(prencent);
					sendList.add(bean);
					}
			}
		return sendList;
	}
	
	public Integer countOfEveSendWithPagination(ReportBaseForm form, String routeType) {
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		StringBuffer sbc;
		sbc=new StringBuffer()
			.append("(select DATE_FORMAT(sendtime,'%Y-%m-%d')  as sendDate,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A ")
			.append(" where ")
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"'");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<'"+end+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and staffId like '%"+staffId+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select count(*)")
				.append(" from (select distinct sendDate")
				.append("       from ("+sbc+")as T )as C");
		return DbBean.getInt(sb.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<EveSendBean> selectEveSendWithPagination(ReportBaseForm form,String routeType) {
		form.setTotalItems(countOfEveSendWithPagination(form,routeType));
		form.adjustPageIndex();
		String staffId=form.getStaffId();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		StringBuffer sbc;
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		sbc=new StringBuffer()
			.append("(select DATE_FORMAT(sendtime,'%Y-%m-%d')  as sendDate,result,phoneType")
			.append(" from SendUser_History_"+routeType+" A")
			.append(" where ")
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime>='"+start+"'");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and sendTime<='"+end+"'");
			}
			if(staffId!=null&&!staffId.trim().equalsIgnoreCase("")){
				sbc.append(" and userName like '%"+staffId+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select sendDate,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='0')as sucTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='1')as faiTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='0' and phoneType=0)as sucTotalMobile,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='1' and phoneType=0)as faiTotalMobile,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='0' and phoneType=1)as sucTotalUnicom,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='1' and phoneType=1)as faiTotalUnicom,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='0' and phoneType=2)as sucTotalTelecom,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.sendDate=C.sendDate and result='1' and phoneType=2)as faiTotalTelecom")
				.append(" from (select distinct sendDate")
				.append("       from ("+sbc+")as T )as C")
				.append(" order by sendDate");
			
		List<EveSendBean> list=DbBean.select(sb.toString(), null, form.getStartIndex(), form.getPageSize(), EveSendBean.class);
		ArrayList<EveSendBean> sendList = new ArrayList<EveSendBean>(0);
		if(list==null||list.size()==0)
			return null;
		else{
			for(Object o:list){
				
			}
		}
		return sendList;
	}

	public Integer countOfSmsReceiveWithPagination(ReportBaseForm form, String routeType){
		String receiver=form.getReceiver();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		StringBuffer sbc;
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		
		sbc=new StringBuffer()
			.append("(select receiver,phoneType")
			.append(" from Received_"+routeType+" A")
			.append(" where ")
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and receiveTime>='"+start+"'");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and receiveTime<='"+end+"'");
			}
			if(receiver!=null&&!receiver.trim().equalsIgnoreCase("")){
				sbc.append(" and receiver like '%"+receiver+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select" )
				.append("      count(*)" )
			   	.append(" from  (select distinct receiver")
				.append("        from ("+sbc+")as T) as C");
			
			
			return DbBean.getInt(sb.toString(), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<SmsReceiveBean> selectSmsReceiveWithPagination(ReportBaseForm form,String routeType) {
		form.setTotalItems(countOfSmsReceiveWithPagination(form,routeType));
		form.adjustPageIndex();
		String receiver=form.getReceiver();
		String startSaleDate=form.getStartDate();
		String endSaleDate=form.getEndDate();
		StringBuffer sbc;
		Timestamp start = null;
		Timestamp end = null;
		if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase(""))
			start = Timestamp.valueOf(startSaleDate+" 00:00:00.0");
		if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase(""))
			end = Timestamp.valueOf(endSaleDate+" 23:59:59.999");
		sbc=new StringBuffer()
			.append("(select receiver,phoneType")
			.append(" from Received_"+routeType+" A")
			.append(" where ")
			.append(" A.EID='"+form.getEid()+"'");
			if(startSaleDate!=null&&!startSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and receiveTime>='"+start+"'");
			}
			if(endSaleDate!=null&&!endSaleDate.trim().equalsIgnoreCase("")){
				sbc.append(" and receiveTime<='"+end+"'");
			}
			if(receiver!=null&&!receiver.trim().equalsIgnoreCase("")){
				sbc.append(" and receiver like '%"+receiver+"%'");
			}
			sbc.append(" ) ");
			StringBuffer sb;
			sb=new StringBuffer()
				.append(" select" )
				.append("       receiver," )
			    .append("       (select count(*) from "+sbc+"as C1 where C1.receiver=C.receiver) as sucTotal,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.receiver=C.receiver and phoneType=0)as sucTotalMobile,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.receiver=C.receiver and phoneType=1)as sucTotalUnicom,")
				.append("       (select count(*) from "+sbc+"as C1 where C1.receiver=C.receiver and phoneType=2)as sucTotalTelecom")
				.append(" from  (select distinct receiver")
				.append("        from ("+sbc+")as T) as C")
				.append(" order by receiver");
			
			System.out.println(sb.toString());
			List<SmsReceiveBean> list=DbBean.select(sb.toString(), null, form.getStartIndex(), form.getPageSize(), SmsReceiveBean.class);
			if(list==null||list.size()==0)
				return null;
			return list;
			
	}
			
}
