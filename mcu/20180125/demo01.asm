;--------------------------------------------------------
; File Created by SDCC : free open source ANSI-C Compiler
; Version 3.5.0 #9253 (Mar 24 2016) (Linux)
; This file was generated Thu Jan 25 18:43:52 2018
;--------------------------------------------------------
	.module demo01
	.optsdcc -mmcs51 --model-small
	
;--------------------------------------------------------
; Public variables in this module
;--------------------------------------------------------
	.globl _main
	.globl _clock
	.globl _clockinc
	.globl _IE
	.globl _TH0
	.globl _TL0
	.globl _TMOD
	.globl _TCON
	.globl _P3
	.globl _clock_time
	.globl _clock_update
;--------------------------------------------------------
; special function registers
;--------------------------------------------------------
	.area RSEG    (ABS,DATA)
	.org 0x0000
_P3	=	0x00b0
_TCON	=	0x0088
_TMOD	=	0x0089
_TL0	=	0x008a
_TH0	=	0x008c
_IE	=	0x00a8
;--------------------------------------------------------
; special function bits
;--------------------------------------------------------
	.area RSEG    (ABS,DATA)
	.org 0x0000
;--------------------------------------------------------
; overlayable register banks
;--------------------------------------------------------
	.area REG_BANK_0	(REL,OVR,DATA)
	.ds 8
;--------------------------------------------------------
; internal ram data
;--------------------------------------------------------
	.area DSEG    (DATA)
_clock_update::
	.ds 1
_clock_time::
	.ds 4
;--------------------------------------------------------
; overlayable items in internal ram 
;--------------------------------------------------------
	.area	OSEG    (OVR,DATA)
;--------------------------------------------------------
; Stack segment in internal ram 
;--------------------------------------------------------
	.area	SSEG
__start__stack:
	.ds	1

;--------------------------------------------------------
; indirectly addressable internal ram data
;--------------------------------------------------------
	.area ISEG    (DATA)
;--------------------------------------------------------
; absolute internal ram data
;--------------------------------------------------------
	.area IABS    (ABS,DATA)
	.area IABS    (ABS,DATA)
;--------------------------------------------------------
; bit data
;--------------------------------------------------------
	.area BSEG    (BIT)
;--------------------------------------------------------
; paged external ram data
;--------------------------------------------------------
	.area PSEG    (PAG,XDATA)
;--------------------------------------------------------
; external ram data
;--------------------------------------------------------
	.area XSEG    (XDATA)
;--------------------------------------------------------
; absolute external ram data
;--------------------------------------------------------
	.area XABS    (ABS,XDATA)
;--------------------------------------------------------
; external initialized ram data
;--------------------------------------------------------
	.area XISEG   (XDATA)
	.area HOME    (CODE)
	.area GSINIT0 (CODE)
	.area GSINIT1 (CODE)
	.area GSINIT2 (CODE)
	.area GSINIT3 (CODE)
	.area GSINIT4 (CODE)
	.area GSINIT5 (CODE)
	.area GSINIT  (CODE)
	.area GSFINAL (CODE)
	.area CSEG    (CODE)
;--------------------------------------------------------
; interrupt vector 
;--------------------------------------------------------
	.area HOME    (CODE)
__interrupt_vect:
	ljmp	__sdcc_gsinit_startup
	reti
	.ds	7
	ljmp	_clockinc
;--------------------------------------------------------
; global & static initialisations
;--------------------------------------------------------
	.area HOME    (CODE)
	.area GSINIT  (CODE)
	.area GSFINAL (CODE)
	.area GSINIT  (CODE)
	.globl __sdcc_gsinit_startup
	.globl __sdcc_program_startup
	.globl __start__stack
	.globl __mcs51_genXINIT
	.globl __mcs51_genXRAMCLEAR
	.globl __mcs51_genRAMCLEAR
	.area GSFINAL (CODE)
	ljmp	__sdcc_program_startup
;--------------------------------------------------------
; Home
;--------------------------------------------------------
	.area HOME    (CODE)
	.area HOME    (CODE)
