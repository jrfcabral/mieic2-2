/**
 * MyRobot
 * @param gl {WebGLRenderingContext}
 * @constructor
 */
function MyRobot(scene, minS, maxS, minT, maxT) {
	CGFobject.call(this,scene);
	
	this.minS = minS || 0;
	this.minT = minT || 0;
	this.maxT = maxT || 1;
	this.maxS = maxS || 1;

	this.angle = 0;
	this.posX = 8;
	this.posZ = 8; 
	
	this.initBuffers();
};

MyRobot.prototype = Object.create(CGFobject.prototype);
MyRobot.prototype.constructor=MyRobot;

MyRobot.prototype.initBuffers = function () {
this.vertices = [
           0.5, -0.3, 0,
            -0.5, -0.3, 0,
            0, 0.3, 2,
            0.5, -0.3, 0,
            -0.5, -0.3, 0,
            0, 0.3, 2
			];
			
	this.indices = [
            0, 1, 2,
            5, 4, 3

        ];

    this.normals = [
    0,0,1,
    0,0,1,
    0,0,1,
    0, 0, -1,
    0, 0, -1,
    0, 0, -1
    ];

    /*this.texCoords = [
    this.minS,this.maxT,
    this.maxS,this.maxT,
    this.minS, this.minT,
    this.maxS,this.minT
    ];*/
		
	this.primitiveType=this.scene.gl.TRIANGLES;
	this.initGLBuffers();
};

MyRobot.prototype.setAngle = function(angle){
	this.angle = angle;
}

MyRobot.prototype.setPod = function(x, y){
	this.posX = x;
	this.posY = y;
}