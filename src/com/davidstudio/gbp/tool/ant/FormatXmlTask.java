package com.davidstudio.gbp.tool.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class FormatXmlTask extends Task {
	@SuppressWarnings("rawtypes")
	private Vector filesets = new Vector();

	@SuppressWarnings("unchecked")
	public void addFileset(FileSet set) {
		this.filesets.addElement(set);
	}

	protected void validateAttributes() throws BuildException {
		if (this.filesets.size() == 0) {
			throw new BuildException("Specify at least one source - a file or a fileset.");
		}
	}

	public void execute() {
		validateAttributes();

		for (int i = 0; i < this.filesets.size(); i++) {
			FileSet fs = (FileSet) this.filesets.elementAt(i);
			DirectoryScanner ds = null;
			ds = fs.getDirectoryScanner(getProject());
			ds.setCaseSensitive(false);
			ds.scan();

			String[] files = ds.getIncludedFiles();
			String strBaseDir = fs.getDir(getProject()) + File.separator;
			for (int k = 0; k < files.length; k++) {
				File f = new File(strBaseDir + files[k]);

				if (f.exists()) {
					FormatXml(f);
				}
			}
		}
	}

	public void scanSourceDir() {
	}

	@SuppressWarnings("unused")
	public void FormatXml(File f) {
		if (f != null) {
			SAXBuilder builder = new SAXBuilder(false);
			try {
				Document doc = builder.build(f);
				String indent = "  ";
				boolean newLines = true;
				XMLOutputter outp = new XMLOutputter();
				// XMLOutputter outp = new XMLOutputter(indent, newLines);
				// outp.setEncoding("GBK");
				//
				// outp.setTextTrim(true);
				// outp.setTrimAllWhite(true);
				outp.output(doc, new FileOutputStream(f));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
