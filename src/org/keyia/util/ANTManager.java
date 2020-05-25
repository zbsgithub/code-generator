package org.keyia.util;

import java.io.File;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.DefaultLogger;

import org.apache.tools.ant.Project;

import org.apache.tools.ant.ProjectHelper;

public class ANTManager {

	public static boolean run(String Path, String task) {

		File buildFile = new File(Path);

		// 创建一个ANT项目

		Project p = new Project();

		// 创建一个默认的监听器,监听项目构建过程中的日志操作

		DefaultLogger consoleLogger = new DefaultLogger();

		consoleLogger.setErrorPrintStream(System.err);

		consoleLogger.setOutputPrintStream(System.out);

		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);

		p.addBuildListener(consoleLogger);

		try {

			p.fireBuildStarted();

			// 初始化该项目

			p.init();

			ProjectHelper helper = ProjectHelper.getProjectHelper();

			// 解析项目的构建文件

			helper.parse(p, buildFile);

			// 执行项目的某一个目标

			p.executeTarget(task);

			p.fireBuildFinished(null);

		} catch (BuildException be) {

			p.fireBuildFinished(be);
			return false;

		}
		return true;

	}
	public static void main(String[] args) {

		ANTManager.run("G:\\Programing\\eclipse-SDK-3.5.2-win32\\workspace\\build.xml"
				,"rebuild");
	}

}
