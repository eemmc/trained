define(['utils'],function(){
    
    return function(radius,color){
        if(radius===undefined){radius=40;}
    	if(color ===undefined){color="#F00";}
    	this.x=0;
    	this.y=0;
    	this.r=radius;
    	this.vx=0;
    	this.vy=0;
    	this.mass=1;
    	this.rotation=0;
    	this.scaleX=1;
    	this.scaleY=1;
    	this.color=utils.parseColor(color);
        this.lineWidth=1;

        this.draw=function(context){
            context.save();
        	context.translate(this.x,this.y);
        	context.rotate(this.rotation);
        	context.scale(this.scaleX,this.scaleY);
        	context.lineWidth=this.lineWidth;
        	context.fillStyle=this.color;
        	context.beginPath();
        	context.arc(0,0,this.r,0,Math.PI*2,true);
        	context.closePath();
        	context.fill();
        	if(this.lineWidth>0){
        		context.stroke();
        	}
            context.restore();
        };

        this.bounds=function(){
            return {
		        x:this.x-this.r,
		        y:this.y-this.r,
	    	    w:this.r*2,
        		h:this.r*2,
            };
        };
    };
});
