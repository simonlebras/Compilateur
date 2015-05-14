Compilateur
===========

Compilateur Pascal

# Grammaire du langage:

PROGRAM	::=	program ID ; BLOCK .

BLOCK	::=	CONSTS VARS INSTS

CONSTS	::=	const ID = NUM ; { ID = NUM ; } | e

VARS	::=	var ID { , ID } ; | e

INSTS	::=	begin INST { ; INST } end

INST	::=	INSTS | AFFEC | SI | TANTQUE | ECRIRE | LIRE | e

AFFEC	::=	ID := EXPR

SI	::=	if COND then INST

TANTQUE	::=	while COND do INST

ECRIRE	::=	write ( EXPR { , EXPR } )

LIRE	::=	read ( ID { , ID } )

COND	::=	EXPR RELOP EXPR

RELOP	::=	= | <> | < | > | <= | >=

EXPR	::=	TERM { ADDOP TERM }

ADDOP	::=	+ | -

TERM	::=	FACT { MULOP FACT }

MULOP	::=	* | /

FACT	::=	ID | NUM | ( EXPR )

# Règles:

Les mots clés sont réservés ; ils apparaissent en minuscule dans la grammaire.

Certains non-terminaux ne sont pas décrits par cette grammaire. Il s'agit des non-terminaux pris en charge par l'analyse lexicale :

ID
représente les identificateurs, c'est-à-dire toute suite de lettres ou chiffres commençant par une lettre et 
qui ne représente pas un mot clé (qui sont les terminaux présents dans la grammaire) ;

NUM
représente les constantes numériques, c'est-à-dire toute suite de chiffres.

Un commentaire est une suite de caractères encadrés des parenthèses (* et *) ;

Un séparateur est un caractère séparateur (espace blanc, tabulation, retour chariot) ou un commentaire ;

Deux ID ou mots clés qui se suivent doivent être séparés par au moins un séparateur ;

Des séparateurs peuvent être insérés partout, sauf à l'intérieur de terminaux.

La sémantique du langage est claquée sur celle de Pascal. Les identificateurs doivent être déclarés pour être utilisés. Toutes les variables sont implicitement déclarées de type entier.

Pour lancer l’interpréteur :
java Interpreteur nom_du_fichier_contenant_le_pcode

Pour lancer le compilateur :
java Analyseur fichier_source fichier_destination_pcode
