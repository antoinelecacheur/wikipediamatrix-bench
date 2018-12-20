# Rapport Lecacheur


## Tableaux

En l'�tat, l'extracteur permet de cr�er 689 fichiers csv avec notamment les statistiques suivantes :


* Nombre minimal de colonnes : 2
* Nombre maximal de colonnes : 30
* Nombre moyen de colonnes : 8,9
* Variance du nombre de colonnes : 4,5


* Nombre minimal de lignes : 0
* Nombre maximal de lignes : 593
* Nombre moyen de lignes : 28,1
* Variance du nombre de lignes : 39,2


Le titre de colonne vide appara�t 89 fois dans les tableaux.

### Qualit�s et faiblesses

Les tests sp�cifiques de BenchTest sur les deux URL permettent de v�rifier que l'on lit bien les tableaux o� les headers sont r�pliqu�s en fin de table (ex : cannon) tout comme les tableaux pour lesquels les headers ne sont pas pr�sents en fin de table (ex : US states and countries). Dans les premi�res impl�mentations, la derni�re ligne du tableau de l'URL US n'�tait pas cr��e dans le csv.


Le code fourni permet �galement de cr�er des tableaux � "double entr�e", c'est-�-dire dont la premi�re colonne est compos�e de cellules th (des titres donc) comme par exemple pour l'url 10 : "Comparison of Asian national space programm" o� des dates font office de header de ligne. Par ailleurs, certaines URL renvoient un code d'erreur HTTP, ces cas sont �galement trait�s et lors de la lecture de l'url, un message est renvoy� pour pr�ciser que l'url est inatteignable lorsque c'est le cas.


Enfin, en cas de titres de colonne identiques dans un m�me tableau, une fonction permet d'ajouter un num�ro au titre pour que l'on puisse cr�er le fichier csv. En effet, si deux colonnes ont un titre vide, il sera impossible de cr�er le csv, la solution propos�e cr�era donc une premi�re colonne dont le titre et vide, et une deuxi�me dont le titre est "1".


Cependant, l'extracteur ne permet pas d'extraire tous les tableaux de wikipedia. Je me suis concentr�s sur l'extraction de tableaux de type "wikitable sortable", mais je n'ai �galement pas trait� le cas o� le tableau peut �tre compos� de cellules th qui ne sont pas des titres de colonne ou de ligne. D'autres tableaux ont pour contenu de cellule plusieurs balises a (href), dont le contenu n'est pas r�cup�r� par l'extracteur propos�. D'autres cas non r�pertori�s peuvent ne pas avoir �t� �galement trait�s.


### Synth�se G�n�rale

De nombreux tableaux sont exploitables par des outils statistiques. Toutefois, il existe quelques tableaux vides (avec uniquement les headers) inexploitables. Par ailleurs, aucune garantie de la qualit� d'information n'est propos�e