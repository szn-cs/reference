/**
 * @file stat_process.h
 * @brief header declarations
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include <pthread.h>

#ifndef __STAT_H__

static int msleep(long tms);
static void getargs(int *sleeptime_ms, int *num_threads, char **shm_name,
                    int argc, char *argv[]);

#endif
