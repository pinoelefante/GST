package Programma;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Prerequisiti {
	private static ArrayList<Dipendenza> list_dipendenze=new ArrayList<Dipendenza>();
	public static void checkDipendenze() {
		try {
			if(list_dipendenze.isEmpty())
				popola_dipendenze();
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
	private static void popola_dipendenze() {
		String sito="http://pinoelefante.altervista.org/software/GST/";
		list_dipendenze.add(new Dipendenza("commons-codec.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 232771L, true, true));
		list_dipendenze.add(new Dipendenza("commons-collections.jar", "commons-collections.jar", "commons-collections.jar", "indipendent", 575389L, true, true));
		list_dipendenze.add(new Dipendenza("commons-io.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 173587L, true, true));
		list_dipendenze.add(new Dipendenza("commons-lang3.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 315805L, true, true));
		list_dipendenze.add(new Dipendenza("commons-logging.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 60686L, true, true));
		list_dipendenze.add(new Dipendenza("cssparser.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 280655L, true, true));
		list_dipendenze.add(new Dipendenza("DJNativeSwing-SWT.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 553683L, true, true));
		list_dipendenze.add(new Dipendenza("DJNativeSwing.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 112867L, true, true));
		list_dipendenze.add(new Dipendenza("htmlunit-core-js.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 975274L, true, true));
		list_dipendenze.add(new Dipendenza("htmlunit.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 1041375L, true, true));
		list_dipendenze.add(new Dipendenza("httpclient.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 427021L, true, true));
		list_dipendenze.add(new Dipendenza("httpcore.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 223374L, true, true));
		list_dipendenze.add(new Dipendenza("httpmime.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 26598L, true, true));
		list_dipendenze.add(new Dipendenza("nekohtml.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 124106L, true, true));
		list_dipendenze.add(new Dipendenza("sac.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 15808L, true, true));
		list_dipendenze.add(new Dipendenza("sqlite-jdbc.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 3201128L, true, true));
		list_dipendenze.add(new Dipendenza("xalan.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 3176148L, true, true));
		list_dipendenze.add(new Dipendenza("xercesImpl.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 1229125L, true, true));

		 
		 
		 
		 
		 
		"swt-linux-x64.jar" 1702474L
		"swt-linux-x86.jar" 1542536L
		"swt-macosx-x64.jar" 1599237L
		"swt-macosx-x86.jar" 1694512L
		"swt-win32-x64.jar" 1878506L
		"swt-win32-x86.jar" 1891572L
		 
		
	}
}
