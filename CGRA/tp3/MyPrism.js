/**
 * MyPrism
 * @constructor
 */
 function MyPrism(scene, slices, stacks) {
 	CGFobject.call(this,scene);
	
	this.slices=slices;
	this.stacks=stacks;

 	this.initBuffers();
 };

 MyPrism.prototype = Object.create(CGFobject.prototype);
 MyPrism.prototype.constructor = MyPrism;

 MyPrism.prototype.initBuffers = function() {
 	
 	


 	/*
 	* TODO:
 	* Replace the following lines in order to build a prism with a **single mesh**.
 	*
 	* How can the this.vertices, indices and normals arrays be defined to
 	* build a prism with varying number of slices and stacks?
 	*/

 	this.vertices = [];
 	this.indices = [];
 	this.normals = [];
 	var ang = 2*Math.PI/this.slices;

 	for(var i = 0; i < this.slices*this.stacks; i++){
 		var x = Math.cos(ang*i);
 		var y = Math.sin(ang*i); 		

 		this.vertices.push(x);
 		this.vertices.push(y);
 		this.vertices.push(Math.floor(i/this.slices));

 		this.vertices.push(x);
 		this.vertices.push(y);
 		this.vertices.push(Math.floor(i/this.slices)+1);

 		var x = Math.cos(ang*(i+1));
 		var y = Math.sin(ang*(i+1));

 		this.vertices.push(x);
 		this.vertices.push(y);
 		this.vertices.push(Math.floor(i/this.slices));

 		
 		this.vertices.push(x);
 		this.vertices.push(y);
 		this.vertices.push(Math.floor(i/this.slices)+1);

 		this.indices.push(i*4+0);
 		this.indices.push(i*4+2);
 		this.indices.push(i*4+3);

 		this.indices.push(i*4+3);
 		this.indices.push(i*4+1);
 		this.indices.push(i*4+0);
 		var face_ang = (ang*i)+ (ang/2);

 		for (var j = 0; j < 4; j++){
 			this.normals.push(Math.cos(face_ang));
 			this.normals.push(Math.sin(face_ang));
 			this.normals.push(0);
 		}


 	}

 	

 	

 	this.primitiveType = this.scene.gl.TRIANGLES;
 	this.initGLBuffers();
 };
