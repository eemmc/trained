#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <curl/curl.h>


static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream)
{
	size_t written=fwrite(ptr,size,nmemb,(FILE *)stream);
	return written;
}

int main(int argc, char **argv)
{
	CURL *handle;
	static const char *pageFileName="page.out";
	FILE *pageFile;
	
	if(argc<2)
	{
		printf("Usage: %s <URL>\N",argv[0]);
		return 1;
	}

	curl_global_init(CURL_GLOBAL_ALL);
	curl_easy_setopt(handle,CURLOPT_URL,argv[1]);
	curl_easy_setopt(handle,CURLOPT_VERBOSE, 1L);
	curl_easy_setopt(handle,CURLOPT_NOPROGRESS, 1L);
	curl_easy_setopt(handle,CURLOPT_WRITEFUNCTION, &write_data);

	pageFile=fopen(pageFileName,"wb");
	if(pageFile)
	{
		curl_easy_setopt(handle,CURLOPT_WRITEDATA,pageFile);
		curl_easy_perform(handle);

		fclose(pageFile);
	}
	curl_easy_cleanup(handle);

	curl_global_cleanup();

	return 0;
}
