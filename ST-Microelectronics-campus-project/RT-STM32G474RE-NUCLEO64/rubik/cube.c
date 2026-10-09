#include "cube.h"
#include <string.h>

void initCube(Cube *cube, const char *state) {
    for (int face = 0; face < 6; face++) {
        for (int i = 0; i < 9; i++) {
            cube->faces[face][i] = state[face * 9 + i];
        }
    }
}

void rotateFace(Cube *cube, Face face, int clockwise) {
    char temp[9];
    memcpy(temp, cube->faces[face], 9);

    if (clockwise) {
        cube->faces[face][0] = temp[6];
        cube->faces[face][1] = temp[3];
        cube->faces[face][2] = temp[0];
        cube->faces[face][3] = temp[7];
        cube->faces[face][4] = temp[4];
        cube->faces[face][5] = temp[1];
        cube->faces[face][6] = temp[8];
        cube->faces[face][7] = temp[5];
        cube->faces[face][8] = temp[2];
    } else {
        cube->faces[face][0] = temp[2];
        cube->faces[face][1] = temp[5];
        cube->faces[face][2] = temp[8];
        cube->faces[face][3] = temp[1];
        cube->faces[face][4] = temp[4];
        cube->faces[face][5] = temp[7];
        cube->faces[face][6] = temp[0];
        cube->faces[face][7] = temp[3];
        cube->faces[face][8] = temp[6];
    }
}

void rotateUp(Cube *cube, int clockwise) {
    rotateFace(cube, UP, clockwise);

    char temp[3];
    if (clockwise) {
        memcpy(temp, &cube->faces[FRONT][0], 3);
        memcpy(&cube->faces[FRONT][0], &cube->faces[RIGHT][0], 3);
        memcpy(&cube->faces[RIGHT][0], &cube->faces[BACK][0], 3);
        memcpy(&cube->faces[BACK][0], &cube->faces[LEFT][0], 3);
        memcpy(&cube->faces[LEFT][0], temp, 3);
    } else {
        memcpy(temp, &cube->faces[FRONT][0], 3);
        memcpy(&cube->faces[FRONT][0], &cube->faces[LEFT][0], 3);
        memcpy(&cube->faces[LEFT][0], &cube->faces[BACK][0], 3);
        memcpy(&cube->faces[BACK][0], &cube->faces[RIGHT][0], 3);
        memcpy(&cube->faces[RIGHT][0], temp, 3);
    }
}

void rotateFront(Cube *cube, int clockwise) {
    rotateFace(cube, FRONT, clockwise);

    char temp[3];
    if (clockwise) {
        temp[0] = cube->faces[UP][6];
        temp[1] = cube->faces[UP][7];
        temp[2] = cube->faces[UP][8];

        cube->faces[UP][6] = cube->faces[LEFT][8];
        cube->faces[UP][7] = cube->faces[LEFT][5];
        cube->faces[UP][8] = cube->faces[LEFT][2];

        cube->faces[LEFT][2] = cube->faces[DOWN][0];
        cube->faces[LEFT][5] = cube->faces[DOWN][1];
        cube->faces[LEFT][8] = cube->faces[DOWN][2];

        cube->faces[DOWN][0] = cube->faces[RIGHT][6];
        cube->faces[DOWN][1] = cube->faces[RIGHT][3];
        cube->faces[DOWN][2] = cube->faces[RIGHT][0];

        cube->faces[RIGHT][0] = temp[2];
        cube->faces[RIGHT][3] = temp[1];
        cube->faces[RIGHT][6] = temp[0];
    } else {
        temp[0] = cube->faces[UP][6];
        temp[1] = cube->faces[UP][7];
        temp[2] = cube->faces[UP][8];

        cube->faces[UP][6] = cube->faces[RIGHT][0];
        cube->faces[UP][7] = cube->faces[RIGHT][3];
        cube->faces[UP][8] = cube->faces[RIGHT][6];

        cube->faces[RIGHT][0] = cube->faces[DOWN][2];
        cube->faces[RIGHT][3] = cube->faces[DOWN][1];
        cube->faces[RIGHT][6] = cube->faces[DOWN][0];

        cube->faces[DOWN][0] = cube->faces[LEFT][2];
        cube->faces[DOWN][1] = cube->faces[LEFT][5];
        cube->faces[DOWN][2] = cube->faces[LEFT][8];

        cube->faces[LEFT][2] = temp[2];
        cube->faces[LEFT][5] = temp[1];
        cube->faces[LEFT][8] = temp[0];
    }
}

