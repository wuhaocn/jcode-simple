package org.letter.spring.simple;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author wuhao
 * @createTime 2022-01-26 17:46:00
 */
@Controller
public class SimpleControl {
	@RequestMapping(value = "/simple", method = GET)
	public void simpleControlReq(HttpServletRequest request, HttpServletResponse response) {
		AsyncContext asyncContext = request.startAsync();
		//设置监听器:可设置其开始、完成、异常、超时等事件的回调处理
		asyncContext.addListener(new AsyncListenerHandler());
		//设置超时时间
		asyncContext.setTimeout(3000);
		asyncContext.start(new AsyncContextHandler(asyncContext));
		System.out.println("Main Thread OK：" + Thread.currentThread().getName());
	}

}
