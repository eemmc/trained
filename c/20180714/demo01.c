#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <curl/multi.h>

static const char *urls[]={
	"http://www.163.com",
	"http://www.bilibili.com",
	"http://www.duowan.com",
	"http://new.163.com",
	"http://www.qq.com",
	"http://www.mysql.com",
	"http://www.microsoft.com",
	"http://www.opensource.org",
	"http://www.msn.org",
};

#define MAX 5
#define CNT sizeof(urls)/sizeof(char *)

static size_t cb(char *d,size_t n,size_t l,void *p)
{
	(void*)d;
	(void*)p;

	return n*l;
}

static void init(CURLM *cm,int i)
{
	CURL *eh = curl_easy_init();
	
	curl_easy_setopt(eh, CURLOPT_WRITEFUNCTION, &cb);
	curl_easy_setopt(eh, CURLOPT_HEADER, 0L);
	curl_easy_setopt(eh, CURLOPT_URL, urls[i]);
	curl_easy_setopt(eh, CURLOPT_PRIVATE, urls[i]);
	curl_easy_setopt(eh, CURLOPT_VERBOSE, 0L);
	
	curl_multi_add_handle(cm, eh);
}

int main(int argc,char *argv[])
{
	CURLM *cm;
	CURLMsg *msg;

	long L;
	unsigned C = 0;
	int M, Q, U = -1;
	fd_set R, W, E;
	struct timeval T;

	curl_global_init(CURL_GLOBAL_ALL);
	
	cm=curl_multi_init();

	curl_multi_setopt(cm, CURLMOPT_MAXCONNECTS, (long)MAX);

	for(C = 0, C < MAX; ++C)
	{
		init(cm,C);
	}
	
	while(U)
	{
		curl_multi_perform(cm,&U);
		if(U)
		{
			FD_ZERO(&R);
			FD_ZERO(&W);
			FD_ZERO(&E);

			if(curl_multi_fdset(cm, &R, &W, &E, &M))
			{
				fprintf(stderr,"E: curl_multi_fdset\n");
				return EXIT_FAILURE;
			}

			if(curl_multi_timeout(cm, &L))
			{
				fprintf(stderr,"E: curl_multi_timeout\n");
				return EXIT_FAILURE;
			}
		
			if(L == -1)
			{
				L=100;
			}

			if(M == -1)
			{
				sleep((unsigned int)L /1000);
			}
			else
			{
				T.tv_sec= L / 1000;
				T.tv_usec = (L%1000)*1000;
				
				if(0 > select(M+1, &R, &W, &E, &T))
				{
					fprintf(stderr, "E: select (%i,,,%li): %i: %s\n",
						M+1, L, errno, strerror(errno));
					return EXIT_FAILURE;
				}
			}
		}

		while((msg = curl_multi_info_read(cm,&Q)))
		{
			if(msg->msg == CURLMSG_DONE)
			{
				char *url;
				CURL *e = msg->easy_handle;
				curl_easy_getinfo(msg->easy_handle,CURLINFO_PRIVATE, &url);
				fprintf(stderr, "R: %d - %s <%s>\n",
					msg->data.result, curl_easy_strerror(msg->data.result), url);
				curl_multi_remove_handle(cm,e);
				curl_easy_cleanup(e);
			}
			else
			{
				fprintf(stderr, "E: CURLMsg (%d) \n", msg->msg);
			}

			if(C < CNT)
			{
				init(cm,C++);
				U++;
			}
		}
	}

	curl_multi_cleanup();
	curl_global_cleanup();

	return 0;
}
