#ifndef RUBIK_CUBE_H
#define RUBIK_CUBE_H

#include <stdint.h>

typedef enum {
    WHITE = 'W',
    YELLOW = 'Y',
    RED = 'R',
    ORANGE = 'O',
    GREEN = 'G',
    BLUE = 'B'
} Color;

typedef enum {
    UP = 0,
    FRONT = 1,
    RIGHT = 2,
    BACK = 3,
    LEFT = 4,
    DOWN = 5
} Face;

typedef struct {
    char faces[6][9];
} Cube;

void initCube(Cube *cube, const char *state);
void rotateFace(Cube *cube, Face face, int clockwise);
void rotateUp(Cube *cube, int clockwise);
void rotateFront(Cube *cube, int clockwise);
void rotateRight(Cube *cube, int clockwise);
void rotateBack(Cube *cube, int clockwise);
void rotateLeft(Cube *cube, int clockwise);
void rotateDown(Cube *cube, int clockwise);
int isSolved(const Cube *cube);
void copyCube(const Cube *src, Cube *dest);

#endif
