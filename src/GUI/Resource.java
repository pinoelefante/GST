package GUI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Resource {
	public static Image createImage(String path, String description) {
		URL imageURL = Interfaccia.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		}
		return new ImageIcon(imageURL, description).getImage();
	}

	public static ImageIcon getIcona(String path) {
		URL imgURL = Resource.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, null);
		}
		return null;
	}
	public static void setImage(JLabel lab, String file_path, int max_w) throws IOException{
		if(lab==null)
			return;
		
		BufferedImage myPicture = ImageIO.read(new File(file_path));
		if(myPicture.getWidth()>max_w && max_w>=0){
			float percent_to_scale=(max_w)/(float)myPicture.getWidth();
			float new_h=myPicture.getHeight()*percent_to_scale;
			float new_w=myPicture.getWidth()*percent_to_scale;
			Image img=myPicture.getScaledInstance((int)new_w, (int)new_h, Image.SCALE_SMOOTH);
			BufferedImage new_image=new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			new_image.getGraphics().drawImage(img, 0, 0, null);
			myPicture=new_image;
		}
		lab.setText("");
		lab.setIcon(new ImageIcon(myPicture));
	}
}
