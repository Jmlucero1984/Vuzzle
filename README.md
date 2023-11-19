# Vuzzle
  Puzzle Game for Android | Real time in-device piece generation

## Description

Born from Valeria Humerez idea, the main concept was to produce puzzle pieces from any image in you phone. This was my second app so don't bother with Design Patterns, Separations of Concerns, Inheritance, and so forth. I know, i'll ammend that somewhere in the future, and i'll do it through Kotlin as the state of the art demands. In fact, sometimes when i look back, i can realize that i deal with some no easy peasy stuff like generics in asyncTasks with no full comprehension even at all. But... when you have an idea, go for it, and hope God will take care of UI/UX.

Now in retrospective, it has no sense making the user wait for the piece factory process to be finished... you could deliver a couple of pre-cut games and the rest could be fetch and donwloaded through and API and the process of cutting would be took place in a background task, you won't even notice that. Let's double the bet, why not attempt to assign that task to a deeper level (C++) passing through all "keep dumb developer safe and sound" memory barriers.

In addition, it has no more "story" than completing the puzzle, there is nothing more beyond, a gift? a quote? another kind of challenge?... so many things to think about for a later version.


## Screen Record

<div align="center">
  <video src="https://github.com/Jmlucero1984/Vuzzle/assets/91501518/dc9de31d-aacf-4fbf-a6b8-1d9edcf88e82" width="400" />
</div>


## Screenshots

![MuseoBellasArtesMéxico](https://github.com/Jmlucero1984/Vuzzle/blob/main/ScreenShot_1.jpeg)
 

![Venezia](https://github.com/Jmlucero1984/Vuzzle/blob/main/ScreenShot_2.jpeg)

![capadocia](https://github.com/Jmlucero1984/Vuzzle/blob/main/ScreenShot_3.jpeg)


## Core 

All turns around pixel substraction and masking. The plug-in piece behavior it's no other than checking proximity from which side and then grouping on.
The turning piece action is performed with double-gesture and relative-to-piece angles.

![CapturaPieces](https://github.com/Jmlucero1984/Vuzzle/assets/91501518/95b31a80-e69c-4c26-a698-9ac4cd6d57ea)

The wood framing final step it's only an stretching image algorithm.

![marco_sup_izq](https://github.com/Jmlucero1984/Vuzzle/assets/91501518/2dda7d5e-ee4e-40f6-8a61-01a36b5ac1d7)

## Bootleg
I don't care this doesn't fit into GitHub best practices, it's part of my beginnings, all this started with a couple bush sketches (December 2021)
![bootleg](https://github.com/Jmlucero1984/Vuzzle/assets/91501518/906a6c5a-0389-4e2c-b075-5cce9f8aefec)
