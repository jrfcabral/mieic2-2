/**
 * MyWheel
 * @constructor
 */
 function MyWheel(scene) {
 	CGFobject.call(this,scene);

	this.cylinder = new MyCylinder(this.scene, 20, 1);
	this.cylinderTop = new MyCylinderTop(this.scene,20);

	var degToRad = Math.PI / 180.0;
	var timer = 0;

	this.materialWheels = new CGFappearance(scene);
	this.materialWheels.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialWheels.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialWheels.setShininess(120);
	this.materialWheels.loadTexture("../resources/images/wheel.jpg");

	this.materialTire = new CGFappearance(scene);
	this.materialTire.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialTire.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialTire.setShininess(120);
	this.materialTire.loadTexture("../resources/images/tire.jpg");
 };

 MyWheel.prototype = Object.create(CGFobject.prototype);
 MyWheel.prototype.constructor = MyRobotPart;


MyWheel.prototype.display = function(){
 	this.scene.pushMatrix();
 		this.scene.scale(0.7, 0.7, 0.5);
 		this.materialTire.apply();
 		this.cylinder.display();
 		this.scene.translate(0, 0, 1);
 		this.materialWheels.apply();
 		this.cylinderTop.display();
 	this.scene.popMatrix();

 };
