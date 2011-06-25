package dinolib.CommunicationObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import dinolib.Giocatore;
import dinolib.Logica;
import dinolib.Exceptions.InvalidTokenException;

public class Classifica implements Serializable {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 2813658587475977859L;

	private ArrayList<TuplaPunteggio> myPunteggi = null;

	public Classifica(Logica myLogica) {
		myPunteggi = new ArrayList<TuplaPunteggio>();

		Iterator<Giocatore> itGiocatori = myLogica.getPMan().iterator();
		while (itGiocatori.hasNext()) {
			aggiungiUnaTupla(itGiocatori.next());
		}
	}

	private void aggiungiUnaTupla(Giocatore giocatore) {
		String curPlayingRazza = null;
		if (giocatore.hasRazza()) {
			curPlayingRazza = giocatore.getRazza().getNome();
		}
		Iterator<String> itRazze = giocatore.getPunteggio().iterator();
		String curRazza = null;
		while (itRazze.hasNext()) {
			curRazza = itRazze.next();
			if (curPlayingRazza != null) {
				if ((curRazza.equals(curPlayingRazza))) {
					myPunteggi.add(new TuplaPunteggio(giocatore.getNome(), curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), 's'));
				}
				else {
					myPunteggi.add(new TuplaPunteggio(giocatore.getNome(), curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), 'n'));
				}
			}
		}
	}
}
