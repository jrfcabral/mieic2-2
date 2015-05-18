/**
 * MyRobotPart
 * @constructor
 */
 function MyRobotPart(scene) {
 	CGFobject.call(this,scene);

	this.cylinder = new MyCylinder(this.scene, 20, 1);
	this.cylinderTop = new MyCylinderTop(this.scene,20);

	var degToRad = Math.PI / 180.0;
	var timer = 0;
 };

 MyRobotPart.prototype = Object.create(CGFobject.prototype);
 MyRobotPart.prototype.constructor = MyRobotPart;


MyRobotPart.prototype.display = function(){
 	this.scene.pushMatrix();
 		this.scene.scale(0.7, 0.7, 0.5);
 		this.cylinder.display();
 		this.scene.translate(0, 0, 1);
 		this.cylinderTop.display();
 	this.scene.popMatrix();

 };

MyRobotPart.prototype.update = function(currTime){
	
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