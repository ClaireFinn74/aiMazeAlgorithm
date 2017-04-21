# Artificial Intelligence Maze Algorithm - Controlling Game Characters with Neural Networks and Fuzzy Logic

## Description:
This project creates a maze game using __Neural Networks__, __Fuzzy Logic__ and __Artificial Intelligence Search Algorithms__.
The game makes use of __Fuzzy Logic__ using _linguistic variables and membership functions_ to create a set of rules for the game. The game also uses some _AI search algorithms_ to find the goal node at it's current row/column in the game. The __goal node__ in this game is you, the _player_, and the _spiders_ use the __AStarAlgorithm__ to find you.


## AStar Heuristic Search Algorithm:
The __rationale__ for choosing this heuristic search algorithm includes the fact that this algorithm is the __most efficient__ one for games. To _traverse_ through the maze in our game, this _algorithm_ calculates the _lowest 'fCost'_ of reaching the _goal node_ which is the _player_ (as stated above). 


The '__FCost'__ is the __'GCost' + the 'Hcost'.__ 
To put this simply, The __'GCost'__ is the _distance from the starting node_ and the __HCost/Heuristic Cost__ is the _distance from the end node._ 
We _add these together_ to get the __FCost__.

The __AStar search algorithm__ will decide _which path to take_ based on these values. It will pick the _lowest FCost_ that is next to it and _traverse that path_ to get to the __goal node__ (the player in this case). Sometimes this may result in the algorithm going a round-about way of getting there, but this leaves room for the player to get a head-start on moving. The origional idea was to get the enemy spiders to follow our Spartan warrior as he moved but in the end we could only get the algorithm to get the starting position of the player. The spiders also were supposed to have independant movement to get to that starting point at the least but that also didn't work out. 


## Fuzzy Logic Rules:
The game sets out __rules__ for the _anger level of the spider_ (__irritated__ or __Irate__) and the _weapons_ the player uses within the game (__sword__, __bomb__, __hydrogen bomb__ and __MOAB__ (Mother of all Bombs)). The _anger level_ ranges from low (__irritated__) to high (__irate__) using _linguistic variables_ starting at 1 with a _membership value_ of 1 and ending at 50 with a _membership value_ of 0. The weapons use _linguistic variables_ to distinguish their _strength_ starting with the __sword__ at 0 with a _membership value_ of 1 and ending with the __MOAB__ at 65 with a _Membership Value_ of 0. We then set out some __Fuzzy rules__ to deal with whether the spider is _irritated/irate_ and what _weapon_ the player currently has which will be __more/less effective__ depending on how _strong_ the weapon is or how _angry_ the spider is. We covered all grounds by laying out every weapon in accordance to the spiders being at low anger(irritated) or high anger (irate) using __fuzzy rules__ within the fcl file.
Within GameRunner, we used the linguistic variables set out in the fcl text file within if statements to say that our spider gets increasingly angry as the player defeats it's friends (the other spiders). The anger level is at max when it reaches 100 and the program will produce an out of range error if you set the variable to be outside the range we set out within the file. This means that we have successfully incorporated the fuzzy rules into our game.


## Currently, in the project we have:

1. Fuzzy Logic with Linguistic variables and Membership values to create fuzzy rules for our game.
2. An fcl text file with accompanying FuzzyLogic to read it in (incorporated into our GameRunner class) and this then produces charts showing the risk of the weapon the player used and the anger level of the spider.
3. The AStar Heuristic Search Algorithm to find our player (the goal node)
4. Some Neural Network logic to evaluate our maze and determine the best course of action for the player (To Run away/avoid/Attack or Panic) based on the items that are generated randomly each time within the maze (sword, bomb and number of enemies as well as your health). 



## Rules of the game:

1. The game includes a Spartan Warrior (You - The Player) and Enemy Spiders.
2. You, as the Spartan Warrior, must traverse the maze and pick up items in order to defeat these spiders.
3. Your health will begin at 40 but will degrade as you fight the enemy spiders.
4. You have no way to regain health so the game is a strategic one.
5. The player can either find 5 trophies, each giving ten points, to end the game or fight their way to the end earning points by beating different strengths of spiders. The player, doing the latter, must be mindful of their health.
6. You can zoom out at any point by pressing Z to see where the items are in the game or to see yourself going through the full maze rather than the zoomed-in one. (All items are distinguished through colour-coding - red for enemies or items, yellow for the player and blue for the trophies).


## The weapons include (in order from weakest to strongest):

1. Sword: Inflicts 10 damage.
2. Bomb:  Inflicts 20 damage.
3. Hydrogen Bomb: Inflicts 30 damage.
4. MOAB (Mother Of All Bombs): The best weapon - does the most damage. (50 damage)


## The Spiders include (In order from weakest to strongest):

1. The Yellow Spider - Fists (No Weapon) Inflicts   5 damage
   With any Weapon   Does		 1 damage

2. The Red Spider - Fists (No Weapon)    Inflicts   6 damage
   With any Weapon	  Does		 2 damage

3. The Orange Spider - Fists (No Weapon) Inflicts   7 damage
   With any Weapon	  Does		 3 damage

4. The Grey Spider - Fists (No Weapon)   Inflicts   8 damage
   With any Weapon	  Does		 4 damage

5. The Green Spider - Fists (No Weapon)  Inflicts   9 damage
   With any Weapon	  Does		 5 damage

6. The Brown Spider - Fists (No Weapon)  Inflicts  10 damage
   With any Weapon	  Does		 6 damage

7. The Blue Spider - Fists (No Weapon)   Inflicts  11 damage
   With any Weapon	  Does		 7 damage

8. The Black spider (The 'Black Widow') - Fists (No Weapon) Inflicts 15 damage
   With any Weapon	  Does		10 damage



## References:

1) https://unicode-table.com/en/#latin-extended-b [Unicode character information]
2) https://www.w3schools.com/colors/colors_picker.asp [RGB colours W3Schools]
3) https://learnonline.gmit.ie/course/view.php?id=3461 [GMIT Learn online module for Artificial Intelligence]
4) https://www.youtube.com/watch?v=-L-WgKMFuhE [Good explanation of the AStar Heuristic search Algorithm]
