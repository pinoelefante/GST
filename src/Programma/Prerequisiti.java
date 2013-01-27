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

			File dir_lib = new File(Settings.getCurrentDir() + "lib");
			if(!dir_lib.isDirectory() || !dir_lib.exists())
				dir_lib.mkdir();
			
			for(int i=0;i<list_dipendenze.size();i++){
				Dipendenza d=list_dipendenze.get(i);
				File file=new File("lib"+File.separator+d.getNomeDest());
				if(file.exists()){
					if(file.length()!=d.getSize()){
						System.out.println("Scaricando: "+d.getNome());
						Download.downloadFromUrl(d.getUrl(), "lib"+File.separator+d.getNomeDest());
					}
				}
				else {
					System.out.println("Scaricando: "+d.getNome());
					Download.downloadFromUrl(d.getUrl(), "lib"+File.separator+d.getNomeDest());
				}
			}
		}
		catch (IOException e1) {
			e1.printStackTrace();
			ManagerException.registraEccezione(e1);
		}
	}
	private static void popola_dipendenze() {
		String arch_vm = System.getProperty("os.arch");
		boolean x86 = arch_vm.contains("x86")||arch_vm.contains("i386");
		String sito="http://pinoelefante.altervista.org/software/GST/";
		
		list_dipendenze.add(new Dipendenza("commons-codec.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 232771L, true, true));
		list_dipendenze.add(new Dipendenza("commons-collections.jar", "commons-collections.jar", sito+"commons-collections.jar", "indipendent", 575389L, true, true));
		list_dipendenze.add(new Dipendenza("commons-io.jar", "commons-io.jar", sito+"commons-io.jar", "indipendent", 173587L, true, true));
		list_dipendenze.add(new Dipendenza("commons-lang3.jar", "commons-lang3.jar", sito+"commons-lang3.jar", "indipendent", 315805L, true, true));
		list_dipendenze.add(new Dipendenza("commons-logging.jar", "commons-logging.jar", sito+"commons-logging.jar", "indipendent", 60686L, true, true));
		list_dipendenze.add(new Dipendenza("cssparser.jar", "cssparser.jar", sito+"cssparser.jar", "indipendent", 280655L, true, true));
		list_dipendenze.add(new Dipendenza("DJNativeSwing-SWT.jar", "DJNativeSwing-SWT.jar", sito+"DJNativeSwing-SWT.jar", "indipendent", 553683L, true, true));
		list_dipendenze.add(new Dipendenza("DJNativeSwing.jar", "DJNativeSwing.jar", sito+"DJNativeSwing.jar", "indipendent", 112867L, true, true));
		list_dipendenze.add(new Dipendenza("htmlunit-core-js.jar", "htmlunit-core-js.jar", sito+"htmlunit-core-js.jar", "indipendent", 975274L, true, true));
		list_dipendenze.add(new Dipendenza("htmlunit.jar", "htmlunit.jar", sito+"htmlunit.jar", "indipendent", 1041375L, true, true));
		list_dipendenze.add(new Dipendenza("httpclient.jar", "httpclient.jar", sito+"httpclient.jar", "indipendent", 427021L, true, true));
		list_dipendenze.add(new Dipendenza("httpcore.jar", "httpcore.jar", sito+"httpcore.jar", "indipendent", 223374L, true, true));
		list_dipendenze.add(new Dipendenza("httpmime.jar", "httpmime.jar", sito+"httpmime.jar", "indipendent", 26598L, true, true));
		list_dipendenze.add(new Dipendenza("nekohtml.jar", "nekohtml.jar", sito+"nekohtml.jar", "indipendent", 124106L, true, true));
		list_dipendenze.add(new Dipendenza("sac.jar", "sac.jar", sito+"sac.jar", "indipendent", 15808L, true, true));
		list_dipendenze.add(new Dipendenza("sqlite-jdbc.jar", "sqlite-jdbc.jar", sito+"sqlite-jdbc.jar", "indipendent", 3201128L, true, true));
		list_dipendenze.add(new Dipendenza("xalan.jar", "xalan.jar", sito+"xalan.jar", "indipendent", 3176148L, true, true));
		list_dipendenze.add(new Dipendenza("xercesImpl.jar", "xercesImpl.jar", sito+"xercesImpl.jar", "indipendent", 1229125L, true, true));
		
		if(Settings.isLinux()){
			list_dipendenze.add(new Dipendenza("jna-3.2.4.jar", "jna-3.2.4.jar", sito+"jna-3.2.4.jar", "indipendent", 944033L, true, true));
			if(!x86)
				list_dipendenze.add(new Dipendenza("swt-linux-x64.jar", "swt.jar", sito+"swt-linux-x64.jar", "linux", 1702474L, false, true));
			else
				list_dipendenze.add(new Dipendenza("swt-linux-x86.jar", "swt.jar", sito+"swt-linux-x86.jar", "linux", 1542536L, true, false));
		
		}
		else if(Settings.isMacOS()){
			list_dipendenze.add(new Dipendenza("jna-3.2.4.jar", "jna-3.2.4.jar", sito+"jna-3.2.4.jar", "indipendent", 944033L, true, true));
			if(!x86)
				list_dipendenze.add(new Dipendenza("swt-macosx-x64.jar", "swt.jar", sito+"swt-macosx-x64.jar", "mac", 1599237L, false, true));
			else
				list_dipendenze.add(new Dipendenza("swt-macosx-x86.jar", "swt.jar", sito+"swt-macosx-x86.jar", "mac", 1694512L, true, false));
		}
		else if(Settings.isWindows()){
			if(!x86)
				list_dipendenze.add(new Dipendenza("swt-win32-x64.jar", "swt.jar", sito+"swt-win32-x64.jar", "windows", 1878506L, false, true));
			else
				list_dipendenze.add(new Dipendenza("swt-win32-x86.jar", "swt.jar", sito+"swt-win32-x86.jar", "windows", 1891572L, true, false));
		}		
	}
}
