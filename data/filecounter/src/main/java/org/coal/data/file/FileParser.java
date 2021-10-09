package org.coal.data.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuhao
 * @createTime 2021-10-09 11:09:00
 */
public class FileParser {

	public static void parserToReport(String source, String report, boolean numSign) throws IOException {
		FileParser fileParser = new FileParser();
		fileParser.numSign = numSign;
		fileParser.parseFile(source);
		fileParser.sortKey();
		fileParser.report(report);
	}

	private ConcurrentHashMap<String, LogBean> logMap = new ConcurrentHashMap<>();
	private List<LogBean> logBeans = new ArrayList<>();
	private boolean numSign = false;

	public String getSign(String sign) {
		if (sign.length() > 30) {
			return sign.substring(0, 29);
		}
		return sign;

	}

	public void parseFile(String filePath) throws IOException {
		File file = new File(filePath);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str;
		while ((str = br.readLine()) != null) {
			String[] logItems = str.trim().split("\t");
			String key = null;
			if (logItems.length > 2 && !logItems[2].equalsIgnoreCase("T45")){
				continue;
			}
			if (logItems.length == 5) {
				key = logItems[2] + "#" + getSign(logItems[3]);
			} else if (logItems.length == 6) {
				if (logItems[2].equalsIgnoreCase("T49")){
					key = logItems[2] + "#" + getSign(logItems[3]);
				} else {
					key = logItems[2] + "#" + getSign(logItems[5]);
				}

			} else if (logItems.length == 4) {
				key = logItems[2] + "#" + getSign(logItems[3]);
			} else {
				//System.out.println(str);
				continue;
			}
			LogBean logBean = logMap.get(key);
			if (logBean == null) {
				logBean = new LogBean(key, null, 0, null);
			}
			if (numSign){
				logBean.setSize(logBean.getSize() +  1);
			} else {
				logBean.setSize(logBean.getSize() + str.length());
			}

			logMap.put(key, logBean);
		}
		br.close();
		fr.close();
	}

	public void sortKey() {
		logBeans.clear();
		for (Map.Entry<String, LogBean> entry : logMap.entrySet()) {
			logBeans.add(entry.getValue());
		}
		logBeans.sort(new Comparator<LogBean>() {
			@Override
			public int compare(LogBean o1, LogBean o2) {
				return (int) (o2.getSize() - o1.getSize());
			}
		});
	}

	public void report(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()){
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		for (LogBean logBean : logBeans) {
			String line = logBean.getKey() + "\t" + logBean.getSize() + "\n";
			fileOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
		}
		fileOutputStream.close();
	}

	public void print() {
		for (LogBean logBean : logBeans) {
			System.out.println(logBean.getSize() + "-----" + logBean.getKey());
		}
	}
}
