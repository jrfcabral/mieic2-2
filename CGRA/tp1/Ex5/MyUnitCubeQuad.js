function MyUnitCubeQuad(scene) {
	CGFobject.call(this,scene);
	this.quad = new MyQuad(this.scene);
	this.quad.initBuffers();
	
	//this.scene.initBuffers();
};
MyUnitCubeQuad.prototype = Object.create(CGFobject.prototype);
MyUnitCubeQuad.prototype.constructor=MyUnitCubeQuad;

MyUnitCubeQuad.prototype.display = function () {
	var deg2rad=Math.PI/180.0;
	this.scene.pushMatrix();
	this.scene.translate(0, 0, 0.5);
	this.quad.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(0, 0, -0.5);
	this.scene.rotate(180.0*deg2rad, 0, 1.0, 0);
	this.quad.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(-0.5, 0, 0);
	this.scene.rotate(-90.0*deg2rad, 0, 1.0, 0);
	this.quad.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(0.5, 0, 0);
	this.scene.rotate(90.0*deg2rad, 0, 1.0, 0);
	this.quad.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(0, 0.5, 0);
	this.scene.rotate(-90*deg2rad, 1.0, 0, 0);
	this.quad.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(0, -0.5, 0);
	this.scene.rotate(90*deg2rad, 1.0, 0, 0);
	this.quad.display();
	this.scene.popMatrix();
};
