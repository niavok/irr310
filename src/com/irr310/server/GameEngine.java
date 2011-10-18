package com.irr310.server;

public class GameEngine extends Engine {

	public GameEngine(ServerGame game) {
		super(game);
	}

	@Override
	protected void frame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processEvent(EngineEvent e) {
		// TODO Auto-generated method stub
		e.accept(new GameEngineEventVisitor());
       /* case EngineEvent::GAME_START :
            pause(false);

            break;
        case EngineEvent::GAME_PAUSE :
            pause(true);
            break;
        case EngineEvent::WORLD_ADD_OBJECT :
            {
                WorldObject *o;
                #ifdef DEBUG


                std::cout<<e->i_data["type"];


        #endif
                switch (e->i_data["type"]) {
                    case WorldObject::STAR :
                         o = new Star(world);

                        break;
                    case WorldObject::PART :
                         o = new Part(world);
                        break;
                    case WorldObject::PLANET :
                         o = new Planet(world);

                        break;
                    case WorldObject::CAMERA :
                         o = new Camera(world);

                        break;
                    case WorldObject::LINEAR_MOTOR :
                         o = new LinearMotor(world);

                        break;

                    case WorldObject::COLLECTION :
                         o = new Collection(world);

                        break;
                    default:
                        o = new WorldObject(world);
                        break;
                }

                if(e->s_data["link"]!=""){

                         Collection *c = (Collection *) world->GetObjectByName(e->s_data["link"]);

                          if(c){
                             c->AddChild(o);
                          }


                      }else{
                          o->Init();
                      }

                      o->SetPosition(e->d_data["x"],e->d_data["y"],e->d_data["z"]);
                      o->SetLineareSpeed(e->d_data["vx"],e->d_data["vy"],e->d_data["vz"]);
                      o->SetRotation(e->d_data["qx"],e->d_data["qy"],e->d_data["qz"]);
                      o->SetRotationSpeed(e->d_data["wx"],e->d_data["wy"],e->d_data["wz"]);

                      if(e->d_data["mass"]!=-1){
                          o->SetMass(e->d_data["mass"]);
                      }

                      if(e->s_data["name"]!=""){
                          world->SetObjectName(e->s_data["name"],o);
                      }



                      //TODO : delete

                      EngineEvent *e = new EngineEvent(Game::ENGINE_COUNT);
                      e->type = EngineEvent::WORLD_OBJECT_ADDED;
                      e->p_data["OBJECT"] = o;
                      sendMessageToAll(e);
                  }
                  break;*/

	}
	
	private final class GameEngineEventVisitor extends
		EngineEventVisitor {
		@Override
		public void visit(QuitGameEvent event) {
			System.out.println("stopping game engine");
			isRunning = false;
		}

		@Override
		public void visit(StartEngineEvent event) {
			pause(false);
		}

		@Override
		public void visit(InitEngineEvent event) {
		}

		@Override
		public void visit(PauseEngineEvent event) {
			pause(true);			
		}

		@Override
		public void visit(UseScriptEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visit(AddWorldObjectEvent event) {
			// TODO Auto-generated method stub
			
		}
	}

}
