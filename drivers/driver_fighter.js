
var gui;
var engine;

function driver() {

    if(!core.me) {
        core.log("not logged: "+core.me);
        return;
    }
    
    
    var theoricalMaxSpeed = 64;
    var speedTarget = 0;
    
    
    // GUI
    gui = new Gui();
    engine = new Engine();
    
    
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
                    if(!useMouseController) {
                    useMouseController = true;
                    mouseControlleurOrigin = core.mouse.getPosition();
                    deadZone = core.gui.createRectangle();
                    deadZone.setPosition(mouseControlleurOrigin.minus(new Vec2(10,10)));
                    deadZone.setSize(new Vec2(20,20));
                    deadZone.setBorderColor(new Color(0.0,0,0.3));
                    controlZone = core.gui.createRectangle();
                    controlZone.setPosition(mouseControlleurOrigin.minus(new Vec2(150,150)));
                    controlZone.setSize(new Vec2(300,300));
                    controlZone.setBorderColor(new Color(0.0,0,0.3));
                    } else {
                        useMouseController = false;
                        core.gui.destroyComponent(controlZone);
                        core.gui.destroyComponent(deadZone);
                    }
                    
                    break;
                case KEY_PLUS:
                    baseThrust += 10;
                    if(baseThrust > 100) {
                        baseThrust = 100;
                    }
                    break;
                case KEY_Z:
                    gui.enableThrustController();
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
                case KEY_Z:
                    gui.disableThrustController();
                    break;
                case KEY_SPACE:
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
        
        gui.update(time);
        
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
        
        
        //bottomEngine.targetThrust = 2000;
        
        
        
        
        
       engine.update(time);
        
    }
    
    function Engine() {
    
        var leftThrustTarget = 0;
        var rightThrustTarget = 0;
        var topThrustTarget = 0;
        var bottomThrustTarget = 0;
        var lastSpeedTarget = 0;
        var lastSpeed = 0;
        var initiaSpeed = 0;
        var lastDeltaSpeed = 0;
        var lastTime = 0;
        var allowPower = 1;
        var maxOptimalPower = 0;
        var optimalPower = 0;
        var minOptimalPower = 0;
        var phase = 0;
        
        this.update = function(time) {

            var currentSpeed = kernel.getLinearSpeed().dot( new Vect3(0,1,0).rotate(kernel.getTransform()));        
            var deltaTime = time - lastTime;
            var deltaSpeed = (currentSpeed - lastSpeed);
            var deltaAcc = (deltaSpeed - lastDeltaSpeed);
            
            if(Math.abs(speedTarget - currentSpeed) > 0.1) {
                

                var maxHorizontalThrust = Math.min(leftEngine.getMaxThrust(), rightEngine.getMaxThrust()) 
                var minHorizontalThrust = Math.min( -leftEngine.getMinThrust(), -rightEngine.getMinThrust())
                var maxVerticalThrust = Math.min(topEngine.getMaxThrust(), bottomEngine.getMaxThrust())
                var minVerticalThrust = Math.min( -topEngine.getMinThrust(), -bottomEngine.getMinThrust())

                if(lastSpeedTarget != speedTarget) {
                    //Thrust target changed, full power allowed
                    allowPower = 1;
                    phase = 0;
                    core.log("engine go to phase 0");
                    
                    maxOptimalPower = 1;
                    minOptimalPower = -1;
                    optimalPower = speedTarget / theoricalMaxSpeed;
                    initiaSpeed = currentSpeed;
                }

                if(phase == 0) {
                    if (speedTarget > currentSpeed) {
                        leftThrustTarget = maxHorizontalThrust;
                        rightThrustTarget = maxHorizontalThrust;
                        topThrustTarget = maxVerticalThrust;
                        bottomThrustTarget = maxVerticalThrust;
                        lastTooSlow = true;
                        lastTooFast = false;
                    } else if (speedTarget < currentSpeed){
                        leftThrustTarget = - minHorizontalThrust;
                        rightThrustTarget = - minHorizontalThrust;
                        topThrustTarget = - minVerticalThrust;
                        bottomThrustTarget = - minVerticalThrust;
                        lastTooSlow = false;
                        lastTooFast = true;
                    } else {
                        leftThrustTarget = 0;
                        rightThrustTarget = 0;
                        topThrustTarget = 0;
                        bottomThrustTarget = 0;
                    }
                    phase = 1;
                    core.log("engine go to phase 1");
                } else if(phase == 1) {
                    core.log("ds="+deltaSpeed);
                    if (speedTarget > currentSpeed && lastTooFast && deltaSpeed < -0.001) {
                        // Too slow
                        leftThrustTarget = maxHorizontalThrust * optimalPower;
                        rightThrustTarget = maxHorizontalThrust * optimalPower;
                        topThrustTarget = maxVerticalThrust * optimalPower;
                        bottomThrustTarget = maxVerticalThrust * optimalPower;
                        phase = 2;
                        core.log("engine go to phase 2 too slow");
                    } else if (speedTarget < currentSpeed && lastTooSlow && deltaSpeed > 0.001) {
                        // Too fast
                        leftThrustTarget = maxHorizontalThrust * optimalPower;
                        rightThrustTarget = maxHorizontalThrust * optimalPower;
                        topThrustTarget = maxVerticalThrust * optimalPower;
                        bottomThrustTarget = maxVerticalThrust * optimalPower;
                        phase = 2;
                        core.log("engine go to phase 2 too fast");
                    }
                } else if(phase == 2) {
                }
                
                
            }


            leftEngine.targetThrust = leftThrustTarget;
            rightEngine.targetThrust = rightThrustTarget;
            topEngine.targetThrust = topThrustTarget;
            bottomEngine.targetThrust = bottomThrustTarget;

            lastSpeed = currentSpeed;
            lastSpeedTarget = speedTarget;
            lastTime = time;
            lastDeltaSpeed = deltaSpeed;
        }
    }
    
    
    function Gui() {
    
        var clockIndicator;
        var thrustControlleurEnabled = false;
        
        this.init = function() {

            this.screenSize = core.gui.getViewportSize()
            
            //Logo
            var logo1 = core.gui.createLabel();
            logo1.setText("IRR");
            logo1.setPosition(new Vec2(10,10));
            logo1.setFontStyle("bold",24);
            logo1.setColor(new Color(0.39,0,0));
        
            var logo2 = core.gui.createLabel();
            logo2.setText("310");
            logo2.setPosition(new Vec2(50,10));
            logo2.setColor(new Color(0,0,0));
            logo2.setFontStyle("bold",24);
        
            var cursorCenter = core.gui.createRectangle();
            cursorCenter.setPosition((new Vec2(640-1,512-1)));
                            cursorCenter.setSize(new Vec2(2,2));
                            cursorCenter.setBorderColor(new Color(0,0.8,0));
            
            var cursorTop = core.gui.createRectangle();
                            cursorTop.setPosition((new Vec2(640,512+10)));
                            cursorTop.setSize(new Vec2(0,30));
                            cursorTop.setBorderColor(new Color(0.8,0.0,0));
            var cursorBottom = core.gui.createRectangle();
                            cursorBottom.setPosition((new Vec2(640,512-40)));
                            cursorBottom.setSize(new Vec2(0,30));
                            cursorBottom.setBorderColor(new Color(0.8,0.0,0));
            var cursorLeft = core.gui.createRectangle();
                            cursorLeft.setPosition((new Vec2(640-40,512)));
                            cursorLeft.setSize(new Vec2(30,0));
                            cursorLeft.setBorderColor(new Color(0.8,0.0,0));
            var cursorRight = core.gui.createRectangle();
                            cursorRight.setPosition((new Vec2(640+10,512)));
                            cursorRight.setSize(new Vec2(30,0));
                            cursorRight.setBorderColor(new Color(0.8,0.0,0));

            // Indicators
            var indicatorBorder = core.gui.createRectangle();
            indicatorBorder.setPosition((new Vec2(120,10)));
            indicatorBorder.setSize(new Vec2(300,30));
            indicatorBorder.setFillColor(new Color(0.9,0.9,0.9, 0.5));
            indicatorBorder.setBorderColor(new Color(0.3,0.0,0.0));
            
            this.clockIndicator = core.gui.createLabel();
            this.clockIndicator.setText("Time: --");
            this.clockIndicator.setPosition(new Vec2(128,17));
            this.clockIndicator.setColor(new Color(0.0,0.0,0.0));
            
            
            this.fpsIndicator = core.gui.createLabel();
            this.fpsIndicator.setText("-- fps");
            this.fpsIndicator.setPosition(new Vec2(235,17));
            this.fpsIndicator.setColor(new Color(0.0,0.0,0.0));
            
            var resolutionIndicator = core.gui.createLabel();
            resolutionIndicator.setText(""+this.screenSize.getX()+"x"+this.screenSize.getY()+" px");
            resolutionIndicator.setPosition(new Vec2(300,17));
            resolutionIndicator.setColor(new Color(0.0,0.0,0.0));
            
            // Navigation indicators
            
            this.speedIndicator = core.gui.createLabel();
            this.speedIndicator.setText("");
            this.speedIndicator.setPosition(new Vec2(this.screenSize.getX() - 300,200));
            this.speedIndicator.setFontStyle("bold",55);
            this.speedIndicator.setColor(new Color(0.0,0.0,0.0));
            
            var speedIndicatorUnit = core.gui.createLabel();
            speedIndicatorUnit.setText("km/h");
            speedIndicatorUnit.setPosition(new Vec2(this.screenSize.getX() - 230,206));
            speedIndicatorUnit.setFontStyle("bold",25);
            speedIndicatorUnit.setColor(new Color(0.0,0.0,0.0));
            
            this.xPositionIndicator = core.gui.createLabel();
            this.xPositionIndicator.setText("x= -- m");
            this.xPositionIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 200 ));
            this.xPositionIndicator.setFontStyle("",16);
            this.xPositionIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.yPositionIndicator = core.gui.createLabel();
            this.yPositionIndicator.setText("y= -- m");
            this.yPositionIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 220 ));
            this.yPositionIndicator.setFontStyle("",16);
            this.yPositionIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.zPositionIndicator = core.gui.createLabel();
            this.zPositionIndicator.setText("z= -- m");
            this.zPositionIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 240 ));
            this.zPositionIndicator.setFontStyle("",16);
            this.zPositionIndicator.setColor(new Color(0.0,0.0,0.0));
            
            
            this.xRotationSpeedIndicator = core.gui.createLabel();
            this.xRotationSpeedIndicator.setText("wx= -- ?");
            this.xRotationSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 260 ));
            this.xRotationSpeedIndicator.setFontStyle("",16);
            this.xRotationSpeedIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.yRotationSpeedIndicator = core.gui.createLabel();
            this.yRotationSpeedIndicator.setText("wy= -- ?");
            this.yRotationSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 280 ));
            this.yRotationSpeedIndicator.setFontStyle("",16);
            this.yRotationSpeedIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.zRotationSpeedIndicator = core.gui.createLabel();
            this.zRotationSpeedIndicator.setText("wz= -- ?");
            this.zRotationSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 300 ));
            this.zRotationSpeedIndicator.setFontStyle("",16);
            this.zRotationSpeedIndicator.setColor(new Color(0.0,0.0,0.0));

            this.xLinearSpeedIndicator = core.gui.createLabel();
            this.xLinearSpeedIndicator.setText("wz= -- ?");
            this.xLinearSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 320 ));
            this.xLinearSpeedIndicator.setFontStyle("",16);
            this.xLinearSpeedIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.yLinearSpeedIndicator = core.gui.createLabel();
            this.yLinearSpeedIndicator.setText("wz= -- ?");
            this.yLinearSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 340 ));
            this.yLinearSpeedIndicator.setFontStyle("",16);
            this.yLinearSpeedIndicator.setColor(new Color(0.0,0.0,0.0));
            

            this.zLinearSpeedIndicator = core.gui.createLabel();
            this.zLinearSpeedIndicator.setText("wz= -- ?");
            this.zLinearSpeedIndicator.setPosition(new Vec2(this.screenSize.getX() - 100,this.screenSize.getY() - 360 ));
            this.zLinearSpeedIndicator.setFontStyle("",16);
            this.zLinearSpeedIndicator.setColor(new Color(0.0,0.0,0.0));
            

            //Speed box
            this.speedBoxMaxSize = 200;
            this.speedCurrentBox = core.gui.createRectangle();
            this.speedCurrentBox.setPosition((new Vec2(this.screenSize.getX() - 120,200)));
            this.speedCurrentBox.setSize(new Vec2(30,0));
            this.speedCurrentBox.setFillColor(new Color(0.8,0.1,0.1));


            var speedTargetBox = core.gui.createRectangle();
            speedTargetBox.setPosition((new Vec2(this.screenSize.getX() - 120,200)));
            speedTargetBox.setSize(new Vec2(30,this.speedBoxMaxSize));
            speedTargetBox.setBorderColor(new Color(0.3,0.0,0));


            var deltaHeigth = this.speedBoxMaxSize * speedTarget / theoricalMaxSpeed;
            this.targetLine = core.gui.createLine();
            this.targetLine.setPosition((new Vec2(this.screenSize.getX() - 125,200 + deltaHeigth)));
            this.targetLine.setSize(new Vec2(40,0));
            this.targetLine.setColor(new Color(0.0,0.0,0.0));
            
            this.speedTargetIndicator = core.gui.createLabel();
            this.speedTargetIndicator.setText((speedTarget * 3.6).toFixed(0)+" km/s");
            this.speedTargetIndicator.setPosition(new Vec2(this.screenSize.getX() - 80,193 + deltaHeigth ));
            this.speedTargetIndicator.setFontStyle("bold",14);
            this.speedTargetIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.lastTime = 0;
        }
        
        this.update = function(time) {
            
            this.clockIndicator.setText("Time: "+time.toFixed(0)+" s");
            
            if(time - this.lastTime > 0.25) {
                
                // Fps
                this.fpsIndicator.setText(""+core.gui.getFps().toFixed(0)+" fps");
                
                
                // Speed
                var speed = (kernel.getLinearSpeed().dot(new Vect3(0,1,0).rotate(kernel.getTransform())) * 3.6).toFixed(0);

                if(speed == -0) {
                 speed = 0;
                }
                if(speed > 99 || speed < -9) {
                  this.speedIndicator.setPosition(new Vec2(this.screenSize.getX() - 335,200));
                } else if (speed > 9 || speed < 0) {
                    this.speedIndicator.setPosition(new Vec2(this.screenSize.getX() - 304,200));
                } else {
                    this.speedIndicator.setPosition(new Vec2(this.screenSize.getX() - 273,200));
                }
                this.speedIndicator.setText(speed);
                
                
               
                
                //Position
                var x = kernel.getTransform().getTranslation().getX().toFixed(1);
                this.xPositionIndicator.setText("x= "+x+" m");
                var y = kernel.getTransform().getTranslation().getY().toFixed(1);
                this.yPositionIndicator.setText("y= "+y+" m");
                var z = kernel.getTransform().getTranslation().getZ().toFixed(1);
                this.zPositionIndicator.setText("z= "+z+" m");
                
                //Rotation speed
                var x = kernel.getRotationSpeed().getX().toFixed(1);
                this.xRotationSpeedIndicator.setText("wx= "+x+" r/s");
                var y = kernel.getRotationSpeed().getY().toFixed(1);
                this.yRotationSpeedIndicator.setText("wy= "+y+" r/s");
                var z = kernel.getRotationSpeed().getZ().toFixed(1);
                this.zRotationSpeedIndicator.setText("wz= "+z+" r/s");
                
                //Translation speed
                var x = kernel.getLinearSpeed().getX().toFixed(1);
                this.xLinearSpeedIndicator.setText("vx= "+x+" m/s");
                var y = kernel.getLinearSpeed().getY().toFixed(1);
                this.yLinearSpeedIndicator.setText("vy= "+y+" m/s");
                var z = kernel.getLinearSpeed().getZ().toFixed(1);
                this.zLinearSpeedIndicator.setText("vz= "+z+" m/s");
                
                this.lastTime = time;
            }
            
            
            //Fast refresh 
            var speed = kernel.getLinearSpeed().dot(new Vect3(0,1,0).rotate(kernel.getTransform()));
            if(speed < 0) {
                speed = 0;
            }
            var speedBarSize = this.speedBoxMaxSize * speed / theoricalMaxSpeed;
            this.speedCurrentBox.setSize(new Vec2(30,speedBarSize));
                
            
            if(this.thrustControlleurEnabled) {
                var deltaMouse = this.initialYMousePosition - core.mouse.getPosition().getY();
                speedTarget = this.initialSpeedTarget - deltaMouse;
                
                if(speedTarget > theoricalMaxSpeed) {
                    this.deltaMouse -= (speedTarget - theoricalMaxSpeed)
                    this.initialSpeedTarget -= (speedTarget - theoricalMaxSpeed)
                    speedTarget = theoricalMaxSpeed;
                }
                
                if(speedTarget < 0) {
                    this.deltaMouse -= speedTarget;
                    this.initialSpeedTarget -= speedTarget;
                    speedTarget = 0;
                }
                
                var deltaHeigth = this.speedBoxMaxSize * speedTarget / theoricalMaxSpeed;
                this.targetLine.setPosition((new Vec2(this.screenSize.getX() - 125,200 + deltaHeigth)));
                this.speedTargetIndicator.setPosition(new Vec2(this.screenSize.getX() - 80, 193 + deltaHeigth ));
                this.speedTargetIndicator.setText((speedTarget * 3.6).toFixed(0)+" km/s");
            }
            
            
            
        }

        this.enableThrustController = function() {
            this.initialYMousePosition = core.mouse.getPosition().getY();
            this.initialSpeedTarget = speedTarget;
            this.thrustControlleurEnabled = true;
        }        
        
        this.disableThrustController = function() {
            this.thrustControlleurEnabled = false;
        }
        
        this.init();

    }
    
}




driver();
