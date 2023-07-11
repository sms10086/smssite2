package com.smssite2.mgmt.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.NoteException;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.report.bean.ClassificationSendBean;
import com.smssite2.mgmt.report.bean.EveSendBean;
import com.smssite2.mgmt.report.bean.GroupSendBean;
import com.smssite2.mgmt.report.bean.ParagraphBean;
import com.smssite2.mgmt.report.bean.SmsReceiveBean;
import com.smssite2.mgmt.report.bean.SmsSendBean;
import com.smssite2.mgmt.report.dao.IReportDao;
import com.smssite2.mgmt.report.dao.impl.ReportDao;
import com.smssite2.mgmt.report.form.ReportBaseForm;
import com.smssite2.mgmt.report.service.IReportService;

public class ReportService implements IReportService {	IReportDao dao = new ReportDao();
	private static Log log = LogFactory.getLog(ReportService.class);

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<SmsSendBean> findSmsSendWithPagination(ReportBaseForm form,
			String routeType) throws NoteException {
		List<SmsSendBean> l = null;
		ArrayList<SmsSendBean> list = new ArrayList<SmsSendBean>();
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder sql = new StringBuilder();
			Map map = new HashMap();
			if (form.getStartDate() != null
					&& form.getStartDate().trim().length() > 0) {
				sb.append(" and senddate>='" + form.getStartDate().trim()
						+ "' ");
			}
			log.info("101>>>>>>" + TimeUtil.now());
			if (form.getEndDate() != null
					&& form.getEndDate().trim().length() > 0) {
				String endDate = form.getEndDate().trim();
				if (endDate.compareTo(StringUtil.getTime(0)) >= 0) {
					endDate = StringUtil.getTime(-1);
					list = DbBean.select(getSmsSendSql(StringUtil.getTime(0),
							form.getEid()),
							new Object[] {
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0) }, 0,
							Integer.MAX_VALUE, SmsSendBean.class);
				}
				sb.append(" and senddate<='" + endDate + "' ");
			}
			log.info("102>>>>>>" + TimeUtil.now());
			if (form.getStaffId() != null
					&& form.getStaffId().trim().length() > 0) {
				sb.append(" and upper(staffid) = '"
						+ form.getStaffId().trim().toUpperCase() + "' ");
			}
			sql
					.append("select upper(staffid) as staffid,sum(total) as total,sum(totalok) as totalok,sum(totalfail) as totalfail,sum(totalnull)"
							+ " as totalnull,sum(totalmobile) as  totalmobile,sum(sucTotalMobile) as sucTotalMobile,"
							+ "sum(faiTotalMobile) as faiTotalMobile, sum(nulltotalmobile) as nulltotalmobile,sum(totalUnicom)"
							+ " as totalUnicom,sum(sucTotalUnicom) as sucTotalUnicom,sum(faiTotalUnicom) "
							+ "as faiTotalUnicom,sum(nulltotalUnicom) as nulltotalUnicom,"
							+ "sum(totalTelecom) as totalTelecom,sum(sucTotalTelecom) as sucTotalTelecom,sum(faiTotalTelecom)"
							+ " as faiTotalTelecom,sum(nulltotalTelecom) as nulltotalTelecom  from stat_smssend where eid='"
							+ form.getEid() + "' ");
			sql.append(sb);
			sql.append(" group by upper(staffid) order by upper(staffid) asc");

