package Programma;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileExistsException;

import SerieTV.Torrent;

public class OperazioniFile {
	public static void ZipDecompress(File input_zip, File output) throws IOException {
		ZipInputStream input = new ZipInputStream(new FileInputStream(input_zip));
		ZipEntry zipEntry;
		while ((zipEntry = input.getNextEntry()) != null) {
			boolean directory = zipEntry.isDirectory();
			if (directory) {
				File dir = new File(output, zipEntry.getName());
				if (!dir.exists())
					dir.mkdir();
				/*else if (dir.isDirectory()){
					input.close();
					throw new IOException("Output directory \"" + dir.getAbsolutePath() + "\" is a file");
				}*/
			}
			else {
				File decompressFile = new File(output, zipEntry.getName());
				/*if (decompressFile.exists()){
					input.close();
					throw new IOException("Output file \"" + decompressFile.getAbsolutePath() + "\" already exists");
				}
				*/
				FileOutputStream fos = new FileOutputStream(decompressFile);
				try {
					byte[] readBuffer = new byte[4096];
					int bytesIn = 0;
					while ((bytesIn = input.read(readBuffer)) != -1)
						fos.write(readBuffer, 0, bytesIn);
				}
				finally {
					fos.close();
				}
			}
		}
		input.close();
	}
	
