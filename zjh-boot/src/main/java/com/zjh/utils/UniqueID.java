package com.zjh.utils;

import java.util.Date;
import java.util.Random;

public class UniqueID {
	
	private static Integer number = new Integer(0);
	
	private String hostId = "";
	
	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public long getId(boolean isRandom)
	{
		synchronized (UniqueID.class)
		{
			return createId(isRandom);
		}
	}
	
	private long createId(boolean isRandom)
	{
	    long uid = 0;
        Date dt = new Date();
        
        // 最大ID(7643726453097023999)
        //dt.setYear(300);
        //dt.setMonth(11);
        //dt.setDate(31);
        //dt.setHours(23);
        //dt.setMinutes(59);
        //dt.setSeconds(59);
        //number = 999999;
        //hostId = "999";
        
	    // 时间去掉毫秒(32位二进制整数,可表示到2200/12/31 23:59:59年)
	    uid   = dt.getTime() / 1000;
	    uid <<= 20;
	    
		// 加上序号(20位二进制整数,最大可表示整数999999)
		uid += number;
		number = (number + 1) % 1000000;
		
		// 加上机器号(3位10进制,最大整数999)
		String currentHostID = hostId;
		
		// 随机生成HostID
		if (isRandom) {
			Random random    = new Random(); // 注意：如果Random给参数，产生重复的概率很大
			int randomHostID = random.nextInt(999);
			currentHostID    = String.format("%1$03d", randomHostID);
		}
		
		return Long.parseLong(String.valueOf(uid)+currentHostID);
	}
	
	public static long getUniqueID()
	{
		UniqueID uniqueID = SpringContext.getBean("UniqueID", UniqueID.class);
		return uniqueID.getId(true);
	}
	
}
