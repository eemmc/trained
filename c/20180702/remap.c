#include "remap.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#define COLOR_SIZE 256

struct _tuple {
	int32_t r;
	int32_t g;
	int32_t b;
	int32_t i;
};

struct _tuple_node {
	uint32_t key;
	struct _tuple *value;
	struct _tuple_node *next;
};

struct _tuple_map {
	struct _tuple_node **buckets;
	size_t capacity;
	size_t size;
};

struct _tuple_box {
	int start;
	int count;

	size_t size;
	struct _tuple_node *data;

	int32_t limit[6];
	struct _tuple value;
};

uint32_t tuple_map_index(uint32_t key, size_t capacity) {
	return key & (capacity - 1);
}

void tuple_map_reset(struct _tuple_map *map, size_t capacity) {

	struct _tuple_node **temp = map->buckets;
	size_t size = sizeof(struct _tuple *) * capacity;
	map->buckets = (struct _tuple_node**) malloc(size);
	memset(map->buckets, 0, size);

	size_t i;
	uint32_t hash;
	struct _tuple_node * curr;
	struct _tuple_node * swap;
	for (i = 0; i < map->capacity; ++i) {
		curr = temp[i];
		while (curr != NULL) {
			swap = curr;
			curr = curr->next;

			hash = tuple_map_index(swap->key, capacity);

			swap->next = map->buckets[hash];
			map->buckets[hash] = swap;
		}
	}

	map->capacity = capacity;
	free(temp);
}

struct _tuple *tuple_map_get_tuple(struct _tuple_map *map, uint32_t key) {

	struct _tuple_node *item;
	uint32_t hash = tuple_map_index(key, map->capacity);
	for (item = map->buckets[hash]; item != NULL; item = item->next) {
		if (item->key == key) {
			return item->value;
		}
	}

	return NULL;
}

void tuple_map_add_tuple(struct _tuple_map *map, uint32_t key,
		struct _tuple *tuple) {

	struct _tuple *swap = tuple_map_get_tuple(map, key);
	if (swap != NULL) {
		return;
	}

	if (0.75 * map->size >= map->capacity) {
		tuple_map_reset(map, map->capacity << 1);
	}

	uint32_t hash = tuple_map_index(key, map->capacity);

	struct _tuple_node *item = (struct _tuple_node*) malloc(
			sizeof(struct _tuple_node));
	item->key = key;
	item->value = tuple;

	item->next = map->buckets[hash];
	map->buckets[hash] = item;

	map->size += 1;
}

void tuple_map_copy_tuple(struct _tuple_map *map, struct _tuple_box *box) {

	struct _tuple_node *head;
	struct _tuple_node *item;
	struct _tuple_node *swap;

	size_t i;
	for (i = 0; i < map->capacity; ++i) {
		item = map->buckets[i];
		while (item != NULL) {
			swap = (struct _tuple_node*) malloc(sizeof(struct _tuple_node));
			swap->value = item->value;
			if (box->data == NULL) {
				box->data = head = swap;
			} else {
				head->next = swap;
				head = head->next;
			}

			item = item->next;
			box->size += 1;
		}
	}
}

void tuple_box_free(struct _tuple_box **boxes, size_t size) {

	size_t i;
	struct _tuple_node *item;
	struct _tuple_node *swap;
	for (i = 0; i < size; ++i) {
		item = boxes[i]->data;
		while (item != NULL) {
			swap = item->next;
			free(item);
			item = swap;
		}
		free(boxes[i]);
	}

	free(boxes);
}

void tuple_map_free(struct _tuple_map *map) {
	size_t i;
	struct _tuple_node *item;
	struct _tuple_node *swap;
	for (i = 0; i < map->capacity; ++i) {
		item = map->buckets[i];
		while (item != NULL) {
			swap = item->next;
			free(item->value);
			free(item);
			item = swap;
		}
	}

	free(map->buckets);
	map->buckets = NULL;
	map->size = 0;
}

struct _tuple * tuple_create(uint32_t pixel) {
	struct _tuple *item = (struct _tuple*) malloc(sizeof(struct _tuple));

	item->r = (pixel >> 16) & 0xFF;
	item->g = (pixel >> 8) & 0xFF;
	item->b = pixel & 0xFF;
	item->i = -1;

	return item;
}

void tuple_box_median_sort_each(struct _tuple *tuple, struct _tuple_box *box) {
	if (tuple->r > box->limit[0]) {
		box->limit[0] = tuple->r;
	} else if (tuple->r < box->limit[1]) {
		box->limit[1] = tuple->r;
	} else {
		box->limit[0] = box->limit[1] = tuple->r;
	}

	if (tuple->g > box->limit[2]) {
		box->limit[2] = tuple->g;
	} else if (tuple->g < box->limit[3]) {
		box->limit[3] = tuple->g;
	} else {
		box->limit[2] = box->limit[3] = tuple->g;
	}

	if (tuple->b > box->limit[4]) {
		box->limit[4] = tuple->b;
	} else if (tuple->b < box->limit[5]) {
		box->limit[5] = tuple->b;
	} else {
		box->limit[4] = box->limit[5] = tuple->b;
	}

}

static int max;
typedef const struct _tuple_node ** box_item;
int tuple_box_median_sort_compar(const void *o1, const void *o2) {
	switch (max) {
	case 0:
		return (*((box_item) o1))->value->r - (*((box_item) o2))->value->r;
	case 1:
		return (*((box_item) o1))->value->g - (*((box_item) o2))->value->g;
	case 2:
		return (*((box_item) o1))->value->b - (*((box_item) o2))->value->b;
	default:
		return 0;
	}
}

