#include <stdio.h>
#include <curl/curl.h>


int main(int argc, char **argv)
{
	
	CURL *curl;
	CURLcode res;

	curl=curl_easy_init();
	if(curl)
	{
		curl_easy_setopt(curl,CURLOPT_URL,"http://www.bilibili.com");
		
		res=curl_easy_perform(curl);

		if(res!=CURLE_OK)
		{
			fprintf(stderr,"curr_easy_perform() failed: %s \n",
				curl_easy_strerror(res));
		}

		curl_easy_cleanup(curl);
	}

	return 0;

}
