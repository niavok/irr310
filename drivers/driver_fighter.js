
function init() {

    if(!core.me) {
        core.log("not logged: "+core.me);
        return;
    }
    
    // GUI
    var cursorCenter = core.gui.createRectangle();
    cursorCenter.setPosition((new Vec2(640-1,512-1)));
                    cursorCenter.setSize(new Vec2(2,2));
                    cursorCenter.setColor(new Color(0,50,0));
    
    var cursorTop = core.gui.createRectangle();
    cursorTop.setPosition((new Vec2(640,512+10)));
                    cursorTop.setSize(new Vec2(0,30));
                    cursorTop.setColor(new Color(0,50,0));
    var cursorBottom = core.gui.createRectangle();
    cursorBottom.setPosition((new Vec2(640,512-40)));
                    cursorBottom.setSize(new Vec2(0,30));
                    cursorBottom.setColor(new Color(0,50,0));
    var cursorLeft = core.gui.createRectangle();
    cursorLeft.setPosition((new Vec2(640-40,512)));
                    cursorLeft.setSize(new Vec2(30,0));
                    cursorLeft.setColor(new Color(0,50,0));
    var cursorRight = core.gui.createRectangle();
    cursorRight.setPosition((new Vec2(640+10,512)));
                    cursorRight.setSize(new Vec2(30,0));
                    cursorRight.setColor(new Color(0,50,0));
    
    
    core.log("Logged as: "+core.me.login);
    
    
    var ships = core.me.getShips();

    if(ships.size() > 0) {
        
        var ship = ships.get(0);

        var leftEngine = ship.getComponentByName("leftReactor").getCapacityByName("linearEngine");
        var rightEngine = ship.getComponentByName("rightReactor").getCapacityByName("linearEngine");
        var topEngine = ship.getComponentByName("topReactor").getCapacityByName("linearEngine");
        var bottomEngine = ship.getComponentByName("bottomReactor").getCapacityByName("linearEngine");
        var gun = ship.getComponentByName("gun").getCapacityByName("gun");
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
        var orderTurnUp = false;
        var orderTurnDown = false;
        var useMouseController = false;

        var baseThrust = 0;
        
        var deadZone = null;
        var controlZone = null;
        
        core.log("maxThrust "+maxThrust);        
        // Add key handler
        core.onKeyPressed = function (keyCode, char) {
            switch(keyCode) {
                case KEY_UP:
                    core.log("press up");
                    orderTurnUp = true;
                    break;
                case KEY_DOWN:
                    orderTurnDown = true;
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
                    useMouseController = true;
                    mouseControlleurOrigin = core.mouse.getPosition();
                    deadZone = core.gui.createRectangle();
                    deadZone.setPosition(mouseControlleurOrigin.minus(new Vec2(10,10)));
                    deadZone.setSize(new Vec2(20,20));
                    deadZone.setColor(new Color(50,0,0));
                    controlZone = core.gui.createRectangle();
                    controlZone.setPosition(mouseControlleurOrigin.minus(new Vec2(150,150)));
                    controlZone.setSize(new Vec2(300,300));
                    controlZone.setColor(new Color(50,0,0));
                    
                    //core.log("press space");
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
                    orderTurnUp = false;
                    break;
                case  KEY_DOWN:
                    orderTurnDown = false;
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
                    useMouseController = false;
                    core.gui.destroyRectangle(controlZone);
                    core.gui.destroyRectangle(deadZone);
                    //core.log("released space");
                    break;
                default:
                    core.log("released undefined key: '"+keyCode+"' / '"+char+"'");
            }   
        }
        
        core.onMouseEvent = function(action, button, x, y) {
            switch(action) {
                case MOUSE_PRESSED:
                    core.log("mouse pressed");
                    if(useMouseController) {
                        if(button == 1) {
                            gun.fire(true);
                        }
                    }
                    break;
                    
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
        var topThrustTarget = 0;
        var bottomThrustTarget = 0;
        
        if(orderTurnUp) {
            topThrustTarget = -maxRotationThrust;
            bottomThrustTarget = maxRotationThrust;
        } else if(orderTurnDown) {
            topThrustTarget = maxRotationThrust;
            bottomThrustTarget = -maxRotationThrust;
        }
        
        
        
        
        if (useMouseController) {
            //core.log("useMouseController");
            var deadZoneRadius = 10;
            var controlRadius = 150.0;
            var mousePosition = core.mouse.getPosition();


            var diffX = (mouseControlleurOrigin.getX() - mousePosition.getX());
            var diffY = (mouseControlleurOrigin.getY() - mousePosition.getY());
            
            if(diffY > controlRadius) {
                diffY = controlRadius;
            }
            if(diffX > controlRadius) {
                diffX = controlRadius;
            }
            

            //var diffVert = diffX * Math.cos(Math.PI / 4.0) + diffY * Math.sin(Math.PI / 4.0);
            //var diffHoriz = -1 * diffX * Math.sin(Math.PI / 4.0) + diffY * Math.cos(Math.PI / 4.0);
            var diffVert = diffX;
            var diffHoriz = diffY;

            if(Math.abs(diffVert) < deadZoneRadius) {
                leftEngine.targetThrust = 0;
                rightEngine.targetThrust = 0;
            } else {
                var move =  (diffVert) / controlRadius;
                
                leftThrustTarget = - move * maxRotationThrust;
                rightThrustTarget = move * maxRotationThrust;
            }
            
            if(Math.abs(diffHoriz) < deadZoneRadius) {
                leftEngine.targetThrust = 0;
                rightEngine.targetThrust = 0;
            } else {
                var move =  (diffHoriz) / controlRadius;
                
                topThrustTarget = move * maxRotationThrust;
                bottomThrustTarget = - move * maxRotationThrust;
            }
                
        }
        
        if(orderTurnLeft) {
            leftThrustTarget = -maxRotationThrust;
            rightThrustTarget = maxRotationThrust;
        } else if(orderTurnRight) {
            leftThrustTarget = maxRotationThrust;
            rightThrustTarget = -maxRotationThrust;
        }
        
        if(baseThrust > 0) {
            leftThrustTarget += (baseThrust * maxThrust) / 100.0;
            rightThrustTarget += (baseThrust * maxThrust) / 100.0;
            topThrustTarget += (baseThrust * maxThrust) / 100.0;
            bottomThrustTarget += (baseThrust * maxThrust) / 100.0;
        } else {
            leftThrustTarget += (baseThrust * minThrust) / 100.0;
            rightThrustTarget += (baseThrust * minThrust) / 100.0;
            topThrustTarget += (baseThrust * minThrust) / 100.0;
            bottomThrustTarget += (baseThrust * minThrust) / 100.0;

        }
        
        leftEngine.targetThrust = leftThrustTarget;
        rightEngine.targetThrust = rightThrustTarget;
        topEngine.targetThrust = topThrustTarget;
        bottomEngine.targetThrust = bottomThrustTarget;


        
    }
}

init();
