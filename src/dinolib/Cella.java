package dinolib;

public class Cella {	
	private String nome;
	
	Cella(nome){
		this.nome = nome;
	}
	
	public class Acqua extends Cella {
		 Acqua () {
			 super("acqua");
		 }
	}
	
	public class Terra extends Cella {
		Terra () {
			super("terra");
		}
	}
	
	public class Carogna extends Cella {
		Carogna () {
			super("carogna");
		}
	}
	
	public class Vegetazione extends Cella {
		Vegetazione () {
			super ("vegetazione")
		}
	}
	
	public String toString () {
		return this.nome;
	}
}
