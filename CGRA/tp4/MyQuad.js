/**
 * MyQuad
 * @param gl {WebGLRenderingContext}
 * @constructor
 */
function MyQuad(scene, minS, maxS, minT, maxT) {
	CGFobject.call(this,scene);
	if (arguments.length < 2){
		this.minS = 0;
		this.maxS = 1;
		this.minT = 0;
		this.maxT = 1;}
	else{
		this.minS = minS;
		this.minT = minT;
		this.maxT = maxT;
		this.maxS = maxS;
	}

	this.initBuffers();
};

MyQuad.prototype = Object.create(CGFobject.prototype);
MyQuad.prototype.constructor=MyQuad;

MyQuad.prototype.initBuffers = function () {
this.vertices = [
           -0.5, -0.5, 0,
            0.5, -0.5, 0,
            -0.5, 0.5, 0,
            0.5, 0.5, 0,
			];
			
	this.indices = [
            0, 1, 2, 
			3, 2, 1,
        ];

    this.normals = [
    0,0,1,
    0,0,1,
    0,0,1,
    0,0,1

    ];

    this.texCoords = [
    this.minS,this.maxT,
    this.maxS,this.maxT,
    this.minS, this.minT,
    this.maxS,this.minT
    ];
		
	this.primitiveType=this.scene.gl.TRIANGLES;
	this.initGLBuffers();
};
