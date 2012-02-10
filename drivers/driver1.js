
function init() {

    if(!core.me) {
        core.log("not logged: "+core.me);
        return;
    }
    
    
    core.log("Logged as: "+core.me.login);
    
    
    var ships = core.me.getShips();

    if(ships.size() > 0) {
        
        var ship = ships.get(0);

        var leftEngine = ship.getComponentByName("mainLeftPropeller").getCapacityByName("linearEngine");
        var rightEngine = ship.getComponentByName("mainRightPropeller").getCapacityByName("linearEngine");
        var kernel = ship.getComponentByName("kernel");
        
        core.log("leftEngine.maxThrust "+leftEngine.getMaxThrust());
        core.log("rightEngine.maxThrust "+rightEngine.getMaxThrust());
        
        var maxThrust = Math.min(leftEngine.getMaxThrust(), rightEngine.getMaxThrust())
        var minThrust = Math.min( -leftEngine.getMinThrust(), -rightEngine.getMinThrust())
        
        var maxRotationThrust = Math.min(minThrust, maxThrust)
        
        var orderAccelerate = false;
        var orderBreak = false;
        var orderTurnLeft = false;
        var orderTurnRight = false;
        var baseThrust = 0;
        
        core.log("maxThrust "+maxThrust);        
        // Add key handler
        core.onKeyPressed = function (keyCode, char) {
            switch(keyCode) {
                case KEY_UP:
                    core.log("press up");
                    orderAccelerate = true;
                    break;
                case KEY_DOWN:
                    orderBreak = true;
                    core.log("press down");
                    //leftEngine.targetThrust = -minThrust;
                    //rightEngine.targetThrust = -minThrust;
                    break;
                case KEY_LEFT:
                    orderTurnLeft = true;
                    core.log("press left");
                    //leftEngine.targetThrust = -maxRotationThrust;
                    //rightEngine.targetThrust = maxRotationThrust;
                    break;
                case KEY_RIGHT:
                    orderTurnRight = true;
                    core.log("press right");
                    //leftEngine.targetThrust = maxRotationThrust;
                    //rightEngine.targetThrust = -maxRotationThrust;

                    break;
                case KEY_SPACE:
                    core.log("press space");
                    //leftEngine.targetThrust = 0;
                    //rightEngine.targetThrust = 0;
                    break;
                case KEY_PLUS:
                    baseThrust += 10;
                    if(baseThrust > 100) {
                        baseThrust = 100;
                    }
                    break;
                case KEY_MINUS:
                    baseThrust -= 10;
                    if(baseThrust < -100) {
                        baseThrust = -100;
                    }
                    break;
                default:
                    core.log("pressed undefined key: '"+keyCode+"' / '"+char+"'");
            }   
        }

        core.onKeyReleased = function (keyCode, char) {
            switch(keyCode) {
                case  KEY_UP:
                    core.log("released up");
                    orderAccelerate = false;
                    break;
                case  KEY_DOWN:
                    orderBreak = false;
                    core.log("released down");
                    break;
                case  KEY_LEFT:
                    orderTurnLeft = false;
                    core.log("released left");
                    break;
                case  KEY_RIGHT:
                    orderTurnRight = false;
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
    
    
    core.onFrame = function(time) {
        
        //core.log("js frame: "+orderAccelerate);
        
        var speedVector = kernel.getLinearSpeed();
        var speed = speedVector.length();
        
        //core.log("kernel speed vector: x="+speedVector.getX()+" y="+speedVector.getY()+" z="+speedVector.getZ());
        //core.log("kernel speed: "+speed);
        
        var leftThrustTarget = 0;
        var rightThrustTarget = 0;
        
        if(orderAccelerate) {
            if(orderTurnLeft) {
                leftThrustTarget = 0;
                rightThrustTarget = maxThrust;
            } else if(orderTurnRight) {
                leftThrustTarget = maxThrust;
                rightThrustTarget = 0;
            } else {
                leftThrustTarget = maxThrust;
                rightThrustTarget = maxThrust;
            }
        } else if(orderBreak) {
            leftThrustTarget = -minThrust;
            rightThrustTarget = -minThrust;
        } else if(orderTurnLeft) {
            leftThrustTarget = -maxRotationThrust;
            rightThrustTarget = maxRotationThrust;
        } else if(orderTurnRight) {
            leftThrustTarget = maxRotationThrust;
            rightThrustTarget = -maxRotationThrust;
        } else {
            leftThrustTarget = 0;
            rightThrustTarget = 0;
        }
        
        if(baseThrust > 0) {
            leftThrustTarget += (baseThrust * maxThrust) / 100.0;
            rightThrustTarget += (baseThrust * maxThrust) / 100.0;
        } else {
            leftThrustTarget += (baseThrust * minThrust) / 100.0;
            rightThrustTarget += (baseThrust * minThrust) / 100.0;
        }
        
        
        leftEngine.targetThrust = leftThrustTarget;
        rightEngine.targetThrust = rightThrustTarget;
        
    }
}

init();
