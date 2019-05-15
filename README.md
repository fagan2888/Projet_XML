# Projet XML

Ce projet a été réalisé dans le cadre de notre M1.

## Getting Started

### Prerequisites
- Windows 10

-Eclipse 

-MySQL Server

### Installing
#### Mise en place de la BDD avec PHPMyAdmin
Ouvrez une session sous PHPMyAdmin

Cliquez sur l'onglet "base de données" 
Saisissez un nom de BDD (exemple: "xml"), cliquez sur l'option "Interclassement" pour la changer en "UTF8_general_ci" 
Et validez

Cliquez sur le nom de votre base de données dans la hiérarchie puis cliquez sur l'onglet "importer" 
Sélectionnez le fichier xml.sql (disponible à la racine de ce répertoire github) 
Et cliquez sur exécuter 


#### Mise en place du projet sous Eclipse
Ouvrez Eclipse:
```
file > new > Java Project
```
renseignez le nom de projet voulu et cliquez sur finish

Dans la hiérarchie d'Eclipse:
```
Clic droit sur le dossier du projet > New > Source Folder (vous pouvez mettre comme nom "resources") 
```
et glissez y les fichiers contenus dans le dossier "resources" du github. 
puis glissez les fichiers contenus dans le dossier "src" du github vers le dossier "src" d'Eclipse.

```
Clic droit sur le dossier du projet > Properties > Build Path > Add External JARs... > "{racine du projet}/resources/mysql-connector-java-5.1.47.jar"
```

### Vérification de la connexion à la BDD 
Executez sous Eclipse le fichier src/TestBDD.java

## Utilisation
### GenEnvelopped.java
Permet de signer des fichiers xml.
Pour cela sous Eclipse: 
```
Clic droit sur GenEnvelopped.java > Run As > Java Application
puis
Clic droit sur GenEnvelopped.java > Run As > Run Configurations...
Cliquez sur Arguments > Program arguments
``` 

et entrez les informations sous ce format: 

```
C:\Users\{nom d'utilisateur}\eclipse-workspace\{nom du projet}\{fichier entrée}.xml 
C:\Users\{nom d'utilisateur}\eclipse-workspace\{nom du projet}\{fichier de sortie}.xml
``` 

``` 
Cliquez sur apply > run 
``` 
### xml/Main.java
Exécutez ce fichier pour lancer l'application.

Vous pourrez ensuite sélectionner un fichier via l'interface graphique et exécuter le traitement de la requête.

### utils/Rechercher.java
Il s'agit du model de notre application.

