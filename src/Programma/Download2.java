package Programma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Download2 {
	private long d_corrente, d_finale;
	private String url_download, path_destinazione;
	private Thread download;
	private boolean complete=false, toStart=true;
	
	public Download2(String url, String path){
		this.url_download=url;
		this.path_destinazione=path;
		download=new Downloader();
	}
	public Download2(String url, String path, boolean toStart){
		this.url_download=url;
		this.path_destinazione=path;
		this.toStart=toStart;
		if(toStart)
			download=new Downloader();
		else
			complete=true;
	}
	/**
	 * 
	 * @return thread che si occupa dell'effettivo download del file
	 */
	public Thread getDownloadThread(){
		return download;
	}
	/**
	 * 
	 * @return grandezza in byte del file da scaricare/che si sta scaricando 
	 */
	public long getFileSize(){
		return d_finale;
	}
	/**
	 * 
	 * @return byte del file scaricati
	 */
	public long getFileSizeDowloaded(){
		return d_corrente;
	}
	/**
	 * avvia il thread di download
	 */
	public void avviaDownload(){
		if(download!=null)
			download.start();
	}
	/**
	 * interrompe il thread di download
	 */
	public void interrompiDownload(){
		download.interrupt();
		complete=true;
	}
	/**
	 * 
	 * @return l'url del file che si sta scaricando
	 */
	public String getUrlDownload(){
		return url_download;
	}
	/**
	 * 
	 * @return percorso in cui si salverà il file che si sta scaricando
	 */
	public String getPathDownload(){
		return path_destinazione;
	}
	/**
	 * 
	 * @return true se il download è completo
	 */
	public boolean isComplete(){
		return complete;
	}
	/**
	 * 
	 * @return false se il download del file non deve essere avviato
	 */
	public boolean isToStart(){
		return toStart;
	}
	
	/**
	 * La classe (thread) si occupa di scaricare effettivamente il file
	 * @author pino
	 *
	 */
	class Downloader extends Thread {
		private InputStream is = null;
		private FileOutputStream fos = null;
		/**
		 * chiude gli stream e cancella il file
		 */
		public void clean(){
			if(is!=null){
				try {
					is.close();
					is=null;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
					fos=null;
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			File f=new File(path_destinazione);
			f.delete();
		}
		/**
		 * in caso di interrupt si cancella il file che si stava scaricando
		 */
		public void interrupt(){
			super.interrupt();
			clean();
		}
		/**
		 * Download del file
		 */
		public void run(){
			String userAgent = "GestioneSerieTV release "+Settings.getVersioneSoftware()+" ("+System.getProperty("os.name")+")";
			URL url = null;
			try {
				url = new URL(url_download);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				URLConnection urlConn = url.openConnection();
				urlConn.setConnectTimeout(300000);
				urlConn.setReadTimeout(300000);
				if (userAgent != null) {
					urlConn.setRequestProperty("User-Agent", userAgent);
				}
				is = urlConn.getInputStream();
				
				if(path_destinazione.contains(File.separator)){
					String path=path_destinazione.substring(0, path_destinazione.lastIndexOf(File.separator));
					File f=new File(path);
					f.mkdirs();
				}
				
				fos = new FileOutputStream(path_destinazione);
				d_finale=urlConn.getContentLengthLong();

				byte[] buffer = new byte[32768]; //32KB
				int len;
				while ((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
					d_corrente+=len;
				}
				complete=true;
				buffer=null;
			} 
			catch (IOException e) {
				clean();
				e.printStackTrace();
			}
			catch (NullPointerException e){
				clean();
			}
			finally {
				try {
					if (is != null)
						is.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					if (fos != null) {
						try {
							fos.close();
						} 
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	public static void downloadMagnet(String magnet_url, String folder) throws IOException {
		String directory_download = Settings.getDirectoryDownload();
		if(Settings.isWindows()){
			String[] cmd={
					Settings.getClientPath(),
					"/NOINSTALL",
					"/DIRECTORY",
					("\"" + directory_download + File.separator + folder + "\""),
					magnet_url
			};
			for(int i=0;i<cmd.length;i++){
				System.out.print(cmd[i]+" ");
			}
			System.out.println();
			Runtime.getRuntime().exec(cmd);
		}
		else if(Settings.isLinux()){
			String[] cmd={
				"wine",
				Settings.getClientPath(),
				"/NOINSTALL",
				"/DIRECTORY",
				("\"" + directory_download + File.separator + folder + "\""),
				magnet_url
			};
			Runtime.getRuntime().exec(cmd);
			//Runtime.getRuntime().exec("wine "+Settings.getClientPath()+ " /NOINSTALL /DIRECTORY " + "'T:"+File.separator +  folder + "'" + " " + url);
		}
	}
	public static void downloadFromUrl(String url_download, String localFilename) throws IOException{
		Download2 download=new Download2(url_download, localFilename);
		download.avviaDownload();
		try {
			download.getDownloadThread().join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Il download non è stato completato");
		}
	}
}
