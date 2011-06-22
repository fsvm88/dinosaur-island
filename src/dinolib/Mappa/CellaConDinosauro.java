package dinolib.Mappa;

import java.io.Serializable;

class CellaConDinosauro extends Cella implements Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = 1420527517663293865L;
	/**
	 * Crea la cella CellaConDinosauro e imposta il campo cellaSuCuiSiTrova con il riferimento alla cella passato durante l'inizializzazione della stessa.
	 */
	protected CellaConDinosauro(String idDinosauroOccupante, Cella cellaSuCuiSiTrova){
		super(idDinosauroOccupante, cellaSuCuiSiTrova);
	}
	/**
	 * Override del metodo di default toString.
	 * Non posso usare il nome "Dinosauro" per la classe, quindi devo ridefinire toString di modo da semplificare la logica di alto livello.
	 */
	public String toString() { return "Dinosauro"; }
	
	// Override dei due metodi della classe Cella utili per questa classe.
	/**
	 * Ritorna l'id del dinosauro presente su questa cella.
	 * @return L'id del dinosauro presente su questa cella.
	 */
	@Override
	public String getIdDelDinosauro() { return super.idDelDinosauro; }
	/**
	 * Ritorna la cella su cui il dinosauro si trova.
	 * @return La cella su cui si trova il dinosauro.
	 */
	@Override
	public Cella getCellaSuCuiSiTrova() { return super.cellaSuCuiSiTrova; }
}
