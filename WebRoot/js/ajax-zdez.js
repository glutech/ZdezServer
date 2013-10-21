//Author:Jokinryou
//Date:2013.08.19


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
function buildSelect1(selectedId,targetId) {
	if("" == selectedId) {			// selectedId为空串表示选中了默认项
		clearSubSel(targetId);		// 清除目标列表及下级列表中的值
		return;						// 直接结束调用，不必向服务器请求信息
	}
	else if("请选择内容" == selectedId) {
		 return;
	}
	
	var obj = document.getElementById("schoolSys");
	var schoolSysId= "";
	for(i=0; i<obj.length; i++) {
		if(obj.options[i].selected){
			schoolSysId+=obj.options[i].value+",";
		}
	}
	if(schoolSysId == "") {
		schoolSysId = 0;
	}
	targetSelId = targetId;			// 将传入的目标列表ID赋值给targetSelId变量
	createXmlHttp();				// 创建XMLHttpRequest对象
	xmlHttp.open("GET", "Admin_getMajorListBySelectedDepartments?selectedId="+ encodeURI(encodeURI(selectedId))+"&schoolSysId=" + encodeURI(encodeURI(schoolSysId)), true);
	xmlHttp.onreadystatechange = buildSelectCallBack1;		// 当readystate值为4时调用回调函数
	xmlHttp.send(null);
}

function buildSelect2(selectedId,targetId) {
	if("" == selectedId) {			// selectedId为空串表示选中了默认项
		clearSubSel(targetId);		// 清除目标列表及下级列表中的值
		return;						// 直接结束调用，不必向服务器请求信息
	}
	else if("请选择内容" == selectedId) {
		 return;
	}
	
	targetSelId = targetId;			// 将传入的目标列表ID赋值给targetSelId变量
	createXmlHttp();				// 创建XMLHttpRequest对象
	xmlHttp.open("GET", "Admin_getSchoolSysBySchool?selectedId="+ encodeURI(encodeURI(selectedId)), true);
	xmlHttp.onreadystatechange = buildSelectCallBack2;		// 当readystate值为4时调用回调函数
	xmlHttp.send(null);
}

function buildSelect3(selectedId,targetId) {
	if("" == selectedId) {			// selectedId为空串表示选中了默认项
		clearSubSel(targetId);		// 清除目标列表及下级列表中的值
		return;						// 直接结束调用，不必向服务器请求信息
	}
	else if("请选择内容" == selectedId) {
		 return;
	}
	
	targetSelId = targetId;			// 将传入的目标列表ID赋值给targetSelId变量
	createXmlHttp();				// 创建XMLHttpRequest对象
	xmlHttp.open("GET", "Admin_getDepartmentBySchool?selectedId="+ encodeURI(encodeURI(selectedId)), true);
	xmlHttp.onreadystatechange = buildSelectCallBack3;		// 当readystate值为4时调用回调函数
	xmlHttp.send(null);
}

// 获取列表选项的回调函数
function buildSelectCallBack1() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
//			var xmlDoc = xmlHttp.responseXML.documentElement;
			var res = xmlHttp.responseXML.getElementsByTagName("res");
			var targetSelNode = document.getElementById(targetSelId);
		
			clearSubSel(targetSelId);
			
			for (var i=0; i<res.length; i++) {
//				targetSelNode.appendChild(createOption(res[i].childNode[0].nodeValue,res[i].childNode[1].nodeValue);
				targetSelNode.appendChild(createOption(res[i].getElementsByTagName("id")[0].firstChild.nodeValue,res[i].getElementsByTagName("name")[0].firstChild.nodeValue));
//				targetSelNode.appendChild(createOption(res[i].firstChild.data,res[i].firstChild.data));
			}
			
	       $("#major").multiselect('refresh');
		}else {
			alert("请求的页面有错误1。");
		}
	}
}

function buildSelectCallBack2() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
//			var xmlDoc = xmlHttp.responseXML.documentElement;
			var res = xmlHttp.responseXML.getElementsByTagName("res");
			var targetSelNode = document.getElementById(targetSelId);
		
			clearSubSel(targetSelId);
			
			for (var i=0; i<res.length; i++) {
//				targetSelNode.appendChild(createOption(res[i].childNode[0].nodeValue,res[i].childNode[1].nodeValue);
				targetSelNode.appendChild(createOption(res[i].getElementsByTagName("id")[0].firstChild.nodeValue,res[i].getElementsByTagName("name")[0].firstChild.nodeValue));
//				targetSelNode.appendChild(createOption(res[i].firstChild.data,res[i].firstChild.data));
			}
			
	       $("#schoolSys").multiselect('refresh');
		}else {
			alert("请求的页面有错误2。");
		}
	}
}

function buildSelectCallBack3() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
//			var xmlDoc = xmlHttp.responseXML.documentElement;
			var res = xmlHttp.responseXML.getElementsByTagName("res");
			var targetSelNode = document.getElementById(targetSelId);
		
			clearSubSel(targetSelId);
			
			for (var i=0; i<res.length; i++) {
//				targetSelNode.appendChild(createOption(res[i].childNode[0].nodeValue,res[i].childNode[1].nodeValue);
				targetSelNode.appendChild(createOption(res[i].getElementsByTagName("id")[0].firstChild.nodeValue,res[i].getElementsByTagName("name")[0].firstChild.nodeValue));
//				targetSelNode.appendChild(createOption(res[i].firstChild.data,res[i].firstChild.data));
			}
			
	       $("#depart").multiselect('refresh');
		}else {
			alert("请求的页面有错误3。");
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
function initSelArray(school,schoolSys,depart,major) {
	selArray[0] = school;
	selArray[1] = schoolSys;
	selArray[2] = depart;
	selArray[3] = major;
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