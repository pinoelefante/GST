package GUI;

import javax.swing.border.TitledBorder;

import Programma.Settings;

public class Language {
	public static final String[] lingue={"Italiano", "English"};
	public static enum lingue_n{
		ITALIANO,
		INGLESE
	};
	
	protected static String	TITLE;
	public static String	INSERIMENTO_TITLE;
	public static String	INSERIMENTO_LABEL_EZTV;
	public static String	INSERIMENTO_LABEL_PROPRIE;
	public static String	INSERIMENTO_BOTTONE_AGGIUNGI;
	public static String	INSERIMENTO_BOTTONE_RICARICA;
	public static String	INSERIMENTO_BOTTONE_RIMUOVI;
	public static String	TAB_DOWNLOAD_TITLE;
	public static String	TAB_DOWNLOAD_TOOLTIP;
	public static String	DOWNLOAD_BOTTONE_AGGIORNA;
	public static String	DOWNLOAD_BOTTONE_TORRENT_OFFLINE;
	public static String	DOWNLOAD_BOTTONE_SELEZIONA_TUTTO;
	public static String	DOWNLOAD_BOTTONE_DESELEZIONA_TUTTO;
	public static String	DOWNLOAD_BOTTONE_INVERTI;
	public static String	DOWNLOAD_BOTTONE_720P;
	public static String	DOWNLOAD_BOTTONE_VISTE;
	public static String	DOWNLOAD_BOTTONE_DOWNLOAD;
	public static String	TAB_OPZIONI_LABEL;
	public static String	TAB_OPZIONI_TOOLTIP;
//	public static String	OPZIONI_PANEL_CLIENT_LABEL;
	public static String	OPZIONI_CLIENT_LABEL_PERCORSO;
	public static String	OPZIONI_CLIENT_BOTTONE_CLIENTPATH;
	public static String	OPZIONI_CLIENT_LABEL_DIRECTORYDOWNLOAD;
	public static String	OPZIONI_CLIENT_BOTTONE_DIRECTORYDOWNLOAD;
//	public static String	OPZIONI_PANEL_AVVIO_LABEL;
	public static String	OPZIONI_AVVIO_AVVIO_WINDOWS;
	public static String	OPZIONI_AVVIO_AVVIO_ICONA;
	public static String	OPZIONI_AVVIO_CONFERMA_CHIUSURA;
//	public static String	OPZIONI_PANEL_RICERCA_LABEL;
	public static String	OPZIONI_RICERCA_ABILITARICERCA;
	public static String	OPZIONI_RICERCA_MINUTIRICERCA;
	public static String	OPZIONI_AGGIORNAMENTI;
	public static String	OPZIONI_SALVAOPZIONI;
//	public static String	OPZIONI_RICERCA_MOSTRAPUNTATEDOWNLOAD;
//	public static String	TAB_PREMIUM_LABEL;
//	public static String	TAB_PREMIUM_TOOLTIP;
//	public static String	DONAZIONI_TESTO;
//	public static String	DONAZIONI_BOTTONE_DONAZIONE;
	public static String	DIALOGUE_EXIT_PROMPT;
	public static String	DIALOGUE_EXIT_PROMPT_TITLE;
	public static String	DIALOGUE_CONNECTION_FAULT;
	public static String	INSERIMENTO_LABEL_INSERITO;
	public static String	INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE;
	public static String	INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE_TITLE;
	public static String	INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA;
	public static String	INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA_TITLE;
	public static String	INSERIMENTO_LABEL_RIMOSSA;
	public static String	INSERIMENTO_DIALOGUE_ERRORE_RIMOZIONE;
	public static String	DOWNLOAD_DIALOGUE_ERRORE_CLIENT;
	public static String	DOWNLOAD_PUNTATE;
	public static String	DOWNLOAD_DIALOGUE_GIA_VISTE;
	public static String	DOWNLOAD_DIALOGUE_GIA_VISTE_TITLE;
	public static String	OPZIONI_MINUTI_RICERCA_TOOLTIP;
	public static String	TRAYICON_RIPRISTINA;
	public static String	TRAYICON_ESCI;
	public static String	DIALOGUE_ERROR_UPDATE;
//	public static String	DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO;
//	public static String	DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO_TITLE;
	public static String	DIALOGUE_UPDATE_ERROR_DOWNLOAD;
	public static String	DIALOGUE_UPDATE_ULTIMA_VERSIONE;
	public static String	DOWNLOAD_ATTENDERE;
	public static String	OPZIONI_LINGUA;
//	public static String	DIALOGUE_CAMBIO_LINGUA;
	public static String	TAB_SOTTOTITOLI_LABEL;
	public static String	TAB_REFACTOR_LABEL;
//	public static String	REFACTOR_BOTTONE_MODIFICA;
//	public static String	REFACTOR_BOTTONE_SALVA;
	public static String	REFACTOR_LABEL_NOMETORRENT;
//	public static String	REFACTOR_LABEL_INSTR1;
	public static String	TAB_REFACTOR_TOOLTIP;
//	public static String	TRAYICON_LOADING;
//	public static String	TRAYICON_EXIT_BEFORE_LOAD;
	public static String	INSERIMENTO_BOXRICERCA;
//	public static String	LIBRARY_DOWNLOAD;
//	public static String 	OPZIONI_PARSING_NOME_TORRENT;
	public static String	REFACTOR_LABEL_DOWNLOADED;
	public static String	REFACTOR_LABEL_TODOWNLOAD;
	public static String 	TAB_DOWNLOAD_EPISODI;
	public static String	OPZIONI_ITASA;
	public static String	OPZIONI_DEFAULT;
	public static String	OPZIONI_ALWAYSONTOP;
	public static String 	OPZIONI_ESPLORA;
	public static String	OPZIONI_VLC_LABEL;
	public static String	TAB_ABOUT;
	public static String	ABOUT_DONATION, ABOUT_ON_DONATION, ABOUT_SITO, ABOUT_ON_SITO;
	public static String	ABOUT_MAIL;

