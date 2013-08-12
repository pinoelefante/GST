package Programma;

public class Update {
	public static void start() {
		if(Settings.isNewUpdate() || Settings.getVersioneSoftware()>Settings.getLastVersion()){
			switch(Settings.getLastVersion()){
				default:
					Settings.setLastVersion(Settings.getVersioneSoftware());
					Settings.setNewUpdate(false);
			}
		}
	}
}