__sdcc_program_startup:
	ljmp	_main
;	return from main will return to caller
;--------------------------------------------------------
; code
;--------------------------------------------------------
	.area CSEG    (CODE)
;------------------------------------------------------------
;Allocation info for local variables in function 'clockinc'
;------------------------------------------------------------
;	demo01.c:27: void clockinc(void) __interrupt(1)
;	-----------------------------------------
;	 function clockinc
;	-----------------------------------------
_clockinc:
	ar7 = 0x07
	ar6 = 0x06
	ar5 = 0x05
	ar4 = 0x04
	ar3 = 0x03
	ar2 = 0x02
	ar1 = 0x01
	ar0 = 0x00
	push	acc
	push	psw
;	demo01.c:29: RESETTIMER(65536-922);
	mov	_TH0,#0xFD
	mov	_TL0,#0x63
;	demo01.c:30: clock_time++;
	mov	a,#0x01
	add	a,_clock_time
	mov	_clock_time,a
	clr	a
	addc	a,(_clock_time + 1)
	mov	(_clock_time + 1),a
	clr	a
	addc	a,(_clock_time + 2)
	mov	(_clock_time + 2),a
	clr	a
	addc	a,(_clock_time + 3)
	mov	(_clock_time + 3),a
;	demo01.c:31: clock_update=1;
	mov	_clock_update,#0x01
	pop	psw
	pop	acc
	reti
;	eliminated unneeded mov psw,# (no regs used in bank)
;	eliminated unneeded push/pop dpl
;	eliminated unneeded push/pop dph
;	eliminated unneeded push/pop b
;------------------------------------------------------------
;Allocation info for local variables in function 'clock'
;------------------------------------------------------------
;ctmp                      Allocated to registers r4 r5 r6 r7 
;------------------------------------------------------------
;	demo01.c:34: unsigned long clock(void)
;	-----------------------------------------
;	 function clock
;	-----------------------------------------
_clock:
;	demo01.c:38: do
00101$:
;	demo01.c:40: clock_update=0;
	mov	_clock_update,#0x00
;	demo01.c:41: ctmp=clock_time;
	mov	r4,_clock_time
	mov	r5,(_clock_time + 1)
	mov	r6,(_clock_time + 2)
	mov	r7,(_clock_time + 3)
;	demo01.c:42: }while(clock_update);
	mov	a,_clock_update
	jnz	00101$
;	demo01.c:44: return ctmp;
	mov	dpl,r4
	mov	dph,r5
	mov	b,r6
	mov	a,r7
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'main'
;------------------------------------------------------------
;	demo01.c:47: void main(void)
;	-----------------------------------------
;	 function main
;	-----------------------------------------
_main:
;	demo01.c:50: RESETTIMER(65536-922);
	mov	_TH0,#0xFD
	mov	_TL0,#0x63
;	demo01.c:51: STARTTIMER();
	mov	_TMOD,#0x01
	orl	_IE,#0x82
	orl	_TCON,#0x10
;	demo01.c:53: while(1){
00102$:
;	demo01.c:54: P3=~(clock()/1000)&0xFF;
	lcall	_clock
	mov	r4,dpl
	mov	r5,dph
	mov	r6,b
	mov	r7,a
	mov	__divulong_PARM_2,#0xE8
	mov	(__divulong_PARM_2 + 1),#0x03
	clr	a
	mov	(__divulong_PARM_2 + 2),a
	mov	(__divulong_PARM_2 + 3),a
	mov	dpl,r4
	mov	dph,r5
	mov	b,r6
	mov	a,r7
	lcall	__divulong
	mov	r4,dpl
	mov	r5,dph
	mov	r6,b
	mov	r7,a
	mov	a,r4
	cpl	a
	mov	_P3,a
	sjmp	00102$
	.area CSEG    (CODE)
	.area CONST   (CODE)
	.area XINIT   (CODE)
	.area CABS    (ABS,CODE)
