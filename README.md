# MyIngrediBox
[Wissens- und KI-basierte Systeme WS 2017/18] Studentische Projektarbeit zu Agentensystemen

## About MyIngrediBox
MyIngrediBox takes a recipe as its input and serves you the required (virtual) ingredients as its output.
The output also contains information, where the ingredients can be bought, whether leftovers will remain and how much money you would spend for the ingredients.
To provide the ingredients, an IngredientBuyer (Agent) is assigned to check the availability of the ingredients in several markets.
Besides there is an opportunity to manipulate the purchase behaviour of the IngredientBuyer by setting its choice preferences, which includes choose 'cheapest' or 'lowest leftovers'. 
MyIngrediBox is an agent-based software, written in Java and using the Java Agent DEvelopment Framework (JADE), which is provided by Telecom Italia Lab (tilab).

As default sample, a recipe (ingredient-table) for vanilla hotcakes was set!

name | type | unit | quantity
---- | ---- | ---- | --------
Mehl|solid|Kilo |0.75
Salz|solid|Kilo|0.01
Milch|liquid|Liter|1.5
Eier|misc|Piece|3
Butterschmalz|solid|Kilo|0.4
Vanille|misc|Piece|1
Apfelkompott|liquid|Liter|0.5

## Installation
* Please make sure JADE (Java Agent DEvelopment Framework) and also json-simple library is imported and configured 
* Clone this repository: `git clone https://github.com/HTW-Berlin-UI/MyIngrediBox.git`
* Choose the 'MyIngrediBox-All_Agents' as projects run configuration
	* In Eclipse IDE you can set the run-config if you go to menu bar and click 'Run' -> 'Run Configuration'
	* The config-file is located as .launch file in the root directory
* MyIngrediBox Project is now ready to start

## External Frameworks and Libraries

* JADE (as jade)
* json-simple (as json)

For information about JADE (Java Agent DEvelopment Framework) please visit the [documentation](http://jade.tilab.com/documentation/tutorials-guides/) by Telecom Italia Lab. 
