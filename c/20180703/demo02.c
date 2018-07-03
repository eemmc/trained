#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

int main(int argc, char **argv)
{
	uint64_t value = 11683231921;
	fprintf(stderr,"%llu\n",value);

	uint64_t i;
	for(i=0;i<value;++i)
	{
		if(i%10000000==0){
			fprintf(stderr,"%llu\n",i);
		}
	}

	return 0;
}
