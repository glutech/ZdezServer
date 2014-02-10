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
		
		// ʵ����һ���ĵ�����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// ͨ���ĵ�������������ȡһ���ĵ�������
		DocumentBuilder builder = factory.newDocumentBuilder();
		// ͨ���ĵ�ͨ���ĵ�����������һ���ĵ�ʵ��
		Document document = builder.parse(inStream);
		//��ȡXML�ļ����ڵ�
		Element root = document.getDocumentElement();
		//��������ӽڵ�
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++)
		{
			//�����ӽڵ�
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element childElement = (Element) childNode;
				//�汾��
				if ("version".equals(childElement.getNodeName()))
				{
					hashMap.put("version",childElement.getFirstChild().getNodeValue());
				}
				//�������
				else if (("name".equals(childElement.getNodeName())))
				{
					hashMap.put("name",childElement.getFirstChild().getNodeValue());
				}
				//���ص�ַ
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
		
		// ʵ����һ���ĵ�����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// ͨ���ĵ�������������ȡһ���ĵ�������
		DocumentBuilder builder = factory.newDocumentBuilder();
		// ͨ���ĵ�ͨ���ĵ�����������һ���ĵ�ʵ��
		Document document = builder.parse(inStream);
		
		// �õ��ĵ�����ΪCCDepartment��Ԫ�صĽڵ��б�
		NodeList nodeList = document.getElementsByTagName("school");
		
		// �����ü��ϣ���ʾ�����е�Ԫ�ؼ�����Ԫ�ص�����
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
