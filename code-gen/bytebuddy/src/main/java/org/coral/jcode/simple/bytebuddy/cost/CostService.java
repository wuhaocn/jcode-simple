package org.coral.jcode.simple.bytebuddy.cost;
/**
 * @author wuhao
 * @createTime 2021-07-27 14:37:00
 */
public class CostService {
    public int play(int value) throws Exception {
        System.out.println("play: " + value);
        Thread.sleep(30);
        return value;
    }

    public static int doWork(int value){
		System.out.println("doWork: " + value);
		return value;
	}

}