package InfoSerie;

public class EpisodioNotFound extends Exception {
	private static final long	serialVersionUID	= 1L;

	public EpisodioNotFound() {
	}

	public EpisodioNotFound(String message) {
		super(message);
	}
}
