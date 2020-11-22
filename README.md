Zoukfiesta est une application permettant de gérer une playlist de musique à plusieurs. Pratique lors de vos soirées pour éviter les multiples appairages bluetooth sur l'unique enceinte dès que qu'une autre personne désire diffuser sa musique.
Le principe est que l'organisateur de la soirée soit le seul à être branché sur un système audio et celui-ci peut inviter ses convives à ajouter leur musiques dans la liste de lecture. L'organisateur doit seulement "Host" une "Fiesta" dans l'application et les invités pourront ensuite rejoindre et proposer des musiques.
L'application utilise le service Google Nearby permettant de créer de petits réseaux locaux s'étendant sur de très petites distances en utilisant les technologies Bluetooth, Wifi et audio.

Liste des fonctionnalités actuelles :
•    Host une "Fiesta"
•    La musique se lance automatiquement sur l’host. La playlist affichée est complètement jouée.
•    Avec un « swap » vertical dans l’host, on peut ouvrir le menu des « settings ». La liste des joueurs y est connectée, il est possible de les kicks depuis la (il faut alors un deuxième appareil Android pour tester cette fonctionnalité).
•    Liste des hosts disponibles à proximité
•    Connection à un host. (Il faut bien sûr un deuxième appareil Android pour hoster)

L’émulateur d’Android studio ne permet pas d’émuler le service Google Nearby. Il est donc nécessaire d’effectuer les tests sur un vrai appareil.
Dans l'état actuel, nous avons pu implémenter les parties les plus techniques du projet (réseau, jouer de la musique, prendre en main Android…) et une classe réseau qui implémente déjà les différentes commandes réseau. La suite va donc être bien plus triviale à implémenter.