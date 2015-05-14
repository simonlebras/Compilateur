package compilateur;

/**
 * Entr�e de la table des symboles
 * @author Simon
 * @version 1.0
 */
public class Entree{

	/**
	 * Offset pour l'adressage
	 */
	public static int offset = 0;
	
	/**
	 * Adresse de l'identificateur en m�moire
	 */
	private int adresse; 
	
	/**
	 * Nom de l'identificateur
	 */
	private String nom;
	
	/**
	 * Type de l'identificateur (programme, constante, variable)
	 */
	private String classe;
	
	/**
	 * Getter nom
	 * @return Nom de l'entr�e
	 */
	public String getNom(){
		return nom;
	}
	
	/**
	 * Getter classe
	 * @return Classe de l'entr�e
	 */
	public String getClasse(){
		return classe;
	}
	
	/**
	 * Getter adresse
	 * @return Adresse de l'entr�e
	 */
	public int getAdresse() {
		return adresse;
	}
	
	/**
	 * Constructeur
	 * @param n Nom de l'identificateur
	 * @param c Type de l'identificateur
	 */
	public Entree(String n, String c){
		nom = n;
		classe = c;
		if(classe.equals("program"))
			adresse = -1;
		else
			adresse = offset++;
	}
}
