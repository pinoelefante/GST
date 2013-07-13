package Programma;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import Programma.ManagerException;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;


public class ArchiviZip {
	public static void main(String[] args) {
		try {
            SevenZip.initSevenZipFromPlatformJAR();
            System.out.println("7-Zip-JBinding library was initialized");
            estrai("D:\\SerieTV\\Baby Daddy\\Baby.Daddy.S02E07.HDTV.x264-ASAP.zip", ".srt", "ciao_pino", "D:\\SerieTV\\Baby Daddy\\");
        } 
		catch (SevenZipNativeInitializationException e) {
            e.printStackTrace();
		}
	}
	public static void estrai(String archivio, final String estensione, final String rename, final String cartella_destinazione){
		RandomAccessFile randomAccessFile = null;
        ISevenZipInArchive inArchive = null;
        
        try{
        	randomAccessFile=new RandomAccessFile(archivio, "r");
        	inArchive=SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
        	
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            
            ArrayList<ISimpleInArchiveItem> estrarre = new ArrayList<ISimpleInArchiveItem>();
            for(ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()){
            	if(!item.isFolder()){
            		if(item.getPath().toLowerCase().endsWith(estensione.toLowerCase())){
            			estrarre.add(item);
            		}
            	}
            }
            
            for(int i=0;i<estrarre.size();i++){
            	ISimpleInArchiveItem item=estrarre.get(i);
            	ExtractOperationResult res = item.extractSlow(new SevenZipExtractor(estensione, rename, cartella_destinazione, i));
            	System.out.println(res.name());
            }
            
        }
        catch(IOException e){
        	e.printStackTrace();
        } 
        catch (SevenZipException e) {
        	ManagerException.registraEccezione(e);
			e.printStackTrace();
		}
        finally {
        	if (inArchive != null) {
                try {
                    inArchive.close();
                } 
                catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e);
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } 
                catch (IOException e) {
                    System.err.println("Error closing file: " + e);
                }
            }
        }
	}
}
