# Tomato
experimental 2d video game engine in Java swing.
currently i have implemented:
- background tiles that form a scene, tiles can be animated
- multiple scenes that can be loaded from a file, a player can walk through certain tiles (i.e doors) to switch a scene
- collision with solid tiles
- walking animations
- melee (zombie) enemies with ai
- basic NPCs with dialogue trees defined in XML
- start/pause menu gui
- shooting projectiles
- a dialogue box for interacting with the world and entities
- healthbars for both entities and the player
- keybind registry
- basic hotbar inventory
- items
- a modding interface ([see example mod](https://github.com/amir16yp/Tomato-ExampleMod))
# Compilation
You can compile the Tomato game engine in two ways:

Using IntelliJ IDEA: Import the project and set up an artifact with the endpoint game.Game.

Using Scripts: Run `compile.ps1` for Windows systems or `bash compile.sh` for *nix systems. Ensure you have a Java JDK in your $PATH.
