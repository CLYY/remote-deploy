package com.clyy;

import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Auther: Charles.Chen <br>
 * Description: 执行命令
 * Date: Create in 22:05 2018/6/15
 **/
public class RemoteCommand {
	final static Logger logger = LoggerFactory.getLogger(RemoteCommand.class);

	public static String execute(ClientSession session, String cmd) throws Exception {
		logger.info("执行远端命令：{}", cmd);
		String result = session.executeRemoteCommand(cmd, System.out, StandardCharsets.UTF_8);
		result = result.length() == 0 ? "SUCCESS" : result;
		logger.info("命令执行结果：\r\n{}", result);
		return result;
	}
}
