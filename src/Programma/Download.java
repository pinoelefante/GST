package Programma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {
	
	public static boolean downloadFromUrl(String url_download, String localFilename) throws IOException {
		InputStream is = null;
		FileOutputStream fos = null;
		String userAgent = "GestioneSerieTV_"+Settings.getVersioneSoftware()+" ("+System.getProperty("os.name")+")";
		URL url = new URL(url_download);
		try {
			URLConnection urlConn = url.openConnection();
			urlConn.setConnectTimeout(300000);
			urlConn.setReadTimeout(300000);

			if (userAgent != null) {
				urlConn.setRequestProperty("User-Agent", userAgent);
			}

			is = urlConn.getInputStream();
			fos = new FileOutputStream(localFilename);

			byte[] buffer = new byte[32768]; //32KB
			int len;
			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			buffer=null;
		}
		finally {
			try {
				if (is != null)
					is.close();
			}
			finally {
				if (fos != null) {
					fos.close();
					return true;
				}
			}
		}
		return false;
	}
	
	public static void downloadMagnet(String url, String folder) throws IOException {

		switch (Settings.getClient()) {
			case 2:
				Runtime.getRuntime().exec(Settings.getClientPath() + " " + url);
			break;
			case 1:
				String directory_download = Settings.getDirectoryDownload();
				Runtime.getRuntime().exec(Settings.getClientPath()
						+ " /NOINSTALL /DIRECTORY " + "\"" + directory_download
						+ File.separator + folder + "\"" + " " + url);
			break;
		}
	}
/*
	public static boolean downloadFromUrl(String url, String destinazione) throws IOException{
	    HttpClient client = new HttpClient();
	    client.setConnectionTimeout(300000);
	    client.setTimeout(300000);
	    
	    GetMethod method=new GetMethod(url);
	    int response=client.executeMethod(method);
	    
	    System.out.println(response);
	    
	    InputStream is=method.getResponseBodyAsStream();
	    
	    FileOutputStream fos=new FileOutputStream(destinazione);
	    byte[] buffer=new byte[4096];
	    int len;
	    
	    while ((len = is.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
	    
	    fos.close();
	    method.releaseConnection();
	    
		return true;
	}
*/
}
