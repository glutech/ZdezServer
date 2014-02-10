package cn.com.zdez.util;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */
public class ParseXmlService
{
	public HashMap<String, String> parseXml(InputStream inStream) throws Exception
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档通过文档构建器构建一个文档实例
		Document document = builder.parse(inStream);
		//获取XML文件根节点
		Element root = document.getDocumentElement();
		//获得所有子节点
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++)
		{
			//遍历子节点
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element childElement = (Element) childNode;
				//版本号
				if ("version".equals(childElement.getNodeName()))
				{
					hashMap.put("version",childElement.getFirstChild().getNodeValue());
				}
				//软件名称
				else if (("name".equals(childElement.getNodeName())))
				{
					hashMap.put("name",childElement.getFirstChild().getNodeValue());
				}
				//下载地址
				else if (("url".equals(childElement.getNodeName())))
				{
					hashMap.put("url",childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}

	public HashMap<String, HashMap<String, String>> parseCCDptXml(InputStream inStream) throws Exception
	{
		HashMap<String, HashMap<String, String>> hashMap = new HashMap<String, HashMap<String,String>>();
		
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档通过文档构建器构建一个文档实例
		Document document = builder.parse(inStream);
		
		// 得到文档名称为CCDepartment的元素的节点列表
		NodeList nodeList = document.getElementsByTagName("school");
		
		// 遍历该集合，显示集合中的元素及其子元素的名字
		for (int i=0; i<nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);

			String id = element.getAttribute("id");
			
			HashMap<String, String> tempHashmap = new HashMap<String, String>();
			
			String stuAffairsId = element.getElementsByTagName("stuAffairsId").item(0).getFirstChild().getNodeValue();
			tempHashmap.put("stuAffairsId", stuAffairsId);
			String employmentId = element.getElementsByTagName("employmentId").item(0).getFirstChild().getNodeValue();
			tempHashmap.put("employmentId", employmentId);
			String youthCorpsCommitteeId = element.getElementsByTagName("youthCorpsCommitteeId").item(0).getFirstChild().getNodeValue();
			tempHashmap.put("youthCorpsCommitteeId", youthCorpsCommitteeId);
			String securityDeptId = element.getElementsByTagName("securityDeptId").item(0).getFirstChild().getNodeValue();
			tempHashmap.put("securityDeptId", securityDeptId);
			
			hashMap.put(id, tempHashmap);
		}
		
		return hashMap;
	}
}
