.PHONY : all

TARGET             := stm32


TOOLCHAIN          :=/opt/ARM_HOME/gcc-arm-none-eabi/bin

CC                 :=$(TOOLCHAIN)/arm-none-eabi-gcc
SIZE               :=$(TOOLCHAIN)/arm-none-eabi-size
OBJCOPY            :=$(TOOLCHAIN)/arm-none-eabi-objcopy


INCLUDES           := -I core/inc

ASM_SRC            :=$(shell find . -name '*\.s')
ASM_OBJ            :=$(ASM_SRC:%.s=%.o)

C_SRC              :=$(shell find . -name '*\.c')
C_OBJ              :=$(C_SRC:%.c=%.o)


C_FLAGS            := -W -Wall -Os
C_FLAGS            += -mthumb -mcpu=cortex-m3
C_FLAGS            += -ffunction-sections -fdata-sections
C_FLAGS            += -DSTM32F10X_MD -DUSE_STDPERIPH_DRIVER

LD_FLAGS           := --specs=nosys.specs
LD_FLAGS           += -Wl,--gc-sections
LD_FLAGS           += -T flash.ld


all : build

$(ASM_OBJ):%.o:%.s
	$(CC) $(C_FLAGS) -o $@ -c $<

$(C_OBJ):%.o:%.c
	$(CC) $(C_FLAGS) $(INCLUDES) -o $@ -c $<


build:$(ASM_OBJ) $(C_OBJ)
	$(CC) $(LD_FLAGS) $(ASM_OBJ) $(C_OBJ) -o $(TARGET).elf
	$(SIZE) $(TARGET).elf
	$(OBJCOPY) $(TARGET).elf $(TARGET).bin -Obinary
	$(OBJCOPY) $(TARGET).elf $(TARGET).hex -Oihex


clean:
	@for i in $(shell find . -name '*\.o'  );\
	    do if [ -e $${i} ];then rm $${i};fi;done
	@for i in $(shell find . -name '*\.elf');\
	    do if [ -e $${i} ];then rm $${i};fi;done
	@for i in $(shell find . -name '*\.bin');\
	    do if [ -e $${i} ];then rm $${i};fi;done
	@for i in $(shell find . -name '*\.hex');\
	    do if [ -e $${i} ];then rm $${i};fi;done
	



