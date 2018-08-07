#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <curl/curl.h>


struct memorystruct
{
	char *memory;
	size_t size;
};

static size_t
writeMemoryCallback(void *contents,size_t size,size_t nmemb,void *userp)
{
	size_t realsize = size *nmemb;
	struct memorystruct *mem=(struct memorystruct*) userp;

	mem->memory=realloc(mem->memory,mem->size+realsize+1);

	if(mem->memory == NULL)
	{
		printf("not enough memory (realloc returned NULL)\n");
		return 0;
	}
	

	memcpy(&(mem->memory[mem->size]),contents,realsize);
	mem->size+=realsize;
	mem->memory[mem->size]=0;

	return realsize;
	
}


int main(int argc, char **argv)
{
	CURL *handle;
	CURLcode res;

	struct memorystruct chunk;

	chunk.memory=malloc(1);
	chunk.size=0;

	curl_global_init(CURL_GLOBAL_ALL);
	handle=curl_easy_init();
	curl_easy_setopt(handle,CURLOPT_URL,"http://www.bilibili.com/");
	
	curl_easy_setopt(handle,CURLOPT_WRITEFUNCTION,&writeMemoryCallback);
	curl_easy_setopt(handle,CURLOPT_WRITEDATA,(void*)&chunk);
	curl_easy_setopt(handle,CURLOPT_USERAGENT,"libcurl-agent/1.0");
	
	res=curl_easy_perform(handle);

	if(res!=CURLE_OK)
	{
		fprintf(stderr,"curl_easy_perform() failed: %s\n",
			curl_easy_strerror(res));
	}
	else
	{
		printf("%lu bytes retrieved\n",(long)chunk.size);
	}	

	curl_easy_cleanup(handle);
	
	return 0;
}
