package test.tools;

import java.io.File;

public class RenameMyComics {

	public static void main(String[] args) {
		String dirpath = "C:\\Users\\lxr\\Desktop\\download";
		File dir = new File(dirpath);
		if (dir != null & dir.isDirectory() && dir.exists()) {
			try {
				String[] sondirlist = dir.list();
				int count = 0;
				if (sondirlist != null) {
					for (String sondirname : sondirlist) {
						File sondir = new File(dirpath, sondirname);
						if (sondir != null && sondir.isDirectory() && sondir.exists()) {
							String newname = rename(sondirname);
							File f = new File(dirpath, newname);
							if (f.exists()) {
								newname = "@EXIST@" + newname;
							}
							System.out.println(newname);
							sondir.renameTo(new File(dirpath, newname));
							count++;
						}
					}
				}
				System.out.println(count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String rename(String sondirname) {
		StringBuffer sb = new StringBuffer(sondirname);
		int a = sb.indexOf("["), b;
		if (a > 0) {
			sb.delete(0, a);
			b = sb.indexOf("[", sb.indexOf("]") + 1);
			if (b > 0) {
				sb.delete(b, sb.length());
			}
		}
		return sb.toString();
	};
}
