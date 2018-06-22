package com.clyy.entity;

/**
 * Auther: Charles.Chen <br>
 * Description: 全局配置
 * Date: Create in 14:18 2018/6/22
 **/
public class GlobalConfig {

	// 是否进行执行前的确认流程
	private boolean executeConfirm = true;

	public boolean isExecuteConfirm() {
		return executeConfirm;
	}

	public void setExecuteConfirm(boolean executeConfirm) {
		this.executeConfirm = executeConfirm;
	}
}
