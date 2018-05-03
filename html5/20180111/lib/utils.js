define(function(){
    if(!window.requestAnimationFrame){
        window.requestAnimationFrame=(
		    window.webkitRequestAnimationFrame ||
    		window.mozRequestAnimationFrame ||
	    	window.msRequestAnimationFrame ||
	    	window.oRequestAnimationFrame ||
	    	function (callback) {
	    		return window.setTimeout(callback, 16);
	    	});
    }

    if(!window.cancelAnimationFrame){
    	window.cancelAnimationFrame=(
    		window.cancelRequestAnimationFrame ||
    		window.webkitCancelAnimationFrame || 
            window.webkitCancelRequestAnimationFrame ||
    		window.mozCancelAnimationFrame || 
            window.mozCancelRequestAnimationFrame ||
    		window.msCancelAnimationFrame || 
            window.msCancelRequestAnimationFrame ||
    		window.oCancelAnimationFrame || 
            window.oCancelRequestAnimationFrame ||
    		window.clearTimeout);
    }

    window.utils={};

    window.utils.captureMouse=function(elem){
	    var mouse={x:0,y:0,e:null};
	    var bScrollLeft=document.body.scrollLeft;
    	var eScrollLeft=document.documentElement.scrollLeft;
	    var bScrollTop =document.body.scrollTop;
    	var eScrollTop =document.documentElement.scrollTop;
    	var offsetLeft =elem.offsetLeft;
    	var offsetTop  =elem.offsetTop;
    	elem.addEventListener('mousemove',function(e){
    		var x,y;
    		if(e.pageX||e.pageY){
    			x=e.pageX;
    			y=e.pageY;
    		}else{
    			x=e.clientX+bScrollLeft+eScrollLeft;
    			y=e.clientY+bScrollTop +eScrollTop;
    		}
    		x-=offsetLeft;
    		y-=offsetTop;
    		mouse.x=x;
    		mouse.y=y;
    		mouse.e=e;
    	},false);
    	return mouse;
    };

    window.utils.captureTouch=function(elem){
	    var touch={x:null,y:null,e:null,pressed:false};
	    var bScrollLeft=document.body.scrollLeft;
            var eScrollLeft=document.documentElement.scrollLeft;
            var bScrollTop =document.body.scrollTop;
            var eScrollTop =document.documentElement.scrollTop;
	    elem.addEventListener('touchstart',function(e){
	    	touch.pressed=true;
	    	touch.e=e;
	    },false);
	    elem.addEventListener('touchend',function(e){
	    	touch.pressed=false;
	    	touch.x=null;
	    	touch.y=null;
	    	touch.e=e;
	    },false);	
	    elem.addEventListener('touchmove',function(e){
	    	var x,y,t=e.touches[0];
	    	if(t.pageX||t.pageY){
	    		x=t.pageX;
	    		y=t.pageY;
	    	}else{
	    		x=t.clientX+bScrollLeft+eScrollLeft;
	    		y=t.clientY+bScrollTop +eScrollTop;
	    	}
	    	x-=offsetLeft;
	    	y-=offsetTop;
	    	touch.x=x;
	    	touch.y=y;
	    	touch.e=e;
	    },false);
	    return touch;
    };

    window.utils.parseColor=function(color,toNumber){
    	if(toNumber===true){
    		if(typeof color==='number'){
    			return color|0;
    		}
    		if(typeof color==='string'&&color[0]==='#'){
    			color=color.slice(1);
    		}
    		return window.parseInt(color,16);
    	}else{
    		if(typeof color==='number'){
    			color='#'+('000000'+(color|0).toString(16)).substr(-6);
    		}
    		return color;
    	}
    };

    window.utils.colorToRGB=function(color,alpha){
    	if(typeof color==='string'&&color[0]==='#'){
    		color=window.parseInt(color.slice(1),16);
    	}
    	alpha=(alpha===undefined)?1:alpha;
    	var r=color>>16&0xff;
    	var g=color>> 8&0xff;
    	var b=color&0xff;
    	var a=Math.min(1,Math.max(0,alpha));
    	if(a===1){
    		return "rgb("+r+","+g+","+b+")";
    	}else{
    		return "rgba("+r+","+g+","+b+","+a+")";
    	}
    };

    window.utils.containsPoint=function(rect,x,y){
    	return !(x<rect.x||x>rect.x+rect.w||
    		 y<rect.y||y>rect.y+rect.h);
    };

    window.utils.intersects=function(rectA,rectB){
    	return !(rectA.x+rectA.w<rectB.x||
    		 rectB.x+rectB.w<rectA.x||
    		 rectA.y+rectA.h<rectB.y||
    		 rectB.y+rectB.h<rectA.y);
    };
});
