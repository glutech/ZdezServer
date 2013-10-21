//Author:Jokinryou
//Date:2013.06.04


var xmlHttp;					// 用于保存XMLHttpRequest对象的全局变量
var targetSelId;				// 用于保存要更新选项的列表ID
var selArray = new Array();		// 用于保存级联菜单ID的数组

// 用于创建XMLHttpRequest对象
function createXmlHttp() {
	if(window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	} else if(window.ActiveXObject){
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

// 获取列表选项的调用函数
function buildSelect(selectedId,targetId) {
	if("" == selectedId) {			// selectedId为空串表示选中了默认项
		clearSubSel(targetId);		// 清除目标列表及下级列表中的值
		return;						// 直接结束调用，不必向服务器请求信息
	}
	else if("请选择内容" == selectedId) {
		 return;
	}
	
	targetSelId = targetId;			// 将传入的目标列表ID赋值给targetSelId变量
	createXmlHttp();				// 创建XMLHttpRequest对象
	xmlHttp.open("GET", "School_getMajorListBySelectedDepartments?selectedId="+ encodeURI(encodeURI(selectedId)), true);
	xmlHttp.onreadystatechange = buildSelectCallBack;		// 当readystate值为4时调用回调函数
	xmlHttp.send(null);
}

// 获取列表选项的回调函数
function buildSelectCallBack() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
			var res = xmlHttp.responseXML.getElementsByTagName("res");
			var targetSelNode = document.getElementById(targetSelId);
		
			clearSubSel(targetSelId);
			
			for (var i=0; i<res.length; i++) {
				// list.add(new
				// Option(res[i].firstChild.data,res[i].firstChild.data));
				targetSelNode.appendChild(createOption(res[i].firstChild.data,res[i].firstChild.data));
			}
			
	       // $("#major").multiselect('refresh');
		}else {
			alert("请求的页面有错误。");
		}
	}
}

// 根据传入的value和text创建选项
function createOption(value,text) {
	var opt = document.createElement("option");			// 创建一个option节点
	opt.setAttribute("value", value);					// 设置value
	opt.appendChild(document.createTextNode(text));	
	return opt;
}

// 清除传入的列表节点内所有选项
function clearOptions(selNode) {
	selNode.options.length = 0;
}

// 初始化列表数组，《Ajax经典案例开发大全》中该方法的代码是有误没有实现真正的初始化
function initSelArray(depart,major) {
	selArray[0] = depart;
	selArray[1] = major;
}

// 清除下级子列表选项
function clearSubSel(targetId) {
	var len = selArray.length;
	for(var i=0;i<len;i++) {
		var j = 0;
		if(selArray[i] == targetId) {
			j = i;
			break;
		}
	}
	for(; j<len; j++) {
		clearOptions(document.getElementById(selArray[j]));
	}	
}

function test() {
	alert("fuck!!");
}