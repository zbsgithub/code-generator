package org.keyia.util;

import OS.Shell;

import com.davidstudio.gbp.tool.util.DBTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.util.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class CodeGenPostgreSQL {
	private String PARAM_FILE_NAME = "gen.properties";
	private Properties properties = null;
	private String url = "jdbc:postgresql://166.111.4.89:5432/kfzhjw";
	private String driver = "org.postgresql.Driver";
	private String owner = "kwzhjw";
	private String username = "zhjw";
	private String password = "zhjw_123";
	private List tableList = new LinkedList();
	private String[] tableShowNames = null;
	private String[] tableComments = null;
	private String[] tableNames = null;
	private String packageValue = "com";
	private String modelValue = "x";
	private String authorValue = "z";
	private String jspSubFolder = null;
	private String configFileValue = "D:/temp/1.xml";
	private String cur_file = this.configFileValue;
	private String antXmlFile = "I:/project/svn/svnChinaCodeGenerator/easyuiCodeGenerator/build.xml";
	private String destZip = "I:/project/svn/svnChinaCodeGenerator/easyuiCodeGenerator/code.zip";
	private String antTask = "generate";
	private String cmd = "cd /home/resinapp/zhjw/autoDeployTools/tmp/jckf;${AUTO_DEPLOY_HOME}libs/ant/bin/ant updateTrunk";

	static String schema = "public";

	public static void main(String[] args) {
		main("v_cj_xsfawcqk",// 如果同时生成多表,使用'\n分隔各表'
				"org.thcic.zhjw", // 包名
				"cj", // 模块包名
				"admin", // 操作人
				"D:/Users/01/workspace/codeGenerator/table.xml",
				"D:/Users/01/workspace/codeGenerator/build.xml",
				"D:/THCIC/codeGen/code.zip", "gen.properties",
				"cmd /c ant generate", "iot");
	}

	public static void main(String tableList, String packageValue,
			String modelValue, String authorValue, String configFileValue,
			String antXmlFile, String destZip, String PARAM_FILE_NAME,
			String cmd, String jspSubFolder) {
		CodeGenPostgreSQL codeGen = new CodeGenPostgreSQL();
		codeGen.cmd = cmd;
		codeGen.jspSubFolder = jspSubFolder;
		codeGen.PARAM_FILE_NAME = PARAM_FILE_NAME;
		codeGen.packageValue = packageValue;
		codeGen.modelValue = modelValue;
		codeGen.authorValue = authorValue;
		codeGen.configFileValue = configFileValue;
		codeGen.cur_file = configFileValue;
		codeGen.antXmlFile = antXmlFile;
		tableList = tableList.replace("\r", "");
		String[] array = tableList.split("\n");
		for (int i = 0; i < array.length; i++) {
			codeGen.tableList.add(array[i].toLowerCase());// .toUpperCase()
															// removed
		}

		System.out.println("start");
		codeGen.dealTable();
		codeGen.generCode();
		ZipCompress t = new ZipCompress();
		FileUtils.delete(new File(destZip));
		System.out.println("finish");
	}

	/**
	 * 判断是否视图
	 * 
	 * @param tableName
	 * @return
	 */
	boolean isTwinceTable(String tableName) {
		if (tableName == null) {
			return false;
		}
		tableName = tableName.toLowerCase();// .toUpperCase() removed
		if ((this.tables.containsKey(tableName))
				&& (this.tables.containsKey("V_" + tableName))) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否视图
	 * 
	 * @param tableName
	 * @return
	 */
	boolean isTwinceView(String tableName) {
		if (tableName == null) {
			return false;
		}
		tableName = tableName.toLowerCase();// .toUpperCase() removed
		if ((tableName.startsWith("V_"))
				&& (this.tables.containsKey(tableName.substring(2)))) {
			return true;
		}
		return false;
	}

	private Map tables = new HashMap();

	public void dealTable() {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = this.tableList.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sb.append(",'" + type + "'");
			this.tables.put(type.toLowerCase(), type.toLowerCase());// .toUpperCase()
																	// removed
		}

		String tables = sb.toString().substring(1);
		System.out.println(tables);

		try {
			DBTool.initDataSource(this.driver, this.url, this.username,
					this.password);

			String command = "SELECT TABLE_SCHEMA as OWNER, TABLE_NAME, TABLE_TYPE "
					+ " FROM information_schema.tables where TABLE_SCHEMA = '"
					+ schema + "' AND TABLE_NAME in (" + tables + ")";
			System.out.println(command);

			List all_tables = DBTool.executeQuery(command);// 查询
															// {table_schema,table,table_type}

			this.tableNames = new String[all_tables.size()];
			this.tableComments = new String[all_tables.size()];
			this.tableShowNames = new String[all_tables.size()];
			int i = 0;
			for (Iterator each = all_tables.iterator(); each.hasNext();) {
				Map cols = (Map) each.next();

				this.tableNames[i] = ((String) cols.get("table_name"));// 将表名填入数组
				this.tableComments[i] = ((String) cols.get("comments"));
				if (this.tableComments[i] != null) {
					this.tableShowNames[i] = (this.tableNames[i] + "   ("
							+ this.tableComments[i] + ")");
				} else {
					this.tableShowNames[i] = this.tableNames[i];// 将表名填入显示名数组
				}
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Document doc = new Document();// 新建文档
		doc.setRootElement(new Element("root"));// <document><root></root></document>
		StringWriter outStr = new StringWriter();
		try {
			for (int n = 0; n < this.tableShowNames.length; n++) {
				handleTable(doc.getRootElement(), this.tableNames[n],
						this.tableComments[n]);
			}
			XMLOutputter out = new XMLOutputter();
			out.output(doc, outStr);// xml文档输出
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(outStr.toString());
		File file = new File(this.cur_file);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(this.cur_file, false);
			writer.write(outStr.toString());
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			try {
				writer.close();
			} catch (IOException localIOException) {
			}
		} finally {
			try {
				writer.close();
			} catch (IOException localIOException1) {
			}
		}
	}

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
			this.properties.store(new FileOutputStream(file), "");// 保存修改的gen.properties
		} catch (Exception e) {
			e.printStackTrace();
		}

		String s = null;
		StringBuffer echo = new StringBuffer();
		try {
			File workDir = new File(".");// 设置目录根路径

			echo.append(Shell.exe(this.cmd));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(echo);
	}

	/**
	 * 生成xml文档：元素，表名，表的comments
	 * 
	 * @param root
	 * @param tableName
	 * @param tableCnName
	 * @throws SQLException
	 */
	private void handleTable(Element root, String tableName, String tableCnName)
			throws SQLException {
		Element object = new Element("object");// <Object></Object>
		object.setAttribute("name", tableName.toLowerCase());// <Object
																// name="tableName"></Object>
		if (tableCnName == null) {
			object.setAttribute("cnName", object.getAttribute("name")
					.getValue());
		} else {
			if(tableCnName.indexOf('\n')!=-1){
				tableCnName = tableCnName.substring(0,tableCnName.indexOf('\n'));
			}
			object.setAttribute("cnName", tableCnName);// <Object
														// name="tableName"
														// cnName="tableCnName"></Object>
		}
		object.setAttribute("numPercentPage", "20");
		object.setAttribute("dbtableName", object.getAttribute("name")
				.getValue().toLowerCase());// .toUpperCase()
											// removed
		object.setAttribute("isSearch", "0");
		object.setAttribute("editStyle", "1");
		object.setAttribute("cached", "0");

		if (isTwinceTable(tableName)) {
			object.setAttribute("twinceTable", "1");
			object.setAttribute("twinceViewName",
					("V_" + tableName).toLowerCase());
			object.setAttribute("twinceViewNameCaptial",
					transFirstCharToUpperCase(("V_" + tableName).toLowerCase()));
		}
		if (isTwinceView(tableName)) {
			object.setAttribute("twinceView", "1");
			object.setAttribute("twinceTableName", tableName.substring(2)
					.toLowerCase());
			object.setAttribute("twinceTableNameCaptial",
					transFirstCharToUpperCase(tableName.substring(2)
							.toLowerCase()));
		}

		List pkColumns = queryPkColumns(tableName);

		List columns = queryAllColumns(tableName);

		for (Iterator each = columns.iterator(); each.hasNext();) {
			Map column = (Map) each.next();

			String col_name = (String) column.get("name");
			String col_cn = null;
			if (column.get("cnname") != null) {
				col_cn = (String) column.get("cnname");
			}
			String col_type = (String) column.get("type");
			Integer col_len = (Integer) column.get("data_length");
			if (col_len == null)
				col_len = 0;
			Integer col_scale = (Integer) column.get("scale");
			if (col_scale == null)
				col_scale = 0;
			String nullable = (String) column.get("nullable");
			boolean isPK = false;
			if (pkColumns.contains(col_name)) {
				isPK = true;
			}

			Element prop = new Element("property");
			prop.setAttribute("name", col_name.toLowerCase());
			prop.setAttribute("isPK", String.valueOf(isPK));
			if (col_cn != null) {
				prop.addContent(new Element("cnName").setAttribute("value",
						col_cn));
			} else {
				prop.addContent(new Element("cnName").setAttribute("value",
						col_name.toLowerCase()));
			}

			if ((col_type.matches("character varying"))
					&& (col_len.intValue() > 100)) {
				prop.addContent(new Element("isList")
						.setAttribute("value", "0"));
			} else {
				prop.addContent(new Element("isList")
						.setAttribute("value", "1"));
			}
			prop.addContent(new Element("listWidth").setAttribute("value", ""));
			prop.addContent(new Element("isSearch").setAttribute("value", "0"));
			if (nullable.equalsIgnoreCase("YES")) {
				prop.addContent(new Element("isNull")
						.setAttribute("value", "1"));
			} else {
				prop.addContent(new Element("isNull")
						.setAttribute("value", "0"));
			}

			prop.addContent(new Element("isRepeat").setAttribute("value", "1"));
			prop.addContent(new Element("dbcolumnTable").setAttribute("value",
					col_name));

			Element showProp = new Element("displayType");
			if (col_type.indexOf("date") > -1 || col_type.indexOf("time") > -1) {
				showProp.setAttribute("value", "date");
			} else {
				showProp.setAttribute("value", "text");
			}
			showProp.setAttribute("readOnly", "0");
			showProp.setAttribute("length", "");
			showProp.setAttribute("defaultValue", "");
			prop.addContent(showProp);

			Element innerProp = new Element("proType");
			innerProp.setAttribute("length", "0");
			innerProp.setAttribute("pointLength", "0");
			innerProp.setAttribute("style", "");
			if (isPK) {
				innerProp.setAttribute("value", "id");
			}
			if (col_type.indexOf("character varying") > -1) {
				innerProp.setAttribute("length",
						String.valueOf(col_len.intValue()));

			} else if (col_type.indexOf("double") > -1) {
				innerProp.setAttribute("value", "double");
			} else if (col_type.indexOf("bigint") > -1) {
				innerProp.setAttribute("value", "long");
			} else if ((col_type.indexOf("numeric") > -1)
					|| (col_type.indexOf("integer") > -1)) {
				innerProp.setAttribute("value", "number");
				innerProp.setAttribute("pointLength", col_scale.toString());
			} else if (col_type.indexOf("date") > -1) {
				innerProp.setAttribute("value", "date");
				innerProp.setAttribute("style", "yyyy-MM-dd");
			} else if (col_type.indexOf("time") > -1) {
				innerProp.setAttribute("value", "date");
				innerProp.setAttribute("style", "yyyy-MM-dd HH:mm:ss");
			} else if (col_type.matches("boolean")) {
				innerProp.setAttribute("value", "boolean");
			} else if (col_type.indexOf("bytea") > -1) {
				innerProp.setAttribute("value", "bytea");
			} else {
				innerProp.setAttribute("value", "string");
				innerProp.setAttribute("length", col_len.toString());
			}
			prop.addContent(innerProp);

			object.addContent(prop);
		}

		root.addContent(object);
	}

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

	private List queryPkColumns(String table) throws SQLException {
		List result = new ArrayList();
		StringBuffer sql = new StringBuffer();

		String table_name = table.toLowerCase();
		if (table_name.startsWith("v_")) {
			sql.append("SELECT column_name FROM information_schema.columns WHERE table_name='");
			sql.append(table_name);
			sql.append("'");
		} else {
			sql.append(" select pg_constraint.conname as pk_name,pg_attribute.attname as column_name,pg_type.typname as typename from ");
			sql.append(" pg_constraint inner join pg_class ");
			sql.append(" on pg_constraint.conrelid = pg_class.oid ");
			sql.append(" inner join pg_attribute on pg_attribute.attrelid = pg_class.oid ");

			sql.append(" and (pg_attribute.attnum = pg_constraint.conkey[1] or pg_attribute.attnum = pg_constraint.conkey[2] or pg_attribute.attnum = pg_constraint.conkey[3] or pg_attribute.attnum = pg_constraint.conkey[4])");
			sql.append(" inner join pg_type on pg_type.oid = pg_attribute.atttypid");
			sql.append(" where pg_class.relname = '");

			sql.append(table_name);
			sql.append("' ");
			sql.append(" and pg_constraint.contype='p'");
		}

		System.out.println(sql.toString());

		List all = DBTool.executeQuery(sql.toString());
		if ((all == null) || (all.size() == 0)) {
			// System.out.println("all is empty ");
			// all =
			// DBTool.executeQuery(" select 'PID' as COLUMN_NAME from dual ");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("column_name", "PID");
			all = new ArrayList<HashMap<String, Object>>(1);
			all.add(map);
			// System.out.println(" select 'PID' as COLUMN_NAME from dual " +
			// all.size());
		}
		Iterator each = all.iterator();
		while (each.hasNext()) {
			result.add(((Map) each.next()).get("column_name"));
		}

		System.out.println("c" + result);
		return result;
	}

	private List queryAllColumns(String table) throws SQLException {
		StringBuffer sql = new StringBuffer();

		table = table.toLowerCase();
		String table_name = table;
		if (table_name.startsWith("v_"))
			table_name = table_name.substring(2);

		sql.append("select ordinal_position as id, column_name as name, cols.is_nullable nullable,");
		sql.append(" data_type as type, character_maximum_length as data_length, numeric_scale as scale,");
		sql.append(" substr(description, 1, 30) as cnname ");
		sql.append(" FROM information_schema.columns cols LEFT JOIN");
		sql.append(" (select d.objsubid, a.attname, d.description");
		sql.append(" from pg_description d, pg_class c, pg_namespace n, pg_attribute a");
		sql.append(" where c.relnamespace = n.oid and d.objoid = c.oid");
		sql.append(" and c.oid = a.attrelid and d.objsubid = a.attnum");
		sql.append(" and n.nspname = '").append(schema.toLowerCase());
		sql.append("' and c.relname ='").append(table_name)
				.append("') as col_desc ");
		sql.append(" ON cols.column_name = col_desc.attname");
		sql.append(" where cols.table_name = '");
		sql.append(table).append("'");
		sql.append(" ORDER BY id");
		System.out.println(sql);

		return DBTool.executeQuery(sql.toString());
	}
}
