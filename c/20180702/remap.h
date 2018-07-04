#ifndef REMAP_CPP_
#define REMAP_CPP_

#include <stdint.h>
#include <stdlib.h>

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

int remap(uint8_t *buffer, uint8_t *indexes, size_t size, uint8_t *map,
		int32_t **dist);
#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* REMAP_CPP_ */
