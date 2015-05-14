package compilateur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyseur lexical
 * @author Simon Le Bras
 * @version 1.0
 */
public class Analyseur{
	
	/**
	 * Dernier token lu
	 */
	private String token;
	
	/**
	 * Forme textuelle du dernier token lu
	 */
	private String sym;
	
	/**
	 * Résultat analyse lexicale
	 */
	private boolean valid;
	
	/**
	 * Variables permettant de se déplacer dans le code source
	 */
	private int lineNumber = 0, curseur = 0;
	
	/**
	 * Liste des instructions du code source
	 */
	private List<String> instructions = new ArrayList<String>();

	/**
	 * Table des symboles
	 */
	private List<Entree> tableSym = new ArrayList<Entree>();
	
	/**
	 * Pcode
	 */
	private List<String> pcode = new ArrayList<String>();
		
	/**
	 * Obtenir le résultat de l'analyse lexicale
	 * @return valid Résultat de l'analyse lexicale
	 */
	public boolean getValid(){
		return valid;
	}
	
	/**
	 * Constructeur
	 * @param file Fichier à analyser
	 */
	public Analyseur(String file){
		valid = true;
		readInstructions(file); //récupération des instructions
		program(); //analyse lexicale du code source
	}

	/**
	 * Récupération des instructions du code source
	 * @param file Fichier contenant le code source
	 */
	private void readInstructions(String file){
		try{
			BufferedReader b = new BufferedReader(new FileReader(file));
			String line;
			while((line = b.readLine()) != null){
				line = line.trim().replace('\t', ' ');
				if(!line.equals(""))
					instructions.add(line.trim());
			}
			b.close();
		}catch(IOException e){
			System.err.println("Erreur fichier");
			System.exit(1);
		}
	}

	/**
	 * Test sur le dernier token obtenu
	 * @param t Token à tester
	 * @param codeErreur Code erreur relatif au token
	 */
	private void teste(String t, String codeErreur){
		if(t.equals(token))
			nextToken();
		else
			printError(codeErreur);
	}
	
	/**
	 * Test sur le dernier token obtenu et le rentre dans la table ou vérifie sa présence
	 * @param t Token à tester
	 * @param c Classe à insérer dans la table
	 * @param check Vérifier la présence du token dans la table
	 */
	private void teste(String t, String c, boolean check){
		if(t.equals(token)){
			boolean present = chercherSymbole(c);
			if(!check){
				if(!present)
					tableSym.add(new Entree(sym, c));
				else
					printError("ERR_DBL_ID");
			}else{
				if(!present)
					printError("ERR_NO_ID");
			}
			nextToken();
		}else
			printError("ID_ERR");
	}

	/**
	 * Cherche le symbole dans la table
	 * @param c Classe permise
	 */
	private boolean chercherSymbole(String c) {
		for(Entree e : tableSym){
			if(e.getNom().equals(sym) && c.contains(e.getClasse()))
				return true;
		}
		return false;
	}

	/**
	 * Afficher la dernière erreur
	 * @param t Token attendu
	 */
	private void printError(String message){
		valid = false;
		System.err.println(message + " - ligne " + (lineNumber + 1));
		System.exit(1);
	}
	
	/**
	 * Récupération du prochain token
	 */
	private void nextToken(){
		char c;
		sym = "";
		boolean continuer = (lineNumber != instructions.size())? true : false;
		
		//prise en compte des commentaires
		if(continuer){
			while(continuer && instructions.get(lineNumber).matches("^\\(\\*.*\\*\\)$")){
				lineNumber++;
				if(lineNumber == instructions.size())
					continuer = false;
			}
		}
		
		if(continuer){
			while(continuer){
				c = instructions.get(lineNumber).charAt(curseur++);
				if(c == ' ' && sym.equals("")){
					continue;
				}else if(c == ' ' && !sym.equals("")){
					continuer = false;
				}else if((c == '<' || c == '>' || c == ':') && sym.equals("")){
					sym += c;
					if(curseur != instructions.get(lineNumber).length()){
						char nextC = instructions.get(lineNumber).charAt(curseur++);
						if(nextC != ' ')
							sym += nextC;
					}
					continuer = false;
				}else if((c == ';' || c == '.' || c == ',' || c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '=' || c == ':' || c == '<' || c == '>')){
					if(sym.equals(""))
						sym += c;
					else
						curseur--;
					continuer = false;
				}else
					sym += c;
				
				//passage à la ligne suivante du pcode
				if(curseur == instructions.get(lineNumber).length()){
					continuer = false;
					lineNumber++;
					curseur = 0;
				}
			}
			switch(sym){
				case "program":
					token = "PROGRAM_TOKEN";
					break;
				case ";":
					token = "PT_VIRG_TOKEN";
					break;
				case "const":
					token = "CONST_TOKEN";
					break;
				case "=":
					token = "EGAL_TOKEN";
					break;
				case "+":
					token = "PLUS_TOKEN";
					break;
				case "-":
					token = "MOINS_TOKEN";
					break;
				case "*":
					token = "MUL_TOKEN";
					break;
				case "/":
					token = "DIV_TOKEN";
					break;
				case "<>":
					token = "DIFF_TOKEN";
					break;
				case "<":
					token = "INF_TOKEN";
					break;
				case ">":
					token = "SUP_TOKEN";
					break;
				case "<=":
					token = "INF_EGAL_TOKEN";
					break;
				case ">=":
					token = "SUP_EGAL_TOKEN";
					break;
				case "(":
					token = "PAR_OUV_TOKEN";
					break;
				case ")":
					token = "PAR_FER_TOKEN";
					break;
				case ",":
					token = "VIRG_TOKEN";
					break;
				case ".":
					token = "POINT_TOKEN";
					break;
				case ":=":
					token = "AFFEC_TOKEN";
					break;
				case "begin":
					token = "BEGIN_TOKEN";
					break;
				case "end":
					token = "END_TOKEN";
					break;
				case "if":
					token = "IF_TOKEN";
					break;
				case "while":
					token = "WHILE_TOKEN";
					break;
				case "then":
					token = "THEN_TOKEN";
					break;
				case "do":
					token = "DO_TOKEN";
					break;
				case "write":
					token = "WRITE_TOKEN";
					break;
				case "read":
					token = "READ_TOKEN";
					break;
				case "var":
					token = "VAR_TOKEN";
					break;
				default:
					if(sym.matches("\\d+"))
						token = "NUM_TOKEN";
					else if(sym.matches("\\w[a-zA-Z0-9]*"))
						token = "ID_TOKEN";
					else
						token = "TOKEN_INCONNU";
					break;
			}
		}
	}

