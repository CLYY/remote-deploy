package com.clyy;

import com.clyy.entity.UpdateFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Auther: Charles.Chen <br>
 * Description: 文件上传
 * Date: Create in 21:56 2018/6/15
 **/
public class RemoteUpdate {
	final static Logger logger = LoggerFactory.getLogger(RemoteUpdate.class);

	private RemoteConnection remoteConnection;

	public void update(RemoteConnection remoteConnection, List<UpdateFile> updateFiles) throws Exception {
		this.remoteConnection = remoteConnection;
		for(UpdateFile updateFile : updateFiles) {
			update(updateFile);
		}
	}

	private void update(UpdateFile updateFile) throws Exception {
		File localFile = new File(updateFile.getLocal());
		if(localFile.isDirectory()) {
			File[] files = localFile.listFiles();
			for (File file : files) {
				updateFile(file, updateFile);
			}
		} else {
			updateFile(localFile, updateFile);
		}
	}

	private void updateFile(File file, UpdateFile updateFile) throws Exception {
		String fileName = updateFile.getRemoteName();
		if(fileName == null || fileName.trim().length() == 0) {
			fileName = file.getName();
		}

		String remoteFile = updateFile.getRemote() + "/" + fileName;

		boolean isExists = true;
		try{
			remoteConnection.getSftpClient().stat(remoteFile);
		} catch (Exception e) {
			isExists = false;
		}

		if(isExists) {
			if (updateFile.isBackup()) {
				logger.info("备份旧数据：{}", remoteFile);
				String cmd = "mv -f " + remoteFile + " " + updateFile.getRemoteBakPath() + "/" + fileName;
				executeCommand(cmd);
			}
			if (updateFile.isCleanUp()) {
				logger.info("删除旧数据：{}", remoteFile);
				String cmd = "rm -rf " + remoteFile;
				executeCommand(cmd);
			}
		}
		logger.info("上传文件：{} ， To：{}", file.getPath(), remoteFile);
		OutputStream outputStream = remoteConnection.getSftpClient().write(remoteFile);
		Files.copy(Paths.get(file.getPath()), outputStream);
		outputStream.close();
	}

	private void executeCommand(String cmd) throws Exception{
		RemoteCommand.execute(remoteConnection.getSession(), cmd);
	}

}
