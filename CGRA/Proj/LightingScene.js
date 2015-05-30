var degToRad = Math.PI / 180.0;

var BOARD_WIDTH = 6.0;
var BOARD_HEIGHT = 4.0;

var BOARD_A_DIVISIONS = 250;
var BOARD_B_DIVISIONS = 100;

function LightingScene() {
	CGFscene.call(this);
}

LightingScene.prototype = Object.create(CGFscene.prototype);
LightingScene.prototype.constructor = LightingScene;

LightingScene.prototype.init = function(application) {
	CGFscene.prototype.init.call(this, application);

	this.initCameras();

	this.initLights();
	this.enableTextures(true);


	this.option1=true; 
	this.option2=false; 	
	this.speed=0.1;
	this.Light0 = true;
	this.Light1 = true;
	this.Light2 = true;
	this.Light3 = true;
	this.enableClock = true;
	

	this.gl.clearColor(0.0, 0.0, 0.0, 1.0);
	this.gl.clearDepth(100.0);
	this.gl.enable(this.gl.DEPTH_TEST);
	this.gl.enable(this.gl.CULL_FACE);
	this.gl.depthFunc(this.gl.LEQUAL);

	this.axis = new CGFaxis(this);

	// Scene elements
	this.table = new myTable(this);
	this.floor = new MyQuad(this,0,10,0,12);
	this.leftwall = new windowWall(this,100);
	this.planewall = new Plane(this, 100);
	this.boardA = new Plane(this, BOARD_A_DIVISIONS,-0.25,1.25,0,1);
	this.boardB = new Plane(this, BOARD_B_DIVISIONS);
	this.prism = new MyPrism(this, 8, 20);
	this.cylinder = new MyCylinder(this, 8, 20);
	this.lamp = new MyLamp(this, 10, 60);
	this.clock = new MyClock(this, 12, 1);
	this.bot = new MyRobot(this);
	this.impostor = new Plane(this, 1);
	this.wheel = new MyRobotPart(this);

	

	// Materials
	this.materialDefault = new CGFappearance(this);
	
	this.materialA = new CGFappearance(this);
	this.materialA.setAmbient(0.3,0.3,0.3,1);
	this.materialA.setDiffuse(0.6,0.6,0.6,1);
	this.materialA.setSpecular(0.0,0.2,0.8,1);
	this.materialA.setShininess(120);

	this.materialB = new CGFappearance(this);
	this.materialB.setAmbient(0.3,0.3,0.3,1);
	this.materialB.setDiffuse(0.6,0.6,0.6,1);
	this.materialB.setSpecular(0.8,0.8,0.8,1);	
	this.materialB.setShininess(120);

	this.materialWalls = new CGFappearance(this);
	this.materialWalls.setAmbient(0.2,0.03,0,1);
	this.materialWalls.setDiffuse(0.2,0.3,0.27,1);
	this.materialWalls.setSpecular(0.01,0.01,0.01,1);	
	this.materialWalls.setShininess(10);

	this.materialLeftWall = new CGFappearance(this);
	this.materialLeftWall.setAmbient(0.2,0.03,0,1);
	this.materialLeftWall.setDiffuse(0.2,0.3,0.27,1);
	this.materialLeftWall.setSpecular(0.01,0.01,0.01,1);	
	this.materialLeftWall.setShininess(10);
	this.materialLeftWall.loadTexture("../resources/images/window.png");
	this.materialLeftWall.setTextureWrap("CLAMP_TO_EDGE","CLAMP_TO_EDGE");

	//madeira
	this.materialFloor = new CGFappearance(this);
	this.materialFloor.setShininess(20);
	this.materialFloor.setDiffuse(0.1,0.03,0,1);
	this.materialFloor.setAmbient(0,1,0.03,0,1);
	this.materialFloor.setSpecular(0.05,0.025,0,1);
	this.materialFloor.loadTexture("../resources/images/floor.png");
	
	this.slidesAppearance = new CGFappearance(this);
	this.slidesAppearance.setSpecular(0.05, 0.05,0.05,1);
	this.slidesAppearance.setDiffuse(1,1,1,1);
	this.slidesAppearance.setShininess(10);
	this.slidesAppearance.loadTexture("../resources/images/slides.png");
	this.slidesAppearance.setTextureWrap("CLAMP_TO_EDGE","CLAMP_TO_EDGE");
	
	this.boardAppearance = new CGFappearance(this);
	this.boardAppearance.setSpecular(0.6, 0.6,0.6,1);
	this.boardAppearance.setDiffuse(0.4,0.4,0.4,1);
	this.boardAppearance.setShininess(100);
	this.boardAppearance.loadTexture("../resources/images/board.png");

	this.clockAppearance = new CGFappearance(this);
	this.clockAppearance.setSpecular(0.6, 0.6,0.6,1);
	this.clockAppearance.setDiffuse(0.4,0.4,0.4,1);
	this.clockAppearance.setShininess(100);
	this.clockAppearance.loadTexture("../resources/images/clock.png");
	this.clockAppearance.setTextureWrap("CLAMP_TO_EDGE", "CLAMP_TO_EDGE");

	this.botAppearance = new CGFappearance(this);
	this.botAppearance.setDiffuse(1.0, 1.0, 0.1, 1);
	this.botAppearance.setSpecular(0.5, 0.5, 0.5, 1);
	this.botAppearance.setShininess(1.0, 1.0, 0.1, 1);
	this.botAppearance.setShininess(120);

	this.impostorAppearance = new CGFappearance(this);
	this.impostorAppearance.setDiffuse(1,1,1,1);
	this.impostorAppearance.setSpecular(0,0,0,1);
	this.impostorAppearance.setShininess(1);
	this.impostorAppearance.loadTexture("../resources/images/impostor.jpg");
	this.impostorAppearance.setTextureWrap("CLAMP_TO_EDGE", "CLAMP_TO_EDGE");

	//define robot material options
	this.materialHead1 = new CGFappearance(this);
	this.materialHead1.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialHead1.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialHead1.setShininess(120);
	this.materialHead1.loadTexture("../resources/images/head1.png");
	this.materialBody1 = new CGFappearance(this);
	this.materialBody1.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialBody1.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialBody1.setShininess(120);
	this.materialBody1.loadTexture("../resources/images/body1.jpg");


	this.materialHead2 = new CGFappearance(this);
	this.materialHead2.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialHead2.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialHead2.setShininess(120);
	this.materialHead2.loadTexture("../resources/images/head2.png");
	this.materialBody2 = new CGFappearance(this);
	this.materialBody2.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialBody2.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialBody2.setShininess(120);
	this.materialBody2.loadTexture("../resources/images/body2.jpg");


	this.materialHead3 = new CGFappearance(this);
	this.materialHead3.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialHead3.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialHead3.setShininess(120);
	this.materialHead3.loadTexture("../resources/images/head3.png");
	this.materialBody3 = new CGFappearance(this);
	this.materialBody3.setDiffuse(1.0, 1.0, 1.0, 1);
	this.materialBody3.setSpecular(0.5, 0.5, 0.5, 1);	
	this.materialBody3.setShininess(120);
	this.materialBody3.loadTexture("../resources/images/body3.jpg");

	//Robot switching textures support structures
	this.RobotTexture = "metal";
	this.TexBodyMap = {};
	this.TexBodyMap["metal"] = this.materialBody1;
	this.TexBodyMap["fabric"] =this.materialBody2;
	this.TexBodyMap["carbon"] = this.materialBody3;
	this.TexHeadMap = {};
	this.TexHeadMap["metal"] = this.materialHead1;
	this.TexHeadMap["fabric"] = this.materialHead2;
	this.TexHeadMap["carbon"] = this.materialHead3;

		

	this.setUpdatePeriod(100);
};

