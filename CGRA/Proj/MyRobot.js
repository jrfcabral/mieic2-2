/**
 * MyRobot
 * @param gl {WebGLRenderingContext}
 * @constructor
 */
function MyRobot(scene, minS, maxS, minT, maxT) {
	CGFobject.call(this,scene);
	
	this.wheelLeft = new MyCylinder(this.scene, 20, 1);
	this.wheelTopLeft = new MyCylinderTop(this.scene, 20);
	this.wheelRight = new MyCylinder(this.scene, 20, 1);
	this.wheelTopRight = new MyCylinderTop(this.scene, 20);

	this.body = new MyCylinder(this.scene, 20, 4);
	this.bodyTop = new MyCylinderTop(this.scene, 20);
	this.bodyBottom = new MyCylinderTop(this.scene, 20);


	this.degToRad = Math.PI / 180.0;
	this.angle = 0;
	this.posX = 0;
	this.posZ = 0; 
	
	this.initBuffers();
};

MyRobot.prototype = Object.create(CGFobject.prototype);
MyRobot.prototype.constructor=MyRobot;

MyRobot.prototype.display = function () {
	this.scene.pushMatrix();
		
		//this.scene.rotate(90*degToRad, 0, 1, 0);

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, 1);
			this.scene.scale(0.5, 0.5, 0.3);
			this.wheelLeft.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, 1.3);
			this.scene.scale(0.5, 0.5, 0.3);
			this.wheelTopLeft.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, -1.3);
			this.scene.scale(0.5, 0.5, 0.3);
			this.wheelRight.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			//this.scene.translate(0, 0.5, -1.3);
			//this.scene.scale(0.5, 0.5, 0.3);
			this.scene.rotate(180*this.degtoRad, 0, 0, 0);

			this.wheelTopRight.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 2.3, 0);
			this.scene.scale(1, 0.5, 1)
			this.scene.rotate(90*degToRad, 1, 0, 0);
			this.body.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0, 1);
			this.scene.rotate(90*this.degtoRad, 0, 0, 1);
			this.bodyTop.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0, 2);
			this.scene.rotate(40*this.degtorad, 0, 0, 0);
			this.bodyBottom.display();
		this.scene.popMatrix();

	this.scene.popMatrix();
}

MyRobot.prototype.setAngle = function(angle){
	this.angle = angle;
}

MyRobot.prototype.setPod = function(x, y){
	this.posX = x;
	this.posY = y;
}
MyRobot.prototype.moveForward = function(speed){
	this.posZ+=speed*Math.cos(this.scene.bot.angle*(Math.PI/180));
	this.posX+=speed*Math.sin(this.scene.bot.angle*(Math.PI/180));
}
MyRobot.prototype.moveBackward = function(speed){
	this.posZ-=speed*Math.cos(this.scene.bot.angle*(Math.PI/180));
	this.posX-=speed*Math.sin(this.scene.bot.angle*(Math.PI/180));
}