package com.br.ott.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 * 
 * @author xiaoyaoyao
 * @version [版本号, 2011-06-08]
 */
public interface Constants {
	/**
	 * 空格
	 */
	String WHITE_SPACE = "";

	/**
	 * 每页显示的数据量
	 */
	int PAGE_DATA = 15;

	/**
	 * 当前页面属性名
	 */
	String CUR_PAGE_NAME = "curPage";

	/**
	 * 当前
	 */
	String PAGE_DATA_NAME = "pageData";

	/**
	 * 要排序的属性名
	 */
	String ORDER_NAME = "orderName";

	/**
	 * 要排序的方式
	 */
	String ORDER_FLAG = "orderFlag";

	/**
	 * 看是否为数字
	 */
	String NUMBER_REGEX = "^[1-9]\\d*$";

	/**
	 * 默认为第一页
	 */
	int FIRST_PAGE = 1;

	/** api点播频道返回action路径*/
	public static final String DB_ACTION_URL = "dxrm?type=openUrl&actionType=vodCategory&id=";

	// 用户初始密码
	public static final String DEFAULT_PWD = "888";

	public static final int PAGE_NUM = 10;

	// 读取io流默认的字节长度
	public static final int DEFAULT_BYTE = 1024;

	public static final int OUT_DEFAULT_BYTE = 32768;

	public static final int NUM0 = 0;

	public static final int NUM1 = 1;

	public static final int NUM2 = 2;

	public static final int NUM3 = 3;

	public static final int NUM4 = 4;

	public static final int NUM5 = 5;

	public static final int NUM6 = 6;

	public static final int NUM7 = 7;

	public static final int NUM8 = 8;

	public static final int NUM9 = 9;

	public static final int NUM10 = 10;

	public static final int NUM12 = 12;

	public static final int NUM31 = 31;

	public static final int NUM1900 = 1900;

	public static final int NUM15 = 15;

	public static final int NUM16 = 16;

	public static final int NUM19 = 19;

	public static final int NUM20 = 20;

	public static final int NUM45 = 45;

	public static final int NUM2000 = 2000;

	public static final int NUM2999 = 2999;

	public static final int OX = 0xf0;

	public static final int OXOF = 0x0f;

	public static final int NUM256 = 256;

	public static final String ZERO = "0";

	public static final String CONTENTTYPE = "text/xml";

