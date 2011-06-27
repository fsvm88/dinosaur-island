package server.BackendCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dinolib.Exceptions.GenericDinosauroException;
import dinolib.Exceptions.InvalidIDException;
import dinolib.Exceptions.InvalidTokenException;
import dinolib.Exceptions.NomeRazzaOccupatoException;
import dinolib.Exceptions.NonCollegatoException;
import dinolib.Exceptions.NonIlTuoTurnoException;
import dinolib.Exceptions.NonInPartitaException;
import dinolib.Exceptions.RazzaNonCreataException;
import dinolib.Exceptions.TroppiGiocatoriException;
import dinolib.Exceptions.UserAuthenticationFailedException;
import dinolib.Exceptions.UserExistsException;
import dinolib.GameObjects.Coord;

public interface Adapter extends Remote {
	Object creaUtente(String nomeUtente, String passwordUtente) throws UserExistsException, RemoteException;
	Object loginUtente(String nomeUtente, String passwordUtente) throws UserAuthenticationFailedException, RemoteException;
	Object creaRazza(String token, String nomeRazza, Character tipo) throws NomeRazzaOccupatoException, InvalidTokenException, RemoteException;
	Object accessoPartita(String token) throws InvalidTokenException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException, RemoteException;
	Object uscitaPartita(String token) throws InvalidTokenException, RemoteException;
	Object listaGiocatori(String token) throws InvalidTokenException, NonInPartitaException, NonCollegatoException, RemoteException;
	Object classifica(String token) throws InvalidTokenException, NonInPartitaException, NonCollegatoException, RemoteException;
	Object logoutUtente(String token) throws InvalidTokenException, RemoteException;
	Object mappaGenerale(String token) throws NonInPartitaException, InvalidTokenException, RemoteException;
	Object listaDinosauri(String token) throws InvalidTokenException, RazzaNonCreataException, NonInPartitaException, RemoteException;
	Object vistaLocale(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException, RemoteException;
	Object statoDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException, RemoteException;
	Object muoviDinosauro(String token, String idDinosauro, Coord newCoord) throws InvalidTokenException, GenericDinosauroException, RemoteException;
	Object cresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, GenericDinosauroException, RemoteException;
	Object deponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException, RemoteException;
	Object confermaTurno(String token) throws NonIlTuoTurnoException, InvalidTokenException, NonInPartitaException, RemoteException;
	Object passaTurno(String token) throws InvalidTokenException, NonInPartitaException, NonIlTuoTurnoException, RemoteException;
}