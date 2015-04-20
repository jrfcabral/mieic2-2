/**
 * MyCylinder
 * @constructor
 */
 function MyCylinder(scene, slices, stacks, xmult, zmult) {
 	CGFobject.call(this,scene);
	
	this.slices=slices;
	this.stacks=stacks+1;
	this.xmult = xmult;
	this.zmult = zmult;
 	this.initBuffers();
 };

 MyCylinder.prototype = Object.create(CGFobject.prototype);
 MyCylinder.prototype.constructor = MyCylinder;

 MyCylinder.prototype.initBuffers = function() {
 	

 	this.vertices = [];
 	this.indices = [];
 	this.normals = [];
 	var ang = 2*Math.PI/this.slices;


 	for(var i = 0; i < this.slices; i++){
 		for(var j = 0; j < this.stacks; j++){
 			var x = Math.cos(ang*i);
 			var y = Math.sin(ang*i);
 			this.vertices.push(x*this.xmult, y*this.xmult, j*this.zmult);

 			this.normals.push(x*this.xmult, y*this.xmult, 0);
 		}
 	}
 
	for(var i = 0;i < this.stacks-1;i++){
		for(var j = 0; j < this.slices-1;j++){
			this.indices.push((j+1)*this.stacks+i, j*this.stacks + (i+1), j*this.stacks+i);
			this.indices.push((j+1)*this.stacks+i, (j+1)*this.stacks+(i+1),  j*this.stacks + (i+1));
		}
		this.indices.push((this.stacks*this.slices - this.stacks)+(i+1), (this.stacks*this.slices - this.stacks)+i , i);
		this.indices.push(i, i+1, (this.stacks*this.slices - this.stacks)+(i+1));
	}

 	this.primitiveType = this.scene.gl.TRIANGLES;
 	this.initGLBuffers();
 };