/**
 * MyInterface
 * @constructor
 */
 
 
function MyInterface() {
	//call CGFinterface constructor 
	CGFinterface.call(this);
};

MyInterface.prototype = Object.create(CGFinterface.prototype);
MyInterface.prototype.constructor = MyInterface;

/**
 * init
 * @param {CGFapplication} application
 */
MyInterface.prototype.init = function(application) {
	// call CGFinterface init
	CGFinterface.prototype.init.call(this, application);
	
	// init GUI. For more information on the methods, check:
	//  http://workshop.chromeexperiments.com/examples/gui
	
	this.gui = new dat.GUI();

	
	this.gui.add(this.scene, 'RobotTexture', [ 'metal', 'fabric', 'carbon', 'Android' ] );
	
	// add a slider
	// must be a numeric variable of the scene, initialized in scene.init e.g.
	// this.speed=3;
	// min and max values can be specified as parameters
	
	this.gui.add(this.scene, 'speed', -5, 5);


	var group2 = this.gui.addFolder("Lights");
	group2.add(this.scene, 'Light0');
	group2.add(this.scene, 'Light1');
	group2.add(this.scene, 'Light2');
	group2.add(this.scene, 'Light3');
	
	this.gui.add(this.scene, 'ToggleClock'); 
	return true;
};

/**
 * processKeyboard
 * @param event {Event}
 */
MyInterface.prototype.processKeyboard = function(event) {
	// call CGFinterface default code (omit if you want to override)
	CGFinterface.prototype.processKeyboard.call(this,event);
	
	// Check key codes e.g. here: http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
	// or use String.fromCharCode(event.keyCode) to compare chars
	
	// for better cross-browser support, you may also check suggestions on using event.which in http://www.w3schools.com/jsref/event_key_keycode.asp
	switch (event.keyCode)
	{
		case (97):	// only works for capital 'A', as it is
			this.scene.bot.turn(this.scene.speed * 17);
			break;
		case(119):
			this.scene.bot.moveForward(this.scene.speed);			
			break;
		case(115):
			this.scene.bot.moveBackward(this.scene.speed);
			break;
		case(100):
			this.scene.bot.turn(-this.scene.speed*17);
			break;
		case(111):
			this.scene.bot.waving = 1;
			break;
	};
};
