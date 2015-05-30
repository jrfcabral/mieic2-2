/**
 * MyCylinder
 * @constructor
 */
 function MyCylinder(scene, slices, stacks, minS, maxS, minT, maxT) {
 	CGFobject.call(this,scene);
	
	this.minS = minS || 0;
	this.minT = minT || 0;
	this.maxT = maxT || 1;
	this.maxS = maxS || 1;
	
	this.slices=slices;
	this.stacks=stacks+1;
	
 	this.initBuffers();
 };

 MyCylinder.prototype = Object.create(CGFobject.prototype);
 MyCylinder.prototype.constructor = MyCylinder;

 MyCylinder.prototype.initBuffers = function() {
 	

 	this.vertices = [];
 	this.indices = [];
 	this.normals = [];
 	this.texCoords = [];
 	var ang = 2*Math.PI/this.slices;


 	for(var i = 0; i <= this.slices; i++){
 		for(var j = 0; j < this.stacks; j++){
 			var x = Math.cos(ang*i);
 			var y = Math.sin(ang*i);
 			this.vertices.push(x, y, j);

 			this.normals.push(x, y, 0); 
 			this.texCoords.push(i/this.slices,j/this.stacks);
 		}
 	}
 	
	

	for(var i = 0;i < this.stacks-1;i++){
		for(var j = 0; j <= this.slices-1;j++){
			this.indices.push((j+1)*this.stacks+i, j*this.stacks + (i+1), j*this.stacks+i);
			this.indices.push((j+1)*this.stacks+i, (j+1)*this.stacks+(i+1),  j*this.stacks + (i+1));
		}
		//this.indices.push((this.stacks*this.slices - this.stacks)+(i+1), (this.stacks*this.slices - this.stacks)+i , i);
		//this.indices.push(i, i+1, (this.stacks*this.slices - this.stacks)+(i+1));
	}
	

 	this.primitiveType = this.scene.gl.TRIANGLES;
 	this.initGLBuffers();
 };