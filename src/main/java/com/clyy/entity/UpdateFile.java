package com.clyy.entity;

/**
 * Auther: Charles.Chen <br>
 * Description: 上传文件
 * Date: Create in 16:08 2018/6/15
 **/
public class UpdateFile {

	private String local;
	private String remote;
	private String remoteName;
	private String remoteBakPath;
	private Boolean isCleanUp;

	public UpdateFile() {}
	public UpdateFile(String local, String remote) {
		this(local, remote, null);
	}

	public UpdateFile(String local, String remote, String remoteName) {
		this(local, remote, remoteName, null);
	}

	public UpdateFile(String local, String remote, String remoteName, String remoteBakPath) {
		this.local = trimValue(local);
		this.remote = trimValue(remote);
		this.remoteName = trimValue(remoteName);
		this.remoteBakPath = trimValue(remoteBakPath);
	}

	private String trimValue(String value) {
		if(value == null || value.trim().length() == 0) {
			return null;
		}
		return value.trim();
	}

	public boolean isBackup() {
		if(this.remoteBakPath != null && this.remoteBakPath.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "UpdateFile{" +
				"local='" + local + '\'' +
				", remote='" + remote + '\'' +
				", remoteName='" + remoteName + '\'' +
				", remoteBakPath='" + remoteBakPath + '\'' +
				'}';
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	public String getRemoteBakPath() {
		return remoteBakPath;
	}

	public void setRemoteBakPath(String remoteBakPath) {
		this.remoteBakPath = remoteBakPath;
	}

	public boolean isCleanUp() {
		if(isCleanUp == null || isCleanUp == false) {
			return false;
		}
		return isCleanUp;
	}

	public void setCleanUp(Boolean cleanUp) {
		isCleanUp = cleanUp;
	}
}
