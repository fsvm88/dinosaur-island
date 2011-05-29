package dinolib;

public class CellaConDinosauro extends Cella {
	/**
	 * Crea la cella CellaConDinosauro e imposta il campo cellaSuCuiSiTrova con il riferimento alla cella passato durante l'inizializzazione della stessa.
	 */
	public CellaConDinosauro(String idDinosauroOccupante, Cella cellaSuCuiSiTrova){
		super(idDinosauroOccupante, cellaSuCuiSiTrova);
	}
	/**
	 * Override del metodo di default toString.
	 * Non posso usare il nome "Dinosauro" per la classe, quindi devo ridefinire toString di modo da semplificare la logica di alto livello.
	 */
	public String toString() {
		return "Dinosauro";
	}
}
