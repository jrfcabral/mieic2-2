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

	this.gl.clearColor(0.0, 0.0, 0.0, 1.0);
	this.gl.clearDepth(100.0);
	this.gl.enable(this.gl.DEPTH_TEST);
	this.gl.enable(this.gl.CULL_FACE);
	this.gl.depthFunc(this.gl.LEQUAL);

	this.axis = new CGFaxis(this);

	// Scene elements
	this.table = new myTable(this);
	this.floor = new MyQuad(this,0,10,0,12);
	this.leftwall = new MyQuad(this,-1,2,-0.5,1.5);
	this.planewall = new Plane(this, 100);
	this.boardA = new Plane(this, BOARD_A_DIVISIONS,-0.25,1.25,0,1);
	this.boardB = new Plane(this, BOARD_B_DIVISIONS);
	this.prism = new MyPrism(this, 8, 20);
	this.cylinder = new MyCylinder(this, 8, 20);
	this.lamp = new MyLamp(this, 8, 20);
	this.clock = new MyClock(this, 12, 1);
	

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


	this.setUpdatePeriod(100);
};

LightingScene.prototype.initCameras = function() {
	this.camera = new CGFcamera(0.4, 0.1, 500, vec3.fromValues(30, 30, 30), vec3.fromValues(0, 0, 0));
};

LightingScene.prototype.initLights = function() {
	this.setGlobalAmbientLight(0,0,0, 1.0);

	this.shader.bind();
	
	// Positions for four lights
	this.lights[0].setPosition(4, 6.0, 1, 1);
	this.lights[1].setPosition(10.5, 6.0, 1.0, 1.0);
	this.lights[2].setPosition(10.5, 2.0, 5.0, 1.0);
	this.lights[3].setPosition(8, 8, 8, 1.0);


	this.lights[0].setAmbient(0, 0, 0, 1);
	this.lights[0].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[0].setSpecular(1.0,1.0,0.0,1.0);
	this.lights[0].enable();

	this.lights[1].setAmbient(0, 0, 0, 1);
	this.lights[1].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[1].enable();

	this.lights[2].setDiffuse(1.0,1.0,1.0,1.0);
	this.lights[2].setSpecular(1.0,1.0,1.0,1.0);	
	this.lights[2].setQuadraticAttenuation(0);
	this.lights[2].setLinearAttenuation(0.2);
	this.lights[2].setConstantAttenuation(0);
	this.lights[2].enable();

	this.lights[3].setAmbient(0, 0, 0, 1);
	this.lights[3].setDiffuse(1.0, 1.0, 1.0, 1.0);
	this.lights[3].setSpecular(1.0,1.0,1.0,1.0);
	this.lights[3].setQuadraticAttenuation(0);
	this.lights[3].setLinearAttenuation(0.5);
	this.lights[3].setConstantAttenuation(0.2);
	this.lights[3].setVisible(true);
	this.lights[3].enable();

	

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

	// ---- END Background, camera and axis setup

	
	// ---- BEGIN Geometric transformation section

	// ---- END Geometric transformation section


	// ---- BEGIN Primitive drawing section

	
	// Floor
	this.materialFloor.apply();
	this.pushMatrix();
		this.translate(7.5, 0, 7.5);
		this.rotate(-90 * degToRad, 1, 0, 0);
		this.scale(15, 15, 0.2);
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

	// First Table
	this.pushMatrix();
		this.translate(5, 0, 8);
		this.table.display();
	this.popMatrix();

	// Second Table
	this.pushMatrix();
		this.translate(12, 0, 8);
		this.table.display();
	this.popMatrix();

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

	//Prism and Cylinder
	this.pushMatrix();
		this.rotate(-90 * degToRad, 1, 0, 0);
		this.translate(2, -12, 0);
		this.prism.display();
		this.translate(12, 0, 0);
		this.cylinder.display();
	this.popMatrix();
	
	//Lamp
	this.pushMatrix();
		this.rotate(-90 * degToRad, 1, 0, 0);
		this.translate(8, -8, 8);
		this.lamp.display();
	this.popMatrix();

	//Clock
	this.pushMatrix();
		this.translate(7.2, 7.2, 0.01);
		this.materialA.apply();
		this.clock.display();
	this.popMatrix();



	// ---- END Primitive drawing section

	this.shader.unbind();
};

LightingScene.prototype.update = function(currTime){
	this.clock.update(currTime);
};