LightingScene.prototype.initCameras = function() {
	this.camera = new CGFcamera(0.4, 0.1, 500, vec3.fromValues(30, 30, 30), vec3.fromValues(0, 0, 0));
};

LightingScene.prototype.initLights = function() {
	this.setGlobalAmbientLight(0,0,0, 1.0);

	this.shader.bind();
	
	// Positions for four lights
	this.lights[0].setPosition(0, 0, 0, 1);
	this.lights[1].setPosition(10.5, 2.0, 3.0, 1.0);
	this.lights[2].setPosition(10.5, 2.0, 5.0, 1.0);
	this.lights[3].setPosition(8, 8, 8, 1.0);


	this.lights[0].setAmbient(0, 0, 0, 1);
	this.lights[0].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[0].setSpecular(1.0,1.0,0.0,1.0);
	this.lights[0].setVisible(true);
	
	this.lights[1].setAmbient(0, 0, 0, 1);
	this.lights[1].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[1].setVisible(true);
	//this.lights[1].enable();

	this.lights[2].setDiffuse(1.0,1.0,1.0,1.0);
	this.lights[2].setSpecular(1.0,1.0,1.0,1.0);	
	this.lights[2].setQuadraticAttenuation(0);
	this.lights[2].setLinearAttenuation(0.2);
	this.lights[2].setConstantAttenuation(0);
	this.lights[2].setVisible(true);
	//this.lights[2].enable();

	this.lights[3].setAmbient(0, 0, 0, 1);
	this.lights[3].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[3].setSpecular(1.0,1.0,1.0,1.0);
	this.lights[3].setQuadraticAttenuation(0);
	this.lights[3].setLinearAttenuation(0.5);
	this.lights[3].setConstantAttenuation(0.2);
	this.lights[3].setVisible(true);
	//this.lights[3].enable();

	

	this.shader.unbind();


};

