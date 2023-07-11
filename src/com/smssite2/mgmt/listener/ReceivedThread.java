package com.smssite2.mgmt.listener;

import java.util.List;
import org.apache.log4j.Logger;
import com.note.common.DbBean;
import com.smssite2.mgmt.received.bean.ReceivedBean;
import com.smssite2.mgmt.received.dao.ReceivedDao;

public class ReceivedThread extends WorkThread {	private static final Logger LOG = Logger.getLogger(ReceivedThread.class);
	ReceivedDao rd = new ReceivedDao();
	int batchNum = 1;

	@Override
	public void work() throws Exception {
		LOG.info("接受上行短信处理开启");
		while (isWork()) {
			if (rd.checkMO()) {

				StringBuilder sql = new StringBuilder(
						"select * from received_d where ID in (select top "+batchNum+" ID  from received_d_wait order by bornDate asc )");
				List<ReceivedBean> rbs = DbBean.select(sql.toString(),
						new Object[] {}, 0, batchNum, ReceivedBean.class);
				for (ReceivedBean rb : rbs) {
					try {
						String groupID = rd.getGroupIDByPhone(rb.getPhone());
						
						rd.sendReplySms(groupID,rb.getPhone());
						
						rd.updateReceived(groupID, rb.getId());
						rd.deleteReceivedWait(rb.getId());
					} catch (Exception e) {
						LOG.info(e.getMessage(), e);
					}
				}

			}
			sleep(500);
		}
		LOG.info("接受上行短信处理结束");
	}

}
