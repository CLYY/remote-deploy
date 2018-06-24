package com.clyy.entity;

/**
 * Auther: Charles.Chen <br>
 * Description: 远端链接属性
 * Date: Create in 14:38 2018/6/15
 **/
public class RemoteLink {

	private String user;
	private String password;
	private String ip;
	private Integer port = 22;

	public RemoteLink() {}

	public RemoteLink(String user, String password, String ip) {
		this(user, password, ip, 22);
	}

	public RemoteLink(String user, String password, String ip, Integer port) {
		this.user = user;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}

	@Override
	public String toString() {
		return "RemoteLink{" +
				"user='" + user + '\'' +
				", password='********'" +
				", ip='" + ip + '\'' +
				", port=" + port +
				'}';
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
