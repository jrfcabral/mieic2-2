/**
 * MyClockHand
 * @constructor
 */
 function MyClockHand(scene) {
 	CGFobject.call(this,scene);
	this.angle = 0.0;
	var degToRad = Math.PI / 180.0;
	this.quad = new MyQuad(this.scene);
 	//this.initBuffers();
 };

 MyClockHand.prototype = Object.create(CGFobject.prototype);
 MyClockHand.prototype.constructor = MyClockHand;

MyClockHand.prototype.display = function(){
	this.scene.pushMatrix();
		this.scene.scale(0.01, 0.5, 1);
		this.scene.translate(0, 0.5, 0);
		
		this.quad.display();
	this.scene.popMatrix();
};

MyClockHand.prototype.setAngle = function(angle){
	this.angle = angle;
};