LightingScene.prototype.updateLights = function() {
	for (i = 0; i < this.lights.length; i++)
		this.lights[i].update();
}


LightingScene.prototype.display = function() {
	this.shader.bind();

	// ---- BEGIN Background, camera and axis setup

	// Clear image and depth buffer everytime we update the scene
	this.gl.viewport(0, 0, this.gl.canvas.width, this.gl.canvas.height);
	this.gl.clear(this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT);

	// Initialize Model-View matrix as identity (no transformation)
	this.updateProjectionMatrix();
	this.loadIdentity();

	// Apply transformations corresponding to the camera position relative to the origin
	this.applyViewMatrix();

	// Update all lights used
	this.updateLights();

	// Draw axis
	this.axis.display();

	this.materialDefault.apply();

	// Floor
	this.materialFloor.apply();
	this.pushMatrix();
		this.translate(7.5, 0, 7.5);
		this.rotate(-90 * degToRad, 1, 0, 0);
		this.scale(15, 15, 1);
		this.floor.display();
	this.popMatrix();

	this.materialLeftWall.apply();
	// Left Wall
	this.pushMatrix();
		this.translate(0, 4, 7.5);
		this.rotate(90 * degToRad, 0, 1, 0);
		this.scale(15, 8, 0.2);
		this.leftwall.display();
	this.popMatrix();

	this.materialWalls.apply();
	// Plane Wall
	this.pushMatrix();
		this.translate(7.5, 4, 0);
		this.scale(15, 8, 0.2);
		this.planewall.display();
	this.popMatrix();

	/*	// First Table
	this.pushMatrix();
		this.translate(5, 0, 8);
		this.table.display();
	this.popMatrix();

	// Second Table
	this.pushMatrix();
		this.translate(12, 0, 8);
		this.table.display();
	this.popMatrix();*/

	// Board A
	this.pushMatrix();
		this.translate(4, 4.5, 0.2);
		this.scale(BOARD_WIDTH, BOARD_HEIGHT, 1);
		this.slidesAppearance.apply();
		this.boardA.display();
	this.popMatrix();
	
	// Board B
	this.pushMatrix();
		this.translate(10.5, 4.5, 0.2);
		this.scale(BOARD_WIDTH, BOARD_HEIGHT, 1);
		this.boardAppearance.apply();
		this.boardB.display();
	this.popMatrix();

	//Impostor
	this.impostorAppearance.apply();
	this.pushMatrix();
		this.translate(-2.5, 4,6);		
		this.rotate(90*degToRad, 0,1,0);
		this.scale(20, 10, 1);
		this.impostor.display();
	this.popMatrix();
		
	//Lamp
	this.materialA.apply();
	this.pushMatrix();
		this.translate(8, 9, 8);
		this.scale(0.5,0.5,0.5);
		this.lamp.display();
	this.popMatrix();

	//Clock
	this.pushMatrix();
		this.translate(7.2, 7.2, 0.01);
		this.materialA.apply();
		this.clock.display();
	this.popMatrix();

	//Robot
	this.slidesAppearance.apply();
	this.pushMatrix();
		this.translate(this.bot.posX, 0, this.bot.posZ);
		this.rotate(this.bot.angle*degToRad, 0, 1, 0);		
		this.bot.display();
	this.popMatrix();




	// ---- END Primitive drawing section

	this.shader.unbind();
};

LightingScene.prototype.loadHeadTexture = function(){
	this.TexHeadMap[this.RobotTexture].apply();
}

LightingScene.prototype.loadBodyTexture = function(){
	this.TexBodyMap[this.RobotTexture].apply();
} 

LightingScene.prototype.update = function(currTime){
	if(this.enableClock)
		this.clock.update(currTime);

	if(this.Light0 == true){
		this.lights[0].enable();
	}
	else{
		this.lights[0].disable();
	}

	if(this.Light1 == true){
		this.lights[1].enable();
	}
	else{
		this.lights[1].disable();
	}

	if(this.Light2 == true){
		this.lights[2].enable();
	}
	else{
		this.lights[2].disable();
	}

	if(this.Light3 == true){
		this.lights[3].enable();
	}
	else{
		this.lights[3].disable();
	}

};


LightingScene.prototype.ToggleClock = function(){
	if(this.enableClock)
		this.enableClock = false;
	else{
		this.enableClock = true;
	}
};

