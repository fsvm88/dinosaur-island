package dinolib;

import dinolib.Mappa.Coord;

interface Adapter {
	Object creaUtente(String nomeUtente, String passwordUtente);
	Object loginUtente(String nomeUtente, String passwordUtente);
	Object creaRazza(String token, String nomeRazza, String tipo);
	Object accessoPartita(String token);
	Object uscitaPartita(String token);
	Object listaGiocatori(String token);
	Object classifica(String token);
	Object logoutUtente(String token);
	Object mappaGenerale(String token);
	Object listaDinosauri(String token);
	Object vistaLocale(String token, String idDinosauro);
	Object statoDinosauro(String token, String idDinosauro);
	Object muoviDinosauro(String token, String idDinosauro, Coord newCoord);
	Object cresciDinosauro(String token, String idDinosauro);
	Object deponiUovo(String token, String idDinosauro);
	Object confermaTurno(String token);
	Object passaTurno(String token);
}