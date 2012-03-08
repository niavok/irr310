
function CoreProxy() {

    this.coreProxy = new Core();

    this.log = function(log) {
        this.coreProxy.log(log);
    }
    
    this.onKeyPressed = function(keyCode, character) {
    
    }

    this.onKeyReleased = function(keyCode, character) {
    
    }

    this.onMouseEvent = function(action, button, x, y) {
    
    }
    
    this.onFrame = function(time) {
    
    }
    
    this.mouse = new Mouse();
    
    this.gui = new Gui();

}

var core = new CoreProxy();


function onKeyPressed(keyCode, character) {
    core.onKeyPressed(keyCode, character);
}

function onKeyReleased(keyCode, character) {
    core.onKeyReleased(keyCode, character);
}

function onMouseEvent(action, button, x, y) {
    core.onMouseEvent(action, button, x, y);
}


function onFrame(time) {
    core.onFrame(time);
}
