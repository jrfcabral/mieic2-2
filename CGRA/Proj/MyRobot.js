/**
 * MyRobot
 * @param gl {WebGLRenderingContext}
 * @constructor
 */
function MyRobot(scene, minS, maxS, minT, maxT) {
	CGFobject.call(this,scene);
	
	this.wheelLeft = new MyRobotPart(this.scene, 20, 1);
	this.wheelRight = new MyRobotPart(this.scene, 20, 1);
	this.bodyTop = new MyRobotPart(this.scene, 20);
	this.bodyBottom = new MyRobotPart(this.scene, 20);
	this.head = new MyLamp(this.scene, 20, 20);
	this.leftArm = new MyRobotPart(this.scene, 20, 1);
	this.rightArm = new MyRobotPart(this.scene, 20, 1);
	this.leftShoulder = new MyLamp(this.scene, 20, 20);
	this.rightShoulder= new MyLamp(this.scene, 20, 20);

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
		
		this.scene.rotate(90*degToRad, 0, 1, 0);

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, 0.5);
			this.scene.scale(0.5, 0.5, 0.3);
			this.wheelLeft.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, -0.5);
			this.scene.scale(0.5, 0.5, 0.3);
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.wheelRight.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 1.3, 0);
			this.scene.scale(0.8, 1.5, 0.8)
			this.scene.rotate(90*degToRad, 1, 0, 0);
			this.bodyBottom.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 1.3, 0);
			this.scene.scale(0.8, 1.5, 0.8);
			this.scene.rotate(-90*degToRad, 1, 0, 0);
			this.bodyTop.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 2, 0);
			this.scene.scale(0.55, 0.6, 0.55);
			this.scene.rotate(-90*degToRad, 1, 0, 0);
			this.head.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 1.8, 0.5);
			this.scene.scale(0.2, 0.2, 0.4);
			this.leftShoulder.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 1.8, 0.7);
			this.scene.rotate(90*degToRad, 1, 0, 0);
			this.scene.scale(0.2,0.2,1.7);
			this.leftArm.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 1.8, -0.5);
			this.scene.scale(0.2, 0.2, 0.4);
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.rightShoulder.display();
		this.scene.popMatrix();


		this.scene.pushMatrix();
			this.scene.translate(0, 1.8, -0.7);
			this.scene.rotate(-90*degToRad, 1, 0, 0);
			this.scene.scale(0.2,0.2,1.7);;
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.rightArm.display();
		this.scene.popMatrix();

	this.scene.popMatrix();
}

MyRobot.prototype.setAngle = function(angle){
	this.angle = angle;
}

MyRobot.prototype.setPos = function(x, y){
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