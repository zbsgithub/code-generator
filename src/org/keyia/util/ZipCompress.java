package org.keyia.util;

/*   
 ����org.apache.tools.zipʵ��ѹ����   
 ҹ����ʹ��java.util.zip������������ĵĻ���   
 ��ѹ����ʱ���ļ����ֻ������롣ԭ���ǽ�ѹ������ı����ʽ��   
 java.util.zip.ZipInputStream�ı����ַ�����ͬ   
 java.util.zip.ZipInputStream���ַ����̶���UTF-8   
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.tools.zip.*;

public class ZipCompress {

	/** ����һ����Ҫѹ�����ļ����ļ���·��,��������ѹ������ļ��� */
	public void zip(String inputFile, String zipFileName) {
		zip(new File(inputFile), zipFileName, null);
	}

	/** ����һ����Ҫѹ����File����,��������ѹ������ļ��� */
	public void zip(File inputFile, String zipFileName, String ingore) {
		try {
			ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(new String(zipFileName.getBytes("gb2312"))));
			System.out.println("ѹ��-->��ʼ");
			zip(zOut, inputFile, "", ingore);
			System.out.println("ѹ��-->����");
			zOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ����һ��ѹ�����������(��������д�����ݲ������ļ�),����������Ҫѹ����File����,����������Ŀ¼,��ѹ�����е��ļ���Ŀ¼ */
	public void zip(ZipOutputStream zOut, File file, String base, String ingore) {     
       try {     
           System.out.println("����ѹ��-->" + file.getName());     
           if (file.isDirectory()) {   
        	   if(file.getName().equals(ingore)){
        		   return;
        	   }
               File[] listFiles = file.listFiles();     
                  
                // �˴����ѹ��δ�����ݲ���ȷ,�����жϵ�ǰ��ʲôϵͳ,�ձ���Windows����������Ͳ�ע�Ͳ������ж�. 
               if (System.getProperty("os.name").startsWith("Windows")) { 
            	   base = base.length() == 0 ? "" : base + "\\"; 
            	   zOut.putNextEntry(new ZipEntry(base)); }
           		else { 
           			base = base.length() == 0 ? "" :base + "/";
           			zOut.putNextEntry(new ZipEntry(base));
           			}   
                //  һ����˵��"/"�������ָ���ļ����ļ�����û�������   
                    
               zOut.putNextEntry(new ZipEntry(base + "/"));     
               base = base.length() == 0 ? "" : base + "/";     
               for (int i = 0; i < listFiles.length; i++) {     
                   zip(zOut, listFiles[i], base + listFiles[i].getName(), ingore);     
                   // System.out.println(new     
                   // String(fl[i].getName().getBytes("gb2312")));     
               }     
           } else {     
               if (base == "") {     
                   base = file.getName();     
               }     
               zOut.putNextEntry(new ZipEntry(base));     
               System.out.println(file.getPath() + "," + base);     
               FileInputStream in = new FileInputStream(file);     
               int len;     
               while ((len = in.read()) != -1)     
                   zOut.write(len);     
               in.close();     
           }     
       } catch (Exception e) {     
           e.printStackTrace();     
       }     
   
   }
	private void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true)
				fl.mkdir();
			else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public void unZip(String zipFileName, String outputDirectory) {
		try {
			ZipFile zipFile = new ZipFile(zipFileName, "GBK");
			java.util.Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				System.out.println("���ڽ�ѹ: " + zipEntry.getName());
				String name = null;
				if (zipEntry.isDirectory()) {
					name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdir();
					System.out.println("����Ŀ¼��" + outputDirectory + File.separator + name);
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					// System.out.println("�����ļ�1��" +fileName);
					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
					}

					File f = new File(outputDirectory + File.separator + zipEntry.getName());

					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);

					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					out.close();
					in.close();

				}

			}
			zipFile.close();
			// ɾ���ļ�����������ɾ����Ϊ�ļ�����ʹ�ã�Ӧ���ϴ��Ǵ�ɾ
			// ��ѹ��ɾ��ѹ���ļ�
			// File zipFileToDel = new File(zipFileName);
			// zipFileToDel.delete();
			// System.out.println("����ɾ���ļ���"+ zipFileToDel.getCanonicalPath());

			// //ɾ����ѹ�����һ��Ŀ¼
			// delALayerDir(zipFileName, outputDirectory);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * ɾ��һ��Ŀ¼
	 * 
	 * @param zipFileName
	 * @param outputDirectory
	 */
	public void delALayerDir(String zipFileName, String outputDirectory) {

		String[] dir = zipFileName.replace('\\', '/').split("/");
		String fileFullName = dir[dir.length - 1]; // �õ�aa.zip
		int pos = -1;
		pos = fileFullName.indexOf(".");
		String fileName = fileFullName.substring(0, pos); // �õ�aa
		String sourceDir = outputDirectory + File.separator + fileName;
		try {
			copyFile(new File(outputDirectory), new File(sourceDir), new File(sourceDir));

			deleteSourceBaseDir(new File(sourceDir));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��sourceDirĿ¼���ļ�ȫ��copy��destDir��ȥ
	 */
	public void copyFile(File destDir, File sourceBaseDir, File sourceDir) throws Exception {

		File[] lists = sourceDir.listFiles();
		if (lists == null)
			return;
		for (int i = 0; i < lists.length; i++) {
			File f = lists[i];
			if (f.isFile()) {
				FileInputStream fis = new FileInputStream(f);
				String content = "";
				String sourceBasePath = sourceBaseDir.getCanonicalPath();
				String fPath = f.getCanonicalPath();
				String drPath = destDir + fPath.substring(fPath.indexOf(sourceBasePath) + sourceBasePath.length());
				FileOutputStream fos = new FileOutputStream(drPath);

				byte[] b = new byte[2048];
				while (fis.read(b) != -1) {
					if (content != null)
						content += new String(b);
					else
						content = new String(b);
					b = new byte[2048];
				}

				content = content.trim();
				fis.close();

				fos.write(content.getBytes());
				fos.flush();
				fos.close();

			} else {
				// ���½�Ŀ¼
				new File(destDir + File.separator + f.getName()).mkdir();

				copyFile(destDir, sourceBaseDir, f); // �ݹ����
			}
		}
	}

	/**
	 * ��sourceDirĿ¼���ļ�ȫ��copy��destDir��ȥ
	 */
	public void deleteSourceBaseDir(File curFile) throws Exception {
		File[] lists = curFile.listFiles();
		File parentFile = null;
		for (int i = 0; i < lists.length; i++) {
			File f = lists[i];
			if (f.isFile()) {
				f.delete();
				// �����ĸ�Ŀ¼û���ļ��ˣ�˵���Ѿ�ɾ�꣬Ӧ��ɾ����Ŀ¼
				parentFile = f.getParentFile();
				if (parentFile.list().length == 0)
					parentFile.delete();
			} else {
				deleteSourceBaseDir(f); // �ݹ����
			}
		}
	}

	public static void main(String[] args) {
		ZipCompress t = new ZipCompress();
		// �����ǵ���ѹ���Ĵ���
		// t.zip("d:\\me.7z", "d:\\test.jar");
		// �����ǵ��ý�ѹ���Ĵ���
		t.unZip("d:\\me.zip", "d:\\my");
	}

}