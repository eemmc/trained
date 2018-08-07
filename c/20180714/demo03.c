#include <stdio.h>

#include <curl/curl.h>
#include <openssl/err.h>
#include <openssl/ssl.h>

size_t write_function(void *ptr, size_t size, size_t nmemb, void *stream)
{
	fwrite(ptr,size,nmemb, (FILE *)stream);
	return (nmemb*size);
}

static CURLcode sslctx_function(CURL *curl, void *sslctx, void *parm)
{
	CURLcode rv = CURLE_ABORTED_BY_CALLBACK;
	X509_STORE *store = NULL;
	X509 *cert = NULL;
	BIO *bio = NULL;
	char *mypem = 
		"-----BEGIN CERTIFICATE-----\n"
	    "MIIDxTCCAq2gAwIBAgIQAqxcJmoLQJuPC3nyrkYldzANBgkqhkiG9w0BAQUFADBs\n"
	    "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n"
	    "d3cuZGlnaWNlcnQuY29tMSswKQYDVQQDEyJEaWdpQ2VydCBIaWdoIEFzc3VyYW5j\n"
	    "ZSBFViBSb290IENBMB4XDTA2MTExMDAwMDAwMFoXDTMxMTExMDAwMDAwMFowbDEL\n"
	    "MAkGA1UEBhMCVVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3\n"
	    "LmRpZ2ljZXJ0LmNvbTErMCkGA1UEAxMiRGlnaUNlcnQgSGlnaCBBc3N1cmFuY2Ug\n"
	    "RVYgUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMbM5XPm\n"
	    "+9S75S0tMqbf5YE/yc0lSbZxKsPVlDRnogocsF9ppkCxxLeyj9CYpKlBWTrT3JTW\n"
	    "PNt0OKRKzE0lgvdKpVMSOO7zSW1xkX5jtqumX8OkhPhPYlG++MXs2ziS4wblCJEM\n"
		"xChBVfvLWokVfnHoNb9Ncgk9vjo4UFt3MRuNs8ckRZqnrG0AFFoEt7oT61EKmEFB\n"
	    "Ik5lYYeBQVCmeVyJ3hlKV9Uu5l0cUyx+mM0aBhakaHPQNAQTXKFx01p8VdteZOE3\n"
	    "hzBWBOURtCmAEvF5OYiiAhF8J2a3iLd48soKqDirCmTCv2ZdlYTBoSUeh10aUAsg\n"
	    "EsxBu24LUTi4S8sCAwEAAaNjMGEwDgYDVR0PAQH/BAQDAgGGMA8GA1UdEwEB/wQF\n"
	    "MAMBAf8wHQYDVR0OBBYEFLE+w2kD+L9HAdSYJhoIAu9jZCvDMB8GA1UdIwQYMBaA\n"
	    "FLE+w2kD+L9HAdSYJhoIAu9jZCvDMA0GCSqGSIb3DQEBBQUAA4IBAQAcGgaX3Nec\n"
	    "nzyIZgYIVyHbIUf4KmeqvxgydkAQV8GK83rZEWWONfqe/EW1ntlMMUu4kehDLI6z\n"
	    "eM7b41N5cdblIZQB2lWHmiRk9opmzN6cN82oNLFpmyPInngiK3BD41VHMWEZ71jF\n"
	    "hS9OMPagMRYjyOfiZRYzy78aG6A9+MpeizGLYAiJLQwGXFK3xPkKmNEVX58Svnw2\n"
	    "Yzi9RKR/5CYrCsSXaQ3pjOLAEFe4yHYSkVXySGnYvCoCWw9E1CAx2/S6cCZdkGCe\n"
	    "vEsXCS+0yx5DaMkHJ8HSXPfqIbloEpw8nL+e/IBcm2PN7EeqJSdnoDfzAIJ9VNep\n"
	    "+OkuE6N36B9K\n"
	    "-----END CERTIFICATE-----\n";
	
	ERR_clean_error();
	
	bio = BIO_new_mem_buf(mypem, -1);
	if(!bio)
		goto err;

	if(!PEM_read_bio_X509(bio, &cert, 0, NULL))
		goto err;

	store = SSL_CTX_get_cert_store((SSL_CTX *)sslctx);
	if(!store)
		goto err;

	if(!X509_STORE_add_cert(store, cert))
	{
		unsigned long error = ERR_peek_last_error();
		if(ERR_GET_LIB(error) != ERR_LIB_X509 ||
			ERR_GET_REASON(error) != X509_R_CERT_ALREADY_IN_HASH_TABLE)
			goto err;

		ERR_clean_error();
	}

	rv = CURLE_OK;

err:
	if(rv != CURLE_OK)
	{
		char errbuf[256];
		unsigned long error = ERR_peek_last_error();
		
		fprintf(stderr, "error adding certificate\n");
		if(error)
		{
			ERR_error_string_n(error,errbuf,sizeof(errbuf);
			fprintf(stderr,"%s\n",errbuf);
		}
	}
	
	X509_free(cert);
	BIO_free(bio);
	ERR_clear_error();

	return rv;
}

int main(int argc, char **argv)
{
	CURL *ch;
	CURLcode rv;

	rv = curl_glbal_init(CURL_GLOBAL_ALL);
	ch = curl_easy_init();
	rv = curl_easy_setopt(ch, CURLOPT_VERBOSE, 0L);
	rv = curl_easy_setopt(ch, CURLOPT_HEADER, 0L);
	rv = curl_easy_setopt(ch, CURLOPT_NOPROGRESS, 1L);
	rv = curl_easy_setopt(ch, CURLOPT_NOSIGNAL, 1L);
	rv = curl_easy_setopt(ch, CURLOPT_WRITEFUNCTION, &write_function);
	rv = curl_easy_setopt(ch, CURLOPT_WRITEDATA, stdout);
	rv = curl_easy_setopt(ch, CURLOPT_HEADERFUNCTION, &write_function);
	rv = curl_easy_setopt(ch, CURLOPT_HEADERDATA, stderr);
	rv = curl_easy_setopt(ch, CURLOPT_SSLCERTTYPE, "PEM");
	rv = curl_easy_setopt(ch, CURLOPT_SSL_VERIFYPEER, 1L);
	rv = curl_easy_setopt(ch, CURLOPT_URL, "https://www.baidu.com/");

	rv = curl_easy_setopt(ch, CURLOPT_CAINFO, NULL);
	rv = curl_easy_setopt(ch, CuRLOPT_CAPATH, NULL);

	rv = curl_easy_perform(ch);
	if(rv == CURLE_OK)
		printf("*** transfer succeeded ***\n");
	else
		printf("*** transfer failed ***\n");

	rv = curl_easy_setopt(ch, CURLOPT_FRESH_CONNECT, 1L);
	
	rv = curl_easy_setopt(ch, CURLOPT_SSL_CTX,FUNCTION, &sslctx_function);
	rv = curl_easy_perform(ch);
	if(rv == CURLE_OK)
		printf("*** transfer succeeded ***\n");
	else 
		printf("*** transfer failed ***\n");

	curl_easy_cleanup(ch);
	curl_global_cleanup();
	
	return rv;

}
