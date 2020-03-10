package com.br.ott.client.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 文件名：AssetFileList.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
public class AssetSource implements Serializable {
	private static final long serialVersionUID = 3258699998644789156L;
	//第几集
	private String episode;
	//资源集合描述
	private List<AssetFile> file;

	public String getEpisode() {
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public List<AssetFile> getFile() {
		return file;
	}

	public void setFile(List<AssetFile> file) {
		this.file = file;
	}

}
