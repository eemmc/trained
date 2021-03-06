#include <stm32f10x_conf.h>

#define CLOCK 72/8

#pragma GCC push_options
#pragma GCC option("O0")

void delay_us(uint32_t us)
{
	uint32_t n;		    
	while(us--)for(n=0;n<CLOCK;n++); 	 
}

void delay_ms(uint32_t ms)
{
	while(ms--)delay_us(1000);	 
}

#pragma GCC pop_options

int main(void) {

	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB, ENABLE);

	GPIO_InitTypeDef GPIO_InitStructure;
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_8;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;
	GPIO_Init(GPIOB, &GPIO_InitStructure);
	
	while (1) {
		GPIO_SetBits(GPIOB, GPIO_Pin_8);
		delay_ms(500);
		GPIO_ResetBits(GPIOB, GPIO_Pin_8);
		delay_ms(600);
	}

	return 0;
}

#ifdef USE_FULL_ASSERT

void assert_failed(uint8_t file,unit32_t line) {
	while(1);
}

#endif
