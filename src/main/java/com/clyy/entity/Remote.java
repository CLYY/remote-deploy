package com.clyy.entity;

import java.util.List;

/**
 * Auther: Charles.Chen <br>
 * Description:
 * Date: Create in 11:09 2018/6/16
 **/
public class Remote {

	private RemoteLink remoteLink;
	private List<UpdateFile> updateFiles;
	private List<ExecuteCommand> commands;

	@Override
	public String toString() {
		return "Remote{" +
				"remoteLink=" + remoteLink +
				", updateFiles=" + updateFiles +
				", commands=" + commands +
				'}';
	}

	public RemoteLink getRemoteLink() {
		return remoteLink;
	}

	public void setRemoteLink(RemoteLink remoteLink) {
		this.remoteLink = remoteLink;
	}

	public List<UpdateFile> getUpdateFiles() {
		return updateFiles;
	}

	public void setUpdateFiles(List<UpdateFile> updateFiles) {
		this.updateFiles = updateFiles;
	}

	public List<ExecuteCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<ExecuteCommand> commands) {
		this.commands = commands;
	}
}
