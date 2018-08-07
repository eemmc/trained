#include <stdio.h>
#include <curl/curl.h>

int main(int argc, char **argv)
{
	CURL *curl;
	CURLcode res;

	curl_global_init(CURL_GLOBAL_ALL);
	
	curl=curl_easy_init();
	if(curl)
	{
		curl_easy_setopt(curl, CURLOPT_URL, "http://www.baidu.com/");
		curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "name=daniel&project=curl");

		res=curl_easy_perform(curl);
		if(res!=CURLE_OK)
		{
			fprintf(stderr,"curl_easy_perform() failed: %s\n",
				curl_easy_strerror(res));
		}
		
		curl_easy_cleanup(curl);
	}

	curl_global_cleanup();

	return 0;
}
