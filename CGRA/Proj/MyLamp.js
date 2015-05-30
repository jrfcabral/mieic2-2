/**
 * MyLamp
 * @constructor
 */
 function MyLamp(scene, slices, stacks) {
 	CGFobject.call(this,scene);
	
	this.slices=slices;
	this.stacks=stacks;

 	this.initBuffers();
 };

 MyLamp.prototype = Object.create(CGFobject.prototype);
 MyLamp.prototype.constructor = MyLamp;

 MyLamp.prototype.initBuffers = function() {
 	

 	this.vertices = [];
 	this.indices = [];
 	this.normals = [];
 	this.texCoords = [];
 	var ang = 2*Math.PI/this.slices;
 	var ang2 = 2*Math.PI/this.stacks;


 	for(var i = 0; i <= this.slices; i++){
 		for(var j = 0; j < this.stacks; j++){
 			var y = Math.cos(ang2*j) - 1.0//2.0*Math.PI*j/this.stacks - 1.0;
 			var r = Math.sqrt(1.0-y^2);
 			var x = Math.sin(ang2*j)*Math.sin(ang*i);
 			var z = Math.sin(ang2*j)*Math.cos(ang*i);
 			this.vertices.push(r*x, r*y, r*z);

 			this.normals.push(x, y, z);
 			this.texCoords.push(i/this.slices, j/this.stacks);
 		}
 	}
	for(var i = 0;i < (this.stacks-1)/4;i++){
		for(var j = 0; j <= this.slices-1;j++){
			this.indices.push(j*this.stacks+i, j*this.stacks + (i+1), (j+1)*this.stacks+i);
			this.indices.push(j*this.stacks + (i+1), (j+1)*this.stacks+(i+1), (j+1)*this.stacks+i );
		}
		//this.indices.push(i, (this.stacks*this.slices - this.stacks)+i ,(this.stacks*this.slices - this.stacks)+(i+1));
		//this.indices.push((this.stacks*this.slices - this.stacks)+(i+1) , i+1, i);
	}

 	this.primitiveType = this.scene.gl.TRIANGLES;
 	this.initGLBuffers();
 };