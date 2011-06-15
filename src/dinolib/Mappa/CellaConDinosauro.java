package dinolib.Mappa;

class CellaConDinosauro extends Cella {
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
	public String toString() {
		return "Dinosauro";
	}
	
	/**
	 * Override dei due metodi della classe Cella utili per questa classe.
	 */
	public String getIdDelDinosauro() { return super.idDelDinosauro; }
	public Cella getCellaSuCuiSiTrova() { return super.cellaSuCuiSiTrova; }
}
