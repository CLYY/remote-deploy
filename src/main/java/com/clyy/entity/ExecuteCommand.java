package com.clyy.entity;

/**
 * Auther: Charles.Chen <br>
 * Description: 执行命令
 * Date: Create in 15:06 2018/6/24
 **/
public class ExecuteCommand {

	/**
	 * 命令语句
	 */
	private String command;
	/**
	 * 是否允许异常
	 */
	private boolean allowsException;

	@Override
	public String toString() {
		return "ExecuteCommand{" +
				"command='" + command + '\'' +
				", allowsExceptions=" + allowsException +
				'}';
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isAllowsException() {
		return allowsException;
	}

	public void setAllowsException(boolean allowsException) {
		this.allowsException = allowsException;
	}
}
