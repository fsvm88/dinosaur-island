package dinolib;

public class CellaConDinosauro extends Cella {
	/**
	 * Crea la cella CellaConDinosauro e imposta il campo cellaSuCuiSiTrova con il riferimento alla cella passato durante l'inizializzazione della stessa.
	 */
	public CellaConDinosauro(String idDinosauroOccupante, Cella cellaSuCuiSiTrova){
		super(idDinosauroOccupante, cellaSuCuiSiTrova);
	}
}
