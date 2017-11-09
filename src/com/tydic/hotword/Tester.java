package com.tydic.hotword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tydic.hotword.HotWordUtil;

public class Tester {

	public static void main(String[] args) {
		List<String> src = new ArrayList<String>();
		src.add("铝合金");
		src.add("铝合金钢板");
		src.add("铝合金  钢板");
		src.add("铝合金    钢板");
		src.add("铝合金 窗户");
		src.add("铝合金门");
		src.add("铝合金墙");
		src.add("铝合金地板");
		src.add("12mm");
		src.add("12mm");
		src.add("12mm");
		src.add("13mm");
		src.add("13mm");
		src.add("一个商城的测试关键字");
		src.add("一个商城的测试关键字");
		src.add("一个商城的测试关键字");
		src.add("Thisisatester");
		src.add("Thisisatester");
		src.add("Thisisatester");
		try {
			List<Map<String, Integer>> rst = HotWordUtil.getTopN(src, 10);
			System.out.print(rst.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("===================================");
	}

}
