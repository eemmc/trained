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

int EGifAddSavedImage(GifFileType *file, int32_t index, uint8_t *indexes,
		uint8_t *colors, const SavedImage *copy) {

	size_t length = file->SWidth * file->SHeight;
	GifByteType * raster = (GifByteType *) malloc(sizeof(GifByteType) * length);
	memcpy(raster, indexes, length);

	GifColorType *coloramp = (GifColorType*) malloc(sizeof(GifColorType) * 256);
	memcpy(coloramp, colors, sizeof(GifColorType) * 256);

	ColorMapObject *mapObject = GifMakeMapObject(256, coloramp);

	SavedImage *image = &file->SavedImages[index];
	image->RasterBits = raster;
	image->ImageDesc.ColorMap = mapObject;
	image->ImageDesc.Interlace = 0;
	image->ImageDesc.Left = 0;
	image->ImageDesc.Top = 0;
	image->ImageDesc.Width = file->SWidth;
	image->ImageDesc.Height = file->SHeight;

	GraphicsControlBlock gcb;
	if (copy != NULL) {
		image->ExtensionBlockCount = copy->ExtensionBlockCount;
		size_t size = sizeof(ExtensionBlock) * copy->ExtensionBlockCount;
		image->ExtensionBlocks = (ExtensionBlock *) malloc(size);
		memcpy(image->ExtensionBlocks, copy->ExtensionBlocks, size);
		DGifSavedExtensionToGCB(file, index, &gcb);
		gcb.TransparentColor = -1;
		EGifGCBToSavedExtension(&gcb, file, index);
	} else {
		GifByteType buf[100];
		gcb.DisposalMode = DISPOSAL_UNSPECIFIED;
		gcb.TransparentColor = -1;
		gcb.DelayTime = 0;

		size_t size = EGifGCBToExtension(&gcb, buf);
		ExtensionBlock *block = (ExtensionBlock *) malloc(
				sizeof(ExtensionBlock));
		block->Function = GRAPHICS_EXT_FUNC_CODE;
		block->ByteCount = size;
		block->Bytes = (GifByteType*) malloc(sizeof(GifByteType) * size);
		memcpy(block->Bytes, buf, sizeof(GifByteType) * size);
		image->ExtensionBlocks = block;
		image->ExtensionBlockCount = 1;
	}

	return 0;
}


int main(int argc, char **argv) {

	char line[200];
	FILE *file = fopen(NAME, "r");
	fread(line, 1, S, file);
	printf("%s", line);

	uint8_t *buffer, *indexes, *colors;
	size_t count = sizeof(uint8_t) * W * H;
	size_t size = count * 3;

	buffer = (uint8_t *) malloc(size);
	indexes = (uint8_t *) malloc(count);
	colors = (uint8_t *) malloc(sizeof(uint8_t) * 256 * 3);

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

	fprintf(stderr, "========================1\n");

	remap(buffer, indexes, size, colors, dist);

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
	gif->SColorResolution = 8;
	gif->SWidth = 1200;
	gif->SHeight = 821;

	gif->ImageCount = 1;
	gif->SavedImages = (SavedImage *) malloc(
			sizeof(SavedImage) * gif->ImageCount);


	EGifAddSavedImage(gif, 0, indexes, colors, NULL);


	EGifSpew(gif);

	return 0;
}
