
function init() {

    if(!core.me) {
        core.log("not logged: "+core.me);
        return;
    }
    
    
    core.log("Logged as: "+core.me.login);
    
    
    var ships = core.me.getShips();

    if(ships.len() > 0) {
        
        var ship = ships[0];

        var leftEngine = ship.getComponentByName("mainLeftPropeller").getCapacityByName("linearEngine");
        var rightEngine = ship.getComponentByName("mainRightPropeller").getCapacityByName("linearEngine");
        
        var maxThrust = Math.min(leftEngine.maxThrust, rightEngine.maxThrust)
        
        // Add key handler
        core.onKeyPressed = function (keyCode, char) {
            switch(keyCode) {
                case KEY_UP:
                    core.log("press up");
                    leftEngine.targetThrust = maxThrust;
                    rightEngine.targetThrust = maxThrust;
                    break;
                case KEY_DOWN:
                    core.log("press down");
                    leftEngine.targetThrust = 0;
                    rightEngine.targetThrust = 0;
                    break;
                case KEY_LEFT:
                    core.log("press left");
                    leftEngine.targetThrust = 0;
                    rightEngine.targetThrust = maxThrust;
                    break;
                case KEY_RIGHT:
                    core.log("press right");
                    leftEngine.targetThrust = maxThrust;
                    rightEngine.targetThrust = 0;

                    break;
                case KEY_SPACE:
                    core.log("press space");
                    break;
                default:
                    core.log("pressed undefined key: '"+keyCode+"' / '"+char+"'");
            }   
        }

        core.onKeyReleased = function (keyCode, char) {
            switch(keyCode) {
                case  KEY_UP:
                    core.log("released up");
                    break;
                case  KEY_DOWN:
                    core.log("released down");
                    break;
                case  KEY_LEFT:
                    core.log("released left");
                    break;
                case  KEY_RIGHT:
                    core.log("released right");
                    break;
                case KEY_SPACE:
                    core.log("released space");
                    break;
                default:
                    core.log("released undefined key: '"+keyCode+"' / '"+char+"'");
            }   
        }


    } else {
        core.log("no ships");
    }
}

init();
