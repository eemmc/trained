#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include "remap.h"

#define NAME "/home/ssc/Pictures/demo.last.ppm"
#define LAST "/home/ssc/Pictures/demo.last.cache.ppm"
#define W 1200
#define H 821
#define S 55

//#define NAME "/home/ssc/Pictures/colormap.ppm"
//#define LAST "/home/ssc/Pictures/colormap.cache.ppm"
//#define W 16
//#define H 16
//#define S 12

int main(int argc, char **argv) {

	char line[200];
	FILE *file = fopen(NAME, "r");
	fread(line, 1, S, file);
	printf("%s", line);

	uint8_t *buffer;
	size_t count = sizeof(uint8_t) * W * H;
	size_t size = count * 3;
	buffer = (uint8_t *) malloc(size);
	fread(buffer, 1, size, file);
	fclose(file);

	int32_t *pixels = (int32_t *) malloc(sizeof(int32_t) * count);

	int i, p;
	for (i = 0, p = 0; i < count; ++i) {
		int r = buffer[p++];
		int g = buffer[p++];
		int b = buffer[p++];

		pixels[i] = b | (g << 8) | (r << 16);
	}

	fprintf(stderr,"========================1\n");
	remap(pixels, count);
	fprintf(stderr,"========================2\n");
	for (i = 0, p = 0; i < count; ++i) {
		buffer[p++] = (pixels[i] >> 16) & 0xFF;
		buffer[p++] = (pixels[i] >> 8) & 0xFF;
		buffer[p++] = pixels[i] & 0xFF;
	}
	fprintf(stderr,"========================3\n");
	FILE *swap = fopen(LAST, "w");
	fprintf(swap, "P6\n%d %d\n%d\n", W, H, 255);
	fwrite(buffer, W * H * 3, 1, swap);
	fflush(swap);
	fclose(swap);

	fprintf(stderr,"========================4\n");
	return 0;
}
