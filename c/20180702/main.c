#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include <gif_lib.h>

#include "remap.h"

#define NAME "/home/ssc/Pictures/demo.last.ppm"
#define FPPM "/home/ssc/Pictures/demo.last.cache.ppm"
#define FGIF "/home/ssc/Pictures/demo.last.cache.gif"
#define W 1200
#define H 821
#define S 55

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


	int32_t **dist = (int32_t **) malloc(sizeof(int32_t *) * 256);
	int i, j, p;
	for (i = 0; i < 256; ++i) {
		dist[i] = (int32_t *) malloc(sizeof(int32_t *) * 256);
		for (j = 0; j < 256; ++j) {
			dist[i][j] = (i - j) * (i - j);
		}
	}

	uint32_t *pixels = (uint32_t *) malloc(sizeof(uint32_t) * count);
	uint8_t *indexes = (uint8_t *) malloc(sizeof(uint8_t) * count);
	uint8_t *colors = (uint8_t *) malloc(sizeof(uint8_t) * 256 * 3);

	for (i = 0, p = 0; i < count; ++i) {
		int r = buffer[p++];
		int g = buffer[p++];
		int b = buffer[p++];

		pixels[i] = b | (g << 8) | (r << 16);
	}

	fprintf(stderr, "========================1\n");

	remap(pixels, indexes, count, colors, dist);

	fprintf(stderr, "========================2\n");


	for (i = 0, p = 0; i < count; ++i) {
		j = indexes[i] * 3;
		buffer[p++] = colors[j + 0];
		buffer[p++] = colors[j + 1];
		buffer[p++] = colors[j + 2];
	}

	fprintf(stderr, "========================3\n");

	FILE *swap = fopen(FPPM, "w");
	fprintf(swap, "P6\n%d %d\n%d\n", W, H, 255);
	fwrite(buffer, W * H * 3, 1, swap);
	fflush(swap);
	fclose(swap);

	fprintf(stderr, "========================4\n");



	GifFileType *gif = EGifOpenFileName(FGIF, 0, 0);

	EGifSetGifVersion(gif, 1);

	gif->SColorResolution = 8;
	gif->SWidth = 1200;
	gif->SWidth = 821;

	fprintf(stderr, "========================5\n");
	SavedImage *image = GifMakeSavedImage(gif, NULL);
	fprintf(stderr, "========================5.1\n");
	GifByteType * raster = (GifByteType *) malloc(sizeof(GifByteType) * count);
	fprintf(stderr, "========================5.1\n");
	memcpy(raster, indexes, count);

	GifColorType *colorMap = (GifColorType*) malloc(sizeof(GifColorType) * 256);
	memcpy(colorMap, colors, sizeof(GifColorType) * 256);
	ColorMapObject *mapObject = GifMakeMapObject(256, colorMap);
	image->ImageDesc.ColorMap = mapObject;
	image->ImageDesc.Width = 1200;
	image->ImageDesc.Height = 821;
	image->RasterBits = raster;

	fprintf(stderr, "========================6\n");
	gif->SavedImages = image;
	gif->ImageCount = 1;




	GraphicsControlBlock *block = (GraphicsControlBlock*) malloc(
			sizeof(GraphicsControlBlock));
	block->DisposalMode = 0;
	block->DelayTime = 0;
	block->TransparentColor = -1;

	EGifGCBToSavedExtension(block, gif, 0);



	fprintf(stderr, "========================7\n");
	int error=EGifSpew(gif);

	fprintf(stderr, "========================8:%d:%s\n",error,GifErrorString(error));

	return 0;
}
