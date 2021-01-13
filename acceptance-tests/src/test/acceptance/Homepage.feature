Feature: Fonctionnalités de ma page d'accueil
	Scenario: Vérification du titre et de la description et du bouton d'inscription
		Given je suis sur la homepage
		Then le titre doit être "Partagez vos passions | Meetup"
		And la description contient "Partagez vos passions et faites bouger votre ville"
		And la punchline (h1) du site doit être "Le monde vous tend les bras"
		And le bouton d'inscription doit contenir le text "Rejoindre Meetup"
		And le bouton d'inscription doit mener vers la page "https://www.meetup.com/fr-FR/register/"
#And le bouton d'inscription doit être présent "en haut de la page"