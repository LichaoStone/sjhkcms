package com.br.ott.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 
 * 随机数生成工具类 <功能详细描述>
 * 
 * @author pKF46373
 * @version [版本号, 2011-10-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RandomUtil {

	private ArrayList<Integer> al = null;

	/**
	 * 
	 * 
	 * @param max
	 *            产生随机数的上限
	 * @param type
	 *            随机数的 类型 1为奇数 类型2为偶数
	 * @param num
	 *            产生随机数的个数
	 * @return 数组
	 */
	public List<Integer> createRandom(int max, int type, int num) {
		// 数据集
		al = new ArrayList<Integer>(num);
		int temp;
		while (al.size() < num) {
			temp = (int) (Math.random() * max);
			if (type == 1) {
				if (temp % 2 != 0) {

					al.add(temp);
				}
			}
			if (type == 2) {
				if (temp % 2 == 0) {

					al.add(temp);
				}
			}

		}
		return al;
	}

	/**
	 * 
	 * 
	 * @param min
	 *            产生随机数的下限
	 * @param max
	 *            产生随机数的上限
	 * @param type
	 *            随机数的 类型 1为奇数 类型2为偶数
	 * @param num
	 *            产生随机数的个数
	 * @return 数组
	 */
	public List<Integer> createRandom(int min, int max, int type, int num) {
		// 数据集
		al = new ArrayList<Integer>(num);
		int temp;
		while (al.size() < num) {
			temp = (int) (Math.random() * max);
			if (type == 1) {
				if (temp % 2 != 0 && temp > min) {

					al.add(temp);
				}
			}
			if (type == 2 && temp > min) {
				if (temp % 2 == 0) {

					al.add(temp);
				}
			}

		}
		return al;
	}

	/**
	 * 获取一个四位随机数，并且四位数不重复
	 * 
	 * @author zhuw
	 * @return
	 */
	public static String GetRandomNumber() {
		// 使用SET以此保证写入的数据不重复
		Set<Integer> set = new HashSet<Integer>();
		// 随机数
		Random random = new Random();
		while (set.size() < 4) {
			// nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
			// 和指定值（不包括）之间均匀分布的 int 值。
			set.add(random.nextInt(10));
		}
		Iterator<Integer> iterator = set.iterator();
		StringBuffer temp = new StringBuffer();
		while (iterator.hasNext()) {
			temp.append(iterator.next());

		}
		return temp.toString();
	}

	public static void main(String args[]) {
		RandomUtil ru = new RandomUtil();

		// 有特定下限的数组
		List<Integer> ls4 = ru.createRandom(90, 100, 1, 10);
		for (int i = 0; i < ls4.size(); i++) {
			System.out.println("第" + (i + 1) + "数：" + ls4.get(i));

		}
		List<Integer> ls5 = ru.createRandom(90, 200, 2, 10);
		for (int i = 0; i < ls5.size(); i++) {
			System.out.println("第" + (i + 1) + "数：" + ls5.get(i));

		}
		List<Integer> ls6 = ru.createRandom(90, 1000, 2, 15);
		for (int i = 0; i < ls6.size(); i++) {
			System.out.println("第" + (i + 1) + "数：" + ls6.get(i));

		}
	}

	public static String getRandomNumberString(int strLen) {
		Random random = new Random();
		String ss = "0123456789";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strLen; i++) {
			int n = random.nextInt(ss.length());
			char r = ss.charAt(n);
			sb.append(r);
		}
		return sb.toString();
	}

	public static int getRandomNumber(int strLen) {
		Random random = new Random();
		String ss = "0123456789";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strLen; i++) {
			int n = random.nextInt(ss.length());
			char r = ss.charAt(n);
			sb.append(r);
		}
		return Integer.parseInt(sb.toString());
	}
}