package com.clyy;

import com.clyy.common.XmlNodeConstant;
import com.clyy.entity.GlobalConfig;
import com.clyy.entity.Remote;
import com.clyy.entity.RemoteLink;
import com.clyy.entity.UpdateFile;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auther: Charles.Chen <br>
 * Description: 远端操作
 * Date: Create in 22:23 2018/6/15
 **/
public class DeployApplication {

	final static Logger logger = LoggerFactory.getLogger(DeployApplication.class);

	static String classPath = "";
	static String configPath = "";
	static {
		configPath = System.getProperty("remote.config");
		if(configPath == null || configPath.trim().length() == 0) {
			configPath = "remote-config.xml";
		}
		classPath = DeployApplication.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		if (classPath.endsWith(".jar")) {
			classPath = classPath.substring(0, classPath.lastIndexOf("/") + 1);
		}
	}

	public static void main(String[] args) throws Exception{
		// 获取配置文件
		if(configPath == null || configPath.trim().length() == 0) {
			throw new NullPointerException("无[remote.config]配置");
		}

		configPath = URLDecoder.decode(classPath + configPath, "utf-8");
		File configFile = new File(configPath);
		if(!configFile.exists()) {
			throw new NullPointerException("Path = " + configPath + "，未查找到配置文件");
		}

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(configFile);
		} catch (Exception e) {
			logger.error("解析XML配置文件异常", e);
			System.exit(0);
		}

		List<Remote> remoteList = new ArrayList<>();
		// 解析配置文件
		Element root = document.getRootElement();

