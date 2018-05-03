__sfr __at(0xa0) P2;
__sfr __at(0xb0) P3;

__sfr __at(0x88) TCON;
__sfr __at(0x89) TMOD;
__sfr __at(0x8a) TL0;
__sfr __at(0x8c) TH0;

__sfr __at(0xa8) IE;


#define TIMER_RESET(num) { \
        TH0 = (num)>>0x8;  \
        TL0 = (num)&0xFF;  \
}

#define TIMER_START() { \
        TMOD  = 0x01;   \
        IE   |= 0x82;   \
        TCON |= 0x10;   \
}


volatile char clock_update;
volatile unsigned long clock_timer;

__code unsigned char table[]={
        0xc0,0xf9,0xa4,0xb0,
        0x99,0x92,0x82,0xf8,
        0x80,0x90,0x88,0x83,
        0xc6,0xa1,0x86,0x8e,
};

void clockinc(void) __interrupt(1)
{
        TIMER_RESET(65536-1000);
        clock_timer++;
        clock_update=1;
}

unsigned long clock(void)
{
        unsigned long ctmp;
        
        do
        {
                clock_update=0;
                ctmp=clock_timer;
        }while(clock_update);

        return ctmp;
}

void dealy(unsigned int us)
{
       while(us--);
}

void main(void)
{
        unsigned char swap;
        unsigned long time;

        TIMER_RESET(65536-1000);
        TIMER_START();
        
        while(1){
                time=clock()/1000;

                swap=time%60;
                P3=0x80;
                P2=table[swap%10];
                dealy(60);
                P3=0x40;
                P2=table[swap/10];
                dealy(60);

                time=time/60;
                swap=time%60;
                P3=0x10;
                P2=table[swap%10];
                dealy(60);
                P3=0x08;
                P2=table[swap/10];
                dealy(60);

                time=time/60;
                swap=time%60;
                P3=0x02;
                P2=table[swap%10];
                dealy(60);
                P3=0x01;
                P2=table[swap/10];
                dealy(60);
        }

}
