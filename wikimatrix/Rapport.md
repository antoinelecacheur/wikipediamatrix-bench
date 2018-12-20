# Rapport Lecacheur


## Tableaux

En l'état, l'extracteur permet de créer 689 fichiers csv avec notamment les statistiques suivantes :


* Nombre minimal de colonnes : 2
* Nombre maximal de colonnes : 30
* Nombre moyen de colonnes : 8,9
* Variance du nombre de colonnes : 4,5


* Nombre minimal de lignes : 0
* Nombre maximal de lignes : 593
* Nombre moyen de lignes : 28,1
* Variance du nombre de lignes : 39,2


Le titre de colonne vide apparaît 89 fois dans les tableaux.

### Qualités et faiblesses

Les tests spécifiques de BenchTest sur les deux URL permettent de vérifier que l'on lit bien les tableaux où les headers sont répliqués en fin de table (ex : cannon) tout comme les tableaux pour lesquels les headers ne sont pas présents en fin de table (ex : US states and countries). Dans les premières implémentations, la dernière ligne du tableau de l'URL US n'était pas créée dans le csv.


Le code fourni permet également de créer des tableaux à "double entrée", c'est-à-dire dont la première colonne est composée de cellules th (des titres donc) comme par exemple pour l'url 10 : "Comparison of Asian national space programm" où des dates font office de header de ligne. Par ailleurs, certaines URL renvoient un code d'erreur HTTP, ces cas sont également traités et lors de la lecture de l'url, un message est renvoyé pour préciser que l'url est inatteignable lorsque c'est le cas.


Cependant, l'extracteur ne permet pas d'extraire tous les tableaux de wikipedia. Je me suis concentrés sur l'extraction de tableaux de type "wikitable sortable", mais je n'ai également pas traité le cas où le tableau peut être composé de cellules th qui ne sont pas des titres de colonne ou de ligne. D'autres tableaux ont pour contenu de cellule plusieurs balises a (href), dont le contenu n'est pas récupéré par l'extracteur proposé. D'autres cas non répertoriés peuvent ne pas avoir été également traités.


### Synthèse Générale

De nombreux tableaux sont exploitables par des outils statistiques. Toutefois, il existe quelques tableaux vides (avec uniquement les headers) inexploitables. Par ailleurs, aucune garantie de la qualité d'information n'est proposée