	/**
	 * Analyser le pcode
	 */
	private void program(){
		nextToken();
		teste("PROGRAM_TOKEN", "PROGRAM_ERR");
		teste("ID_TOKEN", "program", false);
		teste("PT_VIRG_TOKEN", "PT_VIRG_ERR");
		block();
		generer("HLT");
		if(!token.equals("POINT_TOKEN"))
			printError("POINT_ERR");
	}

	/**
	 * Règle BLOCK
	 */
	private void block(){
		if(token.equals("CONST_TOKEN"))
			consts();
		if(token.equals("VAR_TOKEN"))
			vars();
		generer("INT", Entree.offset);
		insts();
	}

	/**
	 * Génération d'une instruction de pcode
	 * @param s Code opération
	 */
	private void generer(String s){
		pcode.add(s);
	}
	
	/**
	 * Génération d'une instruction de pcode
	 * @param s Code opération
	 * @param o Opérande
	 */
	private void generer(String s, int o){
		pcode.add(s + " " + o);
	}

	/**
	 * Règle INSTS
	 */
	private void insts() {
		teste("BEGIN_TOKEN", "BEGIN_ERR"); 
		inst(); 
		while(token.equals("PT_VIRG_TOKEN")){ 
			nextToken();
			inst(); 
		}
		teste("END_TOKEN", "END_ERR"); 
	}

	/**
	 * Règle INST
	 */
	private void inst() {
		switch(token){ 
			case "ID_TOKEN":
				affec();
				break;
			case "IF_TOKEN":
				si();
				break;
			case "WHILE_TOKEN":
				tantque();
				break;
			case "BEGIN_TOKEN":
				insts();
				break;
			case "WRITE_TOKEN":
				ecrire();
				break;
			case "READ_TOKEN":
				lire();
				break;
		}		
	}

	/**
	 * Règle LIRE
	 */
	private void lire() {
		teste("READ_TOKEN", "READ_ERR"); 
		teste("PAR_OUV_TOKEN", "PAR_OUV_ERR");
		String prec = sym;
		teste("ID_TOKEN", "var", true);
		Entree e = trouverEntree(prec);
		generer("LDA", e.getAdresse());
		generer("INN");
		while(token.equals("VIRG_TOKEN")){
			nextToken();
			prec = sym;
			teste("ID_TOKEN", "var", true);
			e = trouverEntree(prec);
			generer("LDA", e.getAdresse());
			generer("INN");
		}
		teste("PAR_FER_TOKEN", "PAV_FER_ERR"); 
	}

	/**
	 * Trouver une entrée dans la table des symboles
	 * @param prec Nom de l'entree
	 * @return Entree dans la table des symboles
	 */
	private Entree trouverEntree(String prec) {
		Entree e = null;
		for(Entree entree : tableSym){
			if(entree.getNom().equals(prec)){
				e = entree;
				break;
			}
		}
		return e;
	}

	/**
	 * Règle ECRIRE
	 */
	private void ecrire() {
		teste("WRITE_TOKEN", "WRITE_ERR");
		teste("PAR_OUV_TOKEN", "PAR_OUV_ERR");
		expr();
		generer("PRN");
		while(token.equals("VIRG_TOKEN")){
			nextToken();
			expr();
			generer("PRN");
		}
		teste("PAR_FER_TOKEN", "PAR_FER_ERR"); 		
	}

