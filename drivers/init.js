
function CoreProxy() {

    this.coreProxy = new Core();

    this.log = function(log) {
        this.coreProxy.log(log);
    }
    
    this.onKeyPressed = function(keyCode, char) {
    
    }

    this.onKeyReleased = function(keyCode, char) {
    
    }
    
    this.onFrame = function(time) {
    
    }
    
    this.mouse = new Mouse();
    
    this.gui = new Gui();

}

var core = new CoreProxy();


function onKeyPressed(keyCode, char) {
    core.onKeyPressed(keyCode, char);
}

function onKeyReleased(keyCode, char) {
    core.onKeyReleased(keyCode, char);
}

function onFrame(time) {
    core.onFrame(time);
}
