__sfr __at(0xb0) P3;

__sfr __at(0x88) TCON;
__sfr __at(0x89) TMOD;
__sfr __at(0x8a) TL0;
__sfr __at(0x8c) TH0;

__sfr __at(0xa8) IE;


#define RESETTIMER(num) {  \
        TH0 = (num)/0xFF; \
        TL0 = (num)%0xFF; \
}

#define STARTTIMER() { \
        TMOD  = 0x01; \
        IE   |= 0x82; \
        TCON |= 0x10; \
}


volatile char clock_update;
volatile unsigned long clock_time;


void clockinc(void) __interrupt(1)
{
        RESETTIMER(65536-922);
        clock_time++;
        clock_update=1;
}

unsigned long clock(void)
{
        unsigned long ctmp;
        
        do
        {
                clock_update=0;
                ctmp=clock_time;
        }while(clock_update);

        return ctmp;
}

void main(void)
{

        RESETTIMER(65536-922);
        STARTTIMER();
        
        while(1){
                P3=~(clock()/1000)&0xFF;
        }

}
