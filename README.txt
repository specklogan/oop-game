-----------------------------------------------------------USERGUIDE----------------------------------------------------

IRON CONVOY 

A 2D Side scrolling game where you shoot down flying enemies. Travel far and see how long you can survive

Collaborators: 
    - Hudson Boothby
    - Logan Speck
    - Walter Fagley

Controls:
    "w" or "Right Arrow Button" : Speed up
    "s" or "Left Arrow Button"  : Slow down
    Mouse Click                 : Fire bullet to mouse location


    "d" : Debug mode Toggle
        "z" : spawn zepplen
        "m" : spawn 5 enemies
        "b" : spawn biplane

-------------------------------------------README -------------------------------------------
Notable design choices:

    We went with RTrees to handle the physics collisions in O(logN), it's a little overkill but I wanted to try something other than an arraylist.
    Our train objects use linked lists to represent the carriages
    We also went with an event system to decouple a lot of the code

known bugs:
    Sometimes skips enemy spawns if moving too fast

contribution per member:
    Hudson Boothby;
        Created enemy spawn method
            Timed enemy spawn to distance
            randomize enemy spawn
        Implemented speed restrictions and edits
        General editing among the code
        Multiple bug fixes

    Walter Fagley;
        Added Biplane Class and modified spawning to include biplanes
        Created Textures for
            - Biplanes
            - Title Page
            - Town Tiles
            - Bombs
            - Plains
            - Dustbowl
            - Mountains

    Logan Speck;
        Implemented Event System
        Implemented Rendering and Physics Systems
        Implemented Core objects and initial project
        Some sprites



----------------------------------------Debugging Collaborators----------------------------------------------

Other Student Help:
    None

Internet URL Help:
    https://github.com/davidmoten/rtree - Helped organize collisons in a 2d tree efficiently
    https://www.pixilart.com/ - Used to create textures for Iron Convoy

    Sounds from War Thunder
    Explosion sprites (https://limofeus.itch.io/pixel-simulations)

TA/Instructor Help:
    None

Self-Reference
    https://github.com/SFA-Computer-Science-Club/jArcade/tree/master/src/main/java/org/goose/core/event/core (I implemented this a few years ago for a previous game engine for the CS club, reused just the event code)


------------------------------------------Hours spent on Project-----------------------------------------------------

Hudson Boothby:
    Around approximately 40-50 hours
Walter Fagley:
    Approximated time worked: 48 hours
Logan Speck:
    Around 50-60 hours