void tuple_box_median_sort(struct _tuple_box *box) {
	memset(box->limit, 0, sizeof(uint32_t) * 6);

	struct _tuple_node **swap = (struct _tuple_node**) malloc(
			sizeof(struct _tuple_node*) * box->size);

	size_t i;
	struct _tuple_node *item;
	for (i = 0, item = box->data; item != NULL; item = item->next) {
		tuple_box_median_sort_each(item->value, box);
		swap[i++] = item;
	}

	int r = box->limit[0] - box->limit[1];
	int g = box->limit[2] - box->limit[3];
	int b = box->limit[4] - box->limit[5];
	if (r >= g && r >= b) {
		max = 0;
	} else if (g >= r && g >= b) {
		max = 1;
	} else if (b >= r && b >= g) {
		max = 2;
	} else {
		max = 0;
	}

	qsort(swap, box->size, sizeof(struct _tuple_node*),
			&tuple_box_median_sort_compar);

	box->data = NULL;
	for (i = 0; i < box->size; ++i) {
		item = swap[i];
		item->next = box->data;
		box->data = item;
	}

	free(swap);
}

void tuple_box_median_cut(uint32_t index, struct _tuple_box **boxes) {

	struct _tuple_box *box = boxes[index];
	if (box->count <= 1) {
		return;
	}

	tuple_box_median_sort(box);
	box->count = box->count / 2;

	struct _tuple_box *newBox = (struct _tuple_box *) malloc(
			sizeof(struct _tuple_box));
	memset(newBox, 0, sizeof(struct _tuple_box));
	newBox->start = box->start + box->count;
	newBox->count = box->count;

	uint32_t i;
	struct _tuple_node *item;
	uint32_t limit = box->size / 2;
	for (i = 0; box->data != NULL && i < limit; ++i) {

		item = box->data;
		box->data = item->next;

		item->next = newBox->data;
		newBox->data = item;

		box->size -= 1;
		newBox->size += 1;
	}

	boxes[newBox->start] = newBox;

	tuple_box_median_cut(index, boxes);
}

void tuple_box_median_dump(struct _tuple_box *box) {
	if (box->size < 1) {
		return;
	}

	memset(&box->value, 0, sizeof(struct _tuple));

	struct _tuple_node *item;
	for (item = box->data; item != NULL; item = item->next) {
		box->value.r += item->value->r;
		box->value.g += item->value->g;
		box->value.b += item->value->b;
	}

	box->value.r = (box->value.r / box->size) & 0xFF;
	box->value.g = (box->value.g / box->size) & 0xFF;
	box->value.b = (box->value.b / box->size) & 0xFF;
}

void tuple_box_find_index(struct _tuple *tuple, struct _tuple_box **boxes,
		size_t size, int32_t **distMap) {

	int cache;
	int distance = INT_MAX;

	size_t i, index = 0;
	struct _tuple *item;
	for (i = 0; i < size; ++i) {
		if (boxes[i]->size < 1) {
			continue;
		}
		item = &(boxes[i]->value);
		cache = distMap[tuple->r][item->r] + distMap[tuple->g][item->g]
				+ distMap[tuple->b][item->b];
		if (cache < distance) {
			index = i;
			distance = cache;
		}
	}

	tuple->i = index;
}

int remap(uint8_t *buffer, uint8_t *indexes, size_t size, uint8_t *map,
		int32_t **dist) {
	struct _tuple_map matrix;
	matrix.buckets = NULL;
	matrix.capacity = 0;
	matrix.size = 0;

	tuple_map_reset(&matrix, 4096);
	size_t i;
	uint32_t key;
	int r, g, b;

	for (i = 0; i < size; i += 3) {
		r = buffer[i];
		g = buffer[i + 1];
		b = buffer[i + 2];
		key = b | (g << 8) | (r << 16);
		tuple_map_add_tuple(&matrix, key, tuple_create(key));
	}

	size_t length = sizeof(struct _tuple_box*) * COLOR_SIZE;
	struct _tuple_box **boxes = (struct _tuple_box**) malloc(length);
	memset(boxes, 0, length);
	{
		boxes[0] = (struct _tuple_box *) malloc(sizeof(struct _tuple_box));
		memset(boxes[0], 0, sizeof(struct _tuple_box));
		boxes[0]->count = COLOR_SIZE;
		tuple_map_copy_tuple(&matrix, boxes[0]);
		size_t j = 0;
		for (i = 0; i < COLOR_SIZE; ++i) {
			tuple_box_median_cut(i, boxes);
			tuple_box_median_dump(boxes[i]);
			map[j++] = boxes[i]->value.r;
			map[j++] = boxes[i]->value.g;
			map[j++] = boxes[i]->value.b;
		}
	}

	struct _tuple *tuple;
	{
		size_t index;
		size = size / 3;
		for (int i = 0; i < size; ++i) {
			index = i * 3;
			r = buffer[index];
			g = buffer[index + 1];
			b = buffer[index + 2];
			key = b | (g << 8) | (r << 16);
			tuple = tuple_map_get_tuple(&matrix, key);
			if (tuple->i == -1) {
				tuple_box_find_index(tuple, boxes, COLOR_SIZE, dist);
			}

			indexes[i] = tuple->i;
		}
	}

	tuple_box_free(boxes, COLOR_SIZE);
	tuple_map_free(&matrix);

	return 0;
}

