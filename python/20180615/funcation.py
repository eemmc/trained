#!/usr/bin/env python3
# -*- coding:utf-8 -*-


import sys
import math

cc = 8.0
ec = math.exp(cc)

def func(x,mx):
	
	gx = (x/mx)*cc
	bx = 1.0/(ec-1)
	tx = math.log10(ec-2)
	
	y0 = 1.0/(1-bx)
	y1 = math.exp(-(gx-tx))
	y2 = 1.0/(1+y1)-bx

	return y0*y2


if __name__ == '__main__':
	list = sys.argv[1:]
	data = []
	for s in list:
		data.append(float(s))
	maxx = max(data)
	for x in data:
		y = func(x,maxx)
		print(" %f => %f" % (x,y))
		
