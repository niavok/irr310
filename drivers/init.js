
function CoreProxy() {

    this.coreProxy = new Core();

    this.log = function(log) {
        this.coreProxy.log(log);
    }

    this.onKeyPressed = function(keyCode, char) {
    
    }

    this.onKeyReleased = function(keyCode, char) {
    
    }

}

var core = new CoreProxy();


function onKeyPressed(keyCode, char) {
    core.onKeyPressed(keyCode, char);
}

function onKeyReleased(keyCode, char) {
    core.onKeyReleased(keyCode, char);
}
