//Author:Jokinryou
//Date:2013.06.04

var xmlHttp; // 用于保存XMLHttpRequest对象的全局变量
var targetSelId; // 用于保存要更新选项的列表ID
var selArray = new Array(); // 用于保存级联菜单ID的数组

// 用于创建XMLHttpRequest对象
function createXmlHttp() {
	if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

// 获取列表选项的调用函数
function buildSelect(selectedId, targetId, type) {
	if ("" == selectedId) { // selectedId为空串表示选中了默认项
		clearSubSel(targetId); // 清除目标列表及下级列表中的值
		return; // 直接结束调用，不必向服务器请求信息
	} else if ("请选择" == selectedId) {
		return;
	}
	
	targetSelId = targetId; // 将传入的目标列表ID赋值给targetSelId变量
	createXmlHttp(); // 创建XMLHttpRequest对象
	xmlHttp.onreadystatechange = buildSelectCallBack; // 当readystate值为4时调用回调函数
	if(type == '1') {
		xmlHttp.open("GET", "Admin_getDepartmentBySchool?selectedId="
				+ encodeURI(encodeURI(selectedId)), true);
	}else {
		xmlHttp.open("GET", "Admin_getMajorByDepartment?selectedId="
				+ encodeURI(encodeURI(selectedId)), true);
	}
	xmlHttp.send(null);
}

// 获取列表选项的回调函数
function buildSelectCallBack() {
	if (xmlHttp.readyState == 4) {
		if (xmlHttp.status == 200) {
			// var xmlDoc = xmlHttp.responseXML.documentElement;
			var res = xmlHttp.responseXML.getElementsByTagName("res");
			var targetSelNode = document.getElementById(targetSelId);

			clearSubSel(targetSelId);

			for ( var i = 0; i < res.length; i++) {
				// targetSelNode.appendChild(createOption(res[i].childNode[0].nodeValue,res[i].childNode[1].nodeValue);
				targetSelNode
						.appendChild(createOption(
								res[i].getElementsByTagName("id")[0].firstChild.nodeValue,
								res[i].getElementsByTagName("name")[0].firstChild.nodeValue));
				// targetSelNode.appendChild(createOption(res[i].firstChild.data,res[i].firstChild.data));
			}
		} else {
			alert("请求的页面有错误。");
		}
	}
}

// 根据传入的value和text创建选项
function createOption(value, text) {
	var opt = document.createElement("option"); // 创建一个option节点
	opt.setAttribute("value", value); // 设置value
	opt.appendChild(document.createTextNode(text));
	return opt;
}

// 清除传入的列表节点内所有选项
function clearOptions(selNode) {
	selNode.options.length = 0;
}

// 初始化列表数组，《Ajax经典案例开发大全》中该方法的代码是有误没有实现真正的初始化
function initSelArray(school, department, major) {
	selArray[0] = school;
	selArray[1] = department;
	selArray[2] = major;
}

// 清除下级子列表选项
function clearSubSel(targetId) {
	var len = selArray.length;
	for ( var i = 0; i < len; i++) {
		var j = 0;
		if (selArray[i] == targetId) {
			j = i;
			break;
		}
	}
	for (; j < len; j++) {
		clearOptions(document.getElementById(selArray[j]));
	}
}