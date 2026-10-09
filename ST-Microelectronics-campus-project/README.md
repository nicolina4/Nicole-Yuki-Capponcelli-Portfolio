# Rubik's Cube on STM32

Embedded project developed during the STMicroelectronics campus programme, running on an STM32G474RE Nucleo-64 board.

## What it does

When the board starts, the display shows the unfolded net of a scrambled Rubik's Cube and the message "Press center to start". Pressing the centre button of the joystick starts the animation: the cube goes back through a sequence of intermediate states, one every half second, until it reaches the solved configuration. At that point the display shows "Success" and the board plays a short melody through its DAC.

## Hardware

- STM32G474RE Nucleo-64 board (ARM Cortex-M4)
- ILI9341 TFT display
- Joystick (centre button on pin PC7)
- Speaker or headphones connected to the DAC1 output

## Software

- **ChibiOS/RT 21.11** as the real-time operating system and hardware abstraction layer
- **uGFX** as the graphics library, with the ILI9341 driver

## Code structure

| File | Role |
| --- | --- |
| `main.c` | Startup, joystick handling and the sequence of cube states shown during the animation |
| `display/` | Draws the net of the cube (six 3x3 faces) and the text messages on the screen |
| `rubik/cube.c` | Cube model: 54 stickers and the rotation of each face, clockwise and counterclockwise |
| `rubik/solver.c` | Parses move sequences in standard notation (U, F, R, B, L, D, with `'` and `2`) and applies them to the cube |
| `music/` | Plays a melody by streaming a sine wave lookup table to DAC1, with timer GPT6 setting the frequency of each note |
| `cfg/` | ChibiOS, HAL, MCU and uGFX configuration |

Each cube state is stored as a string of 54 characters, one per sticker, using the letters W, Y, R, O, G and B for the six colours. The current firmware plays a fixed sequence of states, while the cube model and the move parser are kept as separate modules so they can be used to compute the states at run time.

A serial connection at 115200 baud is already set up in the code and can be enabled with `USE_SERIAL`, for example to receive the cube state from a computer.

## Building and flashing

The `Makefile` expects ChibiOS and uGFX two levels above the project folder:

```
../../chibios2111
../../ugfx
```

With the `arm-none-eabi` toolchain installed, run:

```
make
```

The project was developed in ChibiStudio (Eclipse). The `debug` folder contains the OpenOCD launch configurations to flash and run the firmware on the board.

## Tools

C, ChibiOS/RT, uGFX, ARM GCC, OpenOCD, ChibiStudio.
