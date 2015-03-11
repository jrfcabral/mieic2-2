function myTable(scene) {
	CGFobject.call(this,scene);

	//this.initBuffers();
	this.cube = new MyUnitCubeQuad(this.scene);
	this.materialTop = new CGFappearance(this.scene);
	this.materialLegs = new CGFappearance(this.scene);

	
};

myTable.prototype = Object.create(CGFobject.prototype);
myTable.prototype.constructor=myTable;

myTable.prototype.display = function(){

	//madeira
	this.materialTop.setShininess(20);
	this.materialTop.setDiffuse(0.5,0.2,0,1);
	this.materialTop.setAmbient(1,0.3,0,1);
	this.materialTop.setSpecular(0.05,0.025,0,1);

	//metal
	this.materialLegs.setShininess(200);
	this.materialLegs.setDiffuse(0.3,0.3,0.3,1);
	this.materialLegs.setAmbient(0.1,0.1,0.1,1);
	this.materialLegs.setSpecular(1,1,1,1);


	this.scene.pushMatrix();
	this.scene.translate(0, 1.8, 0);
	

	this.scene.pushMatrix();
	this.materialLegs.apply();
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
	this.materialTop.apply();	
	this.scene.translate(0, 1.9, 0);
	this.scene.scale(5.0, 0.3, 3.0);
	this.cube.display();
	this.scene.popMatrix();

	this.scene.popMatrix();
};