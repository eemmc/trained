__sfr __at(0xa0) P2;
__sfr __at(0xb0) P3;

__sfr __at(0x88) TCON;
__sfr __at(0x89) TMOD;
__sfr __at(0x8a) TL0;
__sfr __at(0x8c) TH0;

__sfr __at(0xa8) IE;


#define TIMER_RESET(num) { \
    TH0 = (num) >> 0x08;   \
    TL0 = (num)  & 0xFF;   \
}

#define TIMER_START(num) { \
    TIMER_RESET(num);      \
    TMOD  = 0x01;          \
    IE   |= 0x82;          \
    TCON |= 0x10;          \
}

#define CLOCK_TICK (65536-1000)

volatile char clock_modif;
volatile unsigned long clock_timer;


void clockinc(void) __interrupt(1)
{
    TIMER_RESET(CLOCK_TICK);
    clock_timer++;
    clock_modif=1;
}

unsigned long clock(void)
{
    unsigned long ctmp;
    do
    {
        clock_modif=0;
        ctmp=clock_timer;
    }while(clock_modif);

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

    TIMER_START(CLOCK_TICK);

    while(1)
    {

    }
}




