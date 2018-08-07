#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <curl/curl.h>

#define URL_BASE "http://speedtest.your.domin/"
#define URL_1M URL_BASE "file_1M.bin"
#define URL_2M URL_BASE "file_2M.bin"
#define URL_3M URL_BASE "file_5M.bin"
#define URL_10M URL_BASE "file_10M.bin"
#define URL_20M URL_BASE "file_20M.bin"
#define URL_50M URL_BASE "file_50M.bin"
#define URL_100M URL_BASE "file_100M.bin"

#define CHKSPEED_VERSION "1.0"

static size_t write_callback(void *ptr,size_t size,size_t memb,void *data)
{
	(void)ptr;
	(void)data;

	return (size_t)(size *nmemb);
}