	/**
	 * Règle TANTQUE
	 */
	private void tantque() {
		teste("WHILE_TOKEN", "WHILE_ERR");
		int debut = pcode.size();
		cond(); 
		teste("DO_TOKEN", "DO_ERR"); 
		int pos = pcode.size();
		generer("BZE", 0);
		inst(); 
		generer("BRN", debut);
		pcode.set(pos, pcode.get(pos).split(" ")[0] + " " + pcode.size());
	}

	/**
	 * Règle SI
	 */
	private void si() {
		teste("IF_TOKEN", "IF_ERR");
		cond();
		teste("THEN_TOKEN", "THEN_ERR");
		int pos = pcode.size();
		generer("BZE", 0);
		inst();
		pcode.set(pos, pcode.get(pos).split(" ")[0] + " " + pcode.size());
	}

	/**
	 * Règle COND
	 */
	private void cond() {
		expr();
		if(token.equals("EGAL_TOKEN") || token.equals("DIFF_TOKEN") || token.equals("INF_TOKEN") || token.equals("SUP_TOKEN") || token.equals("INF_EGAL_TOKEN") || token.equals("SUP_EGAL_TOKEN")){
			String prec = sym;
			nextToken();
			expr();
			switch(prec){
				case "=":
					generer("EQL");
					break;
				case "<>":
					generer("NEQ");
					break;
				case "<":
					generer("LSS");
					break;
				case ">":
					generer("GTR");
					break;
				case "<=":
					generer("LEQ");
					break;
				default:
					generer("GEQ");
					break;
			}
		}		
	}
	
	/**
	 * Règle AFFEC
	 */
	private void affec(){
		String prec = sym;
		teste("ID_TOKEN", "var", true);
		Entree e = trouverEntree(prec);
		generer("LDA", e.getAdresse());
		teste("AFFEC_TOKEN", "AFFEC_ERR"); 
		expr();		
		generer("STO");
	}

	/**
	 * Règle EXPR
	 */
	private void expr() {
		term(); 
		while(token.equals("PLUS_TOKEN") || token.equals("MOINS_TOKEN")){
			String prec = sym;
			nextToken();
			term();
			if(prec.equals("+"))
				generer("ADD");
			else
				generer("SUB");
		}	
	}

	/**
	 * Règle TERM
	 */
	private void term() {
		fact(); 
		while(token.equals("MUL_TOKEN") || token.equals("DIV_TOKEN")){
			String prec = sym;
			nextToken();
			fact(); 
			if(prec.equals("*"))
				generer("MUL");
			else
				generer("DIV");
		} 		
	}

	/**
	 * Règle FACT
	 */
	private void fact() {
		if(token.equals("ID_TOKEN")){
				String prec = sym;
				teste("ID_TOKEN", "const, var", true);
				Entree e = trouverEntree(prec);
				switch(e.getClasse()){
					case "var":
						generer("LDA", e.getAdresse());
						generer("LDV");
						break;
					case "const":
						generer("LDI", e.getAdresse());
						break;
				}
		}else if(token.equals("NUM_TOKEN")){
			generer("LDI", Integer.parseInt(sym));
			nextToken();
		}else{
			teste("PAR_OUV_TOKEN", "PAR_OUV_ERR"); 
			expr();
			teste("PAR_FER_TOKEN", "PAR_FER_ERR"); 
		} 		
	}

	/**
	 * Règle VARS
	 */
	private void vars() {
		teste("VAR_TOKEN", "VAR_ERR"); 
		teste("ID_TOKEN", "var", false); 
		while(token.equals("VIRG_TOKEN")){ 
			nextToken();
			teste("ID_TOKEN", "var", false);
		} 
		teste("PT_VIRG_TOKEN", "PT_VIRG_ERR");
	}

	/**
	 * Règle CONSTS
	 */
	private void consts() {
		teste("CONST_TOKEN", "CONST_ERR"); 
		do{
			teste("ID_TOKEN", "const", false); 
			teste("EGAL_TOKEN", "EGAL_ERR");
			teste("NUM_TOKEN", "NUM_ERR");  
			teste("PT_VIRG_TOKEN", "PT_VIRG_ERR"); 
		}while(token.equals("ID_TOKEN")); 
	}

	public void save(String destination){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
			bw.write(this.toString().trim());
			bw.close();
		}catch(IOException e){
			System.err.println("Erreur fichier " + destination);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		if(args.length != 2){
			System.err.println("Erreur d'arguments");
			System.exit(1);
		}
		Analyseur a = new Analyseur(args[0]);
		if(a.getValid()){
			System.out.println("L'analyse lexicale s'est déroulée avec succès");
			a.save(args[1]);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String s : pcode)
			sb.append(s).append("\n");
		return sb.toString().trim();
	}
}
