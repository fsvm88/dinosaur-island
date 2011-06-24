package client.BackendCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import dinolib.Mappa.Coord;

public class SocketBackendCommunication extends BackendCommunication {
	/**
	 * Istanzia riferimento al socket.
	 * @uml.property  name="mySocket"
	 */
	private Socket mySocket = null;
	/**
	 * Istanzia riferimento al buffer per la ricezione dei dati.
	 * @uml.property  name="BufferedReader"
	 */
	private BufferedReader incomingData = null;
	/**
	 * Istanzia riferimento al buffer per l'invio dei dati.
	 * @uml.property  name="outgoingData"
	 */
	private PrintWriter outgoingData = null;

	public SocketBackendCommunication(String nomeHost, Integer numeroPorta) throws IOException {
		mySocket = new Socket(nomeHost, numeroPorta.intValue());
		incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
	}

	@Override
	public Object creaUtente(String nomeUtente, String passwordUtente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object loginUtente(String nomeUtente, String passwordUtente) {
		
		// TODO implementare!
		return null;
	}

	@Override
	public Object creaRazza(String token, String nomeRazza, Character tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accessoPartita(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object uscitaPartita(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object listaGiocatori(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object classifica(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object logoutUtente(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object mappaGenerale(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object listaDinosauri(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object vistaLocale(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object statoDinosauro(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object muoviDinosauro(String token, String idDinosauro,
			Coord newCoord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object cresciDinosauro(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deponiUovo(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object confermaTurno(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object passaTurno(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
