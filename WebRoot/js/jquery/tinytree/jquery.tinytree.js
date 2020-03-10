/*
 * name: jquery.tinytree.js
 *
 * Copyright (c) 2009
 * Examples and docs at: http://tablesorter.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * $author: huang weijian(黄伟鉴)$
 * $Date: 2009/08/08 07:10:37 $
 * $Contact: alvin.huang.wj@gmail.com$
 */
 /**
 * 一个基于jQuery的tree控件, 支持异步加载
 * 
 * @author huang weijian(alvin.huang.wj@gmail.com)
 */
;(function($) {
	
	var classNames = {
		tinytree : 'tinytree',
		open : "open",
		closed : "closed",
		expandable : "expandable",
		collapsable : "collapsable",
		lastCollapsable : "lastCollapsable",
		lastExpandable : "lastExpandable",
		last : "last",
		file : "file",
		closedfolder : "closedfolder",
		openfolder : "openfolder",
		hitarea : "hitarea",
		checkbox : "checkbox",
		selected : 'selected',
		
		_leafHref : "_leafHref",
		_compositeHref : '_compositeHref'
	};
	
	/**
	 * 构建异步树, 通过第一次得到的树信息创建
	 * 
	 * @param {Element}
	 *            container 异步树的HTML DOM容器
	 * @param {Object}
	 *            settings 配置信息
	 */
	$.buildTinyTree = function(container, settings) {
		
		var $container = $(container);
		
		var tipDivIdPrefix = '_tinytree_tip_';
		var nodeIdPrefix = '_tinytree_node_';
		var hasLoadedChildrenCache = [];
		
		/**
		 * 默认配置
		 */
		var defaults = {
			root : '0', // 根节点的ID
			
			autoload : true, // 是否自动回载第一级节点
			
			url : false, // ajax url
			method : 'POST', // POST 还是 GET
			dataType : 'json', // 调用url后得到的数据格式 'xml' or 'json'
			ajaxCache : true, // 设置为 false 将不会从浏览器缓存中加载请求信息, 将调服务器
			
			loadingMessage : '<font color="red" size="2">正在加载...</font>',			
			timeToShowLoadingMessage : false, // 延迟显示加载提示信息的时间, 单位为: 秒
			
			// javascript的格式是javascript:method, 回传(nodeId, nodeName)
			nodeUrl : false, // 节点触发的事件, 支持http和javascript:两种方式
			target : 'contentFrame', // 事件触发的目标
			ignoreSpecificNodeUrl : false, // 是否忽略每个节点里特定的url. 默认false, 不忽略.
			
			
			filter : false, // 是否显示过滤框?
			showChildren : false,	//是否同时显示过滤后的节点的后代？
			
			closeSameLevel : false, // 当打开该节点时，是否关闭同级的节点
			
			radioSupport : false,		//多选
    		checkboxSupport : false,	//单选
    		radioButLeafCheckboxSupport : false, // 单选，但叶子节点为多选
    		inputName : 'tinytreeInputName', //多选或单选input的name属性
    		selectLeafOnly  : false,	//只选择叶节点
    		clickNodeToCheckRadioOrCheckbox : false, // 点击节点是否勾选radio或checkbox
    		onRadionClick : false,
    		
    		alsoCheckParent	: false,	//是否同时勾选父节点
    		alsoCheckChildren : false, //是否同时勾选子节点
    
    		openAllNodes : false, // 是否加载时打开所有的已加载过的节点?
    		
			onError : false, // 从后台加载数据出错时触发function onError(nodes)
			onSuccess : false,	//加载完成后回调  
			onNodeClick : false, // 节点单击时触发的事件function onNodeClick(nodeId, nodeName)
			
			specifyLeafClass : false, // 特定叶子节点的样式
			
			hiddenNodeClass : 'hiddenNode',
			
			// private, don't set these values by yourself
			_haveLoaded : false, // 是否已经了加载数据, 用于延迟显示加载提示信息
			_firstLoaded : true  // 是否是第一次加载? 
		};
		settings = $.extend({}, defaults, settings);
		loadingNodes = {};	//是否正在加载数据
		
		var gdiv = document.createElement('div');
		gdiv.id = generateNodeId(settings.root);
		gdiv.className = classNames.tinytree;
		$gdiv = $(gdiv);
		
		$container.append(gdiv);
		if (settings.filter) {
			$gdiv.append(createFilterDivHtml());
			bindFilterEvent();
		}
		$gdiv.append(createTipDivHtml(settings.root));
		
		var tinytree = {
			/**
			 * 构建下级树
			 */
			buildLowerTree : function(parentId) {
				if (!settings.url) {
					return;
				}
				var timeout = settings.timeToShowLoadingMessage;
				if (timeout) {
					window.setTimeout(function(parentId) {
						if (!settings._haveLoaded) {
							showTipMessage(parentId);
						}
					}, (timeout * 1000));
				} else {
					showTipMessage(parentId);
				}
				
				//hasLoadedChildrenCache.push(parentId);
				var param = [{name:'parentId', value:parentId}];
				
				if(!loadingNodes['loadTree'+parentId]) {
					loadingNodes['loadTree'+parentId] = true;
		    		$.ajax({
						type : settings.method,
						url : settings.url,
						data : param,
						dataType : settings.dataType,
						cache : settings.ajaxCache,
						success : function(nodes) {
							loadingNodes['loadTree'+parentId] = false;
							settings._haveLoaded = true;
							if (nodes) {
								tinytree.addNodes(parentId, nodes);
							} else {
								hideTipMessage(parentId);
							}
							settings._firstLoaded = false;
							if(settings.onSuccess) {
								settings.onSuccess(); 
							}
						},
						error : function(xhr,status,e) {
							loadingNodes['loadTree'+parentId] = false;
							settings._haveLoaded = true;
							try {
								if (settings.onError) {
									settings.onError(xhr,status,e);
								}
							} catch (e) {}
						}
					});
				}
			},
			
			/**
			 * 把其它节点加到指定的某节点上
			 */
			addNodes : function(parentId, nodes) {
				hideTipMessage(parentId);
				if (settings.dataType == 'xml') {
					parseXmlNodes(parentId, nodes);
				} else {
					parseJsonNodes(parentId, nodes);
				}
				if (settings._firstLoaded && settings.openAllNodes) {
					tinytree.openAll();
				}
				return tinytree;
			},
			
			/**
			 * 得到radio/checkbox被选中的节点信息
			 * 
			 * @return {Array} 选中的节点信息[{nodeId:'xx', nodeName:'xx'}]
			 */
			getSelecetedNodes : function() {
				var $checkeds = $(':checkbox:checked,:radio:checked', gdiv);
				var result = [];
				$.each($checkeds, function(i, checked) {
					var $checked = $(checked);
					result.push({
						parentId : $checked.attr('parentId'),
						nodeId : $checked.attr('treeNodeId'),
						nodeName : $checked.attr('treeNodeName')
					});
				});
				return result;
			},
			
			/**
			 * 展开所有已被加载过的节点
			 */
			openAll : function() {
				justOpenNodes(hasLoadedChildrenCache);
				return tinytree;
			},
			/**
			 * 关闭所有的节点
			 */
			closeAll : function() {
				justCloseNodes(hasLoadedChildrenCache);
				return tinytree;
			},
			openNodes : function(nodeIds) {
				justOpenNodes(nodeIds);
				return tinytree;
			},
			closeNodes : function(nodeIds) {
				justCloseNodes(nodeIds);
				return tinytree;
			},
			
			/**
			 * 根据指定的节点名(filterValue)过滤节点
			 * 
			 * @param {String}
			 *            要进行过滤的节点名
			 */
			doFilter : function(filterValue) {
				filterValue = filterValue ? $.trim(filterValue) : '';
				if (filterValue) {
					// 加亮匹配的节点
					var $matchedAs = $('li > span[link] a:contains("' + filterValue + '")', gdiv);
					highlight($matchedAs);
					// 显示匹配的节点并隐藏没匹配的节点
					var $matchedLis = $matchedAs.parent();
					$('li', gdiv).not($matchedLis).hide();
					$matchedLis.show();
					
					// 展开父节点
					var $parents = $matchedLis.parents('li');
					$parents.show();
					tinytree.closeAll();
					justOpenNodes(getNodeIds($parents));
					
					// 显示过滤后的节点的后代
					if (settings.showChildren) {
						justOpenNodes(getNodeIds($matchedLis));
						$('li', $matchedLis).show();
					}
				} else { // 过滤条件为空时
					// 取消加亮, 并显示所有节点
					unhighlight();
					$('li', gdiv).show();	
				}	
				return tinytree;			
			},
			
			/**
			 * 定位, 加亮指定的节点
			 */
			position : function(nodeId) {
				var $matched = $('#' + generateNodeId(nodeId) + ' > span[link] a', gdiv);
				justOpenNodes(getNodeIds($matched.parents('li')));
				highlight($matched);
				return tinytree;
			},
			
			/**
			 * 单击指定的节点
			 */
			click : function(nodeId) {
				tinytree.position(nodeId);
				var $matched = $('#' + generateNodeId(nodeId) + ' > span[link] a', gdiv);
				$matched.click();
				return tinytree;
			},
			
			/**
			 * 重新加载指定的节点的下级节点
			 */
			reload : function(nodeId) {
				if (nodeId) {
					delete hasLoadedChildrenCache[$.inArray(nodeId, hasLoadedChildrenCache)];
					var nodeElemId = generateNodeId(nodeId);
					$('#' + nodeElemId + ' ul', gdiv).remove();
					tinytree.buildLowerTree(nodeId);
					changeToCollapsableClass(nodeId);
				} else {
					hasLoadedChildrenCache = [];
					$('ul', gdiv).remove();
					tinytree.buildLowerTree(settings.root);
				}
				return tinytree;
			},
			
			showAllHiddenNodes : function() {
				$('ul li[hide]', gdiv).show();
				return tinytree;
			},
			
			hideAllHiddenNodes : function() {
				$('ul li[hide]', gdiv).hide();
				return tinytree;
			},
			
			getFirstNode : function() {
				var $firstHref = $('ul li a:first', gdiv);
				return {
					nodeId : $firstHref.attr('nodeId'),
					nodeName : $firstHref.text()
				};
			},
			
			isLeaf : function(nodeId) {
				return $('#' + generateNodeId(nodeId), gdiv).attr('leaf') == 'true';
			}
		};
		
		//-------------------------------------------------
		if (settings.autoload) {
			tinytree.buildLowerTree(settings.root);
		}
		//make tinytree functions accessible
		container.tinytree = tinytree;
		container.settings = settings;
		
		//--------------------private-----------------------
		/**
		 * Node节点
		 */
		function Node(nodeId, nodeName, parentId, url, checked, disabled, leaf, hide) {
			this.nodeId = nodeId;
			this.nodeName = nodeName;
			this.parentId = parentId;
			this.url = url || false;
			this.checked = checked || false;
			this.disabled = disabled || false;
			this.leaf = leaf || false;
			this.hide = hide || false;
		}
		
		function createTipDivHtml(parentId) {
			return '<div id="' + tipDivIdPrefix + parentId 
						+ '" class="tip" style="display: none;">'
						+ settings.loadingMessage + '</div>';
		};
		
		/**
		 * 创建筛选过滤区域
		 */
		function createFilterDivHtml() {
			var html = '<div>';
			html += '<input type="text" id="_tinytree_text_filter_" class="filterText" value="筛选..."'
					+ ' title="输入条件过滤树节点, 按<回车>进行筛选" onfocus="this.select();"/>';
    		html += '</div>';
    		return html;
		};
		function bindFilterEvent() {
			$('#_tinytree_text_filter_', gdiv).keyup(function(e) {
				var value = $(this).val();
				if (e.keyCode == 13 || value == '') {
					tinytree.doFilter(value);
				}
			});	
		};
		
		function showTipMessage(parentId) {
			$('#' + tipDivIdPrefix + parentId).show();
		};
		
		function hideTipMessage(parentId) {
			$('#' + tipDivIdPrefix + parentId).hide();
		};
		
		function parseXmlNodes(parentId, xmlNode) {
			var $nodes = $('tree > node', xmlNode);
			var html = parseNodeRecursively(parentId, $nodes, false, true);
			appendHtmlTo(parentId, html);
		};
		
		/**
		 * 解析JSON格式的数据
		 */
		function parseJsonNodes(parentId, nodes) {
			var html = parseNodeRecursively(parentId, nodes, false, false);
			appendHtmlTo(parentId, html);
		};
		
		function parseNodeRecursively(parentId, nodes, hide, xmlFormat) {
			var len = nodes.length;
			if (len == 0) {
				return '';
			}
			if ($.inArray(parentId, hasLoadedChildrenCache) == -1) {
				hasLoadedChildrenCache.push(parentId + '');
			}
			var html = '<ul id="' + generateFolderId(parentId) + '"'
					+ (hide ? ' style="display:none;"' : '') + '>';
			for (var i = 0; i < len; i++) {
				var node = nodes[i];
				var nodeObj = xmlFormat ? parseNodeObjInfoFromXml(parentId, node) 
										: parseNodeObjInfoFromJson(parentId, node);
				if (nodeObj.leaf) {
					html += createLeafNodeHtml(nodeObj, (i == (len - 1)));
				} else {
					var children = xmlFormat ? $('> children > node', node)
											 : (node.children || []);
					html += createCompositeNodeHtml(nodeObj, (i == (len - 1)), children, xmlFormat);
				}
			}
			html += '</ul>';
			return html;
		};
		function parseNodeObjInfoFromXml(parentId, node) {
			return new Node($('> id', node).text(), 
							$('> name', node).text(), 
							parentId,
							$('> url', node).text(), 
							$('> checked', node).text() == 'true',
							$('> disabled', node).text() == 'true',
							$('leaf', node).text() == 'true',
							$('hide', node).text() == 'true');	
		};
		function parseNodeObjInfoFromJson(parentId, node) {
			return new Node(node.id, node.name, parentId, node.url, 
							node.checked, node.disabled, node.leaf, node.hide);	
		};
		
		function appendHtmlTo(parentId, html) {
			getNode(parentId).append(html);
			var $folderNode = getFolderNode(parentId);
			addHitAreaEvent($folderNode);
			radioAndCheckboxEvent($folderNode);
			setHyperlinkClickEvent($folderNode);
		};
		
		function generateFolderId(parentId) {
			return "_tinytree_folder_" + parentId;
		}
		function getFolderNode(parentId) {
			return $('#' + generateFolderId(parentId));	
		}
		
		function generateNodeId(nodeId) {
			return nodeIdPrefix + nodeId;
		}
		function getNode(nodeId) {
			return $('#' + generateNodeId(nodeId))	
		}
		
		function createLeafNodeHtml(node, last) {
			return '<li id="' + generateNodeId(node.nodeId) + '"  leaf="true"' 
					+ (last ? ' class="' + classNames.last + '"' : '') 
					+ (node.hide ? ' hide="true" style="display:none;"' : '') + '>'
					+ getRadioOrCheckboxHtml(node, true)
					+ '<span link="true" class="' + classNames.file + ' '
						+ (settings.specifyLeafClass ? settings.specifyLeafClass(node) : '') 
					+ '">'
					+ createHyperlinkHtml(node, true) + '</span>'
					+ (node.hide ? ('<span class="' + settings.hiddenNodeClass + '" title="失效">&nbsp;&nbsp;</span>') : '')
					+ '</li>';
		};
		
		function createCompositeNodeHtml(node, last, children, xmlFormat) {
			var nodeId = node.nodeId;
			var nodeName = node.nodeName;
			var staticLoad = children.length > 0;
			return '<li id="' + generateNodeId(nodeId) + '" composite="true" class="' 
					+ (last ? classNames.lastExpandable : classNames.expandable) + '"'
					+ (node.hide ? ' hide="true" style="display:none;"' : '') + '>'
					+ '<div class="' + classNames.hitarea +'" nodeId="' + nodeId + '"></div>'
					+ getRadioOrCheckboxHtml(node, false)
					+ '<span link="true" class="' + classNames.closedfolder + '">'
					+ createHyperlinkHtml(node, false) 
					+ '</span>'
					+ (node.hide ? ('<span class="' + settings.hiddenNodeClass + '" title="失效">&nbsp;&nbsp;</span>') : '')
					+ parseNodeRecursively(nodeId, children, true, xmlFormat)
					+ '</li>' 
					+ (staticLoad ? '' : createTipDivHtml(nodeId));
		};
		
		function addHitAreaEvent(context) {
			$('div.' + classNames.hitarea, context).click(function(e) {
				e.stopPropagation();
				var nodeId = $(this).attr('nodeId');
				openToggleLowerTree(nodeId);
			});	
		};
		
		function openToggleLowerTree(nodeId, oper) {
			if (settings.closeSameLevel) {
				var $node = getNode(nodeId);
				justCloseNodes(getNodeIds($node.siblings()));	
			}
			if ($.inArray(nodeId, hasLoadedChildrenCache) == -1) {
				tinytree.buildLowerTree(nodeId);
			}
			nodeShowToggle(nodeId, oper);
		}
		
		function nodeShowToggle(nodeId, oper) {
			toggleParentNodeClass(nodeId, oper);
			var $obj = getFolderNode(nodeId);
			if (oper==null || oper!='open') {
				$obj[ $obj.is(":hidden") ? "show" : "hide" ]();
			} else {
				$obj["show"]();
			}
		};
		
		function toggleParentNodeClass(nodeId, oper) {
			if(oper==null || oper!='open') {
				changeToCollapsableClass(nodeId) || changeToExpandableClass(nodeId);
			} else changeToCollapsableClass(nodeId);
		};
		/**
		 * When open children
		 * 
		 * @param {String}
		 *            nodeId
		 * @return true | false 是否转换成功？
		 */
		function changeToCollapsableClass(nodeId) {
			var $node = getNode(nodeId);
			if ($node.hasClass(classNames.lastExpandable)) {
				$('> span[link]', $node).removeClass(classNames.closedfolder).addClass(classNames.openfolder);
				$node.removeClass(classNames.lastExpandable).addClass(classNames.lastCollapsable);
				return true;
			} 
			if ($node.hasClass(classNames.expandable)) {
				$('> span[link]', $node).removeClass(classNames.closedfolder).addClass(classNames.openfolder);
				$node.removeClass(classNames.expandable).addClass(classNames.collapsable);
				return true;
			}
			return false;
		};
		/**
		 * When close children
		 * 
		 * @param {String}
		 *            nodeId
		 * @return true | false 是否转换成功？
		 */
		function changeToExpandableClass(nodeId) {
			var $node = getNode(nodeId);
			if ($node.hasClass(classNames.lastCollapsable)) {
				$('> span[link]', $node).removeClass(classNames.openfolder).addClass(classNames.closedfolder);
				$node.removeClass(classNames.lastCollapsable).addClass(classNames.lastExpandable);
				return true;
			} 
			if ($node.hasClass(classNames.collapsable)) {
				$('> span[link]', $node).removeClass(classNames.openfolder).addClass(classNames.closedfolder);
				$node.removeClass(classNames.collapsable).addClass(classNames.expandable);
				return true;
			}
			return false;
		};
		
		function justOpenNodes(nodeIds) {
			$.each(nodeIds, function(i, nodeId) {
				if (nodeId == settings.root) {
					return true;
				}
				getNode(nodeId).show();
				justToDoNode(nodeId, ':hidden', 'show');
			});
		};
		function justCloseNodes(nodeIds) {
			$.each(nodeIds, function(i, nodeId) {
				if (nodeId == settings.root) {
					return true;
				}
				justToDoNode(nodeId, ':visible', 'hide');
			});
		};
		function justToDoNode(nodeId, filter, operator) {
			var $obj = getFolderNode(nodeId);
			if ($obj && $obj.is(filter)) {
				toggleParentNodeClass(nodeId);
				$obj[operator]();
			}
		};
		
		function getRadioOrCheckboxHtml(node, isLeaf) {
			if (settings.radioButLeafCheckboxSupport) {
				return isLeaf 
						? createRadioOrCheckboxHtml("checkbox", node, isLeaf) 
						: createRadioOrCheckboxHtml("radio", node, isLeaf);
			}
			if (settings.radioSupport) {
				return createRadioOrCheckboxHtml("radio", node, isLeaf);
			}
			if (settings.checkboxSupport) {
				return createRadioOrCheckboxHtml("checkbox", node, isLeaf);
			}
			return '';
		}
		
		function createRadioOrCheckboxHtml(type, node, isLeaf) {
			if (settings.selectLeafOnly && !isLeaf) {
				return '';
			}
			return '<input type="' + type
					+'" name="' + settings.inputName + '"'
					+ (node.checked ? ' checked="checked"' : '')
					+ (node.disabled ? ' disabled="disabled"' : '')
					+ ' class="' + classNames.checkbox + '" value="'+ node.nodeId 
					+ '" id="checkboxId' + node.nodeId + '" treeNodeId="' 
					+ node.nodeId + '" treeNodeName="' + node.nodeName + '" '
					+ ' parentId="' + node.parentId + '"/>';
		};
		
		function radioAndCheckboxEvent(context) {
			$('li :checkbox', context).click(function(e) {
				e.stopPropagation();
				var $checkbox = $(this);
				checkChildrenOrParent($checkbox);
			});
			if (settings.onRadionClick) {
				$('li :radio', context).click(function(e) {
					e.stopPropagation();
					var $radio = $(this);
					var nodeId = $radio.attr('treeNodeId'), nodeName = $radio.attr('treeNodeName');
					settings.onRadionClick(this, nodeId, nodeName);
				});
			}
		};
		
		function checkChildrenOrParent($checkbox) {
			if (settings.alsoCheckChildren) {
				$children = $checkbox.parent().find(':checkbox');
				if ($checkbox.prop('checked')) {
					$children.prop('checked', true);
				} else {
					$children.prop('checked', false);
				}
			}
			if (settings.alsoCheckParent) {
				$parent = $checkbox.parents('li').find(':checkbox:first');
				if ($checkbox.prop('checked')) {
					$parent.prop('checked', true);
				}
			}
		}
		
		function createHyperlinkHtml(node, isLeaf) {
			var href = (settings.ignoreSpecificNodeUrl && node.url) || settings.nodeUrl;
			var hasUrl = href && !/^#*$/.test(href) && true;
			if (hasUrl && (href.indexOf('javascript:') > -1)) { // nodeUrl为javascript方法
				href = href + '(\'' + node.nodeId + '\',\'' + node.nodeName + '\');';
				return '<a href="javascript:void(0);" class="' 
						+ (isLeaf ? classNames._leafHref : classNames._compositeHref) + '"'
						+ ' onclick="' + href + ';return false;"'
						+ ' nodeId="' + node.nodeId + '"'
						+ '>' + node.nodeName + '</a>';
			}
			if (hasUrl) { // 普通的http href
				var existsInterrogation = href.indexOf('?') > -1;
				href = href + (existsInterrogation ? '&' : '?')
					 + 'nodeId=' + node.nodeId + '&nodeName=' + node.nodeName;
			} else {
				href = 'javascript:void(0);';
			}
			if (node.disabled) {
				href = 'javascript:void(0);';
				hasUrl=false;
			}
			return '<a href="' + href + '" class="' 
				+ (isLeaf ? classNames._leafHref : classNames._compositeHref) + '"'
				+ (hasUrl ? ' target="' + (node.target || settings.target) + '"' : '')
				+ ' nodeId="' + node.nodeId + '"'
				+ '>' + node.nodeName + '</a>';
		};
		function setHyperlinkClickEvent(context) {
			$('li span[link] a', context).click(function(e) {
				var $this = $(this);
				e.stopPropagation();
				highlight($this);
				var href = $this.attr('href');
				var inexistsClickEvent = !!$this.attr('onclick');
				var isComposite = $this.hasClass(classNames._compositeHref);
				if (/.*#$/.test(href) && inexistsClickEvent && isComposite) {
					var oper = (settings.nodeUrl.indexOf('javascript:') > -1 ? "open" : "");  //nodeurl以JavaScript开头的点解节点，只打开，不折起来。
					openToggleLowerTree($this.attr('nodeId'), oper);
				}
				if (settings.clickNodeToCheckRadioOrCheckbox) {
					var $radio = $(this).parent().parent().find(' > :radio');
					if ($radio.length > 0) {
						$radio.prop('checked', true);
					} else {
						var $checkbox = $(this).parent().parent().find(' > :checkbox');
						if ($checkbox.prop('checked')) {
							$checkbox.prop('checked', false);
						} else {
							$checkbox.prop('checked', true);
						}
						checkChildrenOrParent($checkbox);
					}
				}
				if (settings.onNodeClick) {
					settings.onNodeClick($this.attr('nodeId'), $this.text());
				}
			});
		};
		
		function highlight($these) {
			unhighlight();
			$these.addClass(classNames.selected);
		};
		function unhighlight() {
			$('.' + classNames.tinytree + ' li span[link] a').removeClass(classNames.selected);	
		};
		
		function getNodeIds($nodes) {
			var nodeIds = [];
			for (var i = 0, len = $nodes.length; i < len; i++) {
				nodeIds.push($('> span[link] a', $nodes[i]).attr('nodeId'));
			}
			return nodeIds;
		};
	};

	// ----------------------------------------------------------------------------
	//-----------------------------扩展的插件方法----------------------------
	//-----------------------------------------------------------------------------
	/**
	 * 该插件的主方法 
	 * 
	 * @param {Object}
	 *            settings 配置信息
	 */
	$.fn.tinytree = function(settings) {
		return this.each(function() {
			$.buildTinyTree(this, (settings ? settings : {}));
		});
	};
	/**
	 * 静态的tree, 配置信息和节点信息
	 */
	$.fn.addStaticNodes = function(settings, nodes) { 
		settings = $.extend(settings || {}, {autoload:false, url:false});
		return this.each(function() {
			$.buildTinyTree(this, settings);
			this.tinytree.addNodes(this.settings.root, nodes);
		});
	}; 
	/**
	 * 静态增加节点到某节点
	 */
	//FIXME 还没有实现静态增加节点到某节点, 主要是节点类型的转变
	$.fn.addNodesTo = function(parentId, nodes) { 
		return this.each(function() {
			if (this.tinytree) {
				this.tinytree.addNodes(parentId, nodes);
			}
		});
	}; 
	/**
	 * function to update general settings
	 */
	$.fn.tinytreeSettings = function(settings) { 
		return this.each( function() {
			if (this.tinytree) {
				$.extend(this.settings, settings);
			}
		});
	}; //end tinyTreeSettings
})(jQuery);
