package GUI;

public class ThreadWait extends Thread {
	private int sec;
	public ThreadWait(int sec){
		this.sec=sec;
	}
	public void run() {
		while(sec>0){
			try {
				Thread.sleep(1000L);
			}catch (InterruptedException e) {}
			sec--;
		}
	}
}
