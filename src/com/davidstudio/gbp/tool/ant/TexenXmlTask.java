package com.davidstudio.gbp.tool.ant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.anakia.AnakiaJDOMFactory;
import org.apache.velocity.anakia.XPathTool;
import org.apache.velocity.context.Context;
import org.apache.velocity.texen.ant.TexenTask;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.keyia.util.CamelCaseUtils;

public class TexenXmlTask extends TexenTask {
	// 生成的table.xml
	private String configFile;
	// 包名 com.iejiaoyu
	private String packageName;
	// 模块名 比如：com.iejiaoyu.**模块
	private String moduleName;
	// 作者名
	private String authorName;
	// 生成的页面放置目录
	private String jspSubFolder;

	public String getConfigFile() {
		return this.configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String projectName) {
		this.packageName = projectName;
	}

	public String getJspSubFolder() {
		return this.jspSubFolder;
	}

	public void setJspSubFolder(String jspSubFolder) {
		this.jspSubFolder = jspSubFolder;
	}

	public TexenXmlTask() {
		System.out.println("第一步 ：进入TaxenXmlTask构造器");
		setInputEncoding("UTF8");
		setOutputEncoding("UTF8");
		setOutputFile("log.txt"); // 日志输出
	}

	@Override
	public Context initControlContext() throws Exception {
		System.out.println("第二步 ：进入initControlContext");
		Context context = new VelocityContext();

		SAXBuilder builder = new SAXBuilder();
		builder.setFactory(new AnakiaJDOMFactory());

		try {
			File file = new File(getConfigFile());

			Document root = builder.build(file);

			Element objEle = root.getRootElement();

			transXMLFileLetterCase(objEle);

			context.put("root", objEle);
			context.put("xpath", new XPathTool());

			context.put("math", new MathTool());

			context.put("esc", new EscapeTool());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException("配置xml文件没有指定或指定的xml文件不存在！ ", e);
		}
		return context;
	}

