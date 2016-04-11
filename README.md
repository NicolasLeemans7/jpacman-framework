![build status](https://travis-ci.org/SERG-Delft/jpacman-framework.svg?branch=master)

JPacman-Framework
=================

About
-----

Pacman-like game used for teaching software testing.
It exposes students to the use of git, maven, JUnit, and mockito.

Parts of the code are well tested, whereas others are left untested intentionally. As a student in software testing, you can extend the test suite, or use the framework to build extensions in a test-driven way. As a teacher, you can use the framework to create your own testing exercises.

We have developed and are using this code at a software testing course at Delft University of Technology, The Netherlands. Teachers interested in seeing the exercises I use there are invited to contact me.

Other universities who have used this material include Antwerp, Eindhoven, and UBC (Vancouver).

If you have any suggestions on how to improve this framework, please do not hesitate to contact us, open issue, or provide a pull request.

Main contributors:

*	Arie van Deursen (versions 1.0-5.x, 2003-2013)
*	Jeroen Roosen (major rewrite, version 6.0, 2014)


Getting Started
---------------

1. Git clone the project
2. If you use Eclipse:
	1. Import
	2. Right Click -> Configure -> Convert to Maven Project
3. To see JPacman in action: run `nl.tudelft.jpacman.Launcher`
4. To run the test suite in maven: `mvn test`
5. To run the test suite in Eclipse: right click -> run as -> JUnit Test.

Lancer le projet (Version Nicolas Leemans)
------------------------------------------
En considérant que Maven est correctement installé sur votre machine et en plaçant un terminal
(une invite de commande ou autre terme décrivant une \og console \fg \, sur votre système d'exploitation) à la racine du projet (c'est-à-dire l'endroit où se trouve le fichier \nolinkurl{pom.xml}), vous pouvez :

1.Exécuter les tests via : mvn test
2.Compiler le projet (inclus l'exécution des tests) via : mvn compile
3.Lancer le jeu via (après avoir compilé) via : mvn exec:java -Dexec.mainClass="nl.tudelft.jpacman.Launcher"
4.Compiler le projet et produire une archive "jar" exécutable via : mvn package
5.L'archive est ensuite exécutable via : java -jar target/jpacman-framework-7.3.0.jar

