
function TPscene() {
    CGFscene.call(this);
}

TPscene.prototype = Object.create(CGFscene.prototype);
TPscene.prototype.constructor = TPscene;

TPscene.prototype.init = function (application) {
    CGFscene.prototype.init.call(this, application);

    this.initCameras();

    this.initLights();


    this.gl.clearColor(0.0, 0.0, 0.0, 1.0);
    this.gl.clearDepth(100.0);
    this.gl.enable(this.gl.DEPTH_TEST);
	this.gl.enable(this.gl.CULL_FACE);
    this.gl.depthFunc(this.gl.LEQUAL);

	this.axis=new CGFaxis(this);
   // this.obj = new MyObject(this);
   /* this.obj = new MyUnitCubeQuad(this);
    this.obj2 = new MyUnitCube(this);*/
    this.obj = new myTable(this);
    this.obj2 = new myFloor(this);

};

TPscene.prototype.initLights = function () {

    this.shader.bind();

	this.lights[0].setPosition(15, 2, 5, 1);
    this.lights[0].setDiffuse(1.0,0.5,1.0,1.0);
    this.lights[0].enable();
    this.lights[0].update();
 
    this.shader.unbind();
};

TPscene.prototype.initCameras = function () {
    this.camera = new CGFcamera(0.4, 0.1, 500, vec3.fromValues(0, 0, 25), vec3.fromValues(0, 0, 0));
};

TPscene.prototype.setDefaultAppearance = function () {
    this.setAmbient(0.2, 0.4, 0.8, 1.0);
    this.setDiffuse(0.2, 0.4, 0.8, 1.0);
    this.setSpecular(0.2, 0.4, 0.8, 1.0);
    this.setShininess(10.0);	
};

TPscene.prototype.display = function () {
	// ---- BEGIN Background, camera and axis setup
    this.shader.bind();
	
	// Clear image and depth buffer everytime we update the scene
    this.gl.viewport(0, 0, this.gl.canvas.width, this.gl.canvas.height);
    this.gl.clear(this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT);

	// Initialize Model-View matrix as identity (no transformation
	this.updateProjectionMatrix();
    this.loadIdentity();

	// Apply transformations corresponding to the camera position relative to the origin
	this.applyViewMatrix();

	// Draw axis
	this.axis.display();
	this.setDefaultAppearance();
	/*this.obj2.display();
	this.translate(2.0, 0, 0);*/
	this.translate(4, 0, 3);
	this.obj.display();
	this.obj2.display();


    this.shader.unbind();
};
