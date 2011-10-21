package com.irr310.server;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.irr310.server.UseScriptEvent.Type;
import com.irr310.server.world.WorldObject;

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
	private final ServerGame game;

	public CommandManager(ServerGame game) {
		this.game = game;
		exprComment = Pattern.compile("^//.*");
		exprQuit = Pattern.compile("^((quit)|(q))");
		exprInit = Pattern.compile("^((init)|(i))");
		exprStart = Pattern.compile("^((start)|(s))");
		exprPause = Pattern.compile("^((pause)|(p))");

		exprAdd = Pattern.compile("^((add)|(a)).*");
		exprDelete = Pattern.compile("^((delete)|(d)).*");
		exprLoad = Pattern.compile("^((load)|(l)).*");
		exprUse = Pattern.compile("^((use)|(u)).*");

	}

	public String execute(String command) {

		String out = "";

		Matcher match;
		if ((match = exprComment.matcher(command)).matches()) {
			// ignore
		} else if ((match = exprQuit.matcher(command)).matches()) {
			// quit
			out = null;
		} else if ((match = exprStart.matcher(command)).matches()) {
			game.sendToAll(new StartEngineEvent());
		} else if ((match = exprInit.matcher(command)).matches()) {
			game.sendToAll(new InitEngineEvent());
		} else if ((match = exprPause.matcher(command)).matches()) {
			game.sendToAll(new PauseEngineEvent());
		} else if ((match = exprUse.matcher(command)).matches()) {
			out = useScriptCommand(command);
		} else if ((match = exprAdd.matcher(command)).matches()) {
			out = addCommand(command);
		} else if ((match = exprDelete.matcher(command)).matches()) {
			// TODO
			/*
			 * boost::regex exprDeleteName( "^(delete|d) (.*)$" ); boost::smatch
			 * what2; if( boost::regex_match( command, what2, exprDeleteName )){
			 * std::string name; name = what2[2];
			 * 
			 * 
			 * EngineEvent *e = new EngineEvent(Game::ENGINE_COUNT); e->type =
			 * EngineEvent::WORLD_DELETE_OBJECT;
			 * 
			 * e->s_data["name"]=name; game->SendToAll(e);
			 * 
			 * }
			 */
		} else if ((match = exprLoad.matcher(command)).matches()) {
			//TODO
			/*
			 * 
			 * 
			 * 
			 * // out = "plop"; boost::regex exprLoadUrl( "^(load|l) (.*)$" );
			 * boost::smatch what2; if( boost::regex_match( command, what2,
			 * exprLoadUrl )){ std::string url; url = what2[2]; std::ifstream
			 * file(url.c_str(), std::ios::in);
			 * 
			 * if(file){
			 * 
			 * std::string line; std::string out2 = "";
			 * 
			 * out = "Executing script ...\n"; while(getline(file, line)){
			 * if(out2!=""){ out +="\n"; } out2 =Execute(line); out+=out2;
			 * 
			 * } file.close(); }else{ out = "File load failed"; }
			 * 
			 * }
			 */

		} else if (command.equals("")) {
			// Empty command

		} else {
			out = "Unknow command";
		}
		return out;

	}

	private String useScriptCommand(String command) {
		Pattern exprUseType = Pattern
				.compile(".*((-t)|(--type)) ([A-Za-z_0-9]*).*");
		Pattern exprUsePath = Pattern
				.compile(".*((-p)|(--path)) ([./A-Za-z_0-9]*).*");

		Matcher typeMatch = exprUseType.matcher(command);
		Matcher pathMatch = exprUsePath.matcher(command);

		if (!typeMatch.matches()) {
			return "Type parameter is missing";
		} else if (!pathMatch.matches()) {
			return "Path parameter is missing";
		}

		File script = new File(pathMatch.group(4));
		if (!script.exists()) {
			return "Script file not found at: " + script.getAbsolutePath();
		}

		String strType = typeMatch.group(4);
		UseScriptEvent.Type type;

		if (strType.equals("driver")) {
			type = Type.DRIVER;
		} else if (strType.equals("bind")) {
			type = Type.BIND;
		} else {
			return "Invalid type '" + strType
					+ "'. Valid type are 'driver' or 'bind' ";
		}

		game.getGameEngine().processEvent(new UseScriptEvent(type, script));
		return "";
	}

	private String addCommand(String command) {

		Pattern exprAddType = Pattern
				.compile(".*((-t)|(--type)) ([A-Za-z_0-9]*).*");
		Pattern exprAddPosition = Pattern
				.compile(".*((-p)|(--position)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*");
		Pattern exprAddRotation = Pattern
				.compile(".*((-q)|(--rotation)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*");
		Pattern exprAddLinearSpeed = Pattern
				.compile(".*((-v)|(--velocity)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*");
		Pattern exprAddRotationSpeed = Pattern
				.compile(".*((-w)|(--angular-velocity)) ([0-9\\.\\-]*) ([0-9\\.\\-]*) ([0-9\\.\\-]*).*");
		Pattern exprAddMass = Pattern
				.compile(".*((-m)|(--mass)) ([0-9\\.\\-]*).*");
		Pattern exprAddName = Pattern
				.compile(".*((-n)|(--name)) ([A-Za-z_0-9]*).*");
		Pattern exprAddLink = Pattern
				.compile(".*((-l)|(--link)) ([A-Za-z_0-9]*).*");

		AddWorldObjectEvent event = new AddWorldObjectEvent();

		Matcher typeMatch = exprAddType.matcher(command);
		if (!typeMatch.matches()) {
			return "Type parameter is missing";
		}

		// Type
		String typeStr = typeMatch.group(4);
		AddWorldObjectEvent.Type type;

		if (typeStr.equals("reference")) {
			type = AddWorldObjectEvent.Type.REFERENCE;
		} else if (typeStr.equals("camera")) {
			type = AddWorldObjectEvent.Type.CAMERA;
		} else if (typeStr.equals("collection")) {
			type = AddWorldObjectEvent.Type.COLLECTION;
		} else if (typeStr.equals("linear_motor")) {
			type = AddWorldObjectEvent.Type.LINEAR_MOTOR;
		} else if (typeStr.equals("part")) {
			type = AddWorldObjectEvent.Type.PART;
		} else if (typeStr.equals("planet")) {
			type = AddWorldObjectEvent.Type.PLANET;
		} else if (typeStr.equals("star")) {
			type = AddWorldObjectEvent.Type.STAR;
		} else {
			return "Invalid type '"
					+ typeStr
					+ "'. Valid type are 'reference' , 'camera', 'collection', 'linear_motor', 'part', 'planet' and 'star' ";
		}

		event.setType(type);

		// Name
		Matcher nameMatch = exprAddName.matcher(command);
		if (nameMatch.matches()) {
			event.setName(nameMatch.group(4));
		} else {
			event.setName("");
		}

		// Name
		Matcher linkMatch = exprAddLink.matcher(command);
		if (linkMatch.matches()) {
			String linkStr = linkMatch.group(4);
			WorldObject linkedObject = game.getWorld().getObjectByName(linkStr);
			if (linkedObject == null) {
				return "No objects named '" + linkStr + "'";
			}
			event.setLinkedObject(linkedObject);
		}

		// Position
		Matcher positionMatch = exprAddPosition.matcher(command);
		if (positionMatch.matches()) {
			String xStr = positionMatch.group(4);
			String yStr = positionMatch.group(5);
			String zStr = positionMatch.group(6);

			try {
				Double x = Double.valueOf(xStr);
				Double y = Double.valueOf(xStr);
				Double z = Double.valueOf(xStr);
				event.setPosition(new Vect3(x, y, z));
			} catch (NumberFormatException e) {
				return "Bad number format for position in '" + xStr + "', '"
						+ yStr + "' or '" + zStr + "'";
			}
		}

		// Rotation
		Matcher rotationMatch = exprAddRotation.matcher(command);
		if (rotationMatch.matches()) {
			String xStr = rotationMatch.group(4);
			String yStr = rotationMatch.group(5);
			String zStr = rotationMatch.group(6);

			try {
				Double x = Double.valueOf(xStr);
				Double y = Double.valueOf(xStr);
				Double z = Double.valueOf(xStr);
				event.setRotation(new Vect3(x, y, z));
			} catch (NumberFormatException e) {
				return "Bad number format for rotation in '" + xStr + "', '"
						+ yStr + "' or '" + zStr + "'";
			}
		}

		// Linear speed
		Matcher linearSpeedMatch = exprAddLinearSpeed.matcher(command);
		if (linearSpeedMatch.matches()) {
			String xStr = linearSpeedMatch.group(4);
			String yStr = linearSpeedMatch.group(5);
			String zStr = linearSpeedMatch.group(6);

			try {
				Double x = Double.valueOf(xStr);
				Double y = Double.valueOf(xStr);
				Double z = Double.valueOf(xStr);
				event.setLinearSpeed(new Vect3(x, y, z));
			} catch (NumberFormatException e) {
				return "Bad number format for linear speed in '" + xStr
						+ "', '" + yStr + "' or '" + zStr + "'";
			}
		}

		// Rotation speed
		Matcher rotationSpeedMatch = exprAddRotationSpeed.matcher(command);
		if (rotationSpeedMatch.matches()) {
			String xStr = rotationSpeedMatch.group(4);
			String yStr = rotationSpeedMatch.group(5);
			String zStr = rotationSpeedMatch.group(6);

			try {
				Double x = Double.valueOf(xStr);
				Double y = Double.valueOf(xStr);
				Double z = Double.valueOf(xStr);
				event.setRotationSpeed(new Vect3(x, y, z));
			} catch (NumberFormatException e) {
				return "Bad number format for rotation speed in '" + xStr
						+ "', '" + yStr + "' or '" + zStr + "'";
			}
		}

		// Mass
		Matcher massMatch = exprAddMass.matcher(command);
		if (massMatch.matches()) {
			String massStr = massMatch.group(4);

			try {
				Double mass = Double.valueOf(massStr);
				event.setMass(mass);
			} catch (NumberFormatException e) {
				return "Bad number format for mass:'" + massStr + "'";
			}
		}

		game.sendToAll(event);
		return "Game : Add object > " + typeStr;

	}
}
