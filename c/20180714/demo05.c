#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

#include <curl/curl.h>

static void
print_cookies(CURL *curl)
{
	CURLcode res;
	struct curl_slist *cookies;
	struct curl_slist *nc;
	int i;

	printf("Cookies, curl knows:\n");
	res = curl_easy_getinfo(curl, CURLINFO_COOKIELIST, &cookies);
	if(res != CURLE_OK)
	{
		fprintf(stderr,"Curl curl_easy_getinfo failed: %s\n",
			curl_easy_strerror(res));
		exit(1);
	}

	i=1;
	nc = cookies;
	while(nc)
	{
		printf("[%d]: %s\n", i, nc->data);
		nc = nc->next;
		i++;
	}
	
	if(i==1)
	{
		printf("(none)\n");
	}
	curl_slist_free_all(cookies);
}


int main(int argc,char **argv)
{
	CURL *curl;
	CURLcode res;

	curl_global_init(CURL_GLOBAL_ALL);
	curl = curl_easy_init();
	if(curl)
	{
		char nline[256];

		curl_easy_setopt(curl, CURLOPT_URL, "http://www.bilibili.com/");
		curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);
		curl_easy_setopt(curl, CURLOPT_COOKIEFILE, "");
		res = curl_easy_perform(curl);
		if(res != CURLE_OK)
		{
			fprintf(stderr, "Curl perform failed: %s\n", curl_easy_strerror(res));
			return 1;
		}

		print_cookies(curl);
		print_easy_setopt(curl, CURLOPT_COOKIELIST, "ALL");

		print_cookies(curl);

		printf("------------------------------------\n"
			   "Setting a cookies \"PREF\" via cookie interface:\n");
	}
}
