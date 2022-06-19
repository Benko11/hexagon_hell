# Hexagon Hell

**Name:** Benjamin Dubovecky

**Project:** Hra o korupcii

Hexagon Hell aims to replicate the Corruption game whose aim is for the players to capture, i.e. corrupt as much of the playing field as possible. Standard ratio is 70% captured by a particular player, which grants the player victory. This value can be adjusted by changing the `ENV.VICTORY` value in the `ENV` class.

*The name of the project serves as a tribute to author's waning composure during the project creation.*

The application can be launched by running the `main` method in the `Index` file, which opens up the main menu where users can choose from various shapes and sizes on which they can play. They can also toggle to play with the AI, or they can load a previously saved game.

When in the match, the current session can be saved and loaded back at a later time, save files are stored in plain text on user's hard drive.

Project's structure is heavily inspired by author's previous (and paltry) experience with making ExpressJS/React and Laravel web applications.