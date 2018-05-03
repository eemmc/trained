define(function(require){
    var Ball=require("ball");

    var canvas=document.createElement("canvas");
    document.body.appendChild(canvas);
    var context=canvas.getContext("2d");

    window.addEventListener("resize",(function(){
        canvas.width=document.documentElement.clientWidth;
        canvas.height=document.documentElement.clientHeight;
        return this;
    })(),false);

    var particles=[];
    var numParicles=30;

    for(var particle,i=0;i<numParicles;i++){
        particle=new Ball(5);
        particle.x=Math.random()*canvas.width;
        particle.y=Math.random()*canvas.height;
        particle.mass=1;
        particles.push(particle);
    }

    function gravitate(partA,partB){
        var dx=partA.x-partB.x;
        var dy=partA.y-partB.y;
        var disq=dx*dx+dy*dy;
        var dist=Math.sqrt(disq);

        var force=partA.mass*partB.mass/disq;
        var ax=force*dx/dist;
        var ay=force*dy/dist;
       
        partA.vx+=ax/partA.mass;
        partA.vy+=ay/partA.mass;
        partB.vx-=ax/partB.mass;
        partB.vy-=ax/partB.mass;
    }

    function move(partA,i){
        partA.x+=partA.vx;
        partA.y+=partA.vy;

        for(var partB,j=i+1;j<numParicles;j++){
            partB=particles[j];
            gravitate(partA,partB);
        }
    }

    function draw(particle){
        particle.draw(context);
    }

    (function loop(){
        window.requestAnimationFrame(loop);
        context.clearRect(0,0,canvas.width,canvas.height);

        particles.forEach(move);
        particles.forEach(draw);

    })();

});
