package com.irr310.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CommandManager {

	private Pattern exprComment;
	private Pattern exprQuit;
	private Pattern exprInit;
	private Pattern exprStart;
	private Pattern exprPause;
	private Pattern exprAdd;
	private Pattern exprDelete;
	private Pattern exprLoad;
	private Pattern exprUse;
	private final Game game;

	public CommandManager(Game game) {
		this.game = game;
		exprComment = Pattern.compile("^//.*");
		exprQuit = Pattern.compile( "^((quit)|(q))" );
		exprInit = Pattern.compile( "^((init)|(i))" );
		exprStart = Pattern.compile( "^((start)|(s))" );
		exprPause = Pattern.compile( "^((pause)|(p))" );

		exprAdd = Pattern.compile( "^((add)|(a)).*" );
		exprDelete = Pattern.compile( "^((delete)|(d)).*" );
		exprLoad = Pattern.compile( "^((load)|(l)).*" );
		exprUse = Pattern.compile("^((use)|(u)).*" );

	}

	public String execute(String command) {
		
		String out = "";

		Matcher match;
		if((match = exprComment.matcher(command)).matches()) {
			// ignore
		} else if((match = exprQuit.matcher(command)).matches()) {
			// quit
			out = null;
		} else if((match = exprStart.matcher(command)).matches()) {
			game.sendToAll(new StartEngineEvent());
		} else if((match = exprInit.matcher(command)).matches()) {
			game.sendToAll(new InitEngineEvent());
		} else if((match = exprPause.matcher(command)).matches()) {
			game.sendToAll(new PauseEngineEvent());
		} else if((match = exprUse.matcher(command)).matches()) {
			/*
			 *  EngineEvent *e = new EngineEvent(1);
            e->type = EngineEvent::SCRIPT_USE;
            boost::regex exprUseType( ".*((-t)|(--type)) ([A-Za-z_0-9]*).*" );
            boost::regex exprUsePath( ".*((-p)|(--path)) ([./A-Za-z_0-9]*).*" );

            boost::smatch what2;
            if( boost::regex_match( command, what2, exprUseType )){
                 e->s_data["type"] = what2[4];
                        }
            game->GetGameEngine()->PushEvent(e);
			 */
		} else if((match = exprAdd.matcher(command)).matches()) {
           
            /*    boost::regex exprAddType( ".*((-t)|(--type)) ([A-Za-z_0-9]*).*" );
                boost::regex exprAddPosition( ".*((-p)|(--position)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*" );
                boost::regex exprAddRotation( ".*((-q)|(--rotation)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*" );
                boost::regex exprAddPositionSpeed( ".*((-v)|(--velocity)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*" );
                boost::regex exprAddRotationSpeed( ".*((-w)|(--angular-velocity)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*" );
    boost::regex exprAddMass( ".*((-m)|(--mass)) ([0-9\\.\\-]*).*" );
        boost::regex exprAddName( ".*((-n)|(--name)) ([A-Za-z_0-9]*).*" );
    boost::regex exprAddLink( ".*((-l)|(--link)) ([A-Za-z_0-9]*).*" );

    EngineEvent *e = new EngineEvent(Game::ENGINE_COUNT);
    e->type = EngineEvent::WORLD_ADD_OBJECT;


                std::string type;
                boost::smatch what2;

                if( boost::regex_match( command, what2, exprAddType )){
        type = what2[4];
                }

    if( boost::regex_match( command, what2, exprAddName )){
        e->s_data["name"] = what2[4];
                }else{
        e->s_data["name"] = "";
                }

                 if( boost::regex_match( command, what2, exprAddLink )){
        e->s_data["link"] = what2[4];
                }else{
        e->s_data["link"] = "";
                }
                 if( boost::regex_match( command, what2, exprAddPosition )){
                     std::string temp;
                     temp = what2[4];
                     e->d_data["x"] = strtod(temp.c_str(),NULL);
                     temp = what2[5];
                     e->d_data["y"] = strtod(temp.c_str(),NULL);
                     temp = what2[6];
                     e->d_data["z"] = strtod(temp.c_str(),NULL);
             }
             if( boost::regex_match( command, what2, exprAddRotation )){
                     std::string temp;
                     temp = what2[4];
                     e->d_data["qx"] = strtod(temp.c_str(),NULL);
                     temp = what2[5];
                     e->d_data["qy"] = strtod(temp.c_str(),NULL);
                     temp = what2[6];
                     e->d_data["qz"] = strtod(temp.c_str(),NULL);



             }
             if( boost::regex_match( command, what2, exprAddPositionSpeed )){
                     std::string temp;
                     temp = what2[4];
                     e->d_data["vx"] = strtod(temp.c_str(),NULL);
                     temp = what2[5];
                     e->d_data["vy"] = strtod(temp.c_str(),NULL);
                     temp = what2[6];
                     e->d_data["vz"] = strtod(temp.c_str(),NULL);
             }
             if( boost::regex_match( command, what2, exprAddRotationSpeed )){
                     std::string temp;
                     temp = what2[4];
                     e->d_data["wx"] = strtod(temp.c_str(),NULL);
                     temp = what2[5];
                     e->d_data["wy"] = strtod(temp.c_str(),NULL);
                     temp = what2[6];
                     e->d_data["wz"] = strtod(temp.c_str(),NULL);
             }

             if( boost::regex_match( command, what2, exprAddMass )){
                     std::string temp;
                     temp = what2[4];
                     e->d_data["mass"] = strtod(temp.c_str(),NULL);

             }else{
            	  e->d_data["mass"] = -1;
             }

              out ="Game : Add object > " + type;




  if(type == "star"){
      e->i_data["type"] = WorldObject::STAR;

 }else if(type == "repere"){
      e->i_data["type"] = WorldObject::REPERE;
 }else if(type == "planet"){
      e->i_data["type"] = WorldObject::PLANET;
 }else if(type == "part"){
      e->i_data["type"] = WorldObject::PART;
 }else if(type == "camera"){
      e->i_data["type"] = WorldObject::CAMERA;
 }else if(type == "linear_motor"){
      e->i_data["type"] = WorldObject::LINEAR_MOTOR;
 }else if(type == "collection"){
      e->i_data["type"] = WorldObject::COLLECTION;
 }else{
     out = " - Unknow type";

  }

 game->SendToAll(e);*/
		} else if((match = exprDelete.matcher(command)).matches()) {
			
			/* boost::regex exprDeleteName( "^(delete|d) (.*)$" );
		      boost::smatch what2;
		      if( boost::regex_match( command, what2, exprDeleteName )){
		         std::string name;
		         name = what2[2];


		         EngineEvent *e = new EngineEvent(Game::ENGINE_COUNT);
		         e->type = EngineEvent::WORLD_DELETE_OBJECT;

		         e->s_data["name"]=name;
		         game->SendToAll(e);

		      }
*/
		} else if((match = exprLoad.matcher(command)).matches()) {
/*
     
                

   // out = "plop";
     boost::regex exprLoadUrl( "^(load|l) (.*)$" );
     boost::smatch what2;
     if( boost::regex_match( command, what2, exprLoadUrl )){
                        std::string url;
        url = what2[2];
        std::ifstream file(url.c_str(), std::ios::in);

        if(file){

            std::string line;
            std::string out2 = "";

            out = "Executing script ...\n";
            while(getline(file, line)){
                if(out2!=""){
                    out +="\n";
                }
                out2 =Execute(line);
                out+=out2;

            }
            file.close();
        }else{
            out = "File load failed";
        }

                }
                */
                
}else if(command.equals("")){
    //Empty command

}else{
        out = "Unknow command";
}
return out;
          
	}
}
