                                      1 ;--------------------------------------------------------
                                      2 ; File Created by SDCC : free open source ANSI-C Compiler
                                      3 ; Version 3.5.0 #9253 (Mar 24 2016) (Linux)
                                      4 ; This file was generated Thu Jan 25 18:43:52 2018
                                      5 ;--------------------------------------------------------
                                      6 	.module demo01
                                      7 	.optsdcc -mmcs51 --model-small
                                      8 	
                                      9 ;--------------------------------------------------------
                                     10 ; Public variables in this module
                                     11 ;--------------------------------------------------------
                                     12 	.globl _main
                                     13 	.globl _clock
                                     14 	.globl _clockinc
                                     15 	.globl _IE
                                     16 	.globl _TH0
                                     17 	.globl _TL0
                                     18 	.globl _TMOD
                                     19 	.globl _TCON
                                     20 	.globl _P3
                                     21 	.globl _clock_time
                                     22 	.globl _clock_update
                                     23 ;--------------------------------------------------------
                                     24 ; special function registers
                                     25 ;--------------------------------------------------------
                                     26 	.area RSEG    (ABS,DATA)
      000000                         27 	.org 0x0000
                           0000B0    28 _P3	=	0x00b0
                           000088    29 _TCON	=	0x0088
                           000089    30 _TMOD	=	0x0089
                           00008A    31 _TL0	=	0x008a
                           00008C    32 _TH0	=	0x008c
                           0000A8    33 _IE	=	0x00a8
                                     34 ;--------------------------------------------------------
                                     35 ; special function bits
                                     36 ;--------------------------------------------------------
                                     37 	.area RSEG    (ABS,DATA)
      000000                         38 	.org 0x0000
                                     39 ;--------------------------------------------------------
                                     40 ; overlayable register banks
                                     41 ;--------------------------------------------------------
                                     42 	.area REG_BANK_0	(REL,OVR,DATA)
      000000                         43 	.ds 8
                                     44 ;--------------------------------------------------------
                                     45 ; internal ram data
                                     46 ;--------------------------------------------------------
                                     47 	.area DSEG    (DATA)
      000008                         48 _clock_update::
      000008                         49 	.ds 1
      000009                         50 _clock_time::
      000009                         51 	.ds 4
                                     52 ;--------------------------------------------------------
                                     53 ; overlayable items in internal ram 
                                     54 ;--------------------------------------------------------
                                     55 	.area	OSEG    (OVR,DATA)
                                     56 ;--------------------------------------------------------
                                     57 ; Stack segment in internal ram 
                                     58 ;--------------------------------------------------------
                                     59 	.area	SSEG
      000011                         60 __start__stack:
      000011                         61 	.ds	1
                                     62 
                                     63 ;--------------------------------------------------------
                                     64 ; indirectly addressable internal ram data
                                     65 ;--------------------------------------------------------
                                     66 	.area ISEG    (DATA)
                                     67 ;--------------------------------------------------------
                                     68 ; absolute internal ram data
                                     69 ;--------------------------------------------------------
                                     70 	.area IABS    (ABS,DATA)
                                     71 	.area IABS    (ABS,DATA)
                                     72 ;--------------------------------------------------------
                                     73 ; bit data
                                     74 ;--------------------------------------------------------
                                     75 	.area BSEG    (BIT)
                                     76 ;--------------------------------------------------------
                                     77 ; paged external ram data
                                     78 ;--------------------------------------------------------
                                     79 	.area PSEG    (PAG,XDATA)
                                     80 ;--------------------------------------------------------
                                     81 ; external ram data
                                     82 ;--------------------------------------------------------
                                     83 	.area XSEG    (XDATA)
                                     84 ;--------------------------------------------------------
                                     85 ; absolute external ram data
                                     86 ;--------------------------------------------------------
                                     87 	.area XABS    (ABS,XDATA)
                                     88 ;--------------------------------------------------------
                                     89 ; external initialized ram data
                                     90 ;--------------------------------------------------------
                                     91 	.area XISEG   (XDATA)
                                     92 	.area HOME    (CODE)
                                     93 	.area GSINIT0 (CODE)
                                     94 	.area GSINIT1 (CODE)
                                     95 	.area GSINIT2 (CODE)
                                     96 	.area GSINIT3 (CODE)
                                     97 	.area GSINIT4 (CODE)
                                     98 	.area GSINIT5 (CODE)
                                     99 	.area GSINIT  (CODE)
                                    100 	.area GSFINAL (CODE)
                                    101 	.area CSEG    (CODE)
                                    102 ;--------------------------------------------------------
                                    103 ; interrupt vector 
                                    104 ;--------------------------------------------------------
                                    105 	.area HOME    (CODE)
      000000                        106 __interrupt_vect:
      000000 02 00 11         [24]  107 	ljmp	__sdcc_gsinit_startup
      000003 32               [24]  108 	reti
      000004                        109 	.ds	7
      00000B 02 00 6D         [24]  110 	ljmp	_clockinc
                                    111 ;--------------------------------------------------------
                                    112 ; global & static initialisations
                                    113 ;--------------------------------------------------------
                                    114 	.area HOME    (CODE)
                                    115 	.area GSINIT  (CODE)
                                    116 	.area GSFINAL (CODE)
                                    117 	.area GSINIT  (CODE)
                                    118 	.globl __sdcc_gsinit_startup
                                    119 	.globl __sdcc_program_startup
                                    120 	.globl __start__stack
                                    121 	.globl __mcs51_genXINIT
                                    122 	.globl __mcs51_genXRAMCLEAR
                                    123 	.globl __mcs51_genRAMCLEAR
                                    124 	.area GSFINAL (CODE)
      00006A 02 00 0E         [24]  125 	ljmp	__sdcc_program_startup
                                    126 ;--------------------------------------------------------
                                    127 ; Home
                                    128 ;--------------------------------------------------------
                                    129 	.area HOME    (CODE)
                                    130 	.area HOME    (CODE)
      00000E                        131 __sdcc_program_startup:
      00000E 02 00 AB         [24]  132 	ljmp	_main
                                    133 ;	return from main will return to caller
                                    134 ;--------------------------------------------------------
                                    135 ; code
                                    136 ;--------------------------------------------------------
                                    137 	.area CSEG    (CODE)
                                    138 ;------------------------------------------------------------
                                    139 ;Allocation info for local variables in function 'clockinc'
                                    140 ;------------------------------------------------------------
                                    141 ;	demo01.c:27: void clockinc(void) __interrupt(1)
                                    142 ;	-----------------------------------------
                                    143 ;	 function clockinc
                                    144 ;	-----------------------------------------
      00006D                        145 _clockinc:
                           000007   146 	ar7 = 0x07
                           000006   147 	ar6 = 0x06
                           000005   148 	ar5 = 0x05
                           000004   149 	ar4 = 0x04
                           000003   150 	ar3 = 0x03
                           000002   151 	ar2 = 0x02
                           000001   152 	ar1 = 0x01
                           000000   153 	ar0 = 0x00
      00006D C0 E0            [24]  154 	push	acc
      00006F C0 D0            [24]  155 	push	psw
                                    156 ;	demo01.c:29: RESETTIMER(65536-922);
      000071 75 8C FD         [24]  157 	mov	_TH0,#0xFD
      000074 75 8A 63         [24]  158 	mov	_TL0,#0x63
                                    159 ;	demo01.c:30: clock_time++;
      000077 74 01            [12]  160 	mov	a,#0x01
      000079 25 09            [12]  161 	add	a,_clock_time
      00007B F5 09            [12]  162 	mov	_clock_time,a
      00007D E4               [12]  163 	clr	a
      00007E 35 0A            [12]  164 	addc	a,(_clock_time + 1)
      000080 F5 0A            [12]  165 	mov	(_clock_time + 1),a
      000082 E4               [12]  166 	clr	a
      000083 35 0B            [12]  167 	addc	a,(_clock_time + 2)
      000085 F5 0B            [12]  168 	mov	(_clock_time + 2),a
      000087 E4               [12]  169 	clr	a
      000088 35 0C            [12]  170 	addc	a,(_clock_time + 3)
      00008A F5 0C            [12]  171 	mov	(_clock_time + 3),a
                                    172 ;	demo01.c:31: clock_update=1;
      00008C 75 08 01         [24]  173 	mov	_clock_update,#0x01
      00008F D0 D0            [24]  174 	pop	psw
      000091 D0 E0            [24]  175 	pop	acc
      000093 32               [24]  176 	reti
                                    177 ;	eliminated unneeded mov psw,# (no regs used in bank)
                                    178 ;	eliminated unneeded push/pop dpl
                                    179 ;	eliminated unneeded push/pop dph
                                    180 ;	eliminated unneeded push/pop b
                                    181 ;------------------------------------------------------------
                                    182 ;Allocation info for local variables in function 'clock'
                                    183 ;------------------------------------------------------------
                                    184 ;ctmp                      Allocated to registers r4 r5 r6 r7 
                                    185 ;------------------------------------------------------------
                                    186 ;	demo01.c:34: unsigned long clock(void)
                                    187 ;	-----------------------------------------
                                    188 ;	 function clock
                                    189 ;	-----------------------------------------
      000094                        190 _clock:
                                    191 ;	demo01.c:38: do
      000094                        192 00101$:
                                    193 ;	demo01.c:40: clock_update=0;
      000094 75 08 00         [24]  194 	mov	_clock_update,#0x00
                                    195 ;	demo01.c:41: ctmp=clock_time;
      000097 AC 09            [24]  196 	mov	r4,_clock_time
      000099 AD 0A            [24]  197 	mov	r5,(_clock_time + 1)
      00009B AE 0B            [24]  198 	mov	r6,(_clock_time + 2)
      00009D AF 0C            [24]  199 	mov	r7,(_clock_time + 3)
                                    200 ;	demo01.c:42: }while(clock_update);
      00009F E5 08            [12]  201 	mov	a,_clock_update
      0000A1 70 F1            [24]  202 	jnz	00101$
                                    203 ;	demo01.c:44: return ctmp;
      0000A3 8C 82            [24]  204 	mov	dpl,r4
      0000A5 8D 83            [24]  205 	mov	dph,r5
      0000A7 8E F0            [24]  206 	mov	b,r6
      0000A9 EF               [12]  207 	mov	a,r7
      0000AA 22               [24]  208 	ret
                                    209 ;------------------------------------------------------------
                                    210 ;Allocation info for local variables in function 'main'
                                    211 ;------------------------------------------------------------
                                    212 ;	demo01.c:47: void main(void)
                                    213 ;	-----------------------------------------
                                    214 ;	 function main
                                    215 ;	-----------------------------------------
      0000AB                        216 _main:
                                    217 ;	demo01.c:50: RESETTIMER(65536-922);
      0000AB 75 8C FD         [24]  218 	mov	_TH0,#0xFD
      0000AE 75 8A 63         [24]  219 	mov	_TL0,#0x63
                                    220 ;	demo01.c:51: STARTTIMER();
      0000B1 75 89 01         [24]  221 	mov	_TMOD,#0x01
      0000B4 43 A8 82         [24]  222 	orl	_IE,#0x82
      0000B7 43 88 10         [24]  223 	orl	_TCON,#0x10
                                    224 ;	demo01.c:53: while(1){
      0000BA                        225 00102$:
                                    226 ;	demo01.c:54: P3=~(clock()/1000)&0xFF;
      0000BA 12 00 94         [24]  227 	lcall	_clock
      0000BD AC 82            [24]  228 	mov	r4,dpl
      0000BF AD 83            [24]  229 	mov	r5,dph
      0000C1 AE F0            [24]  230 	mov	r6,b
      0000C3 FF               [12]  231 	mov	r7,a
      0000C4 75 0D E8         [24]  232 	mov	__divulong_PARM_2,#0xE8
      0000C7 75 0E 03         [24]  233 	mov	(__divulong_PARM_2 + 1),#0x03
      0000CA E4               [12]  234 	clr	a
      0000CB F5 0F            [12]  235 	mov	(__divulong_PARM_2 + 2),a
      0000CD F5 10            [12]  236 	mov	(__divulong_PARM_2 + 3),a
      0000CF 8C 82            [24]  237 	mov	dpl,r4
      0000D1 8D 83            [24]  238 	mov	dph,r5
      0000D3 8E F0            [24]  239 	mov	b,r6
      0000D5 EF               [12]  240 	mov	a,r7
      0000D6 12 00 E6         [24]  241 	lcall	__divulong
      0000D9 AC 82            [24]  242 	mov	r4,dpl
      0000DB AD 83            [24]  243 	mov	r5,dph
      0000DD AE F0            [24]  244 	mov	r6,b
      0000DF FF               [12]  245 	mov	r7,a
      0000E0 EC               [12]  246 	mov	a,r4
      0000E1 F4               [12]  247 	cpl	a
      0000E2 F5 B0            [12]  248 	mov	_P3,a
      0000E4 80 D4            [24]  249 	sjmp	00102$
                                    250 	.area CSEG    (CODE)
                                    251 	.area CONST   (CODE)
                                    252 	.area XINIT   (CODE)
                                    253 	.area CABS    (ABS,CODE)
