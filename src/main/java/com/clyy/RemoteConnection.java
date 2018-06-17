package com.clyy;

import com.clyy.entity.RemoteLink;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auther: Charles.Chen <br>
 * Description: 连接远端
 * Date: Create in 21:23 2018/6/15
 **/
public class RemoteConnection implements AutoCloseable {
	private final static Logger logger = LoggerFactory.getLogger(RemoteConnection.class);

	private ClientSession session;
	private SshClient client;
	private SftpClient sftpClient;

	/**
	 * 获取连接
	 * @param remoteLink
	 * @throws Exception
	 */
	public void connection(RemoteLink remoteLink) throws Exception {
		logger.info("连接远端：IP = {}", remoteLink.getIp());
		client = SshClient.setUpDefaultClient();
		client.start();
		ConnectFuture connectFuture = client.connect(remoteLink.getUser(), remoteLink.getIp(), remoteLink.getPort());
		connectFuture.await();
		session = connectFuture.getSession();

		session.addPasswordIdentity(remoteLink.getPassword());
		AuthFuture authFuture = session.auth();
		authFuture.await();

		if(!authFuture.isSuccess()) {
			logger.error("远端连接失败：IP = {}, PORT = {}, USER = {}", remoteLink.getIp(), remoteLink.getPort(),
					remoteLink.getUser(), authFuture.getException());
		}
		logger.info("远端连接成功！");

		sftpClient = session.createSftpClient();
	}

	/**
	 * 关闭连接
	 * @throws Exception
	 */
	@Override
	public void close() throws Exception {
		logger.info("结束远端连接");
		if(sftpClient != null) {
			sftpClient.close();
		}

		if(session != null) {
			session.close();
		}
		if(client != null) {
			client.close();
		}
	}

	public ClientSession getSession() {
		return session;
	}

	public SftpClient getSftpClient() {
		return sftpClient;
	}

}