	public static void setDefault() {
		setItaliano();
	}

	public static void setLanguage(int lingua) {
		switch (lingua) {
			case 1:
				setItaliano();
				break;
			case 2:
				setInglese();
				break;
			default:
				setDefault();
		}
	}

	public static void setItaliano() {
		TITLE = "GestioneSerieTV v.5_"+Settings.getVersioneSoftware()+" - by pinoelefante";

		INSERIMENTO_TITLE = "Inserimento";

		INSERIMENTO_LABEL_EZTV = "Disponibili";
		INSERIMENTO_LABEL_PROPRIE = "Preferite";
		INSERIMENTO_BOTTONE_AGGIUNGI = "Aggiungi";
		INSERIMENTO_BOTTONE_RICARICA = "Lista";
		INSERIMENTO_BOTTONE_RIMUOVI = "Rimuovi";
		TAB_DOWNLOAD_TITLE = "Download";
		TAB_DOWNLOAD_TOOLTIP = "Scarica le serie tv";
		DOWNLOAD_BOTTONE_AGGIORNA = "Aggiorna";
		DOWNLOAD_BOTTONE_TORRENT_OFFLINE = "Torrent offline";
		DOWNLOAD_BOTTONE_SELEZIONA_TUTTO = "Seleziona tutto";
		DOWNLOAD_BOTTONE_DESELEZIONA_TUTTO = "Deseleziona tutto";
		DOWNLOAD_BOTTONE_INVERTI = "Inverti selezione";
		DOWNLOAD_BOTTONE_720P = "720p";
		DOWNLOAD_BOTTONE_VISTE = "Già viste";
		DOWNLOAD_BOTTONE_DOWNLOAD = "Scarica";
		TAB_OPZIONI_LABEL = "Opzioni";
		TAB_OPZIONI_TOOLTIP = "";
//		OPZIONI_PANEL_CLIENT_LABEL = "Impostazioni client";
		OPZIONI_CLIENT_LABEL_PERCORSO = "Percorso uTorrent: ";
		OPZIONI_CLIENT_BOTTONE_CLIENTPATH = "Percorso uTorrent";
		OPZIONI_CLIENT_LABEL_DIRECTORYDOWNLOAD = "Percorso download: ";
		OPZIONI_CLIENT_BOTTONE_DIRECTORYDOWNLOAD = "Destinazione";
//		OPZIONI_PANEL_AVVIO_LABEL = "Opzioni apertura/chiusura";
		OPZIONI_AVVIO_AVVIO_WINDOWS = "Avvia con il sistema operativo";
		OPZIONI_AVVIO_AVVIO_ICONA = "Avvia ridotto a icona";
//		OPZIONI_PARSING_NOME_TORRENT = "Parsing nome torrent (NomeSerie SxE - es: Lost 1x01)";
		OPZIONI_AVVIO_CONFERMA_CHIUSURA = "Chiedi conferma prima di uscire";
//		OPZIONI_PANEL_RICERCA_LABEL = "Ricerca automatica";
		OPZIONI_RICERCA_ABILITARICERCA = "Abilita download automatico di tutte le puntate";
		OPZIONI_RICERCA_MINUTIRICERCA = "minuti tra ogni ricerca";
		OPZIONI_AGGIORNAMENTI = "Cerca aggiornamenti";
		OPZIONI_SALVAOPZIONI = "Salva";
//		OPZIONI_RICERCA_MOSTRAPUNTATEDOWNLOAD = "All'avvio, cerca i torrent automaticamente";
//		TAB_PREMIUM_LABEL = "";
//		TAB_PREMIUM_TOOLTIP = "";
//		DONAZIONI_TESTO = "Se apprezzi quest'applicazione, effettua una piccola donazione.\nAnche la pi� piccola forma di generosit�� gradita.\n\nAiutami a supportare lo sviluppo dell'applicazione,\nnon chiedo mica di conquistare il mondo! :P\n\nGrazie :)";
//		DONAZIONI_BOTTONE_DONAZIONE = "Effettua donazione";
		DIALOGUE_EXIT_PROMPT = "Vuoi veramente chiudere il programma?";
		DIALOGUE_EXIT_PROMPT_TITLE = "Chiudi";
		DIALOGUE_CONNECTION_FAULT = "Connessione non disponibile o sito irraggiungibile";
		INSERIMENTO_LABEL_INSERITO = " aggiunta";
		INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE = "Vuoi davvero cancellare:\n";
		INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE_TITLE = "Rimozione";
		INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA = "Vuoi cancellare la cartella contenente le puntate già scaricate?";
		INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA_TITLE = "Cancella cartella";
		INSERIMENTO_LABEL_RIMOSSA = " rimossa";
		INSERIMENTO_DIALOGUE_ERRORE_RIMOZIONE = "Errore nell'eliminazione";
		DOWNLOAD_DIALOGUE_ERRORE_CLIENT = "Percorso client invalido\nSettare il percorso del client nella tab \"Opzioni\"";
		DOWNLOAD_PUNTATE = " puntate";
		DOWNLOAD_DIALOGUE_GIA_VISTE = "Vuoi impostare come già viste le puntate selezionate?";
		DOWNLOAD_DIALOGUE_GIA_VISTE_TITLE = "Conferma visione";
		OPZIONI_MINUTI_RICERCA_TOOLTIP = "Clicca \"" + OPZIONI_SALVAOPZIONI + "\" per rendere l'opzione effettiva. Se il formato dei minuti è sbagliato, default: 480 minuti";
		TRAYICON_RIPRISTINA = "Ripristina";
		TRAYICON_ESCI = "Esci";
		DIALOGUE_ERROR_UPDATE = "Impossibile verificare il software";

//		DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO = "Un nuovo aggiornamento � stato installato.\nVuoi riavviare ora il programma?";
//		DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO_TITLE = "Aggiornamento " + TITLE;
		DIALOGUE_UPDATE_ERROR_DOWNLOAD = "Impossibile scaricare l'aggiornamento";
		DIALOGUE_UPDATE_ULTIMA_VERSIONE = "E' in uso l'ultima versione disponibile";
		DOWNLOAD_ATTENDERE = "Attendere...";
		OPZIONI_LINGUA = "Lingua: ";
//		DIALOGUE_CAMBIO_LINGUA = "Devi riavviare l'applicazione affinch� le modifiche abbiano effetto";
		TAB_SOTTOTITOLI_LABEL = "Sottotitoli";
		TAB_REFACTOR_LABEL = "Lettore";
//		REFACTOR_BOTTONE_MODIFICA = "Modifica";
//		REFACTOR_BOTTONE_SALVA = "Salva";
		TAB_REFACTOR_TOOLTIP = "Modifica lo stato di visualizzazione delle puntate";
		REFACTOR_LABEL_NOMETORRENT = "  Nome Torrent: ";
//		REFACTOR_LABEL_INSTR1 = "Seleziona una serie e clicca " + REFACTOR_BOTTONE_MODIFICA + " per modificare lo stato di visualizzazione delle puntate.";
//		TRAYICON_LOADING = "Attendere il caricamento del programma";
//		TRAYICON_EXIT_BEFORE_LOAD = "Attendere il caricamento del programma prima di chiuderlo.";
		INSERIMENTO_BOXRICERCA = "Cerca...";
//		LIBRARY_DOWNLOAD = "Verifica librerie...\nAttendere";
		
		REFACTOR_LABEL_DOWNLOADED="SCARICATA";
		REFACTOR_LABEL_TODOWNLOAD="DA SCARICARE";
		TAB_DOWNLOAD_EPISODI="Episodi";
		OPZIONI_ITASA="Abilita ItaSA";
		OPZIONI_DEFAULT="Predefinito";
		OPZIONI_ALWAYSONTOP="Sempre in primo piano";
		OPZIONI_ESPLORA="Esplora";
		OPZIONI_VLC_LABEL="Percorso VLC: ";
		TAB_ABOUT="Info...";
		ABOUT_DONATION="Donazione"; 
		ABOUT_ON_DONATION="Clicca per donare";
		ABOUT_SITO="Visita http://pinoelefante.altervista.org";
		ABOUT_ON_SITO="Clicca per visitare http://pinoelefante.altervista.org";
		ABOUT_MAIL="Contattami";
	}

