package Sottotitoli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.apache.commons.io.FileExistsException;

import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.Torrent;

public class Renamer {
	public Renamer(){}
	
	public static String generaNomeDownload(Torrent t){
		if(t!=null){
			return t.getNomeSerie().replace(" ", "_")+"_S"+t.getSerie()+"_E"+t.getPuntata()+"_sub"+".zip";
		}
		throw new InvalidParameterException("E' stato passato un parametro non valido");
	}
	public static boolean rinominaSottotitolo(Torrent t){
		String nome_file="";
		try {
			nome_file=OperazioniFile.cercavideofile(t);
			if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".avi")==0){
				nome_file=nome_file.substring(0, nome_file.lastIndexOf(".")-1);
			}
			else if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".mp4")==0){
				nome_file=nome_file.substring(0, nome_file.lastIndexOf(".")-1);
			}
			else if(nome_file.substring(nome_file.lastIndexOf(".")).compareToIgnoreCase(".mkv")==0){
				nome_file=nome_file.substring(0, nome_file.lastIndexOf(".")-1);
			}
		} 
		catch (FileNotFoundException | FileExistsException e) {
			//TODO modificare comportamento quando non viene trovato il file
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		try {
			ArrayList<String> files=OperazioniFile.ZipDecompress(t.getNomeSerieFolder()+File.separator+generaNomeDownload(t), t.getNomeSerieFolder());
			if(files.size()>0){
				String dir_dest=Settings.getDirectoryDownload()+(Settings.getDirectoryDownload().endsWith(File.separator)?"":File.separator)+t.getNomeSerieFolder();
				for(int i=0;i<files.size();i++){
					File f=new File(dir_dest+File.separator+files.get(i));
					String estensione="";
					if(files.get(i).contains("."))
						estensione=files.get(i).substring(files.get(i).lastIndexOf("."));
					f.renameTo(new File(nome_file+"."+(i+1)+estensione));
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
