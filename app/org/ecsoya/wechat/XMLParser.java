package org.ecsoya.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParser {
	
	/**
	 *  解析请求的xml文档
	 *    
	 * @param req
	 * @return
	 */
	public static Map<String, String> parseXML(String xmlText){
		
		Map<String, String> map = new HashMap<String, String>();
		Document document = null; 
		try {
			document = DocumentHelper.parseText(xmlText);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(document == null) {
			return map;
		}
		
		Element root = document.getRootElement();
		
		if(root == null ){
			return map;
		}
		
		for(Iterator iter = root.elementIterator(); iter.hasNext();){
			Element element = (Element) iter.next();
			String key = element.getName();
			String value = element.getText();
			map.put(key, value);
		}
		
		return map;
	}
	
	
	/**
	 *  对象转换为xml格式的字符串  
	 *  
	 *  待完善
	 *  
	 * @param alias  别名
	 * @param obj   对象
	 * @return
	 */
	public static String toXML(String alias, Object obj){
		
		Class clazz = obj.getClass();
		Field[] fields = clazz.getFields(); // 包含父类的域
		
//		Field[] subFields = clazz.getDeclaredFields();
		
//		List<Field> fieldList = sort(subFields, fields);
		
		Document doc = DocumentHelper.createDocument();
		
		if(alias == null || alias == ""){
			alias = clazz.getName();
		}
		
		Element root = doc.addElement(alias);
		
		if(fields.length > 0){
			
			for(int i = 0; i < fields.length; i++){
				
				Field field = fields[i];
				
				String fieldString = field.getName();
				Class fieldClazz = field.getType();
				
				if(!isBaseType(fieldClazz.getName())){
					
					Object superObject = null;
					try {
						superObject = fieldClazz.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
					toXML(alias, superObject);
				}
				
				Object fieldValue = null;
				try {
					fieldValue = field.get(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(fieldClazz.getName().equals("java.lang.String")){
					addElement(root, fieldString, fieldValue.toString(), true);
				}else{
					addElement(root, fieldString, fieldValue.toString(), false);
				}
				
			}
			
		}
		 
		return asXML(doc);
	}
	
	protected static Element addElement(Element element, String ElementName, String ElementText, boolean hasCDATA){
		
		Element retElement = element.addElement(ElementName);
		
		if(hasCDATA){
			retElement.addCDATA(ElementText);
		}else{
			retElement.addText(ElementText);
		}
		
		
		return retElement;
		
	}
	
	
	protected static String asXML(Document doc) {
        try {
            StringWriter out = new StringWriter();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setNewLineAfterDeclaration(false);
            format.setSuppressDeclaration(true);
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(out, format);

            writer.write(doc);
            writer.flush();

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException while generating "
                    + "textual representation: " + e.getMessage());
        }
    }
	
	
	/**
	 *  父类域与子类域的排序，让父类的域在前
	 * @param subFields
	 * @param fields
	 * @return
	 */
	protected List<Field> sort(Field[] subFields, Field[] fields){
		
		List<Field> subFieldList = new ArrayList<Field>();
		List<Field> superFieldList = new ArrayList<Field>();
		
		
		int i = 0;
		
		for(Field subField : subFields){
			
			if(subField.getModifiers() == Member.PUBLIC){
				continue;
			}
			
			for(Field field : fields){
				
				if(field.equals(subField)){
					subFieldList.add(field);
					
				}else{
					superFieldList.add(field);
				}
				
			}
		}
		
		superFieldList.addAll(subFieldList);
		
		return 	superFieldList;
			
	}
	
	/**
	 *  
	 * @param name
	 * @return
	 */
	protected static boolean isBaseType(String name){
		
		String[] strArry = {"int", "long", "double", "java.lang.Integer", "java.lang.Long", "java.lang.Double", "java.lang.BigDecimal", "java.lang.String"};
		
		for(int i = 0; i < strArry.length; i++){
			
			String type = strArry[i];
			
			if(name.equals(type)){
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	
	
	protected String getSuperClassName(Object obj){
		
		Class clazz = obj.getClass();
		
		Class superClazz = clazz.getSuperclass();
		
		return superClazz.getName();
		
	}

	

	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
	
	
	public static String stream2Str(InputStream in) {		
		StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    try {
			for (int n; (n = in.read(b)) != -1;) {
			    out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = out.toString();
		return str;
	}
}
