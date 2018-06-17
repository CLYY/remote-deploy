package com.clyy;

import com.clyy.entity.RemoteLink;
import com.clyy.entity.UpdateFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Charles.Chen <br>
 * Description:
 * Date: Create in 10:55 2018/6/17
 **/
public class DeployApplicationTest {

	private RemoteLink link;
	private RemoteConnection connection;

	@Before
	public void init() {
		link = new RemoteLink("root", "12345678", "127.0.0.1");
	}

	@Test
	public void testConnection() throws Exception{
		connection = new RemoteConnection();
		connection.connection(link);
		Assert.assertNotNull(connection.getSession());
		Assert.assertNotNull(connection.getSftpClient());
	}

	@Test
	public void testUpdateByFile() throws Exception {
		testConnection();

		List<UpdateFile> updateFiles = new ArrayList<>();
		UpdateFile updateFile = new UpdateFile(
				"F:\\test\\test.jar",
				"/root/test/", "test-update.jar", "/root/test_bak/");
		updateFile.setCleanUp(true);
		RemoteUpdate update = new RemoteUpdate();
		updateFiles.add(updateFile);
		update.update(connection, updateFiles);
	}

	@Test
	public void testUpdateByDir() throws Exception {
		testConnection();

		List<UpdateFile> updateFiles = new ArrayList<>();
		UpdateFile updateFile = new UpdateFile(
				"F:\\test\\",
				"/root/test/");
		RemoteUpdate update = new RemoteUpdate();
		updateFiles.add(updateFile);
		update.update(connection, updateFiles);
	}

	@Test
	public void testCommand() throws Exception {
		testConnection();

		String cmd = "ls /root/test";
		RemoteCommand.execute(connection.getSession(), cmd);
	}


}
