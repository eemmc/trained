;--------------------------------------------------------
; File Created by SDCC : free open source ANSI-C Compiler
; Version 3.5.0 #9253 (Mar 24 2016) (Linux)
; This file was generated Fri Jan 26 15:35:43 2018
;--------------------------------------------------------
	.module demo01
	.optsdcc -mmcs51 --model-small
	
;--------------------------------------------------------
; Public variables in this module
;--------------------------------------------------------
	.globl _table
	.globl _main
	.globl _dealy
	.globl _clock
	.globl _clockinc
	.globl _IE
	.globl _TH0
	.globl _TL0
	.globl _TMOD
	.globl _TCON
	.globl _P3
	.globl _P2
	.globl _clock_timer
	.globl _clock_update
;--------------------------------------------------------
; special function registers
;--------------------------------------------------------
	.area RSEG    (ABS,DATA)
	.org 0x0000
_P2	=	0x00a0
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
_clock_timer::
	.ds 4
_main_time_1_10:
	.ds 4
;--------------------------------------------------------
; overlayable items in internal ram 
;--------------------------------------------------------
	.area	OSEG    (OVR,DATA)
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
;	demo01.c:34: void clockinc(void) __interrupt(1)
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
;	demo01.c:36: TIMER_RESET(65536-990);
	mov	_TH0,#0xFC
	mov	_TL0,#0x22
;	demo01.c:37: clock_timer++;
	mov	a,#0x01
	add	a,_clock_timer
	mov	_clock_timer,a
	clr	a
	addc	a,(_clock_timer + 1)
	mov	(_clock_timer + 1),a
	clr	a
	addc	a,(_clock_timer + 2)
	mov	(_clock_timer + 2),a
	clr	a
	addc	a,(_clock_timer + 3)
	mov	(_clock_timer + 3),a
;	demo01.c:38: clock_update=1;
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
;	demo01.c:41: unsigned long clock(void)
;	-----------------------------------------
;	 function clock
;	-----------------------------------------
_clock:
;	demo01.c:45: do
00101$:
;	demo01.c:47: clock_update=0;
	mov	_clock_update,#0x00
;	demo01.c:48: ctmp=clock_timer;
	mov	r4,_clock_timer
	mov	r5,(_clock_timer + 1)
	mov	r6,(_clock_timer + 2)
	mov	r7,(_clock_timer + 3)
;	demo01.c:49: }while(clock_update);
	mov	a,_clock_update
	jnz	00101$