void rotateRight(Cube *cube, int clockwise) {
    rotateFace(cube, RIGHT, clockwise);

    char temp[3];
    if (clockwise) {
        temp[0] = cube->faces[FRONT][2];
        temp[1] = cube->faces[FRONT][5];
        temp[2] = cube->faces[FRONT][8];

        cube->faces[FRONT][2] = cube->faces[DOWN][2];
        cube->faces[FRONT][5] = cube->faces[DOWN][5];
        cube->faces[FRONT][8] = cube->faces[DOWN][8];

        cube->faces[DOWN][2] = cube->faces[BACK][6];
        cube->faces[DOWN][5] = cube->faces[BACK][3];
        cube->faces[DOWN][8] = cube->faces[BACK][0];

        cube->faces[BACK][0] = cube->faces[UP][8];
        cube->faces[BACK][3] = cube->faces[UP][5];
        cube->faces[BACK][6] = cube->faces[UP][2];

        cube->faces[UP][2] = temp[0];
        cube->faces[UP][5] = temp[1];
        cube->faces[UP][8] = temp[2];
    } else {
        temp[0] = cube->faces[FRONT][2];
        temp[1] = cube->faces[FRONT][5];
        temp[2] = cube->faces[FRONT][8];

        cube->faces[FRONT][2] = cube->faces[UP][2];
        cube->faces[FRONT][5] = cube->faces[UP][5];
        cube->faces[FRONT][8] = cube->faces[UP][8];

        cube->faces[UP][2] = cube->faces[BACK][6];
        cube->faces[UP][5] = cube->faces[BACK][3];
        cube->faces[UP][8] = cube->faces[BACK][0];

        cube->faces[BACK][0] = cube->faces[DOWN][8];
        cube->faces[BACK][3] = cube->faces[DOWN][5];
        cube->faces[BACK][6] = cube->faces[DOWN][2];

        cube->faces[DOWN][2] = temp[0];
        cube->faces[DOWN][5] = temp[1];
        cube->faces[DOWN][8] = temp[2];
    }
}

void rotateBack(Cube *cube, int clockwise) {
    rotateFace(cube, BACK, clockwise);

    char temp[3];
    if (clockwise) {
        temp[0] = cube->faces[UP][0];
        temp[1] = cube->faces[UP][1];
        temp[2] = cube->faces[UP][2];

        cube->faces[UP][0] = cube->faces[RIGHT][2];
        cube->faces[UP][1] = cube->faces[RIGHT][5];
        cube->faces[UP][2] = cube->faces[RIGHT][8];

        cube->faces[RIGHT][2] = cube->faces[DOWN][8];
        cube->faces[RIGHT][5] = cube->faces[DOWN][7];
        cube->faces[RIGHT][8] = cube->faces[DOWN][6];

        cube->faces[DOWN][6] = cube->faces[LEFT][0];
        cube->faces[DOWN][7] = cube->faces[LEFT][3];
        cube->faces[DOWN][8] = cube->faces[LEFT][6];

        cube->faces[LEFT][0] = temp[2];
        cube->faces[LEFT][3] = temp[1];
        cube->faces[LEFT][6] = temp[0];
    } else {
        temp[0] = cube->faces[UP][0];
        temp[1] = cube->faces[UP][1];
        temp[2] = cube->faces[UP][2];

        cube->faces[UP][0] = cube->faces[LEFT][6];
        cube->faces[UP][1] = cube->faces[LEFT][3];
        cube->faces[UP][2] = cube->faces[LEFT][0];

        cube->faces[LEFT][0] = cube->faces[DOWN][6];
        cube->faces[LEFT][3] = cube->faces[DOWN][7];
        cube->faces[LEFT][6] = cube->faces[DOWN][8];

        cube->faces[DOWN][6] = cube->faces[RIGHT][8];
        cube->faces[DOWN][7] = cube->faces[RIGHT][5];
        cube->faces[DOWN][8] = cube->faces[RIGHT][2];

        cube->faces[RIGHT][2] = temp[0];
        cube->faces[RIGHT][5] = temp[1];
        cube->faces[RIGHT][8] = temp[2];
    }
}

