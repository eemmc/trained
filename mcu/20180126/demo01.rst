                                      1 ;--------------------------------------------------------
                                      2 ; File Created by SDCC : free open source ANSI-C Compiler
                                      3 ; Version 3.5.0 #9253 (Mar 24 2016) (Linux)
                                      4 ; This file was generated Fri Jan 26 15:35:43 2018
                                      5 ;--------------------------------------------------------
                                      6 	.module demo01
                                      7 	.optsdcc -mmcs51 --model-small
                                      8 	
                                      9 ;--------------------------------------------------------
                                     10 ; Public variables in this module
                                     11 ;--------------------------------------------------------
                                     12 	.globl _table
                                     13 	.globl _main
                                     14 	.globl _dealy
                                     15 	.globl _clock
                                     16 	.globl _clockinc
                                     17 	.globl _IE
                                     18 	.globl _TH0
                                     19 	.globl _TL0
                                     20 	.globl _TMOD
                                     21 	.globl _TCON
                                     22 	.globl _P3
                                     23 	.globl _P2
                                     24 	.globl _clock_timer
                                     25 	.globl _clock_update
                                     26 ;--------------------------------------------------------
                                     27 ; special function registers
                                     28 ;--------------------------------------------------------
                                     29 	.area RSEG    (ABS,DATA)
      000000                         30 	.org 0x0000
                           0000A0    31 _P2	=	0x00a0
                           0000B0    32 _P3	=	0x00b0
                           000088    33 _TCON	=	0x0088
                           000089    34 _TMOD	=	0x0089
                           00008A    35 _TL0	=	0x008a
                           00008C    36 _TH0	=	0x008c
                           0000A8    37 _IE	=	0x00a8
                                     38 ;--------------------------------------------------------
                                     39 ; special function bits
                                     40 ;--------------------------------------------------------
                                     41 	.area RSEG    (ABS,DATA)
      000000                         42 	.org 0x0000
                                     43 ;--------------------------------------------------------
                                     44 ; overlayable register banks
                                     45 ;--------------------------------------------------------
                                     46 	.area REG_BANK_0	(REL,OVR,DATA)
      000000                         47 	.ds 8
                                     48 ;--------------------------------------------------------
                                     49 ; internal ram data
                                     50 ;--------------------------------------------------------
                                     51 	.area DSEG    (DATA)
      000008                         52 _clock_update::
      000008                         53 	.ds 1
      000009                         54 _clock_timer::
      000009                         55 	.ds 4
      00000D                         56 _main_time_1_10:
      00000D                         57 	.ds 4
                                     58 ;--------------------------------------------------------
                                     59 ; overlayable items in internal ram 
                                     60 ;--------------------------------------------------------
                                     61 	.area	OSEG    (OVR,DATA)
                                     62 	.area	OSEG    (OVR,DATA)
                                     63 ;--------------------------------------------------------
                                     64 ; Stack segment in internal ram 
                                     65 ;--------------------------------------------------------
                                     66 	.area	SSEG
      000015                         67 __start__stack:
      000015                         68 	.ds	1
                                     69 
                                     70 ;--------------------------------------------------------
                                     71 ; indirectly addressable internal ram data
                                     72 ;--------------------------------------------------------
                                     73 	.area ISEG    (DATA)
                                     74 ;--------------------------------------------------------
                                     75 ; absolute internal ram data
                                     76 ;--------------------------------------------------------
                                     77 	.area IABS    (ABS,DATA)
                                     78 	.area IABS    (ABS,DATA)
                                     79 ;--------------------------------------------------------
                                     80 ; bit data
                                     81 ;--------------------------------------------------------
                                     82 	.area BSEG    (BIT)
                                     83 ;--------------------------------------------------------
                                     84 ; paged external ram data
                                     85 ;--------------------------------------------------------
                                     86 	.area PSEG    (PAG,XDATA)
                                     87 ;--------------------------------------------------------
                                     88 ; external ram data
                                     89 ;--------------------------------------------------------
                                     90 	.area XSEG    (XDATA)
                                     91 ;--------------------------------------------------------
                                     92 ; absolute external ram data
                                     93 ;--------------------------------------------------------
                                     94 	.area XABS    (ABS,XDATA)
                                     95 ;--------------------------------------------------------
                                     96 ; external initialized ram data
                                     97 ;--------------------------------------------------------
                                     98 	.area XISEG   (XDATA)
                                     99 	.area HOME    (CODE)
                                    100 	.area GSINIT0 (CODE)
                                    101 	.area GSINIT1 (CODE)
                                    102 	.area GSINIT2 (CODE)
                                    103 	.area GSINIT3 (CODE)
                                    104 	.area GSINIT4 (CODE)
                                    105 	.area GSINIT5 (CODE)
                                    106 	.area GSINIT  (CODE)
                                    107 	.area GSFINAL (CODE)
                                    108 	.area CSEG    (CODE)
                                    109 ;--------------------------------------------------------
                                    110 ; interrupt vector 
                                    111 ;--------------------------------------------------------
                                    112 	.area HOME    (CODE)
      000000                        113 __interrupt_vect:
      000000 02 00 11         [24]  114 	ljmp	__sdcc_gsinit_startup
      000003 32               [24]  115 	reti
      000004                        116 	.ds	7
      00000B 02 00 6D         [24]  117 	ljmp	_clockinc
                                    118 ;--------------------------------------------------------
                                    119 ; global & static initialisations
                                    120 ;--------------------------------------------------------
                                    121 	.area HOME    (CODE)
                                    122 	.area GSINIT  (CODE)
                                    123 	.area GSFINAL (CODE)
                                    124 	.area GSINIT  (CODE)
                                    125 	.globl __sdcc_gsinit_startup
                                    126 	.globl __sdcc_program_startup
                                    127 	.globl __start__stack
                                    128 	.globl __mcs51_genXINIT
                                    129 	.globl __mcs51_genXRAMCLEAR
                                    130 	.globl __mcs51_genRAMCLEAR
                                    131 	.area GSFINAL (CODE)
      00006A 02 00 0E         [24]  132 	ljmp	__sdcc_program_startup
                                    133 ;--------------------------------------------------------
                                    134 ; Home
                                    135 ;--------------------------------------------------------
                                    136 	.area HOME    (CODE)
                                    137 	.area HOME    (CODE)
      00000E                        138 __sdcc_program_startup:
      00000E 02 00 B4         [24]  139 	ljmp	_main
                                    140 ;	return from main will return to caller
                                    141 ;--------------------------------------------------------
                                    142 ; code
                                    143 ;--------------------------------------------------------
                                    144 	.area CSEG    (CODE)
                                    145 ;------------------------------------------------------------
                                    146 ;Allocation info for local variables in function 'clockinc'
                                    147 ;------------------------------------------------------------
                                    148 ;	demo01.c:34: void clockinc(void) __interrupt(1)
                                    149 ;	-----------------------------------------
                                    150 ;	 function clockinc
                                    151 ;	-----------------------------------------
      00006D                        152 _clockinc:
                           000007   153 	ar7 = 0x07
                           000006   154 	ar6 = 0x06
                           000005   155 	ar5 = 0x05
                           000004   156 	ar4 = 0x04
                           000003   157 	ar3 = 0x03
                           000002   158 	ar2 = 0x02
                           000001   159 	ar1 = 0x01
                           000000   160 	ar0 = 0x00
      00006D C0 E0            [24]  161 	push	acc
      00006F C0 D0            [24]  162 	push	psw
                                    163 ;	demo01.c:36: TIMER_RESET(65536-990);
      000071 75 8C FC         [24]  164 	mov	_TH0,#0xFC
      000074 75 8A 22         [24]  165 	mov	_TL0,#0x22
                                    166 ;	demo01.c:37: clock_timer++;
      000077 74 01            [12]  167 	mov	a,#0x01
      000079 25 09            [12]  168 	add	a,_clock_timer
      00007B F5 09            [12]  169 	mov	_clock_timer,a
      00007D E4               [12]  170 	clr	a
      00007E 35 0A            [12]  171 	addc	a,(_clock_timer + 1)
      000080 F5 0A            [12]  172 	mov	(_clock_timer + 1),a
      000082 E4               [12]  173 	clr	a
      000083 35 0B            [12]  174 	addc	a,(_clock_timer + 2)
      000085 F5 0B            [12]  175 	mov	(_clock_timer + 2),a
      000087 E4               [12]  176 	clr	a
      000088 35 0C            [12]  177 	addc	a,(_clock_timer + 3)
      00008A F5 0C            [12]  178 	mov	(_clock_timer + 3),a
                                    179 ;	demo01.c:38: clock_update=1;
      00008C 75 08 01         [24]  180 	mov	_clock_update,#0x01
      00008F D0 D0            [24]  181 	pop	psw
      000091 D0 E0            [24]  182 	pop	acc
      000093 32               [24]  183 	reti
                                    184 ;	eliminated unneeded mov psw,# (no regs used in bank)
                                    185 ;	eliminated unneeded push/pop dpl
                                    186 ;	eliminated unneeded push/pop dph
                                    187 ;	eliminated unneeded push/pop b
                                    188 ;------------------------------------------------------------
                                    189 ;Allocation info for local variables in function 'clock'
                                    190 ;------------------------------------------------------------
                                    191 ;ctmp                      Allocated to registers r4 r5 r6 r7 
                                    192 ;------------------------------------------------------------
                                    193 ;	demo01.c:41: unsigned long clock(void)
                                    194 ;	-----------------------------------------
                                    195 ;	 function clock
                                    196 ;	-----------------------------------------
      000094                        197 _clock:
                                    198 ;	demo01.c:45: do
      000094                        199 00101$:
                                    200 ;	demo01.c:47: clock_update=0;
      000094 75 08 00         [24]  201 	mov	_clock_update,#0x00
                                    202 ;	demo01.c:48: ctmp=clock_timer;
      000097 AC 09            [24]  203 	mov	r4,_clock_timer
      000099 AD 0A            [24]  204 	mov	r5,(_clock_timer + 1)
      00009B AE 0B            [24]  205 	mov	r6,(_clock_timer + 2)
      00009D AF 0C            [24]  206 	mov	r7,(_clock_timer + 3)
                                    207 ;	demo01.c:49: }while(clock_update);
      00009F E5 08            [12]  208 	mov	a,_clock_update
      0000A1 70 F1            [24]  209 	jnz	00101$
                                    210 ;	demo01.c:51: return ctmp;
      0000A3 8C 82            [24]  211 	mov	dpl,r4
      0000A5 8D 83            [24]  212 	mov	dph,r5
      0000A7 8E F0            [24]  213 	mov	b,r6
      0000A9 EF               [12]  214 	mov	a,r7
      0000AA 22               [24]  215 	ret
                                    216 ;------------------------------------------------------------
                                    217 ;Allocation info for local variables in function 'dealy'
                                    218 ;------------------------------------------------------------
                                    219 ;us                        Allocated to registers 
                                    220 ;------------------------------------------------------------
                                    221 ;	demo01.c:54: void dealy(unsigned char us){
                                    222 ;	-----------------------------------------
                                    223 ;	 function dealy
                                    224 ;	-----------------------------------------
      0000AB                        225 _dealy:
      0000AB AF 82            [24]  226 	mov	r7,dpl
                                    227 ;	demo01.c:55: while(us--);
      0000AD                        228 00101$:
      0000AD 8F 06            [24]  229 	mov	ar6,r7
      0000AF 1F               [12]  230 	dec	r7
      0000B0 EE               [12]  231 	mov	a,r6
      0000B1 70 FA            [24]  232 	jnz	00101$
      0000B3 22               [24]  233 	ret
                                    234 ;------------------------------------------------------------
                                    235 ;Allocation info for local variables in function 'main'
                                    236 ;------------------------------------------------------------
                                    237 ;swap                      Allocated to registers r0 
                                    238 ;time                      Allocated with name '_main_time_1_10'
                                    239 ;------------------------------------------------------------
                                    240 ;	demo01.c:58: void main(void)
                                    241 ;	-----------------------------------------
                                    242 ;	 function main
                                    243 ;	-----------------------------------------
      0000B4                        244 _main:
                                    245 ;	demo01.c:63: TIMER_RESET(65536-994);
      0000B4 75 8C FC         [24]  246 	mov	_TH0,#0xFC
      0000B7 75 8A 1E         [24]  247 	mov	_TL0,#0x1E
                                    248 ;	demo01.c:64: TIMER_START();
      0000BA 75 89 01         [24]  249 	mov	_TMOD,#0x01
      0000BD 43 A8 82         [24]  250 	orl	_IE,#0x82
      0000C0 43 88 10         [24]  251 	orl	_TCON,#0x10
                                    252 ;	demo01.c:66: while(1){
      0000C3                        253 00102$:
                                    254 ;	demo01.c:67: time=clock()/1000;
      0000C3 12 00 94         [24]  255 	lcall	_clock
      0000C6 AC 82            [24]  256 	mov	r4,dpl
      0000C8 AD 83            [24]  257 	mov	r5,dph
      0000CA AE F0            [24]  258 	mov	r6,b
      0000CC FF               [12]  259 	mov	r7,a
      0000CD 75 11 E8         [24]  260 	mov	__divulong_PARM_2,#0xE8
      0000D0 75 12 03         [24]  261 	mov	(__divulong_PARM_2 + 1),#0x03
      0000D3 E4               [12]  262 	clr	a
      0000D4 F5 13            [12]  263 	mov	(__divulong_PARM_2 + 2),a
      0000D6 F5 14            [12]  264 	mov	(__divulong_PARM_2 + 3),a
      0000D8 8C 82            [24]  265 	mov	dpl,r4
      0000DA 8D 83            [24]  266 	mov	dph,r5
      0000DC 8E F0            [24]  267 	mov	b,r6
      0000DE EF               [12]  268 	mov	a,r7
      0000DF 12 02 95         [24]  269 	lcall	__divulong
      0000E2 85 82 0D         [24]  270 	mov	_main_time_1_10,dpl
      0000E5 85 83 0E         [24]  271 	mov	(_main_time_1_10 + 1),dph
      0000E8 85 F0 0F         [24]  272 	mov	(_main_time_1_10 + 2),b
      0000EB F5 10            [12]  273 	mov	(_main_time_1_10 + 3),a
                                    274 ;	demo01.c:69: swap=time%60;
      0000ED 75 11 3C         [24]  275 	mov	__modulong_PARM_2,#0x3C
      0000F0 E4               [12]  276 	clr	a
      0000F1 F5 12            [12]  277 	mov	(__modulong_PARM_2 + 1),a
      0000F3 F5 13            [12]  278 	mov	(__modulong_PARM_2 + 2),a
      0000F5 F5 14            [12]  279 	mov	(__modulong_PARM_2 + 3),a
      0000F7 85 0D 82         [24]  280 	mov	dpl,_main_time_1_10
      0000FA 85 0E 83         [24]  281 	mov	dph,(_main_time_1_10 + 1)
      0000FD 85 0F F0         [24]  282 	mov	b,(_main_time_1_10 + 2)
      000100 E5 10            [12]  283 	mov	a,(_main_time_1_10 + 3)
      000102 12 02 12         [24]  284 	lcall	__modulong
      000105 A8 82            [24]  285 	mov	r0,dpl
                                    286 ;	demo01.c:70: P3=0x80;
      000107 75 B0 80         [24]  287 	mov	_P3,#0x80
                                    288 ;	demo01.c:71: P2=table[swap%10];
      00010A 75 F0 0A         [24]  289 	mov	b,#0x0A
      00010D E8               [12]  290 	mov	a,r0
      00010E 84               [48]  291 	div	ab
      00010F E5 F0            [12]  292 	mov	a,b
      000111 90 02 FE         [24]  293 	mov	dptr,#_table
      000114 93               [24]  294 	movc	a,@a+dptr
      000115 F5 A0            [12]  295 	mov	_P2,a
                                    296 ;	demo01.c:72: dealy(60);
      000117 75 82 3C         [24]  297 	mov	dpl,#0x3C
      00011A C0 00            [24]  298 	push	ar0
      00011C 12 00 AB         [24]  299 	lcall	_dealy
      00011F D0 00            [24]  300 	pop	ar0
                                    301 ;	demo01.c:73: P3=0x40;
      000121 75 B0 40         [24]  302 	mov	_P3,#0x40
                                    303 ;	demo01.c:74: P2=table[swap/10];
      000124 75 F0 0A         [24]  304 	mov	b,#0x0A
      000127 E8               [12]  305 	mov	a,r0
      000128 84               [48]  306 	div	ab
      000129 90 02 FE         [24]  307 	mov	dptr,#_table
      00012C 93               [24]  308 	movc	a,@a+dptr
      00012D F5 A0            [12]  309 	mov	_P2,a
                                    310 ;	demo01.c:75: dealy(60);
      00012F 75 82 3C         [24]  311 	mov	dpl,#0x3C
      000132 12 00 AB         [24]  312 	lcall	_dealy
                                    313 ;	demo01.c:77: time=time/60;
      000135 75 11 3C         [24]  314 	mov	__divulong_PARM_2,#0x3C
      000138 E4               [12]  315 	clr	a
      000139 F5 12            [12]  316 	mov	(__divulong_PARM_2 + 1),a
      00013B F5 13            [12]  317 	mov	(__divulong_PARM_2 + 2),a
      00013D F5 14            [12]  318 	mov	(__divulong_PARM_2 + 3),a
      00013F 85 0D 82         [24]  319 	mov	dpl,_main_time_1_10
      000142 85 0E 83         [24]  320 	mov	dph,(_main_time_1_10 + 1)
      000145 85 0F F0         [24]  321 	mov	b,(_main_time_1_10 + 2)
      000148 E5 10            [12]  322 	mov	a,(_main_time_1_10 + 3)
      00014A 12 02 95         [24]  323 	lcall	__divulong
      00014D 85 82 0D         [24]  324 	mov	_main_time_1_10,dpl
      000150 85 83 0E         [24]  325 	mov	(_main_time_1_10 + 1),dph
      000153 85 F0 0F         [24]  326 	mov	(_main_time_1_10 + 2),b
      000156 F5 10            [12]  327 	mov	(_main_time_1_10 + 3),a
                                    328 ;	demo01.c:78: swap=time%60;
      000158 75 11 3C         [24]  329 	mov	__modulong_PARM_2,#0x3C
      00015B E4               [12]  330 	clr	a
      00015C F5 12            [12]  331 	mov	(__modulong_PARM_2 + 1),a
      00015E F5 13            [12]  332 	mov	(__modulong_PARM_2 + 2),a
      000160 F5 14            [12]  333 	mov	(__modulong_PARM_2 + 3),a
      000162 85 0D 82         [24]  334 	mov	dpl,_main_time_1_10
      000165 85 0E 83         [24]  335 	mov	dph,(_main_time_1_10 + 1)
      000168 85 0F F0         [24]  336 	mov	b,(_main_time_1_10 + 2)
      00016B E5 10            [12]  337 	mov	a,(_main_time_1_10 + 3)
      00016D 12 02 12         [24]  338 	lcall	__modulong
      000170 A9 82            [24]  339 	mov	r1,dpl
      000172 89 00            [24]  340 	mov	ar0,r1
                                    341 ;	demo01.c:79: P3=0x10;
      000174 75 B0 10         [24]  342 	mov	_P3,#0x10
                                    343 ;	demo01.c:80: P2=table[swap%10];
      000177 75 F0 0A         [24]  344 	mov	b,#0x0A
      00017A E8               [12]  345 	mov	a,r0
      00017B 84               [48]  346 	div	ab
      00017C E5 F0            [12]  347 	mov	a,b
      00017E 90 02 FE         [24]  348 	mov	dptr,#_table
      000181 93               [24]  349 	movc	a,@a+dptr
      000182 F5 A0            [12]  350 	mov	_P2,a
                                    351 ;	demo01.c:81: dealy(60);
      000184 75 82 3C         [24]  352 	mov	dpl,#0x3C
      000187 C0 00            [24]  353 	push	ar0
      000189 12 00 AB         [24]  354 	lcall	_dealy
      00018C D0 00            [24]  355 	pop	ar0
                                    356 ;	demo01.c:82: P3=0x08;
      00018E 75 B0 08         [24]  357 	mov	_P3,#0x08
                                    358 ;	demo01.c:83: P2=table[swap/10];
      000191 75 F0 0A         [24]  359 	mov	b,#0x0A
      000194 E8               [12]  360 	mov	a,r0
      000195 84               [48]  361 	div	ab
      000196 90 02 FE         [24]  362 	mov	dptr,#_table
      000199 93               [24]  363 	movc	a,@a+dptr
      00019A F5 A0            [12]  364 	mov	_P2,a
                                    365 ;	demo01.c:84: dealy(60);
      00019C 75 82 3C         [24]  366 	mov	dpl,#0x3C
      00019F 12 00 AB         [24]  367 	lcall	_dealy
                                    368 ;	demo01.c:86: time=time/60;
      0001A2 75 11 3C         [24]  369 	mov	__divulong_PARM_2,#0x3C
      0001A5 E4               [12]  370 	clr	a
      0001A6 F5 12            [12]  371 	mov	(__divulong_PARM_2 + 1),a
      0001A8 F5 13            [12]  372 	mov	(__divulong_PARM_2 + 2),a
      0001AA F5 14            [12]  373 	mov	(__divulong_PARM_2 + 3),a
      0001AC 85 0D 82         [24]  374 	mov	dpl,_main_time_1_10
      0001AF 85 0E 83         [24]  375 	mov	dph,(_main_time_1_10 + 1)
      0001B2 85 0F F0         [24]  376 	mov	b,(_main_time_1_10 + 2)
      0001B5 E5 10            [12]  377 	mov	a,(_main_time_1_10 + 3)
      0001B7 12 02 95         [24]  378 	lcall	__divulong
      0001BA 85 82 0D         [24]  379 	mov	_main_time_1_10,dpl
      0001BD 85 83 0E         [24]  380 	mov	(_main_time_1_10 + 1),dph
      0001C0 85 F0 0F         [24]  381 	mov	(_main_time_1_10 + 2),b
      0001C3 F5 10            [12]  382 	mov	(_main_time_1_10 + 3),a
                                    383 ;	demo01.c:87: swap=time%60;
      0001C5 75 11 3C         [24]  384 	mov	__modulong_PARM_2,#0x3C
      0001C8 E4               [12]  385 	clr	a
      0001C9 F5 12            [12]  386 	mov	(__modulong_PARM_2 + 1),a
      0001CB F5 13            [12]  387 	mov	(__modulong_PARM_2 + 2),a
      0001CD F5 14            [12]  388 	mov	(__modulong_PARM_2 + 3),a
      0001CF 85 0D 82         [24]  389 	mov	dpl,_main_time_1_10
      0001D2 85 0E 83         [24]  390 	mov	dph,(_main_time_1_10 + 1)
      0001D5 85 0F F0         [24]  391 	mov	b,(_main_time_1_10 + 2)
      0001D8 E5 10            [12]  392 	mov	a,(_main_time_1_10 + 3)
      0001DA 12 02 12         [24]  393 	lcall	__modulong
      0001DD AC 82            [24]  394 	mov	r4,dpl
      0001DF 8C 00            [24]  395 	mov	ar0,r4
                                    396 ;	demo01.c:88: P3=0x02;
      0001E1 75 B0 02         [24]  397 	mov	_P3,#0x02
                                    398 ;	demo01.c:89: P2=table[swap%10];
      0001E4 75 F0 0A         [24]  399 	mov	b,#0x0A
      0001E7 E8               [12]  400 	mov	a,r0
      0001E8 84               [48]  401 	div	ab
      0001E9 E5 F0            [12]  402 	mov	a,b
      0001EB 90 02 FE         [24]  403 	mov	dptr,#_table
      0001EE 93               [24]  404 	movc	a,@a+dptr
      0001EF F5 A0            [12]  405 	mov	_P2,a
                                    406 ;	demo01.c:90: dealy(60);
      0001F1 75 82 3C         [24]  407 	mov	dpl,#0x3C
      0001F4 C0 00            [24]  408 	push	ar0
      0001F6 12 00 AB         [24]  409 	lcall	_dealy
      0001F9 D0 00            [24]  410 	pop	ar0
                                    411 ;	demo01.c:91: P3=0x01;
      0001FB 75 B0 01         [24]  412 	mov	_P3,#0x01
                                    413 ;	demo01.c:92: P2=table[swap/10];
      0001FE 75 F0 0A         [24]  414 	mov	b,#0x0A
      000201 E8               [12]  415 	mov	a,r0
      000202 84               [48]  416 	div	ab
      000203 90 02 FE         [24]  417 	mov	dptr,#_table
      000206 93               [24]  418 	movc	a,@a+dptr
      000207 F5 A0            [12]  419 	mov	_P2,a
                                    420 ;	demo01.c:93: dealy(60);
      000209 75 82 3C         [24]  421 	mov	dpl,#0x3C
      00020C 12 00 AB         [24]  422 	lcall	_dealy
      00020F 02 00 C3         [24]  423 	ljmp	00102$
                                    424 	.area CSEG    (CODE)
                                    425 	.area CONST   (CODE)
      0002FE                        426 _table:
      0002FE C0                     427 	.db #0xC0	; 192
      0002FF F9                     428 	.db #0xF9	; 249
      000300 A4                     429 	.db #0xA4	; 164
      000301 B0                     430 	.db #0xB0	; 176
      000302 99                     431 	.db #0x99	; 153
      000303 92                     432 	.db #0x92	; 146
      000304 82                     433 	.db #0x82	; 130
      000305 F8                     434 	.db #0xF8	; 248
      000306 80                     435 	.db #0x80	; 128
      000307 90                     436 	.db #0x90	; 144
      000308 88                     437 	.db #0x88	; 136
      000309 83                     438 	.db #0x83	; 131
      00030A C6                     439 	.db #0xC6	; 198
      00030B A1                     440 	.db #0xA1	; 161
      00030C 86                     441 	.db #0x86	; 134
      00030D 8E                     442 	.db #0x8E	; 142
                                    443 	.area XINIT   (CODE)
                                    444 	.area CABS    (ABS,CODE)
