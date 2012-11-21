package GUI;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

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
}
