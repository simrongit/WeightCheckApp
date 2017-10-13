package com.simron.weightLoging;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simron.weightLoggin.LogWeight;
import com.simron.weightLoggin.WeightInfo;
import com.simron.weightLoggin.WeightTime;

public class LogWeightTest {
	
	public void createTable() {
		int res = logWeight.createTable();
		Assert.assertEquals(0, res);
	}
	
	private static LogWeight logWeight;
	
	@BeforeClass
	public static void createBeforeAllTests() {
		logWeight = new LogWeight();
	}
	
	public void storeWeightInfo() {
		WeightInfo wInfo = new WeightInfo();
		wInfo.setDate(new Date(System.currentTimeMillis()));
		wInfo.setUserId("simron");
		WeightTime weightTime = new WeightTime();
		weightTime.setHour(9);
		weightTime.setMin(30);
		weightTime.setWeight(172.1);
		wInfo.setWeightTime(weightTime);
		int res = logWeight.storeWeightInfo(wInfo);
		Assert.assertEquals(1, res);
	}
	
	public void getLastEntry() {
		WeightTime weightTime = logWeight.getLastEntry("simron");
		Assert.assertNotNull(weightTime);
		Assert.assertNotNull(weightTime.getHour());
	}
	
	@Test
	public void getHistory() {
		List<WeightInfo> weightInfoList = logWeight.getHistory("simron");
		Assert.assertNotNull(weightInfoList);
		Assert.assertNotNull(weightInfoList.size());
	}

}