Elle se compose d'une méthode `execute` qui va simplement appliquer la méthode `xmlQuery`
`xmlQuery` quand à lui va : 
- Valider le fichier xml via la méthode `validateXmlFile` (Qui n'est autre que la validation du schéma `resources/validate.xsd`)
- Vérifier la signature à l'aide de la méthode `check` de la classe util/XMLSign.java
- Regarder le type de requête demandé et l'exécuter avec la méthode `xmlQueryAction`

`xmlQueryAction` va simplement diriger le type de requête demandé vers l'une de ces méthodes:
- `getQueryInsert(Node node)` 
- `getQueryUpdate(Node node)` 
- `getQueryDelete(Node node)` 
- `getQuerySelect(Node node)` 

qui permette de récupérer la requête (`PreparedStatement`) à exécuter. 
Elles seront ensuite exécuter par l'une de ces deux méthode suivant leur type: 
- `querySelectToFile(PreparedStatement ps)` qui renvoie un fichier contenant les informations des lignes retournées
- `queryUpdateToFile(PreparedStatement ps)` qui renvoie un fichier contenant le nombre de lignes modifiées

Chaque méthode `getQuery??????(Node node)` va gérer à sa façon les informations du noeud fourni.
Pour éviter les injections SQL nous avons une classe `TablesManager` à la fin du fichier qui va stocker et gérer des instances des classes `Table` et `Column`
Cette classe va faire le liens entre les tables et colonnes demandées, et vérifier si elles existent en bases de données (via notre attribut bddTable)

Comme jdbc ne permet pas d'ajouter des noms de colonnes/tables comme on pourrait ajouter des paramètres, 
je créer mes requêtes grâce à un StringBuilder, j'informe TablesManager des tables et colonnes utilisées et lorsqu'il s'agit d'un paramètre je l'ajoute à mon ArrayList<String> de paramètres pour les donner ensemble à mon PreparedStatement avec la méthode `setString`.
Une fois la requête créée j'appelle à la fin de chaque `getQuery?????(Node node)` la méthode `checkAllExist` de `TablesManager`

Notre partie de condition de la requête étant récursive, j'ai créé deux méthodes pour la gérer: 
- `handleCondition(TablesManager tm, StringBuilder sb, ArrayList<String> parameters, Node node)` qui parcours les fils du noeuds fourni (de type CONDITION, OR ou AND) pour préparer les conditions OR et AND
- `handleOperator(TablesManager tm, StringBuilder sb, ArrayList<String> parameters, Node node)` qui parcours les fils du noeuds fourni (de type CONDITION, OR ou AND) pour préparer les opération tels que GREATER, LESS ou EQUALS

Grâce à ce système on arrive à une requête comme:
```SQL
SELECT `j`.`numJoueur`, `j`.`prenom_joueur` FROM `joueur` `j` WHERE ('76'=`j`.`numJoueur`  OR `j`.`numJoueur`>'76' )
```
à l'aide un fichier XML :
```XML
<?xml version="1.0" encoding="UTF-8" standalone="no"?><SELECT>
<CHAMPS>
    <CHAMP table="joueur">
        numJoueur
    </CHAMP>
    <CHAMP table="j">
        prenom_joueur
    </CHAMP>
</CHAMPS>
<TABLES>
    <TABLE alias="j">joueur</TABLE>
</TABLES>
<CONDITION>
    <OR>
        <EQUALS>
            <VALUE>76</VALUE>
            <CHAMP table="j">
                numJoueur
            </CHAMP>
        </EQUALS>
        <GREATER>
            <CHAMP table="j">
                numJoueur
            </CHAMP>
            <VALUE>76</VALUE>
        </GREATER>
    </OR>
</CONDITION>
<Signature xmlns="http://www.w3.org/2000/09/xmldsig#"><SignedInfo><CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"/><SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#dsa-sha1"/><Reference URI=""><Transforms><Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/></Transforms><DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/><DigestValue>0651FNP74kq5tXfntjWfwjKrdco=</DigestValue></Reference></SignedInfo><SignatureValue>GmJqGTnP9dud5cBwvP98pMFIztUsLCLFhm7IBI/5dk/IvlNomeO+yw==</SignatureValue><KeyInfo><KeyValue><DSAKeyValue><P>/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9
xD7nN1kuFw==</P><Q>li7dzDacuo67Jg7mtqEm2TRuOMU=</Q><G>Z4Rxsnqc9E7pGknFFH2xqaryRPBaQ01khpMdLRQnG541Awtx/XPaF5Bpsy4pNWMOHCBiNU0Nogps
QW5QvnlMpA==</G><Y>zxLTsLJWzANvZHxGcWPBbW3ZKWJyvleEy1Xg90zTQDOqkbif3KHfGJNvdYpQnXq5ZFS7NJayX4N7
BX/6vd/fdw==</Y></DSAKeyValue></KeyValue></KeyInfo></Signature></SELECT>
```

## Spécificités
Le programme limite les fichiers à une taille de 64000. 
Notre programme a une écriture strict. Je ne donne pas beaucoup de marge pour éviter les injections SQL. 
Chaque `CHAMP` doit avoir un attribut `table` qui permet d'identifier la table de la colonne visée. On peut notamment ajouter un attribut `alias` aux balises `TABLE` afin de ne pas recopier le nom de la table dans l'attribut `table` des `CHAMP` mais renseigner simplement l'alias. Cela nous permet de vérifier que le champ renseignant la colonne d'une table ne soit pas en réalité une injection mais bien un colonne existante dans une table existante.
Pour sécuriser les conditions, nous avons une structure récursive avec des balises définies: 
- `OR`,`AND` des balises pouvant contenir d'autres `OR`, `AND` et des opérations comme `EQUALS`, `GREATER`, `LESS`
- `EQUALS`, `GREATER`, `LESS` permet de contenir deux balises de type `CHAMP` et `VALUE`

Pour limiter la taille des fichiers à canoniser j'ai utilisé la instructions 
```Java
System.setProperty("entityExpansionLimit","64000");
```

~~Pour ignorer les DTD j'ai utilisé les instructions~~ (Manque de temps pour debug)
```Java
dbf.setFeature("http://xml.org/sax/features/namespaces", false);
dbf.setFeature("http://xml.org/sax/features/validation", false);
dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); 
```

~~Pour éviter les transformations j'ai utilisé les instructions~~ (Manque de temps pour debug)
```Java
TransformerFactory factory = TransformerFactory.newInstance();
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

Transformer transformer = factory.newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
```

## FIX
Lors du rendu je n'ai pas mis les méthodes `checkAllExist` en bas de chaque méthode `getQuery?????(Node node)`

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
