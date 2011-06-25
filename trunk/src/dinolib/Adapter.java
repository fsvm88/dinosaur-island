package dinolib;

import dinolib.Exceptions.GenericDinosauroException;
import dinolib.Exceptions.InvalidTokenException;
import dinolib.Exceptions.NomeRazzaOccupatoException;
import dinolib.Exceptions.NonInPartitaException;
import dinolib.Exceptions.RazzaNonCreataException;
import dinolib.Exceptions.TroppiGiocatoriException;
import dinolib.Exceptions.UserAuthenticationFailedException;
import dinolib.Exceptions.UserExistsException;
import dinolib.Mappa.Coord;

interface Adapter {
	Object creaUtente(String nomeUtente, String passwordUtente) throws UserExistsException;
	Object loginUtente(String nomeUtente, String passwordUtente) throws UserAuthenticationFailedException;
	Object creaRazza(String token, String nomeRazza, Character tipo) throws NomeRazzaOccupatoException, InvalidTokenException;
	Object accessoPartita(String token) throws InvalidTokenException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException;
	Object uscitaPartita(String token) throws InvalidTokenException;
	Object listaGiocatori(String token) throws InvalidTokenException, NonInPartitaException;
	Object classifica(String token) throws InvalidTokenException, NonInPartitaException;
	Object logoutUtente(String token) throws InvalidTokenException;
	Object mappaGenerale(String token);
	Object listaDinosauri(String token);
	Object vistaLocale(String token, String idDinosauro);
	Object statoDinosauro(String token, String idDinosauro);
	Object muoviDinosauro(String token, String idDinosauro, Coord newCoord) throws InvalidTokenException, GenericDinosauroException;
	Object cresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, GenericDinosauroException;
	Object deponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException;
	Object confermaTurno(String token);
	Object passaTurno(String token);
}