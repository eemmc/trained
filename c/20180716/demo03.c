#include <stdio.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <curl/curl.h>

static size_t read_callback(void *ptr,size_t size, size_t nmemb, void *stream)
{
	size_t retcode;
	curl_off_t nread;

	retcode = fread(ptr,size,nmemb,stream);
	nread=(curl_off_t)retcode;
	
	fprintf(stderr,"*** We read %" CURL_FORMAT_CURL_OFF_T
		" bytes from file\n", nread);

	return retcode;
}


int main(int argc, char **argv)
{
	CURL *curl;
	CURLcode res;
	FILE *hd_src;
	struct stat file_info;

	char *file;
	char *url;
	
	if(argc <3)
		return 1;

	file = argv[1];
	url  = argv[2];

	stat(file,&file_info);

	hd_src=fopen(file,"rb");

	curl_global_init(CURL_GLOBAL_ALL);
	
	curl=curl_easy_init();
	if(curl)
	{
		curl_easy_setopt(curl, CURLOPT_READFUNCTION, &read_callback);
		curl_easy_setopt(curl, CURLOPT_UPLOAD, 1L);
		curl_easy_setopt(curl, CURLOPT_PUT, 1L);
		curl_easy_setopt(curl, CURLOPT_URL, url);
		curl_easy_setopt(curl, CURLOPT_READDATA, hd_src);
		curl_easy_setopt(curl, CURLOPT_INFILESIZE_LRGE,(curl_off_t)file_info.st_size);

		res=curl_easy_perform(curl);
		if(res!=CURLE_OK)
			fprintf(stderr,"curl_easy_perform() failed: %s\n",
				curl_easy_strerror(res));

		curl_easy_cleanup(curl);
	}

	fclose(hd_src);
	curl_global_cleanup();

	return 0;
}
