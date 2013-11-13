package Interfacce;

public interface ValueChangeNotifier {
	public void subscribe(ValueChangeSubscriber s);
	public void unsubscribe(ValueChangeSubscriber s);
	public void notificaValueChange();
}
