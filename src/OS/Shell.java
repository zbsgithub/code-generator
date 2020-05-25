package OS;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Shell {
	public static void main(String[] args) throws IOException {
		System.out.println(exe("cmd /c ant generate"));
	}

	/**
	 * cmds="cmd /c ant generate"
	 * @param cmds
	 * @return
	 * @throws IOException
	 */
	public static String exe(String cmds) throws IOException {
		StringBuffer out = new StringBuffer();
		try {
			Process ps = null;//执行的进程
			//判断是linux还是win系统
			if (System.getProperty("os.name").contains("inux")) {
				ps = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", cmds });
			} else if (System.getProperty("os.name").contains("indows")) {
				File workDir = new File(".");
				ps = Runtime.getRuntime().exec(cmds, null, workDir);
			}
			out.append(loadStream(ps.getInputStream()));
			out.append(loadStream(ps.getErrorStream()));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
		return out.toString();
	}

	static StringBuffer loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer;
	}
}
