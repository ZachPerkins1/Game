# About
This project is my work on an as-of-yet untitled shopkeeping game. Even though the concept is a little generic, my plans are to make the game a pretty expansive and accurate simulator of what running a shop would be like in an ever-fluctuating economy. Of course I'm only one developer who is also a full time student with a part time job so this might take a little while.

# What I'm using
For this project I chose to use Java with the Lightweight java game library (lwjgl) which basically just provides Java bindings and memory management for OpenGL, GLFW, OpenAL and some other low-level C libraries. This has given me the chance to get experience working closely with the graphics pipeline as opposed to using a more abstracted library or engine.

# Where it's at
Doing something like this is an ambitious project because, in order to get to the point of game design, a lot of lower level functionality needs to be abstracted to become more customized for my specific purposes. This is currently the part that I'm working on right now.

## Finished Features
- Render a map of tiles with a large background canvas that can be used for sketching more immersive environments
- Implement "entities" that can traverse this environment unbound by the grid
- Create progressive collision with entities on certain tiles
- Create "chunks" within the map of tiles that can be used to store smaller subsets of tiles
- Make it so that the chunks can be generated on demand, therefore creating an "infinite" universe of chunks
- Implement the placement of blocks on this grid
- Implement saving of chunks that is both efficient but allows a high block capacity

## In progress features
- Create a GUI overtop of the environment that is able to perform basic element positioning and UI features (button, text entry, etc.)
- Implement a more robust method of rendering entities (right now it's just done on a per-entity basis with basic OpenGL calls)

## Future features
- Create a method of audio effect loading and playing that is relative to the position of the character in the environment
- Implement lighting in all of the shaders
- Begin to implement core game features (this is pretty much the bulk of the remaining work but to list it would be extraneous)
