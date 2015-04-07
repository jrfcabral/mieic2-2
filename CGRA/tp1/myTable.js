function myTable(scene) {
	CGFobject.call(this,scene);

	//this.initBuffers();
	this.cube = new MyUnitCubeQuad(this.scene);
};

myTable.prototype = Object.create(CGFobject.prototype);
myTable.prototype.constructor=myTable;

myTable.prototype.display = function(){
	
	this.scene.pushMatrix();
	this.scene.translate(0, 1.8, 0);
	
	this.scene.pushMatrix();
	this.scene.translate((-2.5)+0.15, 0, (-1.5)+0.15);
	this.scene.scale(0.3, 3.5, 0.3);
	this.cube.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate((-2.5)+0.15, 0, (1.5)-0.15);
	this.scene.scale(0.3, 3.5, 0.3);
	this.cube.display();
	this.scene.popMatrix();
	
	this.scene.pushMatrix();
	this.scene.translate((2.5)-0.15, 0, (-1.5)+0.15);
	this.scene.scale(0.3, 3.5, 0.3);
	this.cube.display();
	this.scene.popMatrix(),

	this.scene.pushMatrix();
	this.scene.translate((2.5)-0.15, 0, (1.5)-0.15);
	this.scene.scale(0.3, 3.5, 0.3);
	this.cube.display();
	this.scene.popMatrix();

	this.scene.pushMatrix();
	this.scene.translate(0, 1.9, 0);
	this.scene.scale(5.0, 0.3, 3.0);
	this.cube.display();
	this.scene.popMatrix();
	
	this.scene.popMatrix();

};