	/**
	 * 功能描述： 解析table.xml
	 */
	@SuppressWarnings("unchecked")
	private void transXMLFileLetterCase(Element root) {
		System.out.println("第三步 ：进入transXMLFileLetterCase方法");
		List<Object> list = root.getChildren("object");
		if (list == null) {
			return;
		}

		Element ele = null;

		String packageName = getPackageName();
		root.setAttribute("package", packageName);
		String sysName = packageName.substring(
				packageName.lastIndexOf(".") + 1, packageName.length());
		root.setAttribute("sysName", sysName);

		String directory = packageName.replace(".", "/");
		root.setAttribute("directory", directory);

		String moduleName = getModuleName();
		root.setAttribute("module", moduleName);

		String authorName = getAuthorName();
		root.setAttribute("author", authorName);

		String jspSubFolder = getJspSubFolder();
		root.setAttribute("jspSubFolder", jspSubFolder);

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
		String currentDate = fmt.format(new Date());
		root.setAttribute("currentDate", currentDate);

		for (int i = 0; i < list.size(); i++) {

			String json = "";
			String fields = "";
			String fieldsValues = "";
			String insertSelective = "";
			String insertSelectiveValues = "";
			String update = "";
			String updateSelective = "";
			String results = "";
			String where = "";

			ele = (Element) list.get(i);
			String objName = ele.getAttributeValue("name"); // 表name
			String upperName = transFirstCharToUpperCase(objName);

			ele.setAttribute("lowercaseName",
					transFirstCharToLowerCase(objName));// 表名： 首字母为小写 如 user
			ele.setAttribute("firstUpperCase",
					convertOutputTableName(upperName));// 表名：换为首字母大写 如 User
			// 表名：第一个小写 userName
			ele.setAttribute(
					"firstLowerCase",
					transFirstCharToLowerCase(convertOutputTableName(upperName)));
			// 去掉 表名 中的 _
			ele.setAttribute("moudleName", convertOutputMoudleName(objName));
			// 去掉 表名 中的 _
			ele.setAttribute("urlName", convertOutputUrlName(objName));

			List<Object> proList = ele.getChildren("property");

			json += objName + ":{";

			if (proList != null) {
				for (int k = 0; k < proList.size(); k++) {
					Element elePro = (Element) proList.get(k);
					String eleProName = elePro.getAttributeValue("name");// 读取的列名称
					Element cnNameNode = elePro.getChild("cnName");
					// 列表 变为 首字母小写 ，如果中间有_ 的则为驼峰式 如：userName
					String proName = CamelCaseUtils.toCamelCase(elePro
							.getAttributeValue("name"));
					// 列注释
					String cnName = cnNameNode.getAttributeValue("value");
					// 类型
					String jdbcType = elePro.getAttributeValue("jdbcType");

					json += "\"" + proName + "\":" + "\"" + cnName + "\",";
					fields += eleProName + ",";
					fieldsValues += "#{" + proName + ",jdbcType=" + jdbcType
							+ "},";
					insertSelective += "<if test=\\\"" + proName
							+ " != null\\\" > " + eleProName + ", </if> ";
					insertSelectiveValues += " <if test=\\\"" + proName
							+ " != null\\\" > #{" + proName + ",jdbcType="
							+ jdbcType + "}, </if>";
					update += eleProName + "= #{" + proName + ",jdbcType="
							+ jdbcType + "},";
					updateSelective += "<if test=\\\"" + proName
							+ " != null\\\" > " + eleProName + " = #{"
							+ proName + ",jdbcType=" + jdbcType + "}, </if> ";
					results += "@Result(column = \"" + eleProName
							+ "\", property = \"" + proName
							+ "\" , jdbcType = JdbcType." + jdbcType.trim();
					where += "<if test=\\\"" + proName + " != null\\\" > and "
							+ eleProName + " = #{" + proName + ",jdbcType="
							+ jdbcType + "} </if>";

					if (objName.startsWith("view_")) {
						ele.setAttribute("isView", "true");
						String tableName = objName.replace("view_", "");
						ele.setAttribute("tableName", tableName);
						ele.setAttribute("tableFirstUpperCase",
								convertOutputTableName(tableName));
						ele.setAttribute(
								"tableFirstLowerCase",
								transFirstCharToLowerCase(convertOutputTableName(tableName)));
						if (k == 0) {
							ele.setAttribute("fieldId", eleProName);
							ele.setAttribute("propId", proName);
							ele.setAttribute("firstUpperPropId",
									transFirstCharToUpperCase(proName));
							ele.setAttribute("idJdbcType", jdbcType);
							results += " ,id = true ";
						}
					} else if (Boolean
							.valueOf(elePro.getAttributeValue("isPK"))) {
						ele.setAttribute("fieldId", eleProName);
						ele.setAttribute("propId", proName);
						ele.setAttribute("firstUpperPropId",
								transFirstCharToUpperCase(proName));
						ele.setAttribute("idJdbcType", jdbcType);
						results += " ,id = true ";
					}
					results += " ),";

					elePro.setAttribute("lowercaseName",
							transFirstCharToLowerCase(proName));
					elePro.setAttribute("firstUpperCase",
							transFirstCharToUpperCase(proName));
					elePro.setAttribute("firstLowerCase",
							transFirstCharToLowerCase(proName));
					String col = elePro.getChild("dbcolumnTable")
							.getAttributeValue("value");
					elePro.getChild("dbcolumnTable").setAttribute("value",
							col.toLowerCase());
				}
				json = json.substring(0, json.length() - 1);
				json += "}";
				fields = fields.substring(0, fields.length() - 1);
				fieldsValues = fieldsValues.substring(0,
						fieldsValues.length() - 1);
				update = update.substring(0, update.length() - 1);
				results = results.substring(0, results.length() - 1);

				System.out.println(json);
				ele.setAttribute("json", json);
				ele.setAttribute("fields", fields);
				ele.setAttribute("fieldsValues", fieldsValues);
				ele.setAttribute("insertSelective", insertSelective);
				ele.setAttribute("insertSelectiveValues", insertSelectiveValues);
				ele.setAttribute("update", update);
				ele.setAttribute("updateSelective", updateSelective);
				ele.setAttribute("results", results);
				ele.setAttribute("where", where);
			}

		}

	}

	/**
	 * 功能描述： 第一个字母 变成大写的
	 */
	private String transFirstCharToUpperCase(String str) {
		if ((str == null) || (str.length() == 0)) {
			return "";
		}
		System.out.println(str);
		char first = str.charAt(0);
		
		if(str.length()>1){
			System.out.println("长度："+str.length());
			char second = str.charAt(1);
			if (!Character.isUpperCase(first) && !Character.isUpperCase(second)) {
				return Character.toUpperCase(first) + str.substring(1);
			}
		}else {
			System.out.println(Character.toUpperCase(first)+ str.substring(1));
			return Character.toUpperCase(first)+ str.substring(1);
		}
		
		return str;
	}

	/**
	 * 功能描述：把首字母转换为小写的
	 */
	private String transFirstCharToLowerCase(String str) {
		if ((str == null) || (str.length() == 0)) {
			return "";
		}

		char first = str.charAt(0);
		if (!Character.isLowerCase(first)) {
			return Character.toLowerCase(first) + str.substring(1);
		}
		return str;
	}

	/**
	 * 功能描述：处理有 _ 的，处理为驼峰式
	 */
	public String convertOutputTableName(String tableName) {
		StringBuffer sb = new StringBuffer();
		String[] strs = tableName.split("_");
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i].substring(0, 1).toUpperCase()
					+ strs[i].substring(1).toLowerCase());

		}
		return sb.toString();
	}

	/**
	 * 功能描述： 把表中 _ 替换为 空
	 */
	public String convertOutputMoudleName(String tableName) {
		return tableName.replace("_", "");

	}

	/**
	 * 功能描述： 把表中 _ 替换为 空
	 */
	public String convertOutputUrlName(String tableName) {
		return tableName.replace("_", "/");

	}

	public static void main(String[] args) {
		System.out.println("进入main");
		System.out
				.println(new TexenXmlTask().convertOutputTableName("s_x_aac"));
	}
}
