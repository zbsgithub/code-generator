package org.keyia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.davidstudio.gbp.tool.util.DBTool;

import OS.Shell;

public class CodeGenMysql {
	private String PARAM_FILE_NAME = "gen.properties";
	private Properties properties = null;
	private String driver = "com.mysql.jdbc.Driver";
	/**
	 * 执行顺序，先执行codeGenMysql 
	 * 然后执行run ant
	 */
	
	// 数据库连接====》 要改动
	private String url = "jdbc:mysql://rm-2ze1696eo2l154j7v4o.mysql.rds.aliyuncs.com:3306/cctv?useUnicode=true&zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8";
	private String username = "cctvrds";
	private String password = "gz123456";
	
	private List<String> tableList = new LinkedList<String>();
	private String[] tableShowNames = null;
	private String[] tableComments = null;
	private String[] tableNames = null;
	private String packageValue = "com";
	private String modelValue = "x";
	private String authorValue = "z";
	private String jspSubFolder = null;
	private String configFileValue = "D:\\temp\\1.xml";
	private String cur_file = this.configFileValue;
	@SuppressWarnings("unused")
	private String antXmlFile = "D:\\新codeGen\\code-generator\\code-generator2\\build.xml";
	@SuppressWarnings("unused")
	private String destZip = "E:\\";
	@SuppressWarnings("unused")
	private String antTask = "generate";
	private String cmd = "cd /home/resinapp/zhjw/autoDeployTools/tmp/jckf;${AUTO_DEPLOY_HOME}libs/ant/bin/ant updateTrunk";

	// 数据库名称==》要改动
	static String schema = "cctv";//开发

	public static void main(String[] args) {
		main("label_group", "com.z2006", "core", "张兵帅",
				"D:\\新codeGen\\code-generator\\code-generator2\\table.xml",
				"D:\\新codeGen\\code-generator\\code-generator2\\build.xml",
				"gen.properties", "cmd /c ant generate", "mdzf/");
	}

	public static void main(String tableList/** 表名 列表 */
			, String packageValue/** 包名 */
			, String modelValue/** 模块 */
			, String authorValue/** 作者名 */
			, String configFileValue/** table配置文件 */
			, String antXmlFile/** ant-build配置文件 */
			, String PARAM_FILE_NAME/** gen 参数文件 */
			, String cmd/** 未知 */
			,String jspSubFolder/** 前台页面目录 */
	) {
		CodeGenMysql codeGen = new CodeGenMysql();
		codeGen.cmd = cmd;
		codeGen.jspSubFolder = jspSubFolder;
		codeGen.PARAM_FILE_NAME = PARAM_FILE_NAME;
		codeGen.packageValue = packageValue;
		codeGen.modelValue = modelValue;
		codeGen.authorValue = authorValue;
		codeGen.configFileValue = configFileValue;
		codeGen.cur_file = configFileValue;
		codeGen.antXmlFile = antXmlFile;
		/**
		 * 看是否有换行，有则替换成空
		 */
		tableList = tableList.replace("\r", "");
		/**
		 * 逗号分隔
		 */
		String[] array = tableList.split(",");
		for (int i = 0; i < array.length; i++) {
			codeGen.tableList.add(array[i]);
		}

		System.out.println("开始执行");
		codeGen.dealTable();
		codeGen.generCode();
		// ZipCompress t = new ZipCompress();
		// FileUtils.delete(new File(destZip));
		// t.zip(antXmlFile.replace("build.xml", "out"), "e:\\workspace");
		System.out.println("finish");
	}

