/**
 * MyClock
 * @constructor
 */
 function MyClock(scene) {
 	CGFobject.call(this,scene);

	this.cylinder = new MyCylinder(this.scene, 12, 1, 0.7, 0.2);
	this.clockFace = new MyClockFace(this.scene, 12);

	this.clockTex = new CGFappearance(this.scene);
	this.clockTex.setShininess(20);
	this.clockTex.setDiffuse(0.5,0.5,0,5);
	this.clockTex.setAmbient(0.2,0.2,0,2);
	this.clockTex.setSpecular(0.3,0.3,0,3);
	this.clockTex.loadTexture("../resources/images/clock.png");

 	//this.initBuffers();
 };

 MyClock.prototype = Object.create(CGFobject.prototype);
 MyClock.prototype.constructor = MyClock;

MyClock.prototype.display = function(){
 	this.scene.pushMatrix();
 		this.cylinder.display();
 		this.scene.translate(0, 0, 0.2)
 		this.clockTex.apply();
 		this.clockFace.display();
 	this.scene.popMatrix();
 }