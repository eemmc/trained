#include "remap.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

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
	map->buckets = (struct _tuple_node**) malloc(
			sizeof(struct _tuple *) * capacity);
	memset(map->buckets, 0, capacity);

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

struct _tuple *tuple_map_get_tuple(struct _tuple_map *map, uint32_t key) {
	uint32_t hash = tuple_map_index(key, map->capacity);

	struct _tuple_node *item = map->buckets[hash];

	while (item != NULL) {
		if (item->key == key) {
			return item->value;
		}
		item = item->next;
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
			swap=(struct _tuple_node*)malloc(sizeof(struct _tuple_node));
			swap->value=item->value;
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


struct _tuple * tuple_create(uint32_t pixel) {
	struct _tuple *item = (struct _tuple*) malloc(sizeof(struct _tuple));

	item->r = (pixel >> 16) & 0xFF;
	item->g = (pixel >> 8) & 0xFF;
	item->b = pixel & 0xFF;
	item->i = -1;

	return item;
}

uint32_t tuple_to_rgb(struct _tuple *tuple) {

	return tuple->g | (tuple->g << 8) | (tuple->r << 16);
}


void tuple_box_median_sort_each(struct _tuple *tuple, struct _tuple_box *boxes) {
	if (tuple->r > boxes->limit[0]) {
		boxes->limit[0] = tuple->r;
	} else if (tuple->r < boxes->limit[1]) {
		boxes->limit[1] = tuple->r;
	} else {
		boxes->limit[0] = boxes->limit[1] = tuple->r;
	}

	if (tuple->g > boxes->limit[2]) {
		boxes->limit[2] = tuple->g;
	} else if (tuple->g < boxes->limit[3]) {
		boxes->limit[3] = tuple->g;
	} else {
		boxes->limit[2] = boxes->limit[3] = tuple->g;
	}

	if (tuple->b > boxes->limit[4]) {
		boxes->limit[4] = tuple->b;
	} else if (tuple->b < boxes->limit[5]) {
		boxes->limit[5] = tuple->b;
	} else {
		boxes->limit[4] = boxes->limit[5] = tuple->b;
	}

}

int tuple_box_median_sort_compare(struct _tuple *o1, struct _tuple *o2, int max) {
	switch (max) {
	case 0:
		return o1->r - o2->r;
	case 1:
		return o1->g - o2->g;
	case 2:
		return o1->b - o2->b;
	default:
		return 0;
	}
}

void tuple_box_median_sort(struct _tuple_box *boxes){
	memset(boxes->limit, 0, sizeof(uint32_t) * 6);

	struct _tuple_node *item = boxes->data;
	while (item != NULL) {
		tuple_box_median_sort_each(item->value, boxes);
		item = item->next;
	}

	int32_t max;

	int r = boxes->limit[0] - boxes->limit[1];
	int g = boxes->limit[2] - boxes->limit[3];
	int b = boxes->limit[4] - boxes->limit[5];

	if (r >= g && r >= b) {
		max = 0;
	} else if (g >= r && g >= b) {
		max = 1;
	} else if (b >= r && b >= g) {
		max = 2;
	} else {
		max = 0;
	}

	struct _tuple_node *prev;
	struct _tuple_node *curr;
	struct _tuple_node *temp;

	struct _tuple_node *swap = boxes->data;
	boxes->data = boxes->data->next;
	swap->next = NULL;

	int32_t diff;
	while (boxes->data != NULL) {
		temp = boxes->data;
		boxes->data = boxes->data->next;
		for (prev = NULL, curr = swap; curr != NULL; curr = curr->next) {
			diff = tuple_box_median_sort_compare(temp->value, curr->value, max);
			if (diff <= 0) {
				if (prev == NULL) {
					temp->next = swap;
					swap = temp;
				} else {
					prev->next = temp;
					temp->next = curr;
				}
				break;
			}
			prev = curr;
		}
	}

	boxes->data = swap;
}

void tuple_box_median_cut(uint32_t index, struct _tuple_box *boxes) {

	if (boxes[index].count <= 1) {
		return;
	}


	uint32_t start;
	fprintf(stderr, "----------------------->:>1\n");
	tuple_box_median_sort(boxes + index);
	fprintf(stderr, "----------------------->:>2\n");
	boxes[index].count = boxes[index].count / 2;

	start = boxes[index].start + boxes[index].count;
	boxes[start].start = start;
	boxes[start].count = boxes[index].count;

	uint32_t size = boxes[index].size / 2;


	struct _tuple_node *item = boxes[index].data;
	uint32_t i=0;
	while (item != NULL && i < size) {
		fprintf(stderr, "----------------------->:%lu:\n", i);
		boxes[index].data = item->next;

		item->next = boxes[start].data;
		boxes[start].data = item;

		boxes[index].size -= 1;
		boxes[start].size += 1;

		item = boxes[index].data;
		++i;
	}

	tuple_box_median_cut(index, boxes);
}

void tuple_box_median_dump(struct _tuple_box *boxes) {
	struct _tuple_node *item;
	for (item = boxes->data; item != NULL; item = item->next) {
		boxes->value.r += item->value->r;
		boxes->value.g += item->value->g;
		boxes->value.b += item->value->b;
	}

	boxes->value.r = (boxes->value.r / boxes->size) & 0xFF;
	boxes->value.g = (boxes->value.g / boxes->size) & 0xFF;
	boxes->value.b = (boxes->value.b / boxes->size) & 0xFF;
}

void tuple_box_find_index(struct _tuple *tuple, struct _tuple_box *boxes,
		size_t size, int32_t **distMap) {

	int cache;
	int distance = UINT_MAX;

	size_t i, index = 0;
	struct _tuple *item;
	for (i = 0; i < size; ++i) {
		item = &boxes[i].value;
		cache = distMap[tuple->r][item->r] + distMap[tuple->g][item->g]
				+ distMap[tuple->b][item->b];
		if (cache < distance) {
			index = i;
			distance = cache;
		}
	}

	tuple->i = index;
}

bool tuple_box_contains(struct _tuple *tuple, struct _tuple_box *boxes) {
	struct _tuple_node *item;
	for (item = boxes->data; item != NULL; item = item->next) {
		if (tuple->r == item->value->r && tuple->g == item->value->g
				&& tuple->b == item->value->b) {
			return true;
		}
	}

	return false;
}

int remap(int32_t *pixels, size_t size) {
	struct _tuple_map matrix;
	matrix.buckets = NULL;
	matrix.capacity = 0;
	matrix.size = 0;

	tuple_map_reset(&matrix, 4096);

	uint32_t i,j;
	int32_t key;
	for (int i = 0; i < size; ++i) {
		key = pixels[i];
		tuple_map_add_tuple(&matrix, key, tuple_create(key));
	}

	size_t length = sizeof(struct _tuple_box) * COLOR_SIZE;
	struct _tuple_box *boxes = (struct _tuple_box*) malloc(length);
	memset(boxes, 0, length);

	{
		boxes[0].start = 0;
		boxes[0].count = COLOR_SIZE;
		tuple_map_copy_tuple(&matrix, boxes);
		for (i = 0; i < COLOR_SIZE; ++i) {
			tuple_box_median_cut(i, boxes);
			//tuple_box_median_dump(boxes + i);
		}
	}

	{
		int32_t **distMap = (int32_t **) malloc(sizeof(int32_t *) * 256);
		for (i = 0; i < 256; ++i) {
			distMap[i] = (int32_t *) malloc(sizeof(int32_t) * 256);
			for (j = 0; j < 256; ++j) {
				distMap[i][j] = (i - j) * (i - j);
			}
		}

		struct _tuple *tuple;
		for (i = 0 ; i < size; ++i) {
			tuple = tuple_map_get_tuple(&matrix, pixels[i] & 0xFFFFFF);
			if(tuple_box_contains(tuple,boxes)){
				continue;
			}else if(tuple->i==-1){
				tuple_box_find_index(tuple,boxes,COLOR_SIZE,distMap);
			}

			pixels[i] = tuple_to_rgb(&boxes[tuple->i].value);
		}
	}

	return 0;
}

