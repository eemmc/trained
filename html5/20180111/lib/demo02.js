define(function(require){

	var canvas=document.createElement("canvas");
	document.body.appendChild(canvas);
	var context=canvas.getContext("2d");

	window.addEventListener("resize",(function resize(){
 	       canvas.width=document.documentElement.clientWidth;
	       canvas.height=document.documentElement.clientHeight;
	       return resize;
	})(),false);

	function Particle(){
        this.x=Math.random()*canvas.width;
        this.y=Math.random()*canvas.height;
		this.vx=Math.random()*20-10;
		this.vy=Math.random()*20-10;
		var r=Math.random()*255>>0;
		var g=Math.random()*255>>0;
		var b=Math.random()*255>>0;
		this.color="rgba("+r+","+g+","+b+",0.5)";
		
		this.radius=Math.random()*20+20;
	}

	var particles=[];
	for(var i=0;i<50;i++){
		particles.push(new Particle())
	}

    (function draw(){
        window.requestAnimationFrame(draw,canvas);

        context.globalCompositeOperation="source-over";
		context.fillStyle="rgba(0,0,0,0.3)";
		context.fillRect(0,0,canvas.width,canvas.height);
        context.globalCompositeOperation="lighter";

        for(var t=0;t<particles.length;t++){
            var p=particles[t];

            context.beginPath();

            var gradient=context.createRadialGradient(
					p.x, p.y, 0, p.x, p.y, p.radius);

            gradient.addColorStop(0  ,"#fff" );
			gradient.addColorStop(0.4,"#fff" );
		    gradient.addColorStop(0.4,p.color);
			gradient.addColorStop(1  ,"#000" );

            context.fillStyle=gradient;
			context.arc(p.x,p.y,p.radius,Math.PI*2,false);
			context.fill();

            p.x+=p.vx;
			p.y+=p.vy;
			p.vx*=(0.5-Math.random())*0.05+1;
			p.vy*=(0.5-Math.random())*0.05+1;
			if(p.x<-50) p.x=canvas.width +50;
			if(p.y<-50) p.y=canvas.height+50;
			if(p.x>canvas.width +50) p.x=-50;
			if(p.y>canvas.height+50) p.y=-50;
        }
        
    })();

});
