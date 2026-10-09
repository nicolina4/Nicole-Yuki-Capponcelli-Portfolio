#include "ch.h"
#include "hal.h"
#include "chprintf.h"          // <- optional; not used now
#include "display/display.h"
#include "rubik/cube.h"        // <- not needed for showing static configs
#include "rubik/solver.h"      // <- not needed now
#include "music/music.h"

#define CUBE_DATA_SIZE 54
#define BAUD_RATE 115200
#ifndef USE_SERIAL
#define USE_SERIAL 0
#endif
#define JOY_CENTER_LINE PAL_LINE(GPIOC, 7)

/* ---- fwd decls ---- */
static void joystickInit(void);
static void serialInit(void);
static bool joystickPressed(void);
static void waitRelease(void);
static void loadCubeFromString(const char *src54, char dst54[CUBE_DATA_SIZE]);

/* ---- serial cfg (kept for later) ---- */
#if USE_SERIAL
static const SerialConfig serial_cfg = { BAUD_RATE, 0, 0, 0 };
#endif


/* ---- configurations ---- */
static const char configuration_0[CUBE_DATA_SIZE + 1] =
"WWWWWWWWW""RRRRRRRRR""GGGGGGGGG""YYYYYYYYY""OOOOOOOOO""BBBBBBBBB";

static const char configuration_1[CUBE_DATA_SIZE + 1] =
"WWGWWGWWG""RRRRRRRRR""GGYGGYGGY""YYBYYBYYB""OOOOOOOOO""BBWBBWBBW";

static const char configuration_2[CUBE_DATA_SIZE + 1] =
"WWGWWGWWG""RRBRRBRRW""GGYGGYRRR""BBBYYYYYY""GOOGOOYOO""OOOBBWBBW";

static const char configuration_3[CUBE_DATA_SIZE + 1] =
"WWGWWGYOO""RRBRRBWWG""RGGRGGRYY""WRRYYYYYY""GOOGOOBBB""OOOBBWBBW";

static const char configuration_4[CUBE_DATA_SIZE + 1] =
"GOOWWGYOO""WWGRRBWWG""WRRYYYBYY""YYYGOOBBB""GOOGOOBBB""OWWOBBOBB";

static const char configuration_5[CUBE_DATA_SIZE + 1] =
"GOOWWGYOO""RGGWRWGBG""BGYOGYYRR""RYRRYRWYB""BGYBOYOBB""WOORBOOBO";

static const char configuration_6[CUBE_DATA_SIZE + 1] =
"GOOWWGYOO""RGGWRWGBG""GRYBGOGGB""RRBYYYRRW""YYRBOYOBB""WOBRBGWBY";

static const char configuration_7[CUBE_DATA_SIZE + 1] =
"GOOWWGYOO""RGGWRWWWO""GRYBGOGGB""RRBYYYRRW""YYRBOYOBB""WOBRBGWBY";

static const char configuration_8[CUBE_DATA_SIZE + 1] =
"GOOWWGYOO""RGGWRWYGB""YOBRGGWWO""BYWRYRRYR""GBGBOYGBB""WOYRBYWBR";

static const char configuration_9[CUBE_DATA_SIZE + 1] =
"GOGWWWYOB""RGWWRRYGR""WRYWGOOGB""BYGRYYRYB""GBOBOGOBO""WRWBBORYY";

static const char configuration_10[CUBE_DATA_SIZE + 1] =
"GOGBYYOOB""GGWWRRYGR""OWWGGRBOY""RYGWYYYYB""BBOROGRBO""WRWBBORYY";

static const char configuration_11[CUBE_DATA_SIZE + 1] =
"GOGWWWYOB""WRRBRGRWY""RGYRGGWWO""RRBYYYBYG""OGYBYYOBB""RGWRGGWWO";

static const char configuration_12[CUBE_DATA_SIZE + 1] =
"ROGRWWWOB""WRRBRGRWY""RGWYGGBWO""WRBRYYOYG""YOBGOBOBG""GOYWBYYBO";

static const char configuration_13[CUBE_DATA_SIZE + 1] =
"ROYRWYWOO""WRRBRGRWY""RGGYWBWB""WRWRYGOYO""YOBGOBOBG""GOBWBYYBG";



/* order: oldest -> newest */
static const char* const SEQUENCE[] = {
  configuration_0,
  configuration_1,
  configuration_2,
  configuration_3,
  configuration_4,
  configuration_5,
  configuration_6,
  configuration_7,
  configuration_8,
  configuration_9,
  configuration_10,
  configuration_11,
  configuration_12,
  configuration_13
};

/* working buffer for renderer */
static char cube_state[CUBE_DATA_SIZE];

int main(void) {
  halInit();
  chSysInit();

  musicInit();

  displayInit();
  joystickInit();
#if USE_SERIAL
  serialInit();
#endif


  /* show LAST configuration first */
  const size_t N = sizeof(SEQUENCE)/sizeof(SEQUENCE[0]);
  {
    const char* kCubeString = SEQUENCE[N - 1];
    loadCubeFromString(kCubeString, cube_state);
    rubikDrawNetFromCube(cube_state, 10, 25);
    displayString("Press center to start");
  }

  /* wait press */
  while (!joystickPressed()) chThdSleepMilliseconds(20);
  waitRelease();

  /* play backward to configuration_0 */
  for (int i = (int)N - 2; i >= 0; --i) {
    const char* kCubeString = SEQUENCE[i];
    loadCubeFromString(kCubeString, cube_state);
    rubikDrawNetFromCube(cube_state, 10, 25);
    if (i > 0) chThdSleepMilliseconds(500);
  }

  /* reached configuration_0 */
  displayString("Success");
  playMusic();



  /* idle forever so the screen stays up */
  while (true) {
    chThdSleepMilliseconds(250);
  }
}
/* ------------ Helpers ------------ */
static void joystickInit(void) {
  palSetLineMode(JOY_CENTER_LINE, PAL_MODE_INPUT_PULLUP);
}
static bool joystickPressed(void) {
  return palReadLine(JOY_CENTER_LINE) == PAL_LOW;
}
static void waitRelease(void) {
  chThdSleepMilliseconds(20);
  while (joystickPressed()) chThdSleepMilliseconds(10);
  chThdSleepMilliseconds(20);
}
static void serialInit(void){
#if USE_SERIAL
  palSetLineMode(PAL_LINE(GPIOA, 2), PAL_MODE_ALTERNATE(7));
  palSetLineMode(PAL_LINE(GPIOA, 3), PAL_MODE_ALTERNATE(7));
  sdStart(&SD2, &serial_cfg);
#endif
}
static void loadCubeFromString(const char *src54, char dst54[CUBE_DATA_SIZE]) {
  if (!src54) { for (int i=0;i<CUBE_DATA_SIZE;++i) dst54[i]='W'; return; }
  for (int i=0;i<CUBE_DATA_SIZE;++i) {
    char c = src54[i];
    switch (c) { case 'W': case 'Y': case 'R': case 'O': case 'B': case 'G': dst54[i]=c; break;
                 default: dst54[i]='W'; break; }
  }
}