	/**
	 * 判断是否是 V_表名 的表
	 */
	boolean isTwinceTable(String tableName) {
		if (tableName == null) {
			return false;
		}
		tableName = tableName.toUpperCase();
		if ((this.tables.containsKey(tableName))
				&& (this.tables.containsKey("V_" + tableName))) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * 功能描述：判断表的形式
	 *
	 */
	boolean isTwinceView(String tableName) {
		if (tableName == null) {
			return false;
		}
		tableName = tableName.toUpperCase();
		if ((tableName.startsWith("V_"))
				&& (this.tables.containsKey(tableName.substring(2)))) {
			return true;
		}
		return false;
	}

	private Map<Object, Object> tables = new HashMap<Object, Object>();

	/**
	 *
	 * 功能描述： 处理表格
	 *
	 *
	 * @author 张小平
	 *
	 * @since 2016年5月27日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void dealTable() {
		StringBuffer sb = new StringBuffer();
		/**
		 * 迭代表格list,整理成格式 '表名','表名'
		 */
		for (Iterator<String> iterator = this.tableList.iterator(); iterator
				.hasNext();) {
			String type = iterator.next();
			if (iterator.hasNext()) {
				sb.append("'" + type + "',");
			} else {
				sb.append("'" + type + "'");
			}
			this.tables.put(type.toUpperCase(), type.toUpperCase());
		}

		String tables = sb.toString();
		System.out.println("表格有：" + tables);

		try {
			/**
			 * 连接数据库
			 */
			DBTool.initDataSource(this.driver, this.url, this.username,
					this.password);

			String command = "select f.* from  ( SELECT t.TABLE_SCHEMA  as OWNER, t.TABLE_NAME as table_name, 'TABLE' as TABLE_TYPE,t.TABLE_COMMENT as comments FROM information_schema.tables as t  where t.TABLE_SCHEMA = '"
					+ schema + "' AND t.TABLE_NAME in (" + tables + ") ) f ";

			/** 查询表格属性 */
			List<Map<Object, Object>> all_tables = DBTool.executeQuery(command);
			System.out.println("查出的表格属性是：" + all_tables.toString());

			/** 表名 */
			this.tableNames = new String[all_tables.size()];
			/** 注释 */
			this.tableComments = new String[all_tables.size()];
			/** 表名+（注释） */
			this.tableShowNames = new String[all_tables.size()];
			int i = 0;
			for (Iterator<Map<Object, Object>> each = all_tables
					.iterator(); each.hasNext();) {
				Map<Object, Object> cols = each.next();

				this.tableNames[i] = (String) (cols.get("table_name"));
				this.tableComments[i] = (String) (cols.get("comments"));
				if (this.tableComments[i] != null) {
					this.tableShowNames[i] = (this.tableNames[i] + "   ("
							+ this.tableComments[i] + ")");
				} else {
					this.tableShowNames[i] = this.tableNames[i];
				}
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/** 建立 xml文档 */
		Document doc = new Document();
		doc.setRootElement(new Element("root"));
		StringWriter outStr = new StringWriter();
		try {
			for (int n = 0; n < this.tableShowNames.length; n++) {
				handleTable(doc.getRootElement(), this.tableNames[n],
						this.tableComments[n]);
			}
			XMLOutputter out = new XMLOutputter();
			out.output(doc, outStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("组成的xml是：" + outStr.toString());

		/**
		 * 新建xml文件 ，把组织的xml内容，写入xml文件中
		 */
		File file = new File(this.cur_file);
		FileWriter writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(this.cur_file, false);
			writer.write(outStr.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	public void generCode() {
		File file = new File(this.PARAM_FILE_NAME);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			InputStream in = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.properties.setProperty("package", this.packageValue);
		this.properties.setProperty("model", this.modelValue);
		this.properties.setProperty("author", this.authorValue);
		this.properties.setProperty("configFile", this.configFileValue);
		this.properties.setProperty("jspSubFolder", this.jspSubFolder);

		file = new File(this.PARAM_FILE_NAME);
		try {
			this.properties.store(new FileOutputStream(file), "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String s = null;
		StringBuffer echo = new StringBuffer();
		try {
			File workDir = new File(".");

			echo.append(Shell.exe(this.cmd));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(echo);
	}

	/**
	 *
	 * 功能描述：组织element对象 xml 中的表节点
	 *
	 * @param root
	 * @param tableName
	 * @param tableCnName
	 * @throws SQLException
	 *
	 */
	private void handleTable(Element root, String tableName, String tableCnName)
			throws SQLException {
		Element object = new Element("object");
		// object.setAttribute("name", tableName.toLowerCase());
		object.setAttribute("name", tableName);
		if (tableCnName != null) {
			/** 如果 包含 \n */
			if (tableCnName.indexOf('\n') != -1) {
				tableCnName = tableCnName.substring(0,
						tableCnName.indexOf('\n'));
			}
			object.setAttribute("cnName", tableCnName);
		} else {
			object.setAttribute("cnName", tableName);
		}
		object.setAttribute("numPercentPage", "20");
		object.setAttribute("dbtableName", tableName);
		object.setAttribute("isSearch", "0");
		object.setAttribute("editStyle", "1");
		object.setAttribute("cached", "0");

		// if (isTwinceTable(tableName)) {
		// object.setAttribute("twinceTable", "1");
		// object.setAttribute("twinceViewName",
		// ("V_" + tableName).toLowerCase());
		// object.setAttribute("twinceViewNameCaptial",
		// transFirstCharToUpperCase(("V_" + tableName).toLowerCase()));
		// }
		// if (isTwinceView(tableName)) {
		// object.setAttribute("twinceView", "1");
		// object.setAttribute("twinceTableName", tableName.substring(2)
		// .toLowerCase());
		// object.setAttribute("twinceTableNameCaptial",
		// transFirstCharToUpperCase(tableName.substring(2)
		// .toLowerCase()));
		// }
		/** 主键列 */
		List<Object> pkColumns = queryPkColumns(tableName);
		/** 表所有列 */
		List<Map<Object, Object>> columns = queryAllColumns(tableName);
		/** 遍历所有列 */
		for (Iterator<Map<Object, Object>> each = columns.iterator(); each
				.hasNext();) {
			Map<Object, Object> column = each.next();
			String col_name = (String) column.get("name");// 列名称
			String col_cn = null;// 列注释
			BigDecimal col_len = null;// 列长度
			if (column.get("cnname") != null) {
				col_cn = (String) column.get("cnname");
			}

			System.out.println("表：" + tableName + ",列" + col_name
					+ ",属性cnname(注释)： " + (col_cn == null ? "" : col_cn));

			String col_type = (String) column.get("type");// 列类型

			System.out.println("表：" + tableName + ",列" + col_name
					+ ",属性type(类型)： " + col_type);

			col_type = col_type.toUpperCase();
			if ("DATETIME".equals(col_type)) {
				col_type = "TIMESTAMP";
			} else if ("INT".equals(col_type)) {
				col_type = "INTEGER";
			}

			if (column.get("data_length") != null) {
				col_len = BigDecimal.valueOf(
						((BigInteger) column.get("data_length")).doubleValue());
			} else {
				col_len = BigDecimal.valueOf(0);
			}

			System.out.println("表：" + tableName + ",列" + col_name
					+ ",属性data_length(长度)： " + col_len);

			String col_scale = "0";
			if (column.get("scale") != null) {
				col_scale = column.get("scale").toString();
			}

			System.out.println("表：" + tableName + ",列" + col_name
					+ ",属性scale(精度)： " + col_scale);

			String nullable = (String) column.get("nullable");// 是否可为空

			System.out.println("表：" + tableName + ",列" + col_name
					+ ",属性nullable(是否可为空)： " + nullable);

			boolean isPk = false; // 是否为主键PK
			if (pkColumns.contains(col_name)) {
				isPk = true;
			}

			System.out.println(
					"表：" + tableName + ",列" + col_name + ",是否为主键： " + isPk);
			/** object——property 节点 */
			Element prop = new Element("property");
			prop.setAttribute("name", col_name.toLowerCase());
			prop.setAttribute("jdbcType", col_type.toUpperCase());
			prop.setAttribute("isPK", String.valueOf(isPk));

			/** object——property——cnName 节点 */
			if (col_cn != null) {
				prop.addContent(
						new Element("cnName").setAttribute("value", col_cn));
			} else {
				prop.addContent(new Element("cnName").setAttribute("value",
						col_name.toLowerCase()));
			}

			/** object——property——isList 节点 */
			if ((col_type.matches("CHAR")) && (col_len.intValue() > 100)) {
				prop.addContent(
						new Element("isList").setAttribute("value", "0"));
			} else {
				prop.addContent(
						new Element("isList").setAttribute("value", "1"));
			}

			/** object——property——listWidth 节点 */
			prop.addContent(new Element("listWidth").setAttribute("value", ""));

			/** object——property——isSearch 节点 */
			prop.addContent(new Element("isSearch").setAttribute("value", "0"));
			if (nullable.equalsIgnoreCase("Y")) {
				prop.addContent(
						new Element("isNull").setAttribute("value", "1"));
			} else {
				prop.addContent(
						new Element("isNull").setAttribute("value", "0"));
			}

			/** object——property——isRepeat 节点 */
			prop.addContent(new Element("isRepeat").setAttribute("value", "1"));

			/** object——property——dbcolumnTable 节点 */
			prop.addContent(new Element("dbcolumnTable").setAttribute("value",
					col_name));

			/** object——property——displayType 节点 */
			Element showProp = new Element("displayType");
			if (col_type.matches("DATE")) {
				showProp.setAttribute("value", "date");
			} else {
				showProp.setAttribute("value", "text");
			}
			showProp.setAttribute("readOnly", "0");
			showProp.setAttribute("length", "");
			showProp.setAttribute("defaultValue", "");
			prop.addContent(showProp);

			/** object——property——proType 节点 */
			Element innerProp = new Element("proType");
			innerProp.setAttribute("length", "0");
			innerProp.setAttribute("pointLength", "0");
			innerProp.setAttribute("style", "");
			if (isPk) {
				innerProp.setAttribute("value", "id");
			}
			if (col_type.indexOf("CHAR") > -1) {
				innerProp.setAttribute("length",
						String.valueOf(col_len.intValue()));

			} else if ((col_type.indexOf("INT") > -1)) {
				innerProp.setAttribute("value", "integer");

			} else if ((col_type.indexOf("NUMBER") > -1)

					|| (col_type.indexOf("DOUBLE") > -1)) {
				innerProp.setAttribute("value", "double");
				innerProp.setAttribute("pointLength", col_scale.toString());
			} else if ((col_type.indexOf("DECIMAL") > -1)) {
				innerProp.setAttribute("value", "decimal");
				innerProp.setAttribute("pointLength", col_scale.toString());
			} else if ((col_type.indexOf("FLOAT") > -1)) {
				innerProp.setAttribute("value", "float");
				innerProp.setAttribute("pointLength", col_scale.toString());
			} else if ((col_type.indexOf("DATE") > -1)
					|| (col_type.indexOf("TIME") > -1)) {
				innerProp.setAttribute("value", "date");
				innerProp.setAttribute("style", "yyyy-MM-dd");
			} else if (col_type.matches("BOOLEAN") || col_type.matches("BIT")) {
				innerProp.setAttribute("value", "boolean");
			} else {
				innerProp.setAttribute("value", "string");
				innerProp.setAttribute("length", col_len.toString());
			}
			prop.addContent(innerProp);

			object.addContent(prop);
		}

		root.addContent(object);
	}

	@SuppressWarnings("unused")
	private String transFirstCharToUpperCase(String str) {
		if ((str == null) || (str.length() == 0)) {
			return "";
		}

		char first = str.charAt(0);
		if (!Character.isUpperCase(first)) {
			return Character.toUpperCase(first) + str.substring(1);
		}
		return str;
	}

	@SuppressWarnings("unused")
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
	 * 查询主键PK列
	 */
	private List<Object> queryPkColumns(String table) throws SQLException {
		List<Object> result = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append(
				"select f.* from  (  SELECT k.COLUMN_NAME FROM information_schema.table_constraints t LEFT JOIN information_schema.key_column_usage k USING(constraint_name,table_schema,table_name) "
						+ "WHERE t.constraint_type='PRIMARY KEY' AND t.table_schema='"
						+ schema + "' AND t.table_name='" + table + "' ) f");
		/** 查询主键列名称 */
		List<Map<Object, Object>> all = DBTool.executeQuery(sql.toString());

		Iterator<Map<Object, Object>> each = all.iterator();
		while (each.hasNext()) {
			result.add(each.next().get("column_name"));
		}

		System.out.println("表：" + table + ",主键PK列名称：" + result);
		return result;
	}

	/**
	 * 查询所有列
	 */
	private List<Map<Object, Object>> queryAllColumns(String table)
			throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append(
				" select f.* from  (  SELECT t.ORDINAL_POSITION as ID, t.COLUMN_NAME as NAME, left(t.IS_NULLABLE,1) as nullable, t.DATA_TYPE as 'TYPE', t.CHARACTER_MAXIMUM_LENGTH as DATA_LENGTH ");
		sql.append(
				" ,t.NUMERIC_SCALE as SCALE, t.COLUMN_COMMENT as CNNAME FROM INFORMATION_SCHEMA.COLUMNS as t ");
		sql.append("WHERE table_name = '" + table + "' and t.TABLE_SCHEMA = '"
				+ schema + "' ) f");

		return DBTool.executeQuery(sql.toString());
	}
}