	public static final String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static final char HEXDIG[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** xml根节点名称 */
	public static final String XML_ROOT = "root";

	/** CHARSET utf8 编码 */
	public static final String CHARSET = "UTF-8";

	/** xml content type 设置字串 */
	public static final String XML_CONTENTTYPE = "text/xml;charset=UTF-8";

	/** xml文件根节点开始标记 */
	public static final String XML_ROOT_BEGIN = "<root>";

	/** xml文件根节点结束标记 */
	public static final String XML_ROOT_END = "</root>";

	public static final String MAC_REGEX = "^([0-9a-fA-F]{2})(([0-9a-fA-F]{2}){5})$";

	public static final String IMEI_REGEX = "^[1-9]*[1-9][0-9]*$";

	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 通用状态
	 * 
	 * @author pananz
	 * 
	 */
	public final static class State {
		public static final String VALID = "1";
		public static final String INVALID = "0";
		public static final String NOVALID = "-1";
		public static final String AUDIT = "1";
		public static final String UNAUDIT = "0";

		public static final String DEFAULT_INT = "9999";

		private State() {
			// empty!
		}

		public static String getStateName(String state) {
			return VALID.equals(state) ? "有效" : "无效";
		}

		public static String getAuditName(String audit) {
			return AUDIT.equals(audit) ? "已审核" : "未审核";
		}
	}

	/**
	 * 字典类型
	 * 
	 * @author pananz
	 * 
	 */
	public final static class DicType {
		private DicType() {
			// empty
		}

		/** 顶级类型 */
		public static final String TOP_TYPE = "0";
		/** 商家类型 */
		public static final String MERCHANT_TYPE = "MERCHANT";
		/** 区域 */
		public static final String AREA_TYPE = "AREA";
		/** 用户行为类型 */
		public static final String BEHAVIOR_TYPE = "BEHAVIORTYPE";
		// 特殊节目名称
		public static final String TSJM = "TSJM";
		/** 是否开启上传用户行为数据 */
		public static final String BEHAVIOR_OPEN = "BEHAVIOROPEN";
		/** 节目类型 */
		public static final String JMLX = "JMLX";
		/** 类型分类 */
		public static final String LXFL = "LXFL";
		/** 节目资源产地 */
		public static final String JMZYCD = "JMZYCD";
		/** 节目资源语言 */
		public static final String YUYAN = "YUYAN";
		/** 内容提供商 */
		public static final String LRTGS = "LRTGS";
		/** 节目码率 */
		public static final String MALV = "MALV";
		/** 推荐位分类 */
		public static final String TJWFL = "TJWFL";
	}

	/**
	 * 商家类型
	 * 
	 * @author pananz
	 * 
	 */
	public final static class PartnerType {
		private PartnerType() {
			// empty;
		}

		/** 运营商 */
		public static final String PARTNER_OPERATORS = "1";
		/** 平台商 */
		public static final String PARTNER_PLATFORM_PROVIDERS = "2";
		/** 企业内容提供商 */
		public static final String PARTNER_CONTENT_PROVIDERS = "3";
		/** 个人内容提供者 */
		public static final String PARTNER_PERSONEL_PROVIDERS = "4";
		/** 渠道商 */
		public static final String PARTNER_CHANNEL_BUS = "5";
		/** 广告商 */
		public static final String PARTNER_ADVERTISERS = "6";
		/** 品牌/版权提供商 */
		public static final String PARTNER_COPYRIGHT = "7";
	}

	/**
	 * 计费规则类型
	 * 
	 * @author pananz
	 * 
	 */
	public final static class ChargeState {
		private ChargeState() {
			// empty;
		}

		// 电信代扣费
		public static final String TELECOMMUNICATIONS_RULE = "1";
		// 本地代扣费
		public static final String LOCAL_RULE = "2";
	}

	/**
	 * 用户订购信息状态
	 * 
	 * @author pananz
	 * 
	 */
	public final static class UserOrderState {
		private UserOrderState() {
			// empty
		}

		// 0后付费订购失败(手机与宽带)
		public static final String ORDER_FAIL = "0";
		// 1后付费订购成功(手机与宽带)
		public static final String ORDER_SUCCESS = "1";
		// 2过期(数据产品专有)
		public static final String ORDER_TIMEOUT = "2";
		// 3手机实时订购扣费失败
		public static final String ORDER_PHONE_FAIL = "3";
		// 4手机实时订购扣费成功
		public static final String ORDER_PHONE_SUCCESS = "4";

		// 5--续订 6，取消续订，7 -- 取消订购
		/**
		 * 续订
		 */
		public static final String ORDER_RENEWAL = "5";
		/**
		 * 取消续订
		 */
		public static final String ORDER_CANCEL_THE_RENEWAL = "6";
		/**
		 * 取消订购
		 */
		public static final String ORDER_CANCEL_THE_ORDER = "7";
		// 幼教产品会员业务名称
		public static final String CHILD_PRODUCT_NAME = "幼教产品会员业务";
	}

	/**
	 * 订购支付类型 文件名：Constants.java 版权：BoRuiCubeNet. Copyright 2012-2012,All rights
	 * reserved 公司名称：青岛博瑞立方网络科技有限公司 创建人：pananz 创建时间：2012-12-14 - 上午11:21:19
	 */
	public final static class UserOrderPayType {
		private UserOrderPayType() {
			// empty
		}

		// 支付类型:站内支付1 支付宝2 银联支付3 虚拟货币支付4,电信代扣5(数据产品专有)
		/** 站内支付 */
		public static final String ORDER_PAY_LOCAL = "1";
		/** 支付宝 */
		public static final String ORDER_PAY_ALIPAY = "2";
		/** 银联支付 */
		public static final String ORDER_PAY_UNION = "3";
		/** 虚拟货币支付 */
		public static final String ORDER_PAY_VIRTUAL = "4";
		/** 电信代扣 */
		public static final String ORDER_PAY_TELECOM = "5";
	}

	/**
	 * 订购类型：1 表示宽带订购， 2 表示手机订购,默认为宽带订购 文件名：Constants.java 版权：BoRuiCubeNet.
	 * Copyright 2012-2012,All rights reserved 公司名称：青岛博瑞立方网络科技有限公司 创建人：pananz
	 * 创建时间：2012-12-14 - 上午11:29:19
	 */
	public final static class UserOrderType {
		private UserOrderType() {
			// empty
		}

		/** 宽带订购 */
		public static final String ORDER_TYPE_BOUND = "1";
		/** 手机订购 */
		public static final String ORDER_TYPE_PHONE = "14";
	}

	/**
	 * 模板类型 版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
	 * 公司名称：青岛博瑞立方网络科技有限公司
	 * 
	 * @author zhuw
	 * @version 1.0 2013-1-15 下午4:29:45
	 */
	public final static class TemplateListType {
		private TemplateListType() {
		}

		/** 首页 */
		public static final String HOMEPAGE = "homepage";
		/** 列表 */
		public static final String LIST = "list";
		/** 详情 */
		public static final String DETAIL = "detail";
		/** 资讯 */
		public static final String INFORMATION = "information";
		/** 广告 */
		public static final String ADVERTISING = "advert";
		/** 其他 */
		public static final String OTHER = "other";
	}

	/**
	 * 宽带账号返回订购提示语 文件名：Constants.java 版权：BoRuiCubeNet. Copyright 2012-2012,All
	 * rights reserved 公司名称：青岛博瑞立方网络科技有限公司 创建人：pananz 创建时间：2012-12-31 -
	 * 上午10:00:45
	 */
	public final static class BoundOrderReturnStatus {
		private BoundOrderReturnStatus() {
			// empty
		}

		public static final String BOUND_ORDER_RETURN_NOBUY = "1";

		public static final String BOUND_ORDER_RETURN_HAVBUY = "2";

		public static final String BOUND_ORDER_RETURN_CANNOTBUY = "3";

		public static final String BOUND_ORDER_RETURN_TIMEOUT = "4";

		public static final String getOrderReturnType(String key) {
			HashMap<String, String> boundOrderReturnType = new HashMap<String, String>();
			boundOrderReturnType.put(BOUND_ORDER_RETURN_NOBUY, "此宽带账号尚未订购过此产品");
			boundOrderReturnType.put(BOUND_ORDER_RETURN_HAVBUY,
					"此宽带账号已订购过该产品，而宽带账号的机顶盒未订购过此产品");
			boundOrderReturnType.put(BOUND_ORDER_RETURN_CANNOTBUY,
					"此宽带账号的机顶盒已订购过该产品，还在使用有效期内");
			boundOrderReturnType.put(BOUND_ORDER_RETURN_TIMEOUT,
					"此宽带账号的机顶盒已订购过该产品，已过有效使用期");
			return boundOrderReturnType.get(key);
		}
	}

	/**
	 * 手机订购返回提示 文件名：Constants.java 版权：BoRuiCubeNet. Copyright 2012-2012,All
	 * rights reserved 公司名称：青岛博瑞立方网络科技有限公司 创建人：pananz 创建时间：2012-12-31 -
	 * 上午10:01:14
	 */
	public final static class PhoneOrderReturnStatus {
		private PhoneOrderReturnStatus() {
			// empty
		}

		public static final String PHONE_ORDER_RETURN_NOBUY = "1";

		public static final String PHONE_ORDER_RETURN_HAVBUY = "2";

		public static final String PHONE_ORDER_RETURN_CANNOTBUY = "3";

		public static final String PHONE_ORDER_RETURN_TIMEOUT = "4";

		public static final String getOrderReturnType(String key) {
			HashMap<String, String> phoneReturnType = new HashMap<String, String>();
			phoneReturnType.put(PHONE_ORDER_RETURN_NOBUY, "此手机尚未订购过此产品");
			phoneReturnType.put(PHONE_ORDER_RETURN_HAVBUY,
					"此手机綁定的机顶盒已订购过该产品，而手机綁定的机顶盒未订购过此产品");
			phoneReturnType.put(PHONE_ORDER_RETURN_CANNOTBUY,
					"此手机綁定的机顶盒已订购过该产品，还在使用有效期内");
			phoneReturnType.put(PHONE_ORDER_RETURN_TIMEOUT,
					"此手机綁定的机顶盒已订购过该产品，已过有效使用期");
			return phoneReturnType.get(key);
		}
	}

	/**
	 * 运营商编码
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class OperatorsCode {
		private OperatorsCode() {
			// empty
		}

		public static Map<String, String> getOperatorsId() {
			HashMap<String, String> map = new HashMap<String, String>();
			// 山东电视台.移动
			map.put("hngd.yd", "1887");
			// 山东电视台.电信华为
			map.put("hngd.dxhw", "1893");
			// 山东电视台.电信中兴
			map.put("hngd.dxzx", "1895");
			// 山东电视台.P2P
			map.put("dxrm.p2p", "1897");
			return map;
		}
	}

	/**
	 * 合作伙伴流程状态
	 * 
	 * @author pananz
	 * 
	 */
	public final static class PartnerState {
		private PartnerState() {
			// empty
		}

		/** 失效 */
		public static final String PARTNER_INVALID = "0";
		/** 待审核 */
		public static final String PARTNER_APPLY = "1";
		/** 管理员审核中 */
		public static final String PARTNER_PROCESSING_MANAGER = "2";
		/** 管理员审核异常 */
		public static final String PARTNER_EXP_MANAGER = "3";
		/** 待商务审核 */
		public static final String PARTNER_WAITING_BUS = "4";
		/** 商务审核中 */
		public static final String PARTNER_PROCESSING_BUS = "5";
		/** 商务审核异常 */
		public static final String PARTNER_EXP_BUS = "6";
		/** 准入,非合同用户 */
		public static final String PARTNER_READY = "7";
		/** 待合同审核 */
		public static final String PARTNER_WAITING_CON = "8";
		/** 合同审核中 */
		public static final String PARTNER_ROCESSING_CON = "9";
		/** 合同审核异常 */
		public static final String PARTNER_EXP_CON = "10";
		/** 合同审核通过,上线 */
		public static final String PARTNER_PASS = "11";
		/** 强制失效 */
		public static final String PARTNER_FORCED_SHELF = "12";

		public static Map<String, String> getPartnerStatus() {
			HashMap<String, String> status = new HashMap<String, String>();
			status.put(PARTNER_FORCED_SHELF, "强制失效");
			status.put(PARTNER_PASS, "合同审核通过,上线");
			status.put(PARTNER_EXP_CON, "合同审核异常");
			status.put(PARTNER_ROCESSING_CON, "合同审核中");
			status.put(PARTNER_WAITING_CON, "合同待审核");
			status.put(PARTNER_READY, "非合同用户");
			status.put(PARTNER_EXP_BUS, "商务审核异常");
			status.put(PARTNER_PROCESSING_BUS, "商务审核中");
			status.put(PARTNER_WAITING_BUS, "商务待审核");
			status.put(PARTNER_EXP_MANAGER, "管理员审核异常");
			status.put(PARTNER_PROCESSING_MANAGER, "管理员审核中");
			status.put(PARTNER_APPLY, "待审核");
			status.put(PARTNER_INVALID, "失效");
			return status;
		}
	}

	/**
	 * 机顶盒状态
	 */
	public final static class MacBindState {
		private MacBindState() {
		}

		/** 未启用 */
		public static final String STATE0 = "0";
		/** 启用 */
		public static final String STATE1 = "1";
		/** 停机 */
		public static final String STATE2 = "2";
		/** 销户 */
		public static final String STATE3 = "3";
		/** 改号 */
		public static final String STATE4 = "4";
		/** 出库 */
		public static final String STATE5 = "5";
		/** 转出 */
		public static final String STATE6 = "6";
		/** 返修 */
		public static final String STATE7 = "7";

	}

	/**
	 * 组状态
	 */
	public final static class GroupState {
		private GroupState() {
		}

		/** 启用 */
		public static final String STATE1 = "1";
		/** 隐藏 */
		public static final String STATE2 = "2";
		/** 冻结 */
		public static final String STATE3 = "3";
		/** 弃用 */
		public static final String STATE4 = "4";
	}

	/**
	 * 权限状态
	 */
	public final static class RoleState {
		private RoleState() {
		}

		/** 启用 */
		public static final String STATE1 = "1";
		/** 隐藏 */
		public static final String STATE2 = "2";
		/** 冻结 */
		public static final String STATE3 = "3";
		/** 弃用 */
		public static final String STATE4 = "0";
	}

	/**
	 * 商品流程状态
	 * 
	 * @author pananz
	 * 
	 */
	public final static class ProductState {
		private ProductState() {
			// empty
		}

		/** 申请上架 */
		public static final String PRODUCT_APPLY = "1";
		/** 管理员审核中 */
		public static final String PRODUCT_PROCESSING_MANAGER = "2";
		/** 管理员审核异常 */
		public static final String PRODUCT_EXP_MANAGER = "3";
		/** 待质保员审核 */
		public static final String PRODUCT_WAITING_WARRANTY = "4";
		/** 质保员审核中 */
		public static final String PRODUCT_PROCESSING_WARRANTY = "5";
		/** 质保员审核异常 */
		public static final String PRODUCT_EXP_WARRANTY = "6";
		/** 待测试员审核 */
		public static final String PRODUCT_WAITING_TESTERS = "7";
		/** 测试员审核中 */
		public static final String PRODUCT_PROCESSING_TESTERS = "8";
		/** 测试员审核异常 */
		public static final String PRODUCT_EXP_TESTERS = "9";
		/** 待预发布员审核 */
		public static final String PRODUCT_WAITING_PRERELEASE = "10";
		/** 产品预发布中 */
		public static final String PRODUCT_PROCESSING_PRERELEASE = "11";
		/** 预发布异常 */
		public static final String PRODUCT_EXP_PRERELEASE = "12";
		/** 待资费员审核 */
		public static final String PRODUCT_WAITING_TARIFF = "13";
		/** 资费员审核中 */
		public static final String PRODUCT_PROCESSING_TARIFF = "14";
		/** 资费员审核异常 */
		public static final String PRODUCT_EXP_TARIFF = "15";
		/** 资费配置中 */
		public static final String PRODUCT_WAITING_PARTNER = "16";
		/** 待测试员再次审核 */
		public static final String PRODUCT_WAITING2_TESTERS = "17";
		/** 测试员再次审核中 */
		public static final String PRODUCT_PROCESSING2_TESTERS = "18";
		/** 产品待发布 */
		public static final String PRODUCT_PROCESSING3_TESTERS = "19";
		/** 正常上架 */
		public static final String PRODUCT_SHELVES = "0";
		/** 正常下架 */
		public static final String PRODUCT_NORMAL_SHELF = "20";
		/** 强制正常下架 */
		public static final String PRODUCT_FORCED_SHELF = "21";
		
		public static Map<String, String> getProductStatus() {
			HashMap<String, String> status = new HashMap<String, String>();
			status.put(PRODUCT_APPLY, "申请上架");
			status.put(PRODUCT_PROCESSING_MANAGER, "管理员审核中");
			status.put(PRODUCT_EXP_MANAGER, "管理员审核异常");
			status.put(PRODUCT_WAITING_WARRANTY, "待质保员审核");
			status.put(PRODUCT_PROCESSING_WARRANTY, "质保员审核中");
			status.put(PRODUCT_EXP_WARRANTY, "质保员审核异常");
			status.put(PRODUCT_WAITING_TESTERS, "待测试员审核");
			status.put(PRODUCT_PROCESSING_TESTERS, "测试员审核中");
			status.put(PRODUCT_EXP_TESTERS, "测试员审核异常");
			status.put(PRODUCT_WAITING_PRERELEASE, "待预发布员审核");
			status.put(PRODUCT_PROCESSING_PRERELEASE, "产品预发布中");
			status.put(PRODUCT_FORCED_SHELF, "强制正常下架");
			status.put(PRODUCT_NORMAL_SHELF, "正常下架");
			status.put(PRODUCT_SHELVES, "正常上架");
			status.put(PRODUCT_PROCESSING3_TESTERS, "产品待发布");
			status.put(PRODUCT_PROCESSING2_TESTERS, "测试员再次审核中");
			status.put(PRODUCT_WAITING2_TESTERS, "待测试员再次审核");
			status.put(PRODUCT_WAITING_PARTNER, "资费配置中");
			status.put(PRODUCT_EXP_TARIFF, "资费员审核异常");
			status.put(PRODUCT_PROCESSING_TARIFF, "资费员审核中");
			status.put(PRODUCT_WAITING_TARIFF, "待资费员审核");
			status.put(PRODUCT_EXP_PRERELEASE, "预发布异常");
			return status;
		}
	}

	public static final String PRV_CHECK_TYPE = "prv";
	public static final String DEFAULT_CHECK_TYPE = "default";

	public static final String CHECK_TREE_TYPE = "checkbox";
	public static final String DEFAULT_TREE_TYPE = "default";
	public static final String RADIO_TREE_TYPE = "radio";
	public static final String COMPOSITE_TREE_TYPE = "composite";

	public static final String CURRENT_USER = "user";//
	public static final String USER_ROLES = "userRoles"; // session
															// 中存放的当前登录的用户组信息

	public static final String USER_ROLE = "userRole"; // session
														// 中存放的当前登录的用户所拥有的角色信息
	public static final String USER_RESOURCE = "userResource"; // session
																// 中存放的当前登录的用户可操作资源信息
	public static final String USER_MENU_PRI = "userMenuPri";// 用户权限菜单
	public static final String USER_NODE = "userNode";// 用戶操作仓库

	/** 开发组编号 */
	public static final String GROUP_COMMUNITY = "COMMUNITY";
	/** 使用伙伴组编号 */
	public static final String GROUP_PARTNER = "PARTNER";
	/** 合作伙伴编号 */
	public static final String ROLE_PARTNER = "PARTNER";
	/** 开发者初始状态值 */
	public static final String COMMUNITY_STATUS = "1";
	/** 超级管理员组编号 */
	public static final String GROUP_SUPER = "administrator";

	public static final int DICVALUE_THREE = 3;

	/** 字典代码值dicType为零 */
	public static final String DICTYPE_ZERO = "0";

	/** 字典代码值初始值dicValue为100 */
	public static final String INIT_DICVALUE = "100";

	/** 机顶盒值 */
	public static final String MACH_TYPE = "0";
	/**
	 * 手机类型
	 * */
	public static final String ANROID_TELPHONE_TYPE = "1";

	/** 模板图片样式资源路径 */
	public static final String TEMPLATE_SOURCE_URL = "sourceUrl";
	/** 模板区域事件标识 */
	public static final String TEMPLATE_ONCLICK_KEY = "onclick";
	/** 模板区域JS函数 */
	public static final String TEMPLATE_ONCLICK_VALUE = "onclick='toTemplate(this)'";
}
