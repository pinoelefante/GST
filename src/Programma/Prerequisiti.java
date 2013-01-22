package Programma;

import java.io.File;
import java.io.IOException;

public class Prerequisiti {
	class Dipendenza{
		private String nome;
		private String url;
		private long size;
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
	}
	public static void checkDipendenze() {
		try {
			//TODO modificare in modo da controllare anche il file size
			File dir_lib = new File(Settings.getCurrentDir() + "lib");
			String arch_vm = System.getProperty("os.arch");
			//System.out.println("Architettura: "+arch_vm);
			boolean x86 = arch_vm.contains("x86")||arch_vm.contains("i386");
			
			if(!dir_lib.isDirectory() || !dir_lib.exists())
				dir_lib.mkdir();
			if(Settings.isWindows()){
				if(!OperazioniFile.fileExists("lib"+File.separator+"swt.jar")){
					Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/" + (x86 ? "swt-win32-x86.jar" : "swt-win32-x64.jar"), "lib/swt.jar");
				}
			}
			else if(Settings.isLinux()){
				if(!OperazioniFile.fileExists("lib"+File.separator+"swt.jar")){
					Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/" + (x86 ? "swt-linux-x86.jar" : "swt-linux-x64.jar"), "lib/swt.jar");
				}
			}
			else if(Settings.isMacOS()){
				if(!OperazioniFile.fileExists("lib"+File.separator+"swt.jar")){
					Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/" + (x86 ? "swt-macosx-x86.jar" : "swt-macosx-x64.jar"), "lib/swt.jar");
				}
			}
			
			String[] file={
					"sqlite-jdbc.jar",
					"DJNativeSwing.jar",
					"DJNativeSwing-SWT.jar",
					"commons-codec.jar",
					"commons-collections.jar",
					"commons-io.jar",
					"commons-lang3.jar",
					"commons-logging.jar",
					"cssparser.jar",
					"htmlunit-core-js.jar",
					"htmlunit.jar",
					"httpclient.jar",
					"httpcore.jar",
					"httpmime.jar",
					"nekohtml.jar",
					"sac.jar",
					"xalan.jar",
					"xercesImpl.jar"
			};
			for(int i=0;i<file.length;i++){
				if(!OperazioniFile.fileExists("lib"+File.separator+file[i])){
					System.out.println("Scaricando: "+file[i]);
					Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/"+file[i], "lib"+File.separator+file[i]);
				}
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
			ManagerException.registraEccezione(e1);
		}
	}
}
