
var gui;
var engine;

function driver() {

    if(!core.me) {
        return;
    }
    
    
    
    var speedTarget = 0;
    var maxRotationSpeed = 2;
    var rotationSpeedTarget = new Vec3(0,0,0.0);
    
    
    // GUI
    
    var ships = core.me.getShips();
    var ship;

    if(ships.size() > 0) {
        
        ship = ships.get(0);

        var leftEngine = ship.getComponentByName("leftReactor").getCapacitiesByClass("LinearEngineCapacity").get(0);
        var rightEngine = ship.getComponentByName("rightReactor").getCapacitiesByClass("LinearEngineCapacity").get(0);
        var topEngine = ship.getComponentByName("topReactor").getCapacitiesByClass("LinearEngineCapacity").get(0);
        var bottomEngine = ship.getComponentByName("bottomReactor").getCapacitiesByClass("LinearEngineCapacity").get(0);
        /*var gun = ship.getComponentByName("weapon.gun").getCapacityByName("gun");*/
        var kernel = ship.getComponentByName("kernel");
        
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
        
        // Add key handler
        core.onKeyPressed = function (keyCode, character) {
            gui.onKeyPressed(keyCode, character);
        }

        core.onKeyReleased = function (keyCode, character) {
            gui.onKeyReleased(keyCode, character);
        }
        
        core.onMouseEvent = function(action, button, x, y) {
            gui.onMouseEvent(action, button, x, y);
        }


    } else {
        core.log("no ships");
    }
    
    gui = new Gui();
    engine = new Engine();
    
    
    
    core.onFrame = function(time) {
        
        gui.update(time);
        engine.update(time);
        
    }
    
    function Engine() {
    
        this.leftThrustTarget = 0;
        this.rightThrustTarget = 0;
        this.topThrustTarget = 0;
        this.bottomThrustTarget = 0;
        this.lastSpeedTarget = 0;
        this.lastSpeed = 0;
        this.initiaSpeed = 0;
        this.lastDeltaSpeed = 0;
        this.lastTime = 0;
        this.allowPower = 1;
        this.maxOptimalPower = 0;
        this.optimalPower = 0;
        this.minOptimalPower = 0;
        this.phase = 0;

        
        this.update = function(time) {

            this.theoricalMaxSpeed = ship.getTheoricalMaxSpeed();
            var currentSpeed = kernel.getLinearSpeed().dot( new Vec3(0,1,0).rotate(kernel.getTransform()));        
            var deltaTime = time - this.lastTime;
            var deltaSpeed = (currentSpeed - this.lastSpeed);
            var deltaAcc = (deltaSpeed - this.lastDeltaSpeed);
            var maxThrust = Math.min(leftEngine.getMaxThrust(), rightEngine.getMaxThrust())
            var minThrust = Math.min( -leftEngine.getMinThrust(), -rightEngine.getMinThrust())
            var maxRotationThrust = Math.min(minThrust, maxThrust)
            var maxHorizontalThrust = Math.min(leftEngine.getMaxThrust(), rightEngine.getMaxThrust())
            var minHorizontalThrust = Math.min( -leftEngine.getMinThrust(), -rightEngine.getMinThrust())
            var maxVerticalThrust = Math.min(topEngine.getMaxThrust(), bottomEngine.getMaxThrust())
            var minVerticalThrust = Math.min( -topEngine.getMinThrust(), -bottomEngine.getMinThrust())
            this.optimalPower = speedTarget /  this.theoricalMaxSpeed;
            
            if(Math.abs(speedTarget - currentSpeed) > 0.1) {

                if(this.lastSpeedTarget != speedTarget) {
                    //Thrust target changed, full power allowed
                    this.allowPower = 1;
                    this.phase = 0;
                    //core.log("engine go to phase 0");
                    
                    this.maxOptimalPower = 1;
                    this.minOptimalPower = -1;
                    this.initiaSpeed = currentSpeed;
                }

                if(this.phase == 0) {
                    if (speedTarget > currentSpeed) {
                        this.leftThrustTarget = maxHorizontalThrust;
                        this.rightThrustTarget = maxHorizontalThrust;
                        this.topThrustTarget = maxVerticalThrust;
                        this.bottomThrustTarget = maxVerticalThrust;
                        this.lastTooSlow = true;
                        this.lastTooFast = false;
                    } else if (speedTarget < currentSpeed){
                        this.leftThrustTarget = - minHorizontalThrust;
                        this.rightThrustTarget = - minHorizontalThrust;
                        this.topThrustTarget = - minVerticalThrust;
                        this.bottomThrustTarget = - minVerticalThrust;
                        this.lastTooSlow = false;
                        this.lastTooFast = true;
                    } else {
                        this.leftThrustTarget = 0;
                        this.rightThrustTarget = 0;
                        this.topThrustTarget = 0;
                        this.bottomThrustTarget = 0;
                    }
                    this.phase = 1;
                    //core.log("engine go to phase 1");
                } else if(this.phase == 1) {
                    if (speedTarget > currentSpeed && this.lastTooFast && deltaSpeed < -0.001) {
                        // Too slow
                        this.leftThrustTarget = maxHorizontalThrust * this.optimalPower;
                        this.rightThrustTarget = maxHorizontalThrust * this.optimalPower;
                        this.topThrustTarget = maxVerticalThrust * this.optimalPower;
                        this.bottomThrustTarget = maxVerticalThrust * this.optimalPower;
                        this.phase = 2;
                        //core.log("engine go to phase 2 too slow");
                    } else if (speedTarget < currentSpeed && this.lastTooSlow && deltaSpeed > 0.001) {
                        // Too fast
                        this.leftThrustTarget = maxHorizontalThrust * this.optimalPower;
                        this.rightThrustTarget = maxHorizontalThrust * this.optimalPower;
                        this.topThrustTarget = maxVerticalThrust * this.optimalPower;
                        this.bottomThrustTarget = maxVerticalThrust * this.optimalPower;
                        this.phase = 2;
                        //core.log("engine g o to phase 2 too fast");
                    }
                } else if(this.phase == 2) {
                    if(Math.abs(speedTarget - currentSpeed) >  this.theoricalMaxSpeed/6) {
                        this.phase = 0;
                    }
                }
            }

            var worldRotationSpeed = kernel.getRotationSpeed();
            var localRotationSpeed = worldRotationSpeed.rotate(kernel.getTransform().inverse());
            
            var topRotThrust = 0;
            var bottomRotThrust = 0;
            var leftRotThrust = 0;
            var rightRotThrust = 0;

            var minHorizontalThrust = Math.max( -leftEngine.getMinThrust(), -rightEngine.getMinThrust())
            var minVerticalThrust = Math.max( -topEngine.getMinThrust(), -bottomEngine.getMinThrust())


            if(Math.abs(localRotationSpeed.getX() - rotationSpeedTarget.getX()) > 0.001) {
                var deltaThrust = this.topThrustTarget - this.bottomThrustTarget;
                var deltaRot = localRotationSpeed.getX() - rotationSpeedTarget.getX();
                //var thrustRatio = Math.min(Math.pow((Math.abs(deltaRot) / maxRotationSpeed)+0.3,3),1);
                var deltaRotRatio = Math.abs(deltaRot) / maxRotationSpeed
                var thrustRatio = 0;
                if(deltaRotRatio > 0.1) {
                    thrustRatio = 1;
                } else {
                    thrustRatio = 10 * deltaRotRatio //Math.pow(5*deltaRotRatio,2);
                }
                var thrustMax = minHorizontalThrust;
                if((localRotationSpeed.getX() - rotationSpeedTarget.getX() < 0)) {
                    //Too slow
                    topRotThrust -= thrustMax * thrustRatio;
                    bottomRotThrust += thrustMax * thrustRatio;
                }
                
                if((localRotationSpeed.getX() - rotationSpeedTarget.getX() > 0)) {
                    //Too fast
                    topRotThrust += thrustMax * thrustRatio;
                    bottomRotThrust -= thrustMax * thrustRatio;

                }
                
            }
            
            if(Math.abs(localRotationSpeed.getZ() - rotationSpeedTarget.getZ()) > 0.001) {
                var deltaThrust = this.topThrustTarget - this.bottomThrustTarget;
                var deltaRot = localRotationSpeed.getZ() - rotationSpeedTarget.getZ();
                //var thrustRatio = Math.min(Math.pow((Math.abs(deltaRot) / maxRotationSpeed)+0.3,3),1);
                var deltaRotRatio = Math.abs(deltaRot) / maxRotationSpeed
                var thrustRatio = 0;
                if(deltaRotRatio > 0.1) {
                    thrustRatio = 1;
                } else {
                    thrustRatio = 10 * deltaRotRatio //Math.pow(5*deltaRotRatio,2);
                }
                var thrustMax = minVerticalThrust;
                if((localRotationSpeed.getZ() - rotationSpeedTarget.getZ() < 0)) {
                    //Too slow
                    leftRotThrust -= thrustMax * thrustRatio;
                    rightRotThrust += thrustMax * thrustRatio;
                }
                
                if((localRotationSpeed.getZ() - rotationSpeedTarget.getZ() > 0)) {
                    //Too fast
                    leftRotThrust += thrustMax * thrustRatio;
                    rightRotThrust -= thrustMax * thrustRatio;

                }
                
            }



            leftEngine.targetThrust = this.leftThrustTarget + leftRotThrust;
            rightEngine.targetThrust = this.rightThrustTarget + rightRotThrust;
            topEngine.targetThrust = this.topThrustTarget + topRotThrust;
            bottomEngine.targetThrust = this.bottomThrustTarget + bottomRotThrust;

            this.lastSpeed = currentSpeed;
            this.lastSpeedTarget = speedTarget;
            this.lastTime = time;
            this.lastDeltaSpeed = deltaSpeed;
        }
    }
    
    
    function Gui() {
    
        this.clockIndicator;
        this.thrustControlleurEnabled = false;
        this.keyboardXTurn = 0;
        this.keyboardZTurn = 0;
       
        
        
        this.init = function() {

            this.screenSize = core.gui.getViewportSize()
                    
            var cursorCenter = core.gui.createLine();
            cursorCenter.setPosition((new Vec2(this.screenSize.getX()/2-1,this.screenSize.getY()/2-1)));
                            cursorCenter.setSize(new Vec2(1,1));
                            cursorCenter.setColor(new Color(0.8,0.0,0));
            
            var cursorTop = core.gui.createLine();
                            cursorTop.setPosition((new Vec2(this.screenSize.getX()/2,this.screenSize.getY()/2+10)));
                            cursorTop.setSize(new Vec2(0,30));
                            cursorTop.setColor(new Color(0.8,0.0,0));
            var cursorBottom = core.gui.createLine();
                            cursorBottom.setPosition((new Vec2(this.screenSize.getX()/2,this.screenSize.getY()/2-40)));
                            cursorBottom.setSize(new Vec2(0,30));
                            cursorBottom.setColor(new Color(0.8,0.0,0));
            var cursorLeft = core.gui.createLine();
                            cursorLeft.setPosition((new Vec2(this.screenSize.getX()/2-40,this.screenSize.getY()/2)));
                            cursorLeft.setSize(new Vec2(30,0));
                            cursorLeft.setColor(new Color(0.8,0.0,0));
            var cursorRight = core.gui.createLine();
                            cursorRight.setPosition((new Vec2(this.screenSize.getX()/2+10,this.screenSize.getY()/2)));
                            cursorRight.setSize(new Vec2(30,0));
                            cursorRight.setColor(new Color(0.8,0.0,0));

          
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


            var deltaHeigth = this.speedBoxMaxSize * speedTarget /  this.theoricalMaxSpeed;
            this.targetLine = core.gui.createLine();
            this.targetLine.setPosition((new Vec2(this.screenSize.getX() - 125,200 + deltaHeigth)));
            this.targetLine.setSize(new Vec2(40,0));
            this.targetLine.setColor(new Color(0.0,0.0,0.0));
            
            this.speedTargetIndicator = core.gui.createLabel();
            this.speedTargetIndicator.setText((speedTarget * 3.6).toFixed(0)+" km/h");
            this.speedTargetIndicator.setPosition(new Vec2(this.screenSize.getX() - 80,193 + deltaHeigth ));
            this.speedTargetIndicator.setFontStyle("bold",14);
            this.speedTargetIndicator.setColor(new Color(0.0,0.0,0.0));
            
            this.lastTime = 0;
        }
        
        this.update = function(time) {
            
            if(this.theoricalMaxSpeed != ship.getTheoricalMaxSpeed() ) {
                this.theoricalMaxSpeed = ship.getTheoricalMaxSpeed();
                this.targetSpeedUpdated();
            }
            
            //this.clockIndicator.setText("Time: "+time.toFixed(0)+" s");
            
            if(time - this.lastTime > 0.25) {
                
                // Fps
                //this.fpsIndicator.setText(""+core.gui.getFps().toFixed(0)+" fps");
                
                
                // Speed
                var speed = (kernel.getLinearSpeed().dot(new Vec3(0,1,0).rotate(kernel.getTransform())) * 3.6).toFixed(0);

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
                
                
               
                /*
                //Position
                var x = kernel.getTransform().getTranslation().getX().toFixed(1);
                this.xPositionIndicator.setText("x= "+x+" m");
                var y = kernel.getTransform().getTranslation().getY().toFixed(1);
                this.yPositionIndicator.setText("y= "+y+" m");
                var z = kernel.getTransform().getTranslation().getZ().toFixed(1);
                this.zPositionIndicator.setText("z= "+z+" m");
                
                //Rotation speed
                var worldRotationSpeed = kernel.getRotationSpeed();
                var localRotationSpeed = worldRotationSpeed.rotate(kernel.getTransform().inverse());
                var x = localRotationSpeed.getX().toFixed(3);
                this.xRotationSpeedIndicator.setText("wx= "+x+" r/s");
                var y = localRotationSpeed.getY().toFixed(3);
                this.yRotationSpeedIndicator.setText("wy= "+y+" r/s");
                var z = localRotationSpeed.getZ().toFixed(3);
                this.zRotationSpeedIndicator.setText("wz= "+z+" r/s");
                
                //Translation speed
                var x = kernel.getLinearSpeed().getX().toFixed(1);
                this.xLinearSpeedIndicator.setText("vx= "+x+" m/s");
                var y = kernel.getLinearSpeed().getY().toFixed(1);
                this.yLinearSpeedIndicator.setText("vy= "+y+" m/s");
                var z = kernel.getLinearSpeed().getZ().toFixed(1);
                this.zLinearSpeedIndicator.setText("vz= "+z+" m/s");
                */
                this.lastTime = time;
            }
            
            
            //Fast refresh 
            var speed = kernel.getLinearSpeed().dot(new Vec3(0,1,0).rotate(kernel.getTransform()));
            if(speed < 0) {
                speed = 0;
            }
            var speedBarSize = this.speedBoxMaxSize * speed /  this.theoricalMaxSpeed;
            this.speedCurrentBox.setSize(new Vec2(30,speedBarSize));
                
            
            
            
            if(this.keyboardXTurn != 0) {
                rotationSpeedTarget.x =  maxRotationSpeed * this.keyboardXTurn;
            } else {
                rotationSpeedTarget.x =  0;
            }
            
            if(this.keyboardZTurn != 0) {
                rotationSpeedTarget.z =  maxRotationSpeed * this.keyboardZTurn;
            } else {
                rotationSpeedTarget.z =  0;
            }
            
            
            
            if(this.thrustControlleurEnabled) {
                var deltaMouse = this.initialYMousePosition - core.mouse.getPosition().getY();
                speedTarget = this.initialSpeedTarget - deltaMouse;
                
                if(speedTarget >  this.theoricalMaxSpeed) {
                    this.deltaMouse -= (speedTarget -  this.theoricalMaxSpeed)
                    this.initialSpeedTarget -= (speedTarget -  this.theoricalMaxSpeed)
                    speedTarget =  this.theoricalMaxSpeed;
                }
                
                if(speedTarget < 0) {
                    this.deltaMouse -= speedTarget;
                    this.initialSpeedTarget -= speedTarget;
                    speedTarget = 0;
                }
                
                this.targetSpeedUpdated();
                
            } else if(this.rotationControlleurEnabled) {
            
                var mousePosition = core.mouse.getPosition();

                

                var diffX = (this.rotationControlleurMouseOrigin.getX() - mousePosition.getX());
                var diffY = (this.rotationControlleurMouseOrigin.getY() - mousePosition.getY());

                this.rotationControlleurControlZone.setSize(new Vec2(-diffX,-diffY));
                
                if(diffY > this.rotationControlleurControlRadius) {
                    diffY = this.rotationControlleurControlRadius;
                }
                if(diffX > this.rotationControlleurControlRadius) {
                    diffX = this.rotationControlleurControlRadius;
                }
                if(diffY < -this.rotationControlleurControlRadius) {
                    diffY = -this.rotationControlleurControlRadius;
                }
                if(diffX < -this.rotationControlleurControlRadius) {
                    diffX = -this.rotationControlleurControlRadius;
                }
                
                if(Math.abs(diffX) < this.rotationControlleurDeadZoneRadius) {
                    diffX = 0;
                } else if(diffX > 0) {
                    diffX -= this.rotationControlleurDeadZoneRadius;
                } else if(diffX < 0) {
                    diffX += this.rotationControlleurDeadZoneRadius;
                }
                
                if(Math.abs(diffY) < this.rotationControlleurDeadZoneRadius) {
                    diffY = 0;
                } else if(diffY > 0) {
                    diffY -= this.rotationControlleurDeadZoneRadius;
                } else if(diffY < 0) {
                    diffY += this.rotationControlleurDeadZoneRadius;
                }
                
                
                //var diffVert = diffX * Math.cos(Math.PI / 4.0) + diffY * Math.sin(Math.PI / 4.0);
                //var diffHoriz = -1 * diffX * Math.sin(Math.PI / 4.0) + diffY * Math.cos(Math.PI / 4.0);
                var diffVert = diffX;
                var diffHoriz = diffY;
        
                var percentX = diffVert / (this.rotationControlleurControlRadius - this.rotationControlleurDeadZoneRadius);
                var percentY = diffHoriz / (this.rotationControlleurControlRadius - this.rotationControlleurDeadZoneRadius);
                rotationSpeedTarget.z = rotationSpeedTarget.getZ() + maxRotationSpeed *  (percentX > 0 ? 1: -1) * Math.pow(percentX,2);
                rotationSpeedTarget.x = rotationSpeedTarget.getX() - maxRotationSpeed * (percentY > 0 ? 1: -1) * Math.pow(percentY,2);
            }
        }

        this.targetSpeedUpdated = function() {
            var deltaHeigth = this.speedBoxMaxSize * speedTarget /  this.theoricalMaxSpeed;
            this.targetLine.setPosition((new Vec2(this.screenSize.getX() - 125,200 + deltaHeigth)));
            this.speedTargetIndicator.setPosition(new Vec2(this.screenSize.getX() - 80, 193 + deltaHeigth ));
            this.speedTargetIndicator.setText((speedTarget * 3.6).toFixed(0)+" km/h");
        }

        this.enableThrustController = function() {
            this.initialYMousePosition = core.mouse.getPosition().getY();
            this.initialSpeedTarget = speedTarget;
            this.thrustControlleurEnabled = true;
            
            
        }        
        
        this.disableThrustController = function() {
            this.thrustControlleurEnabled = false;
            if(this.rotationControlleurEnabled) {
                this.disableRotationController();
                this.enableRotationController();
            }
            
        }
        
        
        // Rotation controlleur
        this.rotationControlleurEnabled = false;
        this.rotationControlleurDeadZoneRadius = 5.0;
        this.rotationControlleurControlRadius = 500.0;
        this.rotationControlleurDeadZone = null;
        this.rotationControlleurControlZone = null;
        this.rotationControlleurMouseOrigin = null;
        
        this.enableRotationController = function() {
        
            this.rotationControlleurMouseOrigin = core.mouse.getPosition();
            this.rotationControlleurEnabled = true;

            this.rotationControlleurDeadZone = core.gui.createRectangle();
            this.rotationControlleurDeadZone.setPosition(this.rotationControlleurMouseOrigin.minus(new Vec2(this.rotationControlleurDeadZoneRadius,this.rotationControlleurDeadZoneRadius)));
            this.rotationControlleurDeadZone.setSize(new Vec2(this.rotationControlleurDeadZoneRadius * 2,this.rotationControlleurDeadZoneRadius*2));
            this.rotationControlleurDeadZone.setBorderColor(new Color(0.3,0,0.0));
            
            //this.rotationControlleurControlZone = core.gui.createRectangle();
            //this.rotationControlleurControlZone.setPosition(this.rotationControlleurMouseOrigin.minus(new Vec2(this.rotationControlleurControlRadius,this.rotationControlleurControlRadius)));
            ///this.rotationControlleurControlZone.setSize(new Vec2(this.rotationControlleurControlRadius*2,this.rotationControlleurControlRadius*2));
            //this.rotationControlleurControlZone.setBorderColor(new Color(0.0,0,0.3));
            this.rotationControlleurControlZone = core.gui.createLine();
            this.rotationControlleurControlZone.setPosition(this.rotationControlleurMouseOrigin);
            this.rotationControlleurControlZone.setSize(new Vec2(10,10));
            this.rotationControlleurControlZone.setColor(new Color(0.3,0,0.0));
        }        
        
        this.disableRotationController = function() {
            this.rotationControlleurEnabled = false;
            
            core.gui.destroyComponent(this.rotationControlleurControlZone);
            core.gui.destroyComponent(this.rotationControlleurDeadZone);
            
            rotationSpeedTarget.z = 0;
            rotationSpeedTarget.x = 0;
            
        }
        
        this.toogleRotationController = function() {
            if(this.rotationControlleurEnabled) {
                 this.disableRotationController();
            } else {
                this.enableRotationController();
            }
        }
        
        
        this.onMouseEvent = function(action, button, x, y) {
            switch(action) {
                case MOUSE_PRESSED:
                    //core.log("mouse pressed");
                    if(button == 2) {
                        fire(true);
                    }
                    
                    if(button == 1) {
                        this.enableRotationController();
                    }
                    break;
               case MOUSE_RELEASED:
                    //core.log("mouse pressed");
                    if(button == 2) {
                            fire(false);
                    }
                    
                    if(button == 1) {
                        this.disableRotationController();
                    }
                    break; 
            }
        
        }
        
        
        this.onKeyPressed = function (keyCode, character) {
            switch(keyCode) {
                case KEY_UP:
                    break;
                case KEY_DOWN:
                    break;
                case KEY_LEFT:
                    break;
                case KEY_RIGHT:
                    break;
                case KEY_SPACE:
                    fire(true);
                    //gun.fire(true);
                    break;
                case KEY_PLUS:
                    speedTarget += 1/3.6;
                    this.targetSpeedUpdated();
                    break;
                case KEY_A:
                    break;
                case KEY_LEFT_SHIFT:
                    gui.enableThrustController();
                    break;
                case  KEY_Z:
                    this.keyboardXTurn = 1;
                    break;
                case  KEY_S:
                    this.keyboardXTurn = -1;
                    break;
                case  KEY_Q:
                    this.keyboardZTurn = 1;
                    break;
                case  KEY_D:
                    this.keyboardZTurn = -1;
                    break;
                case KEY_E:
                    break;
                case KEY_R:
                    fireRocket(true);
                    break;   
                case KEY_MINUS:
                    speedTarget -= 1/3.6;
                    this.targetSpeedUpdated();
                    break;
                default:
                    //core.log("pressed undefined key: '"+keyCode+"' / '"+character+"'");
            }   
        }
        
        this.onKeyReleased = function (keyCode, character) {
            switch(keyCode) {
                case KEY_UP:
                    break;
                case  KEY_DOWN:
                    break;
                case  KEY_LEFT:
                    break;
                case  KEY_RIGHT:
                    break;
                case KEY_A:
                    break;
                case KEY_LEFT_SHIFT:
                    gui.disableThrustController();
                    break;0
                case  KEY_Z:
                    this.keyboardXTurn = 0;
                    break;
                case  KEY_S:
                    this.keyboardXTurn = 0;
                    break;
                case  KEY_Q:
                    this.keyboardZTurn = 0;
                    break;
                case  KEY_D:
                    this.keyboardZTurn = 0;
                    break;
                case KEY_E:
                    break;
                case KEY_R:
                    fireRocket(false);
                    break;
                case KEY_SPACE:
                    fire(false);
                    //gun.fire(false);
                    break;
                default:
                    //core.log("released undefined key: '"+keyCode+"' / '"+character+"'");
            }   
        }
        
        this.init();

    }
    
    this.fire = function(order) {
        
        var gunComponents = ship.getComponentsByName("weapon.gun")
        
        for(var i = 0; i< gunComponents.size() ; i++) {
            var guns = gunComponents.get(i).getCapacitiesByClass("BalisticWeaponCapacity");
            
             for(var j = 0; j< guns.size() ; j++) {
                var gun = guns.get(j);
                gun.fire(order);
             }
        }
    }
    
    this.fireRocket = function(order) {
        
        var gunComponents = ship.getComponentsByName("weapon.gun")
        
        for(var i = 0; i< gunComponents.size() ; i++) {
            var guns = gunComponents.get(i).getCapacitiesByClass("RocketWeaponCapacity");
            
             for(var j = 0; j< guns.size() ; j++) {
                var gun = guns.get(j);
                gun.fire(order);
             }
        }
    }
    
}




driver();
