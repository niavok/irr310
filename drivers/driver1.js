
// Add key handler
core.onKeyPressed = function (keyCode, char) {
    switch(keyCode) {
        case KEY_UP:
            core.log("press up");
            break;
        case KEY_DOWN:
            core.log("press down");
            break;
        case KEY_LEFT:
            core.log("press left");
            break;
        case KEY_RIGHT:
            core.log("press right");
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
        default:
            core.log("released undefined key: '"+keyCode+"' / '"+char+"'");
    }   
}