			log.info("109>>>>>>" + TimeUtil.now());
			l = DbBean.select(sql.toString(), null, 0, Integer.MAX_VALUE,
					SmsSendBean.class);
			log.info("111>>>>>>" + TimeUtil.now());
			if (l != null) {
				for (int i = 0; i < l.size(); i++) {
					map.put(l.get(i).getStaffId(), i);
				}
			}
			if (list != null) {
				for (SmsSendBean bean : list) {
					if (map.get(bean.getStaffId()) != null) {
						SmsSendBean b = l.get(StringUtil.parseInt(map.get(bean
								.getStaffId()), 0));

						b.setFaiTotalMobile(StringUtil.parseLong(b
								.getFaiTotalMobile(), 0)
								+ StringUtil.parseLong(
										bean.getFaiTotalMobile(), 0));
						b.setFaiTotalTelecom(b.getFaiTotalTelecom()
								+ bean.getFaiTotalTelecom());
						b.setFaiTotalUnicom(b.getFaiTotalUnicom()
								+ bean.getFaiTotalUnicom());
						b.setNulltotalmobile(b.getNulltotalmobile()
								+ bean.getNulltotalmobile());
						b.setNulltotalTelecom(b.getNulltotalTelecom()
								+ bean.getNulltotalTelecom());
						b.setNulltotalUnicom(b.getNulltotalUnicom()
								+ bean.getNulltotalUnicom());
						b.setSucTotalMobile(b.getSucTotalMobile()
								+ bean.getSucTotalMobile());
						b.setSucTotalTelecom(b.getSucTotalTelecom()
								+ bean.getSucTotalTelecom());
						b.setSucTotalUnicom(b.getSucTotalUnicom()
								+ bean.getSucTotalUnicom());
						b.setTotal(b.getTotal() + bean.getTotal());
						b.setTotalfail(b.getTotalfail() + bean.getTotalfail());
						b.setTotalmobile(b.getTotalmobile()
								+ bean.getTotalmobile());
						b.setTotalnull(b.getTotalnull() + bean.getTotalnull());
						b.setTotalok(b.getTotalok() + bean.getTotalok());
						b.setTotalTelecom(b.getTotalTelecom()
								+ bean.getTotalTelecom());
						b.setTotalunicom(b.getTotalunicom()
								+ bean.getTotalunicom());

					} else {
						l.add(bean);
					}
				}
			}
			long total = 0;
			long totalok = 0;
			long totalfail = 0;
			long totalnull = 0;
			long totalmobile = 0;
			long sucTotalMobile = 0;
			long faiTotalMobile = 0;
			long nulltotalmobile = 0;
			long totalunicom = 0;
			long sucTotalUnicom = 0;
			long faiTotalUnicom = 0;
			long nulltotalUnicom = 0;
			long totalTelecom = 0;
			long sucTotalTelecom = 0;
			long faiTotalTelecom = 0;
			long nulltotalTelecom = 0;
			if (l != null && l.size() > 0) {
				for (SmsSendBean sms : l) {
					total += sms.getTotal();
					totalok += sms.getTotalok();
					totalfail += sms.getTotalfail();
					totalnull += sms.getTotalnull();
					totalmobile += sms.getTotalmobile();
					sucTotalMobile += sms.getSucTotalMobile();
					faiTotalMobile += sms.getFaiTotalMobile();
					nulltotalmobile += sms.getNulltotalmobile();
					totalTelecom += sms.getTotalTelecom();
					totalunicom += sms.getTotalunicom();
					nulltotalUnicom += sms.getNulltotalUnicom();
					sucTotalUnicom += sms.getSucTotalUnicom();
					faiTotalUnicom += sms.getFaiTotalUnicom();
					faiTotalTelecom += sms.getFaiTotalTelecom();
					sucTotalTelecom += sms.getSucTotalTelecom();
					nulltotalTelecom += sms.getNulltotalTelecom();
				}
				SmsSendBean smsb = new SmsSendBean();
				smsb.setStaffId("总计");
				smsb.setTotal(total);
				smsb.setTotalok(totalok);
				smsb.setTotalfail(totalfail);
				smsb.setTotalnull(totalnull);
				smsb.setTotalmobile(totalmobile);
				smsb.setSucTotalMobile(sucTotalMobile);
				smsb.setFaiTotalMobile(faiTotalMobile);
				smsb.setNulltotalmobile(nulltotalmobile);
				smsb.setTotalunicom(totalunicom);
				smsb.setTotalTelecom(totalTelecom);
				smsb.setSucTotalTelecom(sucTotalTelecom);
				smsb.setFaiTotalTelecom(faiTotalTelecom);
				smsb.setSucTotalUnicom(sucTotalUnicom);
				smsb.setFaiTotalUnicom(faiTotalUnicom);
				smsb.setNulltotalTelecom(nulltotalTelecom);
				smsb.setNulltotalUnicom(nulltotalUnicom);
				l.add(smsb);
				form.setTotalItems(l.size());
			} else {
				form.setTotalItems(0);
			}
			log.info("146>>>>>>" + TimeUtil.now());
			form.adjustPageIndex();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NoteException(e.getMessage(), e);
		}
		return this.getPages(l, form.getStartIndex(), form.getPageSize());
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<ParagraphBean> findParagraphWithPagination(ReportBaseForm form,
			String routeType) throws NoteException {
		List<ParagraphBean> l = new ArrayList<ParagraphBean>();

		List<ParagraphBean> phbs = null;
		HashMap map = new HashMap();
		try {
			StringBuffer w = new StringBuffer();
			StringBuffer sql = new StringBuffer();

			if (form.getStartDate() != null
					&& form.getStartDate().trim().length() > 0) {
				w
						.append(" and senddate>='" + form.getStartDate().trim()
								+ "' ");
			}
			if (form.getEndDate() != null
					&& form.getEndDate().trim().length() > 0) {
				String endDate = form.getEndDate().trim();
				if (endDate.compareTo(StringUtil.getTime(0)) >= 0) {
					endDate = StringUtil.getTime(-1);
					phbs = DbBean.select(getParagraph(StringUtil.getTime(0),
							form.getEid()), new Object[]{StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0)}, 0, Integer.MAX_VALUE,
							ParagraphBean.class);
				}
				w.append(" and senddate<='" + endDate + "' ");
			}
			if (form.getStaffId() != null
					&& form.getStaffId().trim().length() > 0) {
				w.append(" and staffid = '" + form.getStaffId().trim() + "' ");
			}
			sql
					.append(" select phone3,sum(total) as total,sum(suc) as sucTotal,sum(fail) as faiTotal,sum(totalnull) as totalnull"
							+ " from stat_phone where eid='"
							+ form.getEid()
							+ "' ");
			sql.append(w);
			sql.append(" group by phone3 ");
			l = DbBean.select(sql.toString(), null, 0, Integer.MAX_VALUE,
					ParagraphBean.class);
			if (l != null) {
				for (int i = 0; i < l.size(); i++) {
					ParagraphBean phb = l.get(i);
					map.put(phb.getPhone3(), i);

				}
			}
			if (phbs != null) {
				for (ParagraphBean phb : phbs) {
					if (map.get(phb.getPhone3()) != null) {
						ParagraphBean bean = l.get((Integer) map.get(phb
								.getPhone3()));
						bean.setTotal(bean.getTotal()
								+ StringUtil.parseInt(phb.getTotal(), 0));
						bean.setSucTotal(bean.getSucTotal()
								+ StringUtil.parseInt(phb.getSucTotal(), 0));
						bean.setFaiTotal(bean.getFaiTotal()
								+ StringUtil.parseInt(phb.getFaiTotal(), 0));
						bean.setTotalnull(bean.getTotalnull()
								+ StringUtil.parseInt(phb.getTotalnull(), 0));
					} else {
						phb.setFaiTotal(StringUtil.parseInt(phb.getFaiTotal(),
								0));
						phb.setSucTotal(StringUtil.parseInt(phb.getSucTotal(),
								0));
						phb.setTotal(StringUtil.parseInt(phb.getTotal(), 0));
						phb.setTotalnull(StringUtil.parseInt(
								phb.getTotalnull(), 0));
						l.add(phb);
					}
				}
			}

			int total = 0;
			int suctotal = 0;
			int failtotal = 0;
			int nulltotal = 0;
			for (ParagraphBean pb : l) {

				total += pb.getTotal();
				suctotal += pb.getSucTotal();
				failtotal += pb.getFaiTotal();
				nulltotal += pb.getTotalnull();
			}
			ParagraphBean p = new ParagraphBean();
			p.setPhone3("总计");
			p.setTotal(total);
			p.setTotalnull(nulltotal);
			p.setFaiTotal(failtotal);
			p.setSucTotal(suctotal);
			l.add(p);
			form.setTotalItems(l.size());

			form.adjustPageIndex();
			log.info("224>>>>>>" + TimeUtil.now());
			return this.getPages(l, form.getStartIndex(), form.getPageSize());
		} catch (Exception e) {
			e.printStackTrace();
			throw new NoteException(e.getMessage(), e);
		}
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<EveSendBean> findEveSendWithPagination(ReportBaseForm form,
			String routeType) throws NoteException {
		List<EveSendBean> l = new ArrayList<EveSendBean>();
		List<EveSendBean> ll = null;
		try {
			StringBuffer w = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			EveSendBean esb = null;
			if (form.getStartDate() != null
					&& form.getStartDate().trim().length() > 0) {
				w
						.append(" and senddate>='" + form.getStartDate().trim()
								+ "' ");
			}
			if (form.getEndDate() != null
					&& form.getEndDate().trim().length() > 0) {
				String endDate = form.getEndDate().trim();
				if (endDate.compareTo(StringUtil.getTime(0)) >= 0) {
					endDate = StringUtil.getTime(-1);
					esb = (EveSendBean) DbBean.selectFirst(getSmsSendSql(
							StringUtil.getTime(0), form.getEid()), new Object[] {
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0),
									StringUtil.parseTimestamp(StringUtil
											.getTime(0), 0) },
							EveSendBean.class);
				}
				w.append(" and senddate<='" + endDate + "' ");
			}
			if (form.getStaffId() != null
					&& form.getStaffId().trim().length() > 0) {
				w.append(" and staffid = '" + form.getStaffId().trim() + "' ");
			}
			sql
					.append("select senddate,sum(total) as total,sum(totalok) as totalok,sum(totalfail) as totalfail,sum(totalnull)"
							+ " as totalnull,sum(totalmobile) as  totalmobile,sum(sucTotalMobile) as sucTotalMobile,"
							+ "sum(faiTotalMobile) as faiTotalMobile, sum(nulltotalmobile) as nulltotalmobile,sum(totalUnicom)"
							+ " as totalUnicom,sum(sucTotalUnicom) as sucTotalUnicom,sum(faiTotalUnicom) "
							+ "as faiTotalUnicom,sum(nulltotalUnicom) as nulltotalUnicom,"
							+ "sum(totalTelecom) as totalTelecom,sum(sucTotalTelecom) as sucTotalTelecom,sum(faiTotalTelecom)"
							+ " as faiTotalTelecom,sum(nulltotalTelecom) as nulltotalTelecom    from stat_smssend where eid='"
							+ form.getEid() + "' ");
			sql.append(w);
			sql.append(" group by senddate");
			log.info("316>>>>>>" + TimeUtil.now());
			ll = DbBean.select(sql.toString(), null, 0, Integer.MAX_VALUE,
					EveSendBean.class);
			log.info("3186>>>>>>" + TimeUtil.now());
			if (esb != null) {
				esb.setSendDate(StringUtil.getTime(0));
				l.add(esb);
			}
			l.addAll(ll);
			if (l != null && l.size() > 0) {
				long total = 0;
				long totalok = 0;
				long totalfail = 0;
				long totalnull = 0;
				long totalmobile = 0;
				long sucTotalMobile = 0;
				long faiTotalMobile = 0;
				long nulltotalmobile = 0;
				long totalunicom = 0;
				long sucTotalUnicom = 0;
				long faiTotalUnicom = 0;
				long nulltotalUnicom = 0;
				long totalTelecom = 0;
				long sucTotalTelecom = 0;
				long faiTotalTelecom = 0;
				long nulltotalTelecom = 0;
				for (EveSendBean sms : l) {
					total += sms.getTotal();
					totalok += sms.getTotalok();
					totalfail += sms.getTotalfail();
					totalnull += sms.getTotalnull();
					totalmobile += sms.getTotalmobile();
					sucTotalMobile += sms.getSucTotalMobile();
					faiTotalMobile += sms.getFaiTotalMobile();
					nulltotalmobile += sms.getNulltotalmobile();
					totalTelecom += sms.getTotalTelecom();
					totalunicom += sms.getTotalunicom();
					nulltotalUnicom += sms.getNulltotalUnicom();
					sucTotalUnicom += sms.getSucTotalUnicom();
					faiTotalUnicom += sms.getFaiTotalUnicom();
					faiTotalTelecom += sms.getFaiTotalTelecom();
					sucTotalTelecom += sms.getSucTotalTelecom();
					nulltotalTelecom += sms.getNulltotalTelecom();
				}
				EveSendBean smsb = new EveSendBean();
				smsb.setSendDate("总计");
				smsb.setTotal(total);
				smsb.setTotalok(totalok);
				smsb.setTotalfail(totalfail);
				smsb.setTotalnull(totalnull);
				smsb.setTotalmobile(totalmobile);
				smsb.setSucTotalMobile(sucTotalMobile);
				smsb.setFaiTotalMobile(faiTotalMobile);
				smsb.setNulltotalmobile(nulltotalmobile);
				smsb.setTotalunicom(totalunicom);
				smsb.setTotalTelecom(totalTelecom);
				smsb.setSucTotalTelecom(sucTotalTelecom);
				smsb.setFaiTotalTelecom(faiTotalTelecom);
				smsb.setSucTotalUnicom(sucTotalUnicom);
				smsb.setFaiTotalUnicom(faiTotalUnicom);
				smsb.setNulltotalTelecom(nulltotalTelecom);
				smsb.setNulltotalUnicom(nulltotalUnicom);
				l.add(smsb);
				form.setTotalItems(l.size());
			} else {
				form.setTotalItems(0);
			}
			form.adjustPageIndex();
			log.info("354>>>>>>" + TimeUtil.now());
			return this.getPages(l, form.getStartIndex(), form.getPageSize());
		} catch (Exception e) {
			e.printStackTrace();
			throw new NoteException(e.getMessage(), e);
		}
	}

	public List<GroupSendBean> findGroupsendWithPagination(
			ReportBaseForm form1, String routeType) throws NoteException {
		List<GroupSendBean> l = null;
		try {
			return l;
		} catch (Exception e) {
			throw new NoteException(e.getMessage(), e);
		}
	}

	public List<ClassificationSendBean> findClassificationSendWithPagination(
			ReportBaseForm form1, String routeType) throws NoteException {
		List<ClassificationSendBean> l = null;
		try {
			return l;
		} catch (Exception e) {
			throw new NoteException(e.getMessage(), e);

		}
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<SmsReceiveBean> findSmsReceiveWithPagination(
			ReportBaseForm form, String routeType) throws NoteException {
		List<SmsReceiveBean> l = new ArrayList<SmsReceiveBean>();
		List<SmsReceiveBean> ll = null;
		try {

			StringBuffer w = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			SmsReceiveBean srb = null;
			if (form.getStartDate() != null
					&& form.getStartDate().trim().length() > 0) {
				w
						.append(" and borndate>='" + form.getStartDate().trim()
								+ "' ");
			}
			if (form.getEndDate() != null
					&& form.getEndDate().trim().length() > 0) {
				String endDate = form.getEndDate().trim();
				if (endDate.compareTo(StringUtil.getTime(0)) >= 0) {
					endDate = StringUtil.getTime(-1);
					srb = (SmsReceiveBean) DbBean.selectFirst(getReceiveSql(
							StringUtil.getTime(0), form.getEid()), null,
							SmsReceiveBean.class);
				}
				w.append(" and borndate<='" + endDate + "' ");
			}
			if (form.getReceiver() != null
					&& form.getReceiver().trim().length() > 0) {
				w
						.append(" and receiver = '" + form.getReceiver().trim()
								+ "' ");
			}
			sql
					.append(" select receiver,borndate,sum(total) as total,sum(totalMobile) as totalmobile,sum(totalunicom) as totalunicom,sum(totaltelecom) as totaltelecom "
							+ "from stat_receive where eid='"
							+ form.getEid()
							+ "'");
			sql.append(w);
			sql.append(" group by receiver,borndate order by borndate");

			ll = DbBean.select(sql.toString(), null, 0, Integer.MAX_VALUE,
					SmsReceiveBean.class);
			if (srb != null) {
				srb.setBorndate(StringUtil.getTime(0));
				srb.setTotal(StringUtil.parseInt(srb.getTotal(), 0));
				srb
						.setTotalmobile(StringUtil.parseInt(srb
								.getTotalmobile(), 0));
				srb.setTotaltelecom(StringUtil.parseInt(srb.getTotaltelecom(),
						0));
				srb
						.setTotalunicom(StringUtil.parseInt(srb
								.getTotalunicom(), 0));
				l.add(srb);
			}
			l.addAll(ll);
			int total = 0;
			int totalmobile = 0;
			int totalunicom = 0;
			int totaltelecom = 0;
			for (SmsReceiveBean s : l) {
				total += s.getTotal();
				totalmobile += s.getTotalmobile();
				totalunicom += s.getTotalunicom();
				totaltelecom += s.getTotaltelecom();
			}
			SmsReceiveBean sb = new SmsReceiveBean();
			sb.setBorndate("");
			sb.setReceiver("总计");
			sb.setTotal(total);
			sb.setTotalmobile(totalmobile);
			sb.setTotalunicom(totalunicom);
			sb.setTotaltelecom(totaltelecom);
			l.add(sb);
			form.setTotalItems(l.size());
			form.adjustPageIndex();
			return this.getPages(l, form.getStartIndex(), form.getPageSize());
		} catch (Exception e) {
			throw new NoteException(e.getMessage(), e);

		}
	}

	@SuppressWarnings("unchecked")
	public static List getPages(List list, int startIndex, int pageSize) {
		if (list == null)
			return new ArrayList();
		if (pageSize <= 0)
			pageSize = 16;
		if (startIndex <= 0)
			startIndex = 0;
		List list1 = new ArrayList();
		if (list.size() >= startIndex + pageSize) {
			for (int i = 0; i < pageSize; i++) {
				list1.add(list.get(startIndex + i));
			}
		} else {
			for (int i = 0; i < list.size() - startIndex; i++) {
				list1.add(list.get(startIndex + i));
			}
		}
		return list1;
	}

	private static String getSmsSendSql(String today, String eid) {
		StringBuilder sb = new StringBuilder();
		sb
				.append("select a.eid,upper(a.staffid) as staffid ,total ,totalok,totalfail,totalnull,totalmobile,sucTotalMobile,faiTotalMobile,nulltotalmobile,totalUnicom,sucTotalUnicom,faiTotalUnicom,nulltotalUnicom,totalTelecom,sucTotalTelecom,faiTotalTelecom,nulltotalTelecom from ");
		sb
				.append("(select eid,staffid,count(id) as total from smsmt   where  sendTime>=? and eid='"
						+ eid + "' and result>-1 group by eid,staffid) a  ");
		sb.append("left join");
		sb
				.append(" (select eid,staffid,count(id) as totalok from smsmt   where  sendTime>=? and eid='"
						+ eid + "' and status='0' group by eid,staffid) b ");
		sb.append(" on a.eid=b.eid and a.staffid=b.staffid ");
		sb.append("  left join ");
		sb
				.append("(select eid,staffid,count(id) as totalfail from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and status<>'0' and status<>'10004' and status<>'10004' group by eid,staffid) c ");
		sb.append(" on a.eid=c.eid and a.staffid=c.staffid ");
		sb.append(" left join ");
		sb
				.append("  (select eid,staffid,count(id) as totalnull from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and (status is null or status='10004') and result>-1 group by eid,staffid) d ");
		sb.append("  on a.eid=d.eid and a.staffid=d.staffid ");
		sb.append("  left join ");
		sb
				.append(" (select eid,staffid,count(id) as totalmobile from smsmt   where   sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=0 and result>-1 group by eid,staffid) e ");
		sb.append(" on a.eid=e.eid and a.staffid=e.staffid ");
		sb.append(" left join ");
		sb
				.append(" (select eid,staffid,count(id) as sucTotalMobile from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=0 and status='0' group by eid,staffid) f ");
		sb.append(" on a.eid=f.eid and a.staffid=f.staffid ");
		sb.append(" left join ");
		sb
				.append(" (select eid,staffid,count(id) as faiTotalMobile from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=0 and status<>'0' and status<>'10004' group by eid,staffid) g on a.eid=g.eid and a.staffid=g.staffid");
		sb.append("   left join ");
		sb
				.append("  (select eid,staffid,count(id) as nulltotalmobile from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=0 and (status is null or status='10004') and result>-1 group by eid,staffid) h ");
		sb.append(" on a.eid=h.eid and a.staffid=h.staffid ");
		sb.append("   left join ");
		sb
				.append("   (select eid,staffid,count(id) as totalUnicom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=1 and result>-1 group by eid,staffid) o ");
		sb.append("   on a.eid=o.eid and a.staffid=o.staffid ");
		sb.append("   left join ");
		sb
				.append("  (select eid,staffid,count(id) as sucTotalUnicom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=1 and status='0' group by eid,staffid) i ");
		sb.append("  on a.eid=i.eid and a.staffid=i.staffid ");
		sb.append("left join ");
		sb
				.append(" (select eid,staffid,count(id) as faiTotalUnicom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=1 and status<>'0' and status<>'10004' group by eid,staffid) j ");
		sb.append(" on a.eid=j.eid and a.staffid=j.staffid ");
		sb.append("  left join ");
		sb
				.append(" (select eid,staffid,count(id) as nulltotalUnicom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=1 and (status is null or status='10004') and result>-1 group by eid,staffid) k ");
		sb.append(" on a.eid=k.eid and a.staffid=k.staffid ");
		sb.append("  left join ");
		sb
				.append("(select eid,staffid,count(id) as totalTelecom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=2 and result>-1 group by eid,staffid) p ");
		sb.append("on a.eid=p.eid and a.staffid=p.staffid ");
		sb.append("   left join ");
		sb
				.append("  (select eid,staffid,count(id) as sucTotalTelecom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=2 and status='0' group by eid,staffid) l ");
		sb.append("  on a.eid=l.eid and a.staffid=l.staffid ");
		sb.append("  left join ");
		sb
				.append(" (select eid,staffid,count(id) as faiTotalTelecom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=2 and status<>'0' and status<>'10004' group by eid,staffid) m ");
		sb.append(" on a.eid=m.eid and a.staffid=m.staffid ");
		sb.append("   left join ");
		sb
				.append("  (select eid,staffid,count(id) as nulltotalTelecom from smsmt   where  sendTime>=? and eid='"
						+ eid
						+ "' and phonetype=2 and (status is null or status='10004') and result>-1 group by eid,staffid) n ");
		sb.append("  on a.eid=n.eid and a.staffid=n.staffid");

		sb.append(" order by upper(a.staffid) asc");
		return sb.toString();
	}

	private static String getReceiveSql(String today, String eid) {
		StringBuilder sb = new StringBuilder();
		sb
				.append(
						"select a.eid,a.receiver ,total,totalmobile,totalunicom,totaltelecom from ")
				.append(
						" (select eid,receiver,count(id) as total from received_d where receivetime>'"
								+ today + " 00:00' and receivetime<'" + today
								+ " 24:00' and eid='" + eid
								+ "' group by eid,receiver) a ")
				.append(" left join ")
				.append(
						" (select eid,receiver,count(id) as totalmobile from received_d where receivetime>'"
								+ today
								+ " 00:00' and receivetime<'"
								+ today
								+ " 24:00' and eid='"
								+ eid
								+ "' and phonetype=0 group by eid,receiver) b ")
				.append("on a.eid=b.eid and a.receiver=b.receiver ")
				.append("left join ")
				.append(
						"(select eid,receiver,count(id) as totalunicom from received_d where receivetime>'"
								+ today
								+ " 00:00' and receivetime<'"
								+ today
								+ " 24:00' and eid='"
								+ eid
								+ "' and phonetype=1 group by eid,receiver) c ")
				.append(" on a.eid=c.eid and a.receiver=c.receiver ")
				.append("left join  ")
				.append(
						"(select eid,receiver,count(id) as totaltelecom from received_d where receivetime>'"
								+ today
								+ " 00:00' and receivetime<'"
								+ today
								+ " 24:00' and eid='"
								+ eid
								+ "' and phonetype=2 group by eid,receiver) d ")
				.append(" on a.eid=d.eid and a.receiver=d.receiver");
		return sb.toString();
	}

	private static String getParagraph(String today, String eid) {
		StringBuilder sb = new StringBuilder();
		sb
				.append(
						"select a.eid,a.phone3,total,suc as sucTotal,fail as faiTotal,totalnull   from ")
				.append(
						" (select eid,substr(phone,1,3) as phone3,count(id) as total from smsmt  where  sendTime>=? "
								 
								+ "  and eid='"
								+ eid
								+ "' and result>-1 group by eid,substr(phone,1,3)) a ")
				.append(" left join ")
				.append(
						" (select eid,substr(phone,1,3) as phone3,count(id) as suc from smsmt where  sendTime>=? "
								 
								+ " and eid='"
								+ eid
								+ "' and status='0'   group by eid,substr(phone,1,3)) b  ")
				.append(" on a.eid=b.eid and a.phone3=b.phone3 ")
				.append(" left join ")
				.append(
						" (select eid,substr(phone,1,3) as phone3,count(id) as fail from smsmt where  sendTime>=? "
								 
								+ "  and eid='"
								+ eid
								+ "' and status<>'0' and status<>'10004'   group by eid,substr(phone,1,3)) c   ")
				.append(" on a.eid=c.eid and a.phone3=c.phone3 ")
				.append(" left join ")
				.append(
						" (select eid,substr(phone,1,3) as phone3,count(id) as totalnull from smsmt where  sendTime>=? "
								 
								+ " and eid='"
								+ eid
								+ "' and (status is null or status='10004') and result>-1 group by eid,substr(phone,1,3)) d  ")
				.append(" on a.eid=d.eid and a.phone3=d.phone3 ");
		return sb.toString();
	}
}
