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
	this.armAngle = 0;
	this.waveAngle = 0;
	this.motion = 0; //0 = increase; 1 = decrease
	this.waveMotion = 0;
	this.waving = 0;
	this.waveCount = 0;

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
			if(!this.waving){
				this.scene.rotate(this.armAngle*degToRad, 0, 1, 0);
			}
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
			if(!this.waving){
				this.scene.rotate(this.armAngle*degToRad, 0, 1, 0); //Normal rotation
			}
			else{
				if(this.waveCount == 4){
					this.waving = 0;
					this.waveCount = 0;
					this.waveAngle = 0;
				}
				this.scene.rotate(this.waveAngle*degToRad, 1, 0, 0); //Waving rotation

			}
			this.scene.scale(0.2,0.2,1.7);
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.rightArm.display();
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

	if(this.waving)
		return;

	if(this.motion == 0){
		if(this.armAngle <= 90){
			this.armAngle +=5;
		}
		else{
			this.motion = 1;
		}
	}
	else if(this.motion = 1){
		if(this.armAngle >= -90){
			this.armAngle -=5;
		}
		else{
			this.motion = 0;
		}
	}
	
}
MyRobot.prototype.moveBackward = function(speed){

	this.posZ-=speed*Math.cos(this.scene.bot.angle*(Math.PI/180));
	this.posX-=speed*Math.sin(this.scene.bot.angle*(Math.PI/180));

	if(this.waving)
		return;

	if(this.motion == 0){
		if(this.armAngle <= 90){
			this.armAngle +=5;
		}
		else{
			this.motion = 1;
		}
	}
	else if(this.motion = 1){
		if(this.armAngle >= -90){
			this.armAngle -=5;
		}
		else{
			this.motion = 0;
		}
	}
	
}

MyRobot.prototype.update = function(){
	if(this.waving){
		if(this.waveCount == 3){
			if(this.waveAngle > 0){
				this.waveAngle -= 15;
			}
			else{
				this.waveCount++;
			}
		}
		else{
			if(this.waveMotion == 0){
			if(this.waveAngle <= 180){
				this.waveAngle +=15;
				console.log(this.waveAngle);
			}
			else{
				this.waveMotion = 1;
				if(this.waving){
					this.waveCount++;
				}
			}
	}
		else if(this.waveMotion = 1){
			if(this.waveAngle >= 90){
				this.waveAngle -=15;
				console.log(this.waveAngle);
			}
			else{
				this.waveMotion = 0;
			}
		}
	}
		}
			
};