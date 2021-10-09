package org.coal.data.file;

import java.io.IOException;

/**
 * @author wuhao
 * @createTime 2021-10-09 15:36:00
 */
public class NumParseTest {
	public static void main(String[] args) throws IOException {
		String file20 = "raw_2021-10-08_20.log";
		String report20 = "report/appt20.txt";
		FileParser.parserToReport(file20, report20, true);
		String file21 = "raw_2021-10-08_21.log";
		String report21 = "report/appt21.txt";
		FileParser.parserToReport(file21, report21, true);
	}
}