	public static ArrayList<String> zipList(File zip) throws IOException {
		ArrayList<String> nomi=new ArrayList<String>();
		ZipInputStream input=new ZipInputStream(new FileInputStream(zip));
		ZipEntry zip_file;
		while((zip_file=input.getNextEntry())!=null){
			if(zip_file.isDirectory())
				continue;
			else
				nomi.add(zip_file.getName());
		}
		input.close();
		return nomi;
	}
	public static boolean DeleteDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] contenuto = dir.list();
			for (int i = 0; i < contenuto.length; i++) {
				boolean success = DeleteDirectory(new File(dir, contenuto[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	public static void deleteFile(String nomefile){
		(new File(nomefile)).delete();
	}

	public static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println(f2.getAbsolutePath());
			System.out.println("File copied.");
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void dumpfileclean(){
		File directory=new File(Settings.getCurrentDir());
		if(directory.isDirectory()){
			String[] contenuto = directory.list();
			for(int i=0;i<contenuto.length;i++){
				if(contenuto[i].startsWith("hs_err_") && contenuto[i].endsWith(".log")){
					deleteFile(contenuto[i]);
				}
			}
		}
	}
	public static void esploraCartella(String path) throws Exception{
		File dir=new File(path);
		if(dir.isDirectory()){
			Desktop d=Desktop.getDesktop();
			d.open(dir);
		}
		else
			throw new Exception("Il percorso non è una cartella");
	}
	public static void esploraWeb(String url){
		Desktop d=Desktop.getDesktop();
		try {
			d.browse(new URI(url));
		}
		catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void email(String uri){
		Desktop d=Desktop.getDesktop();
		try {
			d.mail(new URI(uri));
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	public static boolean fileExists(String path){
		File f=new File(path);
		//FIXME remove this
		System.out.println("Controllando esistenza file: "+f.getAbsolutePath());
		if(f.exists()){
			if(f.isFile())
				return true;
			else
				return false;
		}
		else
			return false;
	}
	public static String cercavideofile(int serie, int puntata, String nome, String folder, boolean is720p, boolean isRepack){
		String folder_c=Settings.getDirectoryDownload()+File.separator+folder;
		File cartella=new File(folder_c);
		if(cartella.exists()){
			if(cartella.isDirectory()){
				String[] lista=cartella.list();
				String puntata_s=puntata<10?"0"+puntata:puntata+"";
				String serie_s=serie<10?"0"+serie:serie+"";
				//System.out.println(puntata_s+" "+serie_s);
				for(int i=0;i<lista.length;i++){
					//
					if(lista[i].endsWith(".srt"))
						continue;
					
					if(lista[i].contains("720p")!=is720p)
						continue;
					if(lista[i].contains("REPACK")!=isRepack)
						continue;
					
					if(lista[i].contains(nome))
						return lista[i];
					
					int index_serie=lista[i].indexOf(serie_s);
					if(index_serie<0 && serie_s.startsWith("0")){
						serie_s=serie+"";
						index_serie=lista[i].indexOf(serie_s);
					}
					String linea;
					linea=lista[i].substring(index_serie>0?index_serie+serie_s.length():0);
					int index_ep=linea.indexOf(puntata_s);
					if(index_serie>=0 && index_ep>=0){
						return lista[i];	
					}
				}
			}
		}
		return null;
	}
	public static String cercavideofile(Torrent t) throws FileNotFoundException, FileExistsException{
		String path_download=Settings.getDirectoryDownload()+(Settings.getDirectoryDownload().endsWith(File.separator)?"":File.separator)+t.getNomeSerieFolder();
		File cartella_download=new File(path_download);
		if(!cartella_download.exists())
			throw new FileNotFoundException("La cartella "+path_download+" non esiste");
		if(!cartella_download.isDirectory())
			throw new FileExistsException("Il percorso "+path_download+" non è una directory");
		//
		String[] lista=cartella_download.list();
		String puntata_s=t.getPuntata()<10?"0"+t.getPuntata():t.getPuntata()+"";
		String serie_s=t.getSerie()<10?"0"+t.getSerie():t.getSerie()+"";
		for(int i=0;i<lista.length;i++){
			if(!(lista[i].endsWith(".avi")||lista[i].endsWith(".mp4")||lista[i].endsWith(".mkv")))
				continue;
			
			if(lista[i].contains("720p")!=t.is720p())
				continue;
			if(lista[i].contains("REPACK")!=t.isRepack())
				continue;
			
			if(lista[i].contains(t.getName()))
				return lista[i];
			
			int index_serie=lista[i].indexOf(serie_s);
			if(index_serie<0 && serie_s.startsWith("0")){
				serie_s=t.getSerie()+"";
				index_serie=lista[i].indexOf(serie_s);
			}
			String linea;
			linea=lista[i].substring(index_serie>0?index_serie+serie_s.length():0);
			int index_ep=linea.indexOf(puntata_s);
			if(index_serie>=0 && index_ep>=0){
				return lista[i];	
			}
		}
		//
		
		throw new FileNotFoundException("File non trovato");
	}
	public static boolean fileexistspartialfilename(String inizio, String fine, String folder){
		String path=Settings.getDirectoryDownload()+File.separator+folder;
		File cartella=new File(path);
		if(cartella.exists()){
			if(cartella.isDirectory()){
				String[] elenco_file=cartella.list();
				for(int i=0;i<elenco_file.length;i++){
					if(elenco_file[i].startsWith(inizio) && elenco_file[i].endsWith(fine))
						return true;
				}
			}
		}
		return false;
	}
	public static ArrayList<String> ZipDecompress(String input_zip, String cartella_output) throws IOException {
		ArrayList<String> estratti=new ArrayList<String>();
		ZipInputStream input = new ZipInputStream(new FileInputStream(input_zip));
		ZipEntry zipEntry;
		while ((zipEntry = input.getNextEntry()) != null) {
			if(zipEntry.isDirectory())
				continue;
			
			String nome_file=zipEntry.getName();
			if(zipEntry.getName().contains(File.separator)){
				nome_file=nome_file.substring(nome_file.lastIndexOf(File.separator)+1);
			}
			if(nome_file.compareToIgnoreCase(".DS_Store")==0)
				continue;
			if(nome_file.compareToIgnoreCase(".Thumbs.db")==0)
				continue;
			
			/*
			if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".srt")!=0)
				continue;
			else if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".ass")!=0)
				continue;
			else if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".ub")!=0)
				continue;
			*/
			
			//FIXME remove this
			//System.out.println("Estraendo: "+nome_file);
			File dir_extr=new File(cartella_output);
			if(!dir_extr.exists())
				dir_extr.mkdir();
			FileOutputStream fos = new FileOutputStream(cartella_output+(cartella_output.endsWith(File.separator)?"":File.separator)+nome_file);

			try {
				byte[] readBuffer = new byte[4096];
				int bytesIn = 0;
				while ((bytesIn = input.read(readBuffer)) != -1)
					fos.write(readBuffer, 0, bytesIn);
				estratti.add(nome_file);
			}
			finally {
				fos.close();
			}
		}
		input.close();
		for(int i=0;i<estratti.size();i++)
			System.out.println(estratti.get(i));
		return estratti;
	}
}
