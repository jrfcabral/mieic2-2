/**
 * MyClock
 * @constructor
 */
 function MyClock(scene) {
 	CGFobject.call(this,scene);

	this.cylinder = new MyCylinder(this.scene, 12, 1, 0.7, 0.2);
	this.clockFace = new MyClockFace(this.scene, 12);
	this.secondHand = new MyClockHand(this.scene);
	this.minuteHand = new MyClockHand(this.scene);
	this.hourHand = new MyClockHand(this.scene);

	this.clockTex = new CGFappearance(this.scene);
	this.clockTex.setShininess(20);
	this.clockTex.setDiffuse(0.5,0.5,0,5);
	this.clockTex.setAmbient(0.2,0.2,0,2);
	this.clockTex.setSpecular(0.3,0.3,0,3);
	this.clockTex.loadTexture("../resources/images/clock.png");

	this.secondHandMat = new CGFappearance(this.scene); 
	this.secondHandMat.setDiffuse(1,0.1,0.1);
	this.secondHandMat.setAmbient(1,0.2,0,2);
	this.secondHandMat.setSpecular(0.3,0.3,0,3);

	this.hourMinuteHandMat = new CGFappearance(this.scene);
	this.hourMinuteHandMat.setDiffuse(0,0,0);
	this.hourMinuteHandMat.setAmbient(1,0.2,0,2);
	this.hourMinuteHandMat.setSpecular(0.3,0.3,0,3);

	var degToRad = Math.PI / 180.0;
	var timer = 0;
 };

 MyClock.prototype = Object.create(CGFobject.prototype);
 MyClock.prototype.constructor = MyClock;


MyClock.prototype.display = function(){
 	this.scene.pushMatrix();
 		this.cylinder.display();
 		this.scene.translate(0, 0, 0.2);
 		//this.clockTex.apply();
 		this.clockFace.display();
 
 	this.scene.popMatrix();

	this.scene.pushMatrix();
 		this.scene.translate(0, 0, 0.21);
		this.scene.rotate(this.secondHand.angle*degToRad, 0, 0, 1);
		
 		this.secondHandMat.apply();
 		this.secondHand.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
 		this.scene.translate(0, 0, 0.22);
 		this.scene.rotate(this.minuteHand.angle*degToRad, 0, 0, 1);
 		this.scene.scale(1.5, 1, 1);
 		
 		this.hourMinuteHandMat.apply();
 		this.minuteHand.display();
 	this.scene.popMatrix();

	this.scene.pushMatrix();
		this.scene.translate(0, 0, 0.23);
		this.scene.rotate(this.hourHand.angle*degToRad, 0, 0, 1);
		this.scene.scale(2, 0.7, 1)
		
		this.hourHand.display();
	this.scene.popMatrix();

 };

MyClock.prototype.update = function(currTime){
	
	var x = currTime / 1000;
	var seconds = x % 60;
	x /= 60;
	var minutes = x % 60;
	x /= 60;
	var hours = x % 24;
	
	this.secondHand.setAngle(-seconds*6);

	this.minuteHand.setAngle(-minutes*6);
	this.hourHand.setAngle(-(hours+1)*30);
};