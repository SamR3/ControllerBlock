ControllerBlock
===============

Remake of the Controller Block mod, originally by Sunrise.

After reading an inspirational post by BuzzBazz on the Minecraft Mod Development Forums, I decided to take on remaking
the mod as a project.

This has been done as a team.

The code is now fully functional for Minecraft versions 1.6.2-1.6.4.

Here follows a brief description of what this remake includes:
==============================================================

This mod adds two blocks and two items. Ids can be changed in a config file.

The first block, called the Controller, switch on/off other blocks in the world with corresponding redstone input. You must first place the Controller, then right click it with a Linker item, then blocks can be selected with the Linker by right click. You can add or remove blocks. Linker has description, and chat messages are sent for ease of use.Those are only accessible in the Creative menu.

The second block is called the Animator, and works with the Remote item.
The Animator handles "Frames", unlimited lists of blocks that will appear successively while the Animator is powered. Those blocks must be selected with the Remote.

Here is how one is supposed to make an "animation":
-Put the Animator block somewhere
-Right click it with the Remote item to make the connection
-Right click blocks you want in the first frame (you will receive chat messages doing so)
-Right click the Animator with the Remote to validate the first frame
-Right click blocks you want in the second frame...
-When had enough (or want to test), open the Animator gui by right click the AnimatorPosted Image
-Tweak the settings to your liking:
[Max for the number of frame that will be displayed before stop, -1 for infinite]
[Frame for first frame displayed]
[Switch: ORDER,REVERSE,RANDOM,LOOP, for the animation process, corresponding to the order of the frames you set]
[Plus and Minus to set the amount of seconds/ticks (change this display in config file) between each frame displayed]
[Force Reset to reboot those settings to default]
-Put the Remote into the slot and push "Reset Link" to free the Remote-Animator connection.
-Close the gui
-Power the Animator with redstone
-If any changes in the frames is needed, unpower the Animator, repeat from the beginning except for Animator set. (fast selection of a frame can be done into the animator gui, with Frame button)
The Remote itself has a GUI you can bring up with the "Remote Control Key" (default: R).
You can then access some data of linked Animator from afar.
Animator and Remote can be crafted with mildly-expensive components.

Both Linker and Remote have a multiple block (cube pattern) selection mode when player is sneaking.
