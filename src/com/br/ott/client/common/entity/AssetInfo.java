package com.br.ott.client.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 文件名：AssetInfo.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
public class AssetInfo implements Serializable {

	private static final long serialVersionUID = -5705399771820227728L;
	// 节目id
	private String assetId;
	// 连播剧为1，其他为0
	private String assetStatus;
	// 资产分类：基础或专题
	private String assetMode;
	// 节目名称
	private String assetTitle;
	// 地区 如：欧美、大陆、日韩
	private String assetArea;
	// 资产类型 可属于多级类型，通过“-”区分一二级，通过“|”分割多个类型 如：“电影-欧美大片|少儿-动画电影”
	private String assetType;
	// 检索关键字 可多个，多个关键字用“|”分割
	private String keywords;
	// 语言 如：国语、韩语、英语
	private String assetLanguage;
	// 导演
	private String assetDirector;
	// 主演
	private String assetActor;
	// 描述
	private String assetDescription;
	// 看点
	private String assetSummary;
	// 封面大图地址 ftp地址，详情和推荐用
	private String coverDImg;
	// 封面小图地址 ftp地址，列表展示用
	private String coverXImg;
	// 推荐大图地址 ftp地址，推荐位置用
	private String recommendDImg;
	// 推荐小图地址 ftp地址，推荐位置用
	private String recommendXImg;
	// 链接地址 当assetMode为基础时，为空，传完整地址
	private String linkUrl;
	// 生产年份
	private String year;
	// 操作动作 ON，上线 OFF，下线
	private String action;
	// 子资产信息
	private List<AssetSource> files;

	public static void main(String[] args) {
		AssetInfo ai= new AssetInfo();
		ai.setAction("ON");
		ai.setAssetActor("周星驰 吴孟达 钟丽缇 林国斌");
		ai.setAssetId("12321");
		ai.setAssetDirector("李力持");
		ai.setAssetMode("基础");
		ai.setAssetStatus("0");
		ai.setAssetTitle("破坏之王2");
		ai.setYear("1996");
		ai.setAssetDescription("快餐小子何金银偶遇在武术训练场的阿丽并对她产生了好感，却受到同样喜欢阿丽的教练林国斌的羞辱。何金银觉得自己太弱小便拜自称是“魔鬼筋肉人”的鬼王达为师，学习搏击技术。不料鬼王达却是个骗子，只是用各种手段骗取人们的钱财，没有真实功夫实力。一次何金银偶遇阿丽遭坏人拦截，于是他头戴加菲猫的面具出手相救，但拳法太差，情急之下从高台上抱着歹徒滚下，由此创出了“无敌风火轮”，事后却被林国斌领功。何金银为了证实自己的实力，报名参加了与日本人决斗的搏击比赛，并凭借着“风火轮”打败了日本人，最后感动了阿丽，二者相爱。");
		ai.setKeywords("喜剧|动作");
		ai.setAssetType("电影-喜剧幽默");
		ai.setAssetSummary("草根星爷逆袭白富美");
		
		List<AssetSource> asList= new ArrayList<AssetSource>();
		AssetSource as= new AssetSource();
		as.setEpisode("1");
		
		List<AssetFile> file =new ArrayList<AssetFile>();
		AssetFile af= new AssetFile();
		af.setFileId("1111");
		af.setFtpUrl("");
		af.setAssetBitrate("1200");
		af.setMd5("d7aa0af58f6db4663fddb5cadef356e1");
		af.setPlayUrl("http://222.85.81.15:6060/WASUOTT/32/2438726?virtualDomain=WASUOTT.vod_hpd.zte.com");
		file.add(af);
		as.setFile(file);
		asList.add(as);
		ai.setFiles(asList);
		
		JSONObject jsonObject = JSONObject.fromObject(ai);
		System.out.println(jsonObject.toString());
		
	}
	
	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetMode() {
		return assetMode;
	}

	public void setAssetMode(String assetMode) {
		this.assetMode = assetMode;
	}

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public List<AssetSource> getFiles() {
		return files;
	}

	public void setFiles(List<AssetSource> files) {
		this.files = files;
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public String getAssetArea() {
		return assetArea;
	}

	public void setAssetArea(String assetArea) {
		this.assetArea = assetArea;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAssetLanguage() {
		return assetLanguage;
	}

	public void setAssetLanguage(String assetLanguage) {
		this.assetLanguage = assetLanguage;
	}

	public String getAssetDirector() {
		return assetDirector;
	}

	public void setAssetDirector(String assetDirector) {
		this.assetDirector = assetDirector;
	}

	public String getAssetActor() {
		return assetActor;
	}

	public void setAssetActor(String assetActor) {
		this.assetActor = assetActor;
	}

	public String getAssetDescription() {
		return assetDescription;
	}

	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}

	public String getAssetSummary() {
		return assetSummary;
	}

	public void setAssetSummary(String assetSummary) {
		this.assetSummary = assetSummary;
	}

	public String getCoverDImg() {
		return coverDImg;
	}

	public void setCoverDImg(String coverDImg) {
		this.coverDImg = coverDImg;
	}

	public String getCoverXImg() {
		return coverXImg;
	}

	public void setCoverXImg(String coverXImg) {
		this.coverXImg = coverXImg;
	}

	public String getRecommendDImg() {
		return recommendDImg;
	}

	public void setRecommendDImg(String recommendDImg) {
		this.recommendDImg = recommendDImg;
	}

	public String getRecommendXImg() {
		return recommendXImg;
	}

	public void setRecommendXImg(String recommendXImg) {
		this.recommendXImg = recommendXImg;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