;	demo01.c:51: return ctmp;
	mov	dpl,r4
	mov	dph,r5
	mov	b,r6
	mov	a,r7
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'dealy'
;------------------------------------------------------------
;us                        Allocated to registers 
;------------------------------------------------------------
;	demo01.c:54: void dealy(unsigned char us){
;	-----------------------------------------
;	 function dealy
;	-----------------------------------------
_dealy:
	mov	r7,dpl
;	demo01.c:55: while(us--);
00101$:
	mov	ar6,r7
	dec	r7
	mov	a,r6
	jnz	00101$
	ret
;------------------------------------------------------------
;Allocation info for local variables in function 'main'
;------------------------------------------------------------
;swap                      Allocated to registers r0 
;time                      Allocated with name '_main_time_1_10'
;------------------------------------------------------------
;	demo01.c:58: void main(void)
;	-----------------------------------------
;	 function main
;	-----------------------------------------
_main:
;	demo01.c:63: TIMER_RESET(65536-994);
	mov	_TH0,#0xFC
	mov	_TL0,#0x1E
;	demo01.c:64: TIMER_START();
	mov	_TMOD,#0x01
	orl	_IE,#0x82
	orl	_TCON,#0x10
;	demo01.c:66: while(1){
00102$:
;	demo01.c:67: time=clock()/1000;
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
	mov	_main_time_1_10,dpl
	mov	(_main_time_1_10 + 1),dph
	mov	(_main_time_1_10 + 2),b
	mov	(_main_time_1_10 + 3),a
;	demo01.c:69: swap=time%60;
	mov	__modulong_PARM_2,#0x3C
	clr	a
	mov	(__modulong_PARM_2 + 1),a
	mov	(__modulong_PARM_2 + 2),a
	mov	(__modulong_PARM_2 + 3),a
	mov	dpl,_main_time_1_10
	mov	dph,(_main_time_1_10 + 1)
	mov	b,(_main_time_1_10 + 2)
	mov	a,(_main_time_1_10 + 3)
	lcall	__modulong
	mov	r0,dpl
;	demo01.c:70: P3=0x80;
	mov	_P3,#0x80
;	demo01.c:71: P2=table[swap%10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	a,b
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:72: dealy(60);
	mov	dpl,#0x3C
	push	ar0
	lcall	_dealy
	pop	ar0
;	demo01.c:73: P3=0x40;
	mov	_P3,#0x40
;	demo01.c:74: P2=table[swap/10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:75: dealy(60);
	mov	dpl,#0x3C
	lcall	_dealy
;	demo01.c:77: time=time/60;
	mov	__divulong_PARM_2,#0x3C
	clr	a
	mov	(__divulong_PARM_2 + 1),a
	mov	(__divulong_PARM_2 + 2),a
	mov	(__divulong_PARM_2 + 3),a
	mov	dpl,_main_time_1_10
	mov	dph,(_main_time_1_10 + 1)
	mov	b,(_main_time_1_10 + 2)
	mov	a,(_main_time_1_10 + 3)
	lcall	__divulong
	mov	_main_time_1_10,dpl
	mov	(_main_time_1_10 + 1),dph
	mov	(_main_time_1_10 + 2),b
	mov	(_main_time_1_10 + 3),a
;	demo01.c:78: swap=time%60;
	mov	__modulong_PARM_2,#0x3C
	clr	a
	mov	(__modulong_PARM_2 + 1),a
	mov	(__modulong_PARM_2 + 2),a
	mov	(__modulong_PARM_2 + 3),a
	mov	dpl,_main_time_1_10
	mov	dph,(_main_time_1_10 + 1)
	mov	b,(_main_time_1_10 + 2)
	mov	a,(_main_time_1_10 + 3)
	lcall	__modulong
	mov	r1,dpl
	mov	ar0,r1
;	demo01.c:79: P3=0x10;
	mov	_P3,#0x10
;	demo01.c:80: P2=table[swap%10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	a,b
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:81: dealy(60);
	mov	dpl,#0x3C
	push	ar0
	lcall	_dealy
	pop	ar0
;	demo01.c:82: P3=0x08;
	mov	_P3,#0x08
;	demo01.c:83: P2=table[swap/10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:84: dealy(60);
	mov	dpl,#0x3C
	lcall	_dealy
;	demo01.c:86: time=time/60;
	mov	__divulong_PARM_2,#0x3C
	clr	a
	mov	(__divulong_PARM_2 + 1),a
	mov	(__divulong_PARM_2 + 2),a
	mov	(__divulong_PARM_2 + 3),a
	mov	dpl,_main_time_1_10
	mov	dph,(_main_time_1_10 + 1)
	mov	b,(_main_time_1_10 + 2)
	mov	a,(_main_time_1_10 + 3)
	lcall	__divulong
	mov	_main_time_1_10,dpl
	mov	(_main_time_1_10 + 1),dph
	mov	(_main_time_1_10 + 2),b
	mov	(_main_time_1_10 + 3),a
;	demo01.c:87: swap=time%60;
	mov	__modulong_PARM_2,#0x3C
	clr	a
	mov	(__modulong_PARM_2 + 1),a
	mov	(__modulong_PARM_2 + 2),a
	mov	(__modulong_PARM_2 + 3),a
	mov	dpl,_main_time_1_10
	mov	dph,(_main_time_1_10 + 1)
	mov	b,(_main_time_1_10 + 2)
	mov	a,(_main_time_1_10 + 3)
	lcall	__modulong
	mov	r4,dpl
	mov	ar0,r4
;	demo01.c:88: P3=0x02;
	mov	_P3,#0x02
;	demo01.c:89: P2=table[swap%10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	a,b
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:90: dealy(60);
	mov	dpl,#0x3C
	push	ar0
	lcall	_dealy
	pop	ar0
;	demo01.c:91: P3=0x01;
	mov	_P3,#0x01
;	demo01.c:92: P2=table[swap/10];
	mov	b,#0x0A
	mov	a,r0
	div	ab
	mov	dptr,#_table
	movc	a,@a+dptr
	mov	_P2,a
;	demo01.c:93: dealy(60);
	mov	dpl,#0x3C
	lcall	_dealy
	ljmp	00102$
	.area CSEG    (CODE)
	.area CONST   (CODE)
_table:
	.db #0xC0	; 192
	.db #0xF9	; 249
	.db #0xA4	; 164
	.db #0xB0	; 176
	.db #0x99	; 153
	.db #0x92	; 146
	.db #0x82	; 130
	.db #0xF8	; 248
	.db #0x80	; 128
	.db #0x90	; 144
	.db #0x88	; 136
	.db #0x83	; 131
	.db #0xC6	; 198
	.db #0xA1	; 161
	.db #0x86	; 134
	.db #0x8E	; 142
	.area XINIT   (CODE)
	.area CABS    (ABS,CODE)
