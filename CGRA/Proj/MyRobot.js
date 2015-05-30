/**
 * MyRobot
 * @param gl {WebGLRenderingContext}
 * @constructor
 */
function MyRobot(scene, minS, maxS, minT, maxT) {
	CGFobject.call(this,scene);
	this.currTexture = 1;


 	
	this.wheelLeft = new MyWheel(this.scene);
	this.wheelRight = new MyWheel(this.scene);
	this.bodyBottom = new MyRobotPart(this.scene);
	this.head = new MyLamp(this.scene, 190, 190);
	this.leftArm = new MyRobotPart(this.scene);
	this.rightArm = new MyRobotPart(this.scene);
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
	this.lftWheelAngle = 0;
	this.rgtWheelAngle = 0;
	this.initBuffers();
};

MyRobot.prototype = Object.create(CGFobject.prototype);
MyRobot.prototype.constructor=MyRobot;

MyRobot.prototype.display = function () {
	this.scene.pushMatrix();
		
		this.scene.rotate(90*degToRad, 0, 1, 0);
		//this.materialTire.apply();
		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, 0.5);
			this.scene.scale(0.5, 0.5, 0.3);
			this.scene.rotate(this.lftWheelAngle*degToRad, 0, 0, 1);
			this.scene.slidesAppearance.apply();
			this.wheelLeft.display();
		this.scene.popMatrix();

		this.scene.pushMatrix();
			this.scene.translate(0, 0.5, -0.5);
			this.scene.scale(0.5, 0.5, 0.3);
			this.scene.rotate(this.rgtWheelAngle*degToRad, 0, 0, 1);
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.wheelRight.display();
		this.scene.popMatrix();


		this.scene.pushMatrix();
			this.scene.translate(0, 1.3, 0);
			this.scene.scale(0.79, 1.5, 0.79);
			this.scene.rotate(180*degToRad, 0, 1, 0);
			this.scene.rotate(90*degToRad, 1, 0, 0);
			this.bodyBottom.display();
		this.scene.popMatrix();


		this.scene.pushMatrix();
			this.scene.translate(0, 2.7, 0);
			this.scene.scale(0.325, 0.4, 0.325);
			this.scene.loadHeadTexture();
			this.head.display();
		this.scene.popMatrix();
	
		this.scene.loadBodyTexture();
		this.scene.pushMatrix();
			this.scene.translate(0, 2.05, 0);
			this.scene.scale(0.8, 3.2, 0.8)
			this.scene.rotate(90*degToRad, 1, 0, 0);
			this.bodyBottom.display();
		this.scene.popMatrix();


		this.scene.pushMatrix();
			this.scene.translate(0, 1.8, 0.9);
			this.scene.scale(0.12, 0.12, 0.35);
			this.scene.rotate(90*degToRad, 1, 0, 0);
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
			this.scene.translate(0, 1.8, -0.9);
			this.scene.scale(0.12, 0.12, 0.35);
			this.scene.rotate(-90*degToRad, 1, 0, 0);
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

MyRobot.prototype.setPos = function(x, y){
	this.posX = x;
	this.posY = y;
}
MyRobot.prototype.moveForward = function(speed){
	
	this.posZ+=speed*Math.cos(this.scene.bot.angle*(Math.PI/180));
	this.posX+=speed*Math.sin(this.scene.bot.angle*(Math.PI/180));

	this.lftWheelAngle += this.scene.speed*45;
	this.rgtWheelAngle += this.scene.speed*45;

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

	this.lftWheelAngle -= this.scene.speed*45;
	this.rgtWheelAngle -= this.scene.speed*45;

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

MyRobot.prototype.turn = function(val){
	this.scene.bot.setAngle(this.scene.bot.angle + val);
	
	if(val < 0){
		this.rgtWheelAngle -= this.scene.speed*45;
		this.lftWheelAngle += this.scene.speed*45;
	}
	else if(val > 0){
		this.rgtWheelAngle += this.scene.speed*45;
		this.lftWheelAngle -= this.scene.speed*45;
	}
		
};


