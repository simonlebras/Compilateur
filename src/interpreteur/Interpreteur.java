package interpreteur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interpreteur de pcode
 * @author Simon Le Bras
 * version 1.0
 */
public class Interpreteur{

	/**
	 * Liste des instructions du pcode
	 */
	private List<String> instructions = new ArrayList<>();

	/**
	 * Pile d'execution
	 */
	private List<Integer> stack = new ArrayList<>();

	/**
	 * Constructeur
	 * @param file Fichier contenant le pcode à interpreter
	 */
	public Interpreteur(String file){
		readInstructions(file); //récupération des instructions
		program(); //interpretation du pcode
	}

	/**
	 * Interprétation du pcode
	 */
	private void program() {
		int SP = -1, operand = 0, pos = 0, value;
		Scanner sc = new Scanner(System.in);
		String instruction;
		for(int i = 0; i < instructions.size(); i++){
			instruction = instructions.get(i);

			//récupération d'une éventuelle opérande
			if((pos = instruction.indexOf(' ')) != -1)
				operand = Integer.valueOf(instruction.substring(pos + 1));

			switch(instruction.substring(0, 3)){
				case "ADD":
					stack.set(SP - 1, stack.get(SP - 1) + stack.get(SP));
					stack.remove(SP--);
					break;
				case "SUB":
					stack.set(SP - 1, stack.get(SP - 1) - stack.get(SP));
					stack.remove(SP--);
					break;
				case "MUL":
					stack.set(SP - 1, stack.get(SP - 1) * stack.get(SP));
					stack.remove(SP--);
					break;
				case "DIV":
					stack.set(SP - 1, stack.get(SP - 1) / stack.get(SP));
					stack.remove(SP--);
					break;
				case "EQL":
					value = (stack.get(SP - 1) == stack.get(SP))? 1 : 0;
					stack.set(SP - 1, value);
					stack.remove(SP--);
					break;
				case "NEQ":
					value = (stack.get(SP - 1) != stack.get(SP))? 1 : 0;
					stack.set(SP - 1, value);
					stack.remove(SP--);
					break;
				case "GTR":
					value = (stack.get(SP - 1) > stack.get(SP))? 1 : 0;
					stack.set(SP - 1, value);
					stack.remove(SP--);
					break;
				case "LSS":
					value = (stack.get(SP - 1) < stack.get(SP))? 1 : 0;
					stack.set(SP  -1, value);
					stack.remove(SP--);
					break;
				case "GEQ":
					value = (stack.get(SP - 1) >= stack.get(SP))? 1 : 0;
					stack.set(SP - 1, value);
					stack.remove(SP--);
					break;
				case "LEQ":
					value = (stack.get(SP - 1) <= stack.get(SP))? 1 : 0;
					stack.set(SP - 1, value);
					stack.remove(SP--);
					break;
				case "PRN":
					System.out.println(stack.get(SP));
					stack.remove(SP--);
					break;
				case "INN":
					stack.set(stack.get(SP), sc.nextInt());
					stack.remove(SP--);
					break;
				case "INT":
					for(int j = 0; j < operand; j++)
						stack.add(0);
					SP += operand;
					break;
				case "LDI": case "LDA":
					stack.add(operand);
					SP++;
					break;
				case "LDV":
					stack.set(SP, stack.get(stack.get(SP)));
					break;
				case "STO":
					stack.set(stack.get(SP-1), stack.get(SP));
					stack.remove(SP--);
					stack.remove(SP--);
					break;
				case "BRN":
					i = operand - 1;
					break;
				case "BZE":
					if(stack.get(SP) == 0)
						i = operand - 1;
					stack.remove(SP--);
					break;
				case "HLT":
					System.exit(0);
					break;
			}
		}
		sc.close();		
	}

	/**
	 * Récupération des instructions du pcode
	 * @param file Fichier contenant le pcode
	 */
	private void readInstructions(String file){
		try{ 
			Scanner sc = new Scanner(new File(file));
			while(sc.hasNextLine())
				instructions.add(sc.nextLine());
			sc.close();
		}catch(FileNotFoundException e){
			System.err.println("Erreur sur le fichier à interpréter");
			System.exit(1);
		}
	}

	public static void main(String[] args){
		if(args.length != 1){
			System.err.println("Erreur sur le nombre d'arguments");
			System.exit(1);
		}
		new Interpreteur(args[0]);
	}
}
