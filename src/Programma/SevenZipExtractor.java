package Programma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;
import Programma.ManagerException;

public class SevenZipExtractor implements ISequentialOutStream{
	private String destinazione;
	public SevenZipExtractor(String estensione, String rename, String cartella_destinazione, int count){
		destinazione=cartella_destinazione+(cartella_destinazione.endsWith(File.separator)?"":File.separator)+rename+"."+count+estensione;
	}
	public int write(byte[] arg0) throws SevenZipException {
		try {
			System.out.println(destinazione);
			FileWriter file_out=new FileWriter(destinazione);
			String output=new String(arg0);
			file_out.write(output.toCharArray(), 0, arg0.length);
			file_out.close();
		} 
		catch (IOException e) {
			ManagerException.registraEccezione(e);
			e.printStackTrace();
		}
		return arg0.length;
	}
}
