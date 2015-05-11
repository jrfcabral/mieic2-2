//Represents a plane with a window hole
function windowWall(scene, nrDivs){
    CGFobject.call(this,scene);
    nrDivs = typeof nrDivs !== 'undefined' ? nrDivs : 10;

    this.materialLeftWall = new CGFappearance(this);
	this.materialLeftWall.setAmbient(0.2,0.03,0,1);
	this.materialLeftWall.setDiffuse(0.2,0.3,0.27,1);
	this.materialLeftWall.setSpecular(0.01,0.01,0.01,1);	
	this.materialLeftWall.setShininess(10);
	//this.materialLeftWall.loadTexture("../resources/images/window.png");
	//this.materialLeftWall.setTextureWrap("CLAMP_TO_EDGE","CLAMP_TO_EDGE");
	this.leftPortion = new Plane(scene, nrDivs);
	this.rightPortion = new Plane(scene,nrDivs);
	this.topPortion = new Plane(scene,nrDivs);
	this.bottomPortion = new Plane(scene,nrDivs);

}

windowWall.prototype = Object.create(CGFobject.prototype);
windowWall.prototype.constructor = windowWall;

windowWall.prototype.display = function() {
    this.scene.pushMatrix();
        this.scene.translate(-0.333,0,0);
        this.scene.scale(0.333,1,1);
        this.leftPortion.display();
    this.scene.popMatrix();
    
    this.scene.pushMatrix();
        this.scene.translate(0.333,0,0);
        this.scene.scale(0.333,1,1);
        this.rightPortion.display();
    this.scene.popMatrix();
    
    this.scene.pushMatrix();
        this.scene.translate(0,0.333,0);
        this.scene.scale(0.333,0.333,1);
        this.topPortion.display();
    this.scene.popMatrix();

    this.scene.pushMatrix();
        this.scene.translate(0,-0.333,0);
        this.scene.scale(0.333,0.333,1);
        this.bottomPortion.display();
    this.scene.popMatrix();
}