void rotateLeft(Cube *cube, int clockwise) {
    rotateFace(cube, LEFT, clockwise);

    char temp[3];
    if (clockwise) {
        temp[0] = cube->faces[FRONT][0];
        temp[1] = cube->faces[FRONT][3];
        temp[2] = cube->faces[FRONT][6];

        cube->faces[FRONT][0] = cube->faces[UP][0];
        cube->faces[FRONT][3] = cube->faces[UP][3];
        cube->faces[FRONT][6] = cube->faces[UP][6];

        cube->faces[UP][0] = cube->faces[BACK][8];
        cube->faces[UP][3] = cube->faces[BACK][5];
        cube->faces[UP][6] = cube->faces[BACK][2];

        cube->faces[BACK][2] = cube->faces[DOWN][6];
        cube->faces[BACK][5] = cube->faces[DOWN][3];
        cube->faces[BACK][8] = cube->faces[DOWN][0];

        cube->faces[DOWN][0] = temp[0];
        cube->faces[DOWN][3] = temp[1];
        cube->faces[DOWN][6] = temp[2];
    } else {
        temp[0] = cube->faces[FRONT][0];
        temp[1] = cube->faces[FRONT][3];
        temp[2] = cube->faces[FRONT][6];

        cube->faces[FRONT][0] = cube->faces[DOWN][0];
        cube->faces[FRONT][3] = cube->faces[DOWN][3];
        cube->faces[FRONT][6] = cube->faces[DOWN][6];

        cube->faces[DOWN][0] = cube->faces[BACK][8];
        cube->faces[DOWN][3] = cube->faces[BACK][5];
        cube->faces[DOWN][6] = cube->faces[BACK][2];

        cube->faces[BACK][2] = cube->faces[UP][6];
        cube->faces[BACK][5] = cube->faces[UP][3];
        cube->faces[BACK][8] = cube->faces[UP][0];

        cube->faces[UP][0] = temp[0];
        cube->faces[UP][3] = temp[1];
        cube->faces[UP][6] = temp[2];
    }
}

void rotateDown(Cube *cube, int clockwise) {
    rotateFace(cube, DOWN, clockwise);

    char temp[3];
    if (clockwise) {
        memcpy(temp, &cube->faces[FRONT][6], 3);
        memcpy(&cube->faces[FRONT][6], &cube->faces[LEFT][6], 3);
        memcpy(&cube->faces[LEFT][6], &cube->faces[BACK][6], 3);
        memcpy(&cube->faces[BACK][6], &cube->faces[RIGHT][6], 3);
        memcpy(&cube->faces[RIGHT][6], temp, 3);
    } else {
        memcpy(temp, &cube->faces[FRONT][6], 3);
        memcpy(&cube->faces[FRONT][6], &cube->faces[RIGHT][6], 3);
        memcpy(&cube->faces[RIGHT][6], &cube->faces[BACK][6], 3);
        memcpy(&cube->faces[BACK][6], &cube->faces[LEFT][6], 3);
        memcpy(&cube->faces[LEFT][6], temp, 3);
    }
}

int isSolved(const Cube *cube) {
    for (int face = 0; face < 6; face++) {
        char center = cube->faces[face][4];
        for (int i = 0; i < 9; i++) {
            if (cube->faces[face][i] != center) {
                return 0;
            }
        }
    }
    return 1;
}

void copyCube(const Cube *src, Cube *dest) {
    memcpy(dest, src, sizeof(Cube));
}
