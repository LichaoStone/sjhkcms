package com.br.ott.client.common.entity;

import java.io.Serializable;


/**
 * 文件名：EpgProgram.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-3-28
 */
public class EpgProgram implements Serializable {
	private static final long serialVersionUID = -5479942635098965222L;
	// 节目名称
	private String name;
	// 所属频道ID
	private String channelId;
	// 所属频道名称
	private String channelName;
	// 播放时间
	private String playTime;
	// 播放时长
	private String timeLongth;
	// 节目剧集 如是电视剧时，第几集
	private String queue;
	// 节目单对应节目基础数据名称
	private String basicName;

	public EpgProgram(String name, String channelId, String channelName,
			String playTime, String timeLongth, String queue, String basicName) {
		super();
		this.name = name;
		this.channelId = channelId;
		this.channelName = channelName;
		this.playTime = playTime;
		this.timeLongth = timeLongth;
		this.queue = queue;
		this.basicName = basicName;
	}

	public EpgProgram() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public String getTimeLongth() {
		return timeLongth;
	}

	public void setTimeLongth(String timeLongth) {
		this.timeLongth = timeLongth;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getBasicName() {
		return basicName;
	}

	public void setBasicName(String basicName) {
		this.basicName = basicName;
	}

}