	public static void setInglese() {
		TITLE = "TV Shows Manager v.5_"+Settings.getVersioneSoftware()+" - by pinoelefante";

		INSERIMENTO_TITLE = "Add series";

		INSERIMENTO_LABEL_EZTV = "Available";
		INSERIMENTO_LABEL_PROPRIE = "Favourites";
		INSERIMENTO_BOTTONE_AGGIUNGI = "Add";
		INSERIMENTO_BOTTONE_RICARICA = "List";
		INSERIMENTO_BOTTONE_RIMUOVI = "Remove";
		TAB_DOWNLOAD_TITLE = "Download";
		TAB_DOWNLOAD_TOOLTIP = "Downloads episodes";
		DOWNLOAD_BOTTONE_AGGIORNA = "Update";
		DOWNLOAD_BOTTONE_TORRENT_OFFLINE = "Offline Torrents";
		DOWNLOAD_BOTTONE_SELEZIONA_TUTTO = "Select All";
		DOWNLOAD_BOTTONE_DESELEZIONA_TUTTO = "Deselect All";
		DOWNLOAD_BOTTONE_INVERTI = "Invert selection";
		DOWNLOAD_BOTTONE_720P = "720p";
		DOWNLOAD_BOTTONE_VISTE = "Already seen";
		DOWNLOAD_BOTTONE_DOWNLOAD = "Download";
		TAB_OPZIONI_LABEL = "Settings";
		TAB_OPZIONI_TOOLTIP = "";
//		OPZIONI_PANEL_CLIENT_LABEL = "Client Settings";
		OPZIONI_CLIENT_LABEL_PERCORSO = "Client's Path: ";
		OPZIONI_CLIENT_BOTTONE_CLIENTPATH = "Client's path";
		OPZIONI_CLIENT_LABEL_DIRECTORYDOWNLOAD = "Download directory: ";
		OPZIONI_CLIENT_BOTTONE_DIRECTORYDOWNLOAD = "Download directory";
//		OPZIONI_PANEL_AVVIO_LABEL = "Open/Close settings";
		OPZIONI_AVVIO_AVVIO_WINDOWS = "Start when Windows starts";
		OPZIONI_AVVIO_AVVIO_ICONA = "Start minimized";
//		OPZIONI_PARSING_NOME_TORRENT = "Parsing torrent's name (Series SxE - e.g.: Lost 1x01)";
		OPZIONI_AVVIO_CONFERMA_CHIUSURA = "Show confirmation dialogue on exit";
//		OPZIONI_PANEL_RICERCA_LABEL = "Auto Search";
		OPZIONI_RICERCA_ABILITARICERCA = "Enable automatic search and download";
		OPZIONI_RICERCA_MINUTIRICERCA = "minutes beetween every search";
		OPZIONI_AGGIORNAMENTI = "Update";
		OPZIONI_SALVAOPZIONI = "Save";
//		OPZIONI_RICERCA_MOSTRAPUNTATEDOWNLOAD = "At start, show new torrents";
//		TAB_PREMIUM_LABEL = "";
//		TAB_PREMIUM_TOOLTIP = "";
//		DONAZIONI_TESTO = "If you appreciate this program, make a donation.\n\nHelp me to develop this program and add new functions,\nI don't ask to conquer the world :P\n\nThanks :)";
//		DONAZIONI_BOTTONE_DONAZIONE = "Donate!";
		DIALOGUE_EXIT_PROMPT = "Are you sure you want to exit GestioneSerieTV?";
		DIALOGUE_EXIT_PROMPT_TITLE = "Exit";
		DIALOGUE_CONNECTION_FAULT = "Connection not avaible or unreachable website";
		INSERIMENTO_LABEL_INSERITO = " added";
		INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE = "Do you want delete:\n";
		INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE_TITLE = "Deleting...";
		INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA = "Do you want delete the folder?";
		INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA_TITLE = "Folder delete";
		INSERIMENTO_LABEL_RIMOSSA = " deleted";
		INSERIMENTO_DIALOGUE_ERRORE_RIMOZIONE = "An error occurred during deleting folder";
		DOWNLOAD_DIALOGUE_ERRORE_CLIENT = "Invalid client path\nSet client path in tab \"Settings\"";
		DOWNLOAD_PUNTATE = " episodes";
		DOWNLOAD_DIALOGUE_GIA_VISTE = "Do you want set this episodes as already seen?";
		DOWNLOAD_DIALOGUE_GIA_VISTE_TITLE = "Confirm already seen";
		OPZIONI_MINUTI_RICERCA_TOOLTIP = "Click \"" + OPZIONI_SALVAOPZIONI + "\" to save the option. If format is wrong, default: 60 minutes";
		TRAYICON_RIPRISTINA = "Restore";
		TRAYICON_ESCI = "Exit";
		DIALOGUE_ERROR_UPDATE = "Error: version";

//		DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO = "Software updated.\nWould you restart it now?";
//		DIALOGUE_UPDATE_NUOVOAGGIORNAMENTO_TITLE = TITLE + " update";
		DIALOGUE_UPDATE_ERROR_DOWNLOAD = "An error occurred while downloading new version";
		DIALOGUE_UPDATE_ULTIMA_VERSIONE = "No update is available";
		DOWNLOAD_ATTENDERE = "Wait please...";
		OPZIONI_LINGUA = "Language: ";
//		DIALOGUE_CAMBIO_LINGUA = "You have to restart the application for the changes to take effect";
		TAB_SOTTOTITOLI_LABEL = "Subtitles";
		TAB_REFACTOR_LABEL = "Player";
//		REFACTOR_BOTTONE_MODIFICA = "Modify";
//		REFACTOR_BOTTONE_SALVA = "Save";
		REFACTOR_LABEL_NOMETORRENT = "  Torrent's name: ";
		TAB_REFACTOR_TOOLTIP = "Modifica lo stato di visualizzazione delle puntate";
//		REFACTOR_LABEL_INSTR1 = "Select a TV Show and click on " + REFACTOR_BOTTONE_MODIFICA + " to modify episode' seen/unseen status. At the end click on " + REFACTOR_BOTTONE_SALVA + ".";
//		TRAYICON_LOADING = "Wait, please. Loading...";
//		TRAYICON_EXIT_BEFORE_LOAD = "You can't close it now. Loading...";
		INSERIMENTO_BOXRICERCA = "Search...";
//		LIBRARY_DOWNLOAD = "Checking libraries...\nWait please";
		
		REFACTOR_LABEL_DOWNLOADED="DOWNLOADED";
		REFACTOR_LABEL_TODOWNLOAD="TO DOWNLOAD";
		TAB_DOWNLOAD_EPISODI="Episodes";
		OPZIONI_ITASA="Enable ItaSA";
		OPZIONI_DEFAULT="Default";
		OPZIONI_ALWAYSONTOP="Always on top";
		OPZIONI_ESPLORA="Explore";
		OPZIONI_VLC_LABEL="VLC's path: ";
		TAB_ABOUT="About...";
		
		ABOUT_DONATION="Donate"; 
		ABOUT_ON_DONATION="Click to donate";
		ABOUT_SITO="Visit http://pinoelefante.altervista.org";
		ABOUT_ON_SITO="Click to visit http://pinoelefante.altervista.org";
		ABOUT_MAIL="Send me an email";
	}

