package interfaccia2;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Resource {
	public static void setImage(JLabel lab, String file_path, int max_w) throws IOException{
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
	public static void main(String[] args){
		JFrame frame=new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel img=new JLabel();
		JLabel img2=new JLabel();
		frame.add(img);
		frame.add(img2);
		try {
			setImage(img, "C:\\codec\\Koala.jpg", 200);
			setImage(img2, "C:\\codec\\Koala.jpg", 100);
		} 
		catch (IOException e) {
			img.setText("<html><i>IMMAGINE<br>NON<br>DISPONIBILE</i></html>");
		}
	}
}
