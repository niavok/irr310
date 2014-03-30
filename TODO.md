TODO
====

Refactoring
-----------

- virer V3D
- virer fengui

Graphisme
---------

- Concept art
- Mouse hover
- Active button


Bug fix 
-------

- Corriger le rendu des :
-fix warnings
- corriger le process name

Outils
------
- Système de sauvegarde
- Passer le moteur graphique à 1 fps s'il est minimisé
- Diminution automatique des graphismes avec poids pour , models, texture, framerate, etc

Interface
---------
- Raccourcis clavier
- HUD : indicateur de vitesse
- HUD : indicateur de  puissance des moteurs
- HUD : controle manuel

- Implémenter le bouton continue
- Créer le système de widget
- Ajouter le menu 
- Ajouter le fil d'ariane
- Prise de notes par activity


Gameplay
--------
- Implémenter le système de vibration
- Implémenter le système de température
- Implémenter de charge electrique
- Implémenter le Coef de destruction
- Nexus en forme de sphere. Nexus generator : genère un sphre sur 1 coté. plusieurs Nexus generator génère un plus grand nexus. Le nexus generator est présent que d'un coté mais visible mais le nexus est visible des 2 coté. On peux forcer le passe dans un nexus avec un nexus enforcer
- Rayon tracteur : permet de ramener des vaisseau endommangé ou captuer des vaisseaux ennemis
- Ransonnage des vaisseaux ennemis capturés
- Analyse des technologies des vaisseaux capturés
- Module d'autodestruction
- Skin custom des composants d'une vaction. Logo custom.
- Reacteur, plus efficace à haute vitesse
- Resistance à l'air élevé pour les hélices : pas de grande vitesse max
- Vanne de vidange : ejecter carburant, ressources, autre pour alléger
- Bombe nucléaire
- Moteur nucléaire
- Pile atomique : radiation -> electricité
- Mucus inflamable : surchauffe des systèmes + feux
- feu : propagation variable (vitesse ? température ? pression ? matériaux ?)
- bombe à froid
- bombe intertielle : zone à forte inertie
- bombe à vide : réduction de la pression  moins de résistance à l'air, moins de feu
- bouclier : réduit la vitesse des éléments, inutlise contre les lasers
- lasers : fait chauffer
- ventilation : augmente la conduction de température
- radiateur, augmente la conduction et le rayonnement
- propriété de slot : debit electrique, débit matière (munition, carburant) , débit chaleur, débit vibration,
- amortisseur
- générateur à vibration
- canon accoustique : fait vibrer, peut abimer les structures
- EMP : charge globale au vaisseau, décharge lente. Positive ou négative. Réduit ou augment d'autant la charge du vaisseau. Précharger le vaisseau permet de tirer plusieurs coups sans trop se charger soit même. Les canon localement accumuler de la charge pour ne pas faire d'accoup au vaisseau, ou en mode rapide et non secure, prendre de la charge du vaisseau d'un coup. Un canon a emp détruit peut relacher sa charge
- La charge augemente la probabilité de blackout d'un système
- Compensateur de charge : décharge le vaisseau plus rapidement.
- Detecteur de charge electrique distante.
- Visualisateur de charge : informe de sa propre charge
- Les conduction thermique sont liés aux slots. Il y a une valeur de air diffusion, une valeur de component diffusion, et une valeur de empty diffusion. En composnant avec plein de slot vide dissipe plus sa chaleur.
- Les débits electriques et de matériaux ont des débit limités aux niveaux des slots. Il y a des upgrade de vitesse de slots.
- Pour réduire la charge d'un vaisseau, il est possible de créer un appareil qui transforme la charge en chaleur, ou plus cher, la charge en energie.
- Changement de strategie de production : en largeur ou en profondeur. Le deuxieme choix permet de changer d'avis tardivement.
- Réparation rapide, par échange avec une pièce de rechange. Très rapide et pas couteux
- Nom customisé pour certains vaisseaux
- Radioactivité. Contamine une zone et se dilate. Ionise les matériaux et attaque les composants même à l'intérieurs. Les nuages se dispersent très très lentemet et s'étalant.
- Les échanges thermiques sont augmentés par la vitesse
- Les hautes températures ramolit les blindage, les rendant plus vulnérable aux explosions
- Un canon primitif avec un seul tir
- recul sur les armes
- Liste d'astuce sur le home
- Eoliene reversible pour la production d'electricité en freinant (genre elolienne de secours pour avion, leger et efficasse)
- bobinne tesla à efet de pointe : basse portée mais chaffe pas mal et ionnise beaucoup
- flak canon
- module kernel d'acquisition : designation de cible

Technologie
-----------

Définir des domaines scientifiques ???


Nexus technologies
- Nexus enforcer technologie
- Technologie de portail de deploiement rapide à n'importe quel nexus


Rayon tracteur technologies
- Repousser
- Attirer
 -> Controle de distance
    - Amarrage
    - Capture
- augmentation de distance
- augmentation de réactivité -> capaciteur à impulsion : détournement de missiles
- augmentation de puissance max
- amélioration de rendement


Meta technologie
- Analyse des technologies des vaisseaux capturés


Commerce

- Contrat de vente de soudoiement

Diplomatie

- Ransonnage des vaisseaux





    
