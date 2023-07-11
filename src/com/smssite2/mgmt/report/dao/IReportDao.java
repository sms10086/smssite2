import com.smssite2.mgmt.report.form.ReportBaseForm;

	public interface IReportDao {		public Integer countOfSmsSendWithPagination(ReportBaseForm form, String routeType) ;
		public List<SmsSendBean> selectSmsSendWithPagination(ReportBaseForm form, String routeType);
		public Float getPrencent(Integer in1,Integer in2);
		public Integer countOfParagraphWithPagination(ReportBaseForm form,String routeType);
		public List<ParagraphBean> selectParagraphWithPagination(ReportBaseForm form, String routeType);
		public Integer countOfEveSendWithPagination(ReportBaseForm form,String routeType);
		public List<EveSendBean> selectEveSendWithPagination(ReportBaseForm form, String routeType);
		public Integer countOfSmsReceiveWithPagination(ReportBaseForm form,String routeType);
		public List<SmsReceiveBean> selectSmsReceiveWithPagination(ReportBaseForm form,String routeType);
	
	}
