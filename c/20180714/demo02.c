#include <stdio.h>
#include <fcntl.h>

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

#include <curl/curl.h>

static curlioerr my_ioctrl(CURL *handle, curliocmd cmd, void *userp)
{
	int *fdp = (int *)userp;
	int fd = *fdp;

	(void) handle;

	switch(cmd)
	{
	case CURLIOCMD_RESTARTREAD:
		if(-1 == lseek(fd, 0, SEEK_SET))
			return CURLIO_FAILRESTART;
		break;
	default:
		return CURLIOE_UNKOWNCMD;
	}

	return CURLIOE_OK;
}

static size_t read_callback(void *ptr, size_t size, size_t nmemb, void *stream)
{
	ssize_t retcode;
	curl_off_t nread;
	
	int *fdp=(int *)stream;
	int fd = *fdp;

	retcode = read(fd, ptr, size* nemmb);
	nread = (curl_off_t)retcode;
	
	fprintf(stder, "*** We read %" CURL_FORMAT_CURL_OFF_T
			" bytes from file\n",nread);

	return retcode;
}

int main(int argc, char **argv)
{
	CURL *curl;
	CURLcode res;
	int hd;
	struct stat file_info;
	
	char *file;
	char *url;
	
	if(argc < 3)
		return 1;

	file = argv[1];
	url = argv[2];

	hd = open(file, ORDONLY);
	fstat(hd, &file_info);

	curl_global_init(CURL_GLOBAL_ALL);
	
	curl = curl_easy_init();

	if(curl)
	{
		curl_easy_setopt(curl, CURLOPT_READFUNCTION, &read_callback);
		curl_easy_setopt(curl, CURLOPT_READDATA, (void)&hd);
		curl_easy_setopt(curl, CURLOPT_IOCTLFUNCTION, &my_ioctl);
		curl_easy_setopt(curl, CURLOPT_IOCTLDATA, (void*)&hd);
		curl_easy_setopt(curl, CURLOPT_UPLOAD, 1L);
		curl_easy_setopt(curl, CURLOPT_URL, url);
		curl_easy_setopt(curl, CURLOPT_INFILESIZE_LARGE, (curl_off_t)file_info.st_size);
		curl_easy_setopt(curl, CURLOPT_HTTPAUTH, (long)CURLAUTH_ANY);
		curl_easy_setopt(curl, CURLOPT_USERPWD, "user:password");

		res = curl_easy_perform(curl);
		if(res != CURLE_OK)
		{
			fprintf(stderr,"curl_easy_perform() failed: %s\n", 
				curl_easy_sterror(res));
		}
			
		curl_easy_cleanup(curl);
	}
	close(hd);

	curl_global_cleanup();
	
	return 0;
}
