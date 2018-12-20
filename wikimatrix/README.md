# Wikimatrix Lecacheur


## Objectif

Le logiciel fourni permet de cr�er un extracteur de tableau provenant de pages wikipedia, pour r�cup�rer leur contenu dans des fichiers csv.


### Diagramme de classes

![Diagramme UML](UML.png "Diagramme UML")

La classe principale MatrixExtractor r�alise la plupart des traitements n�cessaires � la lecture du fichier HTML source de la page wikipedia pour r�cup�rer les tableaux, ainsi que l'export de ces tableaux au format csv.

### D�marrer l'extraction

Pour extraire les tableaux des pages wikipedia dont les URL sont fournies dans inputdata/wikiurls.txt, il suffit de lancer la m�thode testBenchExtractors() de la classe BenchTest.

## License

Ce projet est enregistr� sous une license libre GNU.