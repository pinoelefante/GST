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
					System.out.println(d.getNome()+": "+file.length()+"/"+d.getSize());
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
		checkVLC();
	}
	private static ArrayList<Dipendenza> vlc_dep=new ArrayList<Dipendenza>(3);
	private static void checkVLC(){
		if(vlc_dep.isEmpty()){
			popola_vlc();
		}
		String destinazione="lib"+File.separator+"vlc"+File.separator+Settings.getOSName()+"-"+Settings.getVMArch()+File.separator;
		File dest=new File(destinazione);
		if(!dest.exists())
			dest.mkdirs();
		
		for(int i=0;i<vlc_dep.size();i++){
			Dipendenza d=vlc_dep.get(i);
			File f_check=new File(d.getNomeDest());
			if(f_check.exists()){
				if(f_check.length()!=d.getSize()){
					String dest_nome=d.getUrl().substring(d.getUrl().lastIndexOf("/"));
					
					try {
						Download.downloadFromUrl(d.getUrl(), destinazione+dest_nome);
						ArchiviZip.estrai_tutto(destinazione+dest_nome, destinazione);
						OperazioniFile.deleteFile(destinazione+dest_nome);
					}
					catch (IOException e) {
						ManagerException.registraEccezione(e);
						e.printStackTrace();
					}
				}
			}
			else {
				String dest_nome=d.getUrl().substring(d.getUrl().lastIndexOf("/"));
				try {
					Download.downloadFromUrl(d.getUrl(), destinazione+dest_nome);
					ArchiviZip.estrai_tutto(destinazione+dest_nome, destinazione);
					OperazioniFile.deleteFile(destinazione+dest_nome);
				}
				catch (IOException e) {
					ManagerException.registraEccezione(e);
					e.printStackTrace();
				}
			}
		}
		
	}
	private static void popola_vlc() {
		String destinazione="lib"+File.separator+"vlc"+File.separator+Settings.getOSName()+"-"+Settings.getVMArch()+File.separator;
		if(Settings.isWindows()){
			if(Settings.is32bit()){
				vlc_dep.add(new Dipendenza("libvlc.dll", destinazione+"libvlc.dll", sito+"vlc_win32.7z", "windows", 144896L, true, false));
				vlc_dep.add(new Dipendenza("libvlccore.dll", destinazione+"libvlccore.dll", sito+"vlc_win32.7z", "windows", 2376192L, true, false));
			}
			else {
				vlc_dep.add(new Dipendenza("libvlc.dll", destinazione+"libvlc.dll", sito+"vlc_win64.7z", "windows", 150016L, false, true));
				vlc_dep.add(new Dipendenza("libvlccore.dll", destinazione+"libvlccore.dll", sito+"vlc_win64.7z", "windows", 2401280L, false, true));
			}
		}
		else if(Settings.isLinux()){
			if(Settings.is32bit()){
				
			}
			else {
				
			}
		}
		else if(Settings.isMacOS()){
			if(Settings.is32bit()){
				
			}
			else {
				
			}
		}
	}
	private final static String sito="http://pinoelefante.altervista.org/software/GST/";
	private static void popola_dipendenze() {
		String arch_vm = System.getProperty("os.arch");
		boolean x86 = arch_vm.contains("x86")||arch_vm.contains("i386");
		
		
		list_dipendenze.add(new Dipendenza("commons-codec.jar", "commons-codec.jar", sito+"commons-codec.jar", "indipendent", 232771L, true, true));
		list_dipendenze.add(new Dipendenza("commons-collections.jar", "commons-collections.jar", sito+"commons-collections.jar", "indipendent", 575389L, true, true));
		list_dipendenze.add(new Dipendenza("commons-io.jar", "commons-io.jar", sito+"commons-io.jar", "indipendent", 173587L, true, true));
		list_dipendenze.add(new Dipendenza("commons-lang3.jar", "commons-lang3.jar", sito+"commons-lang3.jar", "indipendent", 315805L, true, true));
		list_dipendenze.add(new Dipendenza("commons-logging.jar", "commons-logging.jar", sito+"commons-logging.jar", "indipendent", 60686L, true, true));
		list_dipendenze.add(new Dipendenza("cssparser.jar", "cssparser.jar", sito+"cssparser.jar", "indipendent", 280655L, true, true));
		/*DJNative 27/04/2013*/
		list_dipendenze.add(new Dipendenza("DJNativeSwing-SWT.jar", "DJNativeSwing-SWT.jar", sito+"DJNativeSwing-SWT_2.jar", "indipendent", 555114L, true, true));
		list_dipendenze.add(new Dipendenza("DJNativeSwing.jar", "DJNativeSwing.jar", sito+"DJNativeSwing_2.jar", "indipendent", 113311L, true, true));
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
		
		/*VLCJ 2.4.0 SevenzipBinding 4.65-1.06-rc*/
		list_dipendenze.add(new Dipendenza("jna-3.5.2.jar", "jna-3.5.2.jar", sito+"jna-3.5.2.jar", "indipendent", 692070L, true, true));
		list_dipendenze.add(new Dipendenza("platform-3.5.2.jar", "jna-platform-3.5.2.jar", sito+"platform-3.5.2.jar", "indipendent", 1192357L, true, true));
		list_dipendenze.add(new Dipendenza("sevenzipjbinding-AllPlatforms.jar", "sevenzipjbinding-AllPlatforms.jar", sito+"sevenzipjbinding-AllPlatforms.jar", "indipendent", 5686371L, true, true));
		list_dipendenze.add(new Dipendenza("sevenzipjbinding.jar", "sevenzipjbinding.jar", sito+"sevenzipjbinding.jar", "indipendent",31653L, true, true));
		list_dipendenze.add(new Dipendenza("vlcj-2.4.0.jar", "vlcj.jar", sito+"vlcj-2.4.0.jar", "indipendent", 345388L, true, true));
		
		if(Settings.isLinux()){
			if(!x86){
				list_dipendenze.add(new Dipendenza("swt-linux-x64.jar", "swt.jar", sito+"swt-linux-x64.jar", "linux", 1702474L, false, true));
			}
			else {
				list_dipendenze.add(new Dipendenza("swt-linux-x86.jar", "swt.jar", sito+"swt-linux-x86.jar", "linux", 1542536L, true, false));
			}
		}
		else if(Settings.isMacOS()){
			if(!x86){
				list_dipendenze.add(new Dipendenza("swt-macosx-x64.jar", "swt.jar", sito+"swt-macosx-x64.jar", "mac", 1599237L, false, true));
			}
			else {
				list_dipendenze.add(new Dipendenza("swt-macosx-x86.jar", "swt.jar", sito+"swt-macosx-x86.jar", "mac", 1694512L, true, false));
			}
		}
		else if(Settings.isWindows()){
			if(!x86){
				list_dipendenze.add(new Dipendenza("swt-win32-x64.jar", "swt.jar", sito+"swt-win32-x64.jar", "windows", 1878506L, false, true));
			}
			else {
				list_dipendenze.add(new Dipendenza("swt-win32-x86.jar", "swt.jar", sito+"swt-win32-x86.jar", "windows", 1891572L, true, false));
			}
		}
	}
}