	public static void setLang() {
		Interfaccia.opzioni_label_client.setText("Client: ");
		Interfaccia.opzioni_label_path_download.setText(OPZIONI_CLIENT_LABEL_DIRECTORYDOWNLOAD);
		Interfaccia.opzioni_label_path_client.setText(OPZIONI_CLIENT_LABEL_PERCORSO);
		Interfaccia.opzioni_box_askonclose.setText(OPZIONI_AVVIO_CONFERMA_CHIUSURA);
//		Interfaccia.opzioni_box_parse_nomi.setText(OPZIONI_PARSING_NOME_TORRENT);
		Interfaccia.opzioni_box_starthidden.setText(OPZIONI_AVVIO_AVVIO_ICONA);
		Interfaccia.opzioni_box_startwindows.setText(OPZIONI_AVVIO_AVVIO_WINDOWS);
		Interfaccia.opzioni_box_abilita_ricerca.setText(OPZIONI_RICERCA_ABILITARICERCA);
		Interfaccia.opzioni_label_cerca_min.setText(OPZIONI_RICERCA_MINUTIRICERCA);

		Interfaccia.opzioni_label_lingua.setText(OPZIONI_LINGUA);
		Interfaccia.opzioni_bottone_directory_download.setText(OPZIONI_CLIENT_BOTTONE_DIRECTORYDOWNLOAD);
		Interfaccia.opzioni_bottone_seleziona_client.setText(OPZIONI_CLIENT_BOTTONE_CLIENTPATH);
		Interfaccia.opzioni_bottone_salva.setText(OPZIONI_SALVAOPZIONI);
		Interfaccia.about_bottone_update.setText(OPZIONI_AGGIORNAMENTI);
		Interfaccia.opzioni_textfield_minuti.setToolTipText(OPZIONI_MINUTI_RICERCA_TOOLTIP);

		Interfaccia.download_bottone_aggiungi.setText(INSERIMENTO_BOTTONE_AGGIUNGI);
		Interfaccia.download_bottone_rimuovi.setText(INSERIMENTO_BOTTONE_RIMUOVI);
		Interfaccia.download_bottone_reload.setText(INSERIMENTO_BOTTONE_RICARICA);
		Interfaccia.download_text_top_left.setText(INSERIMENTO_BOXRICERCA);
		Interfaccia.download_text_top_right.setText(INSERIMENTO_BOXRICERCA);
		Interfaccia.download_bottone_aggiorna_torrent.setText(DOWNLOAD_BOTTONE_AGGIORNA);
		Interfaccia.download_bottone_torrent_offline.setText(DOWNLOAD_BOTTONE_TORRENT_OFFLINE);
		Interfaccia.download_bottone_selectAll.setText(DOWNLOAD_BOTTONE_SELEZIONA_TUTTO);
		Interfaccia.download_bottone_inverti_selezione.setText(DOWNLOAD_BOTTONE_INVERTI);
		Interfaccia.download_bottone_720p.setText(DOWNLOAD_BOTTONE_720P);
		Interfaccia.download_bottone_already_seen.setText(DOWNLOAD_BOTTONE_VISTE);
		Interfaccia.download_bottone_download.setText(DOWNLOAD_BOTTONE_DOWNLOAD);
		((TitledBorder) Interfaccia.download_panel_download.getBorder()).setTitle(TAB_DOWNLOAD_TITLE);
		((TitledBorder) Interfaccia.download_panel_inserimento.getBorder()).setTitle(INSERIMENTO_TITLE);

//		Interfaccia.libreria_bottone_modifica.setText(REFACTOR_BOTTONE_MODIFICA);
//		Interfaccia.libreria_bottone_salva.setText(REFACTOR_BOTTONE_SALVA);
		Interfaccia.opzioni_box_itasa.setText(OPZIONI_ITASA);
		Interfaccia.opzioni_bottone_ripristina.setText(OPZIONI_DEFAULT);
		Interfaccia.opzione_box_alwaysontop.setText(OPZIONI_ALWAYSONTOP);
		Interfaccia.opzioni_label_vlc.setText(OPZIONI_VLC_LABEL);
		
		Interfaccia.indirizzo.setText(Language.ABOUT_SITO);
		Interfaccia.about_email.setText(Language.ABOUT_MAIL);
	}
}
