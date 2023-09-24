# 8Bit
This is an attempt to create 8-bit computer. It is highly inspired by the perfect Ben Eater youtube series [Building an 8-bit breadboard computer!](https://www.youtube.com/watch?v=HyznrdDSSGM&list=PLowKtXNTBypGqImE405J2565dvjafglHU)

## Requirements
In addition to original Ben Eater cpu capabilities there is a need to have more RAM to be able to run more interesting programs. Also IO and ability to easily switch programs is desired.

Requirements:
* 8 bit data bus
* 16 bit long ram address
* I/O flag
* Execution code read from eeprom
* Compilable assembler code
  * Arithmetic operations
  * Data move operations
  * Jump operation
  * Call operation

## Simulation
Having very limited peaces of necessary components and hardware overall, there is a desire to make as less mistakes as possible. Speed of prototyping is important as well.

Unfortunately there is a very limited availability of software to easily simulate desired set of components. Therefore the first part of the project is aiming at simulating electric circuit and mimicking behavior of each chip in the circuit.

Once the simulation is giving desired output, then simulated circuits would be replicated in hardware.

As part of simulation a virtual 8bit computer programmable by custom assembly language was created. To demonstrate its capabilities a snake mini-game was implemented.

Run snake:
```bash
cd simulation
sbt "run -ef snake.asm"
```

## Hardware
TBD

