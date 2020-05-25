package org.keyia.util;

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

import com.davidstudio.gbp.tool.util.DBTool;

import OS.Shell;

public class CodeGenOracle {
	private String PARAM_FILE_NAME = "gen.properties";
	private Properties properties = null;
	private String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.200.129)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.200.126)(PORT=1521))(LOAD_BALANCE=yes))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=orcl)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC)(RETRIES=180)(DELAY= 5))))";
	private String owner = "SDDXJW";
	private String username = "sddxjw";
	private String password = "123";
	@SuppressWarnings("rawtypes")
	private List tableList = new LinkedList();
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
	private String antXmlFile = "I:\\project\\svn\\svnChinaCodeGenerator\\easyuiCodeGenerator\\build.xml";
	@SuppressWarnings("unused")
	private String destZip = "I:\\project\\svn\\svnChinaCodeGenerator\\easyuiCodeGenerator\\code.zip";
	@SuppressWarnings("unused")
	private String antTask = "generate";
	private String cmd = "cd /home/resinapp/zhjw/autoDeployTools/tmp/jckf;${AUTO_DEPLOY_HOME}libs/ant/bin/ant updateTrunk";

	public static void main(String[] args) {
		main("JS_LXFSB\nCODE_CJLXB\nCODE_RWLXB\nCODE_SKZLB\nCODE_KSRWB\nCODE_KHFSB\nCODE_KSFSB\nCODE_SJLXB\nCODE_JCMSB\nRL_SKJSB\nDEL_RL_JSRLB\nRL_JXRLB\nRW_JXRWB\nRW_CJPZB\nTMP_RW_SJDDB\nRW_SJDDB\nRW_SKJSB\nRW_KKBJB\nRW_RWFZB\nRL_RLFZB\nRL_JXRLMXB\nRL_JSCJTJB\nRW_FZXSB\nCODE_MSJCB\nCODE_JS_SKJSB\nCJ_LRCJB",// 如果同时生成多表,使用'\n分隔各表'
				"org.thcic.sdu", // 包名
				"jxrl", // 模块包名
				"admin", // 操作人
				"D:/Users/01/workspace/codeGenerator/table.xml",
				"D:/Users/01/workspace/codeGenerator/build.xml",
				"D:/THCIC/codeGen/code.zip", "gen.properties",
				"cmd /c ant generate", "iot");
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String tableList, String packageValue,
			String modelValue, String authorValue, String configFileValue,
			String antXmlFile, String destZip, String PARAM_FILE_NAME,
			String cmd, String jspSubFolder) {
		CodeGenOracle codeGen = new CodeGenOracle();
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
			codeGen.tableList.add(array[i].toUpperCase());
		}

		System.out.println("start");
		codeGen.dealTable();
		codeGen.generCode();
		ZipCompress t = new ZipCompress();
		FileUtils.delete(new File(destZip));
		// t.zip(antXmlFile.replace("build.xml", "out"), destZip);
		System.out.println("finish");
	}

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

	@SuppressWarnings("rawtypes")
	private Map tables = new HashMap();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dealTable() {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = this.tableList.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sb.append(",'" + type + "'");
			this.tables.put(type.toUpperCase(), type.toUpperCase());
		}

		String tables = sb.toString().substring(1);
		System.out.println(tables);

		try {
			DBTool.initDataSource(this.url, this.username, this.password);

			String command = "SELECT * from (SELECT  c.* FROM all_tables t, all_tab_comments c  WHERE t.owner = c.owner AND t.table_name = c.table_name AND t.owner = '"
					+ this.owner.toUpperCase()
					+ "'   AND t.table_name in ("
					+ tables
					+ ") "
					+ " union SELECT  c.* FROM all_views t, all_tab_comments c  WHERE t.owner = c.owner AND t.view_name = c.table_name AND t.owner = '"
					+ this.owner.toUpperCase()
					+ "'    AND t.view_name in ("
					+ tables + ") ) ORDER BY table_name";
			System.out.println(command);

			List all_tables = DBTool.executeQuery(command);

			this.tableNames = new String[all_tables.size()];
			this.tableComments = new String[all_tables.size()];
			this.tableShowNames = new String[all_tables.size()];
			int i = 0;
			for (Iterator each = all_tables.iterator(); each.hasNext();) {
				Map cols = (Map) each.next();

				this.tableNames[i] = ((String) cols.get("table_name"));
				this.tableComments[i] = ((String) cols.get("comments"));
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

		Document doc = new Document();
		doc.setRootElement(new Element("root"));
		StringWriter outStr = new StringWriter();
		try {
			for (int n = 0; n < this.tableShowNames.length; n++) {
				handleTable(doc.getRootElement(), this.tableNames[n],
						this.tableComments[n]);
			}

			// XMLOutputter out = new XMLOutputter("  ", true, "UTF8");
			XMLOutputter out = new XMLOutputter();
			out.output(doc, outStr);
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

	@SuppressWarnings("rawtypes")
	private void handleTable(Element root, String tableName, String tableCnName)
			throws SQLException {
		Element object = new Element("object");
		object.setAttribute("name", tableName.toLowerCase());
		if (tableCnName == null) {
			object.setAttribute("cnName", object.getAttribute("name")
					.getValue());
		} else {
			if(tableCnName.indexOf('\n')!=-1){
				tableCnName = tableCnName.substring(0,tableCnName.indexOf('\n'));
			}
			object.setAttribute("cnName", tableCnName);
		}
		object.setAttribute("numPercentPage", "20");
		object.setAttribute("dbtableName", object.getAttribute("name")
				.getValue().toUpperCase());
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
			BigDecimal col_len = (BigDecimal) column.get("data_length");
			String col_scale = "0";
			if (column.get("scale") != null) {
				col_scale = column.get("scale").toString();
			}
			String nullable = (String) column.get("nullable");
			boolean isPk = false;
			if (pkColumns.contains(col_name)) {
				isPk = true;
			}

			Element prop = new Element("property");
			prop.setAttribute("name", col_name.toLowerCase());
			prop.setAttribute("isPK", String.valueOf(isPk));
			if (col_cn != null) {
				prop.addContent(new Element("cnName").setAttribute("value",
						col_cn));
			} else {
				prop.addContent(new Element("cnName").setAttribute("value",
						col_name.toLowerCase()));
			}

			if ((col_type.matches("CHAR")) && (col_len.intValue() > 100)) {
				prop.addContent(new Element("isList")
						.setAttribute("value", "0"));
			} else {
				prop.addContent(new Element("isList")
						.setAttribute("value", "1"));
			}
			prop.addContent(new Element("listWidth").setAttribute("value", ""));
			prop.addContent(new Element("isSearch").setAttribute("value", "0"));
			if (nullable.equalsIgnoreCase("Y")) {
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
			if (col_type.matches("DATE")) {
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
			if (isPk) {
				innerProp.setAttribute("value", "id");
			}
			if (col_type.indexOf("CHAR") > -1) {
				innerProp.setAttribute("length",
						String.valueOf(col_len.intValue()));

			} else if ((col_type.indexOf("NUMBER") > -1)
					|| (col_type.indexOf("INT") > -1)
					|| (col_type.indexOf("DECIMAL") > -1)
					|| (col_type.indexOf("DOUBLE") > -1)
					|| (col_type.indexOf("FLOAT") > -1)) {
				innerProp.setAttribute("value", "number");
				innerProp.setAttribute("pointLength", col_scale.toString());
			} else if ((col_type.indexOf("DATE") > -1)
					|| (col_type.indexOf("TIME") > -1)) {
				innerProp.setAttribute("value", "date");
				innerProp.setAttribute("style", "yyyy-MM-dd");
			} else if (col_type.matches("BOOLEAN")) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List queryPkColumns(String table) throws SQLException {
		List result = new ArrayList();
		StringBuffer sql = new StringBuffer();

		sql.append("select   COLUMN_NAME as COLUMN_NAME    from   user_cons_columns   where   constraint_name   =   (select   constraint_name    from   user_constraints  where   table_name   =   '"
				+ table.toUpperCase()
				+ "' and   constraint_type   ='P' and OWNER = '"
				+ this.owner.toUpperCase() + "')");

		System.out.println(sql.toString());

		List all = DBTool.executeQuery(sql.toString());
		if ((all == null) || (all.size() == 0)) {
			System.out.println("all is empty ");
			all = DBTool
					.executeQuery(" select 'PID' as COLUMN_NAME from dual ");
			System.out.println(" select 'PID' as COLUMN_NAME from dual "
					+ all.size());
		}
		Iterator each = all.iterator();
		while (each.hasNext()) {
			result.add(((Map) each.next()).get("column_name"));
		}

		System.out.println("c" + result);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private List queryAllColumns(String table) throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append("Select cols.column_id as id, cols.column_name as name, nullable,");
		sql.append(" data_type as type,data_length,data_scale scale,substr(comments,1,30) cnname ");
		sql.append(" FROM sys.user_col_comments coms, sys.user_tab_columns cols");
		sql.append(" where coms.table_name=cols.Table_Name and coms.column_name=cols.column_Name");
		sql.append(" and cols.table_name = '").append(table.toUpperCase())
				.append("'");
		sql.append(" order by column_id");

		return DBTool.executeQuery(sql.toString());
	}
}