		Element global = root.element(XmlNodeConstant.GLOBAL);
		GlobalConfig globalConfig = getGlobalConfig(global);
		if(globalConfig.isExecuteConfirm()) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("******************************");
			System.out.println("*         1：确认执行         *");
			System.out.println("*         0：取消执行         *");
			System.out.println("******************************");
			System.out.print("请输入你的选择：");
			String print = scanner.next();
			if(!"1".equals(print)) {
				System.exit(0);
			}
		}

		List<Element> remoteNodes = root.elements(XmlNodeConstant.REMOTE);
		for(Element element : remoteNodes) {
			if(!isActive(element)) {
				continue;
			}
			remoteList.add(parseRemote(element));
		}
		logger.info("操作远端对象数量：size = {}", remoteList.size());

		logger.debug("远端操作数据：{}", remoteList);

		// 执行
		for(Remote remote : remoteList) {
			try(RemoteConnection connection = new RemoteConnection()) {
				connection.connection(remote.getRemoteLink());

				if(remote.getUpdateFiles() != null && remote.getUpdateFiles().size() > 0) {
					RemoteUpdate update = new RemoteUpdate();
					update.update(connection, remote.getUpdateFiles());
				}

				if(remote.getCommands() != null && remote.getCommands().size() > 0) {
					for(String command : remote.getCommands()) {
						RemoteCommand.execute(connection.getSession(), command);
					}
				}
			} catch (Exception e) {
				logger.error("执行远端操作异常：{}", remote.getRemoteLink(), e);
			}
		}

		logger.info("远端操作结束！");
	}

	/**
	 * 获取全局设置
	 * @param global
	 * @return
	 * @throws Exception
	 */
	private static GlobalConfig getGlobalConfig(Element global) throws Exception {
		GlobalConfig config = new GlobalConfig();
		if(global == null) {
			return config;
		}
		Element executeConfirm = global.element(XmlNodeConstant.EXECUTE_CONFIRM);
		if(executeConfirm != null ) {
			String confirm = executeConfirm.getText();
			if("false".equals(confirm)) {
				config.setExecuteConfirm(false);
			}
		}

		return config;
	}

	/**
	 * 解析Remote节点
	 * @param remoteNode
	 * @return
	 * @throws Exception
	 */
	private static Remote parseRemote(Element remoteNode) throws Exception {
		Remote remote = new Remote();

		// 获取连接配置
		RemoteLink remoteLink = new RemoteLink();
		remoteLink.setIp(getValue(remoteNode, XmlNodeConstant.ATTR_IP));
		remoteLink.setPassword(getValue(remoteNode, XmlNodeConstant.ATTR_PASSWORD));
		String port = remoteNode.attributeValue(XmlNodeConstant.ATTR_PORT);
		if(port != null && port.trim().length() > 0) {
			remoteLink.setPort(Integer.valueOf(port.trim()));
		}
		remoteLink.setUser(getValue(remoteNode, XmlNodeConstant.ATTR_USER));
		remote.setRemoteLink(remoteLink);

		// 获取上传文件配置
		List<UpdateFile> updateFiles = new ArrayList<>();
		List<Element> updateNodes = remoteNode.elements(XmlNodeConstant.UPDATE);
		for(Element element : updateNodes) {
			if(!isActive(element)) {
				continue;
			}
			List<UpdateFile> tempUpdateFiles = parseUpdateFile(element);
			if(tempUpdateFiles == null) {
				continue;
			}
			updateFiles.addAll(tempUpdateFiles);
		}
		remote.setUpdateFiles(updateFiles);

		Element updateAfterNode = remoteNode.element(XmlNodeConstant.UPDATE_AFTER);
		if(updateAfterNode != null) {
			remote.setCommands(parseCommand(updateAfterNode));
		}

		return remote;
	}

	/**
	 * 解析Update 节点
	 * @param updateNode
	 * @return
	 * @throws Exception
	 */
	private static List<UpdateFile> parseUpdateFile(Element updateNode) throws Exception {
		Element filesNode = updateNode.element(XmlNodeConstant.FILES);
		if(filesNode == null) {
			return null;
		}

		List<UpdateFile> updateFiles = new ArrayList<>();
		UpdateFile commonUpdateFile = getUpdateFile(filesNode);
		List<Element> fileNodes = filesNode.elements(XmlNodeConstant.FILE);
		for(Element element : fileNodes) {
			UpdateFile updateFile = getUpdateFile(element);
			if(isReplace(updateFile.getLocal(), commonUpdateFile.getLocal())) {
				updateFile.setLocal(commonUpdateFile.getLocal());
			}
			if(isReplace(updateFile.getRemote(), commonUpdateFile.getRemote())) {
				updateFile.setRemote(commonUpdateFile.getRemote());
			}
			if(isReplace(updateFile.getRemoteName(), commonUpdateFile.getRemoteName())) {
				updateFile.setRemoteName(commonUpdateFile.getRemoteName());
			}
			if(isReplace(updateFile.getRemoteBakPath(), commonUpdateFile.getRemoteBakPath())) {
				updateFile.setRemoteBakPath(commonUpdateFile.getRemoteBakPath());
			}
			if(isReplace(updateFile.isCleanUp(), commonUpdateFile.isCleanUp())) {
				updateFile.setLocal(commonUpdateFile.getLocal());
			}
			updateFiles.add(updateFile);
		}

		return updateFiles;
	}

	private static boolean isReplace(Object oldValue, Object newValue) {
		if(oldValue == null && newValue != null) {
			return true;
		}
		return false;
	}

	/**
	 * 获取需要上传的文件配置
	 * @param fileNode
	 * @return
	 */
	private static UpdateFile getUpdateFile(Element fileNode) {
		String local = fileNode.attributeValue(XmlNodeConstant.ATTR_LOCAL);
		String remote = fileNode.attributeValue(XmlNodeConstant.ATTR_REMOTE);
		String remoteName = fileNode.attributeValue(XmlNodeConstant.ATTR_REMOTENAME);
		String remoteBakPath = fileNode.attributeValue(XmlNodeConstant.ATTR_REMOTEBAKPATH);
		String isCleanUp = fileNode.attributeValue(XmlNodeConstant.ATTR_ISCLEANUP);

		UpdateFile updateFile = new UpdateFile(local, remote, remoteName, remoteBakPath);
		updateFile.setCleanUp(Boolean.parseBoolean(isCleanUp));
		return updateFile;
	}

	/**
	 * 获取上传文件后的执行命令
	 * @param updateAfterNode
	 * @return
	 * @throws Exception
	 */
	private static List<String> parseCommand(Element updateAfterNode) throws Exception {
		List<Element> commandNodes = updateAfterNode.elements(XmlNodeConstant.COMMAND);
		if(commandNodes == null || commandNodes.size() == 0) {
			return null;
		}
		List<String> commands = new ArrayList<>();
		for(Element element : commandNodes) {
			String cmd = element.getText();
			if(cmd == null || cmd.trim().length() == 0) {
				continue;
			}
			commands.add(cmd);
		}
		return commands;
	}

	/**
	 * 当前节点是否激活
	 * @param element
	 * @return
	 */
	private static boolean isActive(Element element) {
		String active = element.attributeValue(XmlNodeConstant.ATTR_ACTIVE);
		if("false".equals(active)) {
			return false;
		}
		return true;
	}

	private static String getValue(Element element, String attrName) throws Exception{
		String value = element.attributeValue(attrName);
		if(value == null || value.trim().length() == 0) {
			throw new IllegalArgumentException("参数：[" + attrName + "]异常");
		}
		return value.trim();
	}
}
