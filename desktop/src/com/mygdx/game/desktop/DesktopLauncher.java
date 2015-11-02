package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Bridge;
import com.mygdx.game.JSONPoke;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;
import com.mygdx.game.battlewindow.TaskService;

import java.util.Random;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 360;
		config.resizable = false;

        DesktopBridge bridge = new DesktopBridge(true);

		new LwjglApplication(new ContinuousGameFrame(bridge, bridge.me == 1), config);
	}

	private static class DesktopBridge extends Bridge {
		public int me = 0;
		public int opp = 1;

        public DesktopBridge() {

        }

        public DesktopBridge(boolean reversed) {
            me = 1;
            opp = 0;
        }

        @Override
		public void pause() {

		}

		public void specialplay(Batch batch, float delta) {
			//effect.draw(batch, delta);
		}

		@Override
		public void unpause() {

		}

		@Override
		public void finished() {
			int randomNum = random.nextInt(37);
			addEvent(new Events.BackgroundChange(randomNum));
			Poke me = new Poke((byte) (random.nextInt(95) + 5), "Test", (short) random.nextInt(600), (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			//mebattle = new Poke((byte) 100, "test", (short) 4, (byte)1,(byte) 100,(short) 0,(short) 100,(short) 100);
			Poke opp = new Poke((byte) (random.nextInt(100) + 5), "Test", (short) 49, (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			addEvent(new Events.SpriteChange(me, this.me, true));
			addEvent(new Events.SpriteChange(opp, this.opp, false));
			//service.offer(new Events.HUDChangeBattling(mebattle));
			addEvent(new Events.HUDChange(me, this.me));
			addEvent(new Events.HUDChange(opp, this.opp));
//			addEvent(new Events.SendOut(this.opp, me));
//			addEvent(new Events.SendOut(this.me, opp));
//			addEvent(new Events.SendOut(this.opp, opp));
//			addEvent(new Events.SendOut(this.me, me));
		}

		Random random = new Random();
		@Override
		public TaskService setGame(ContinuousGameFrame game) {
			this.game = game;
			TaskService service = new DesktopService();
			return service;
		}

		@Override
		public void alert(String message) {
			int status = random.nextInt(4) + 1;
			if (message == "true") {
				processEvent(new Events.KO(0));
				//Events.Weather event = new Events.Weather(status);
				//event.process();
				//game.service.offer(event);
			} else if (message == "false") {
				JSONPoke poke = new JSONPoke() {
					@Override
					public byte gender() {
						return 0;
					}

					@Override
					public byte level() {
						return 100;
					}

					@Override
					public String name() {
						return "Pikachu";
					}

					@Override
					public short num() {
						return 25;
					}

					@Override
					public byte percent() {
						return 100;
					}

					@Override
					public boolean shiny() {
						return false;
					}

					@Override
					public int status() {
						return 0;
					}

					@Override
					public byte forme() {
						return 0;
					}

					@Override
					public short life() {
						return 100;
					}

					@Override
					public short totallife() {
						return 100;
					}
				};
				game.service.offer(new Events.SendOut(0, poke));
				//game.service.offer(new Events.KO(false));
				//game.service.offer(new Events.StatusChange(0, status));
			} else {
				log(message);
			}
			log(status + "");
		}

        @Override
        public  void onHover(int spot) {
            //log("hover " + spot);
        }

        @Override
        public boolean isDebug() {
            return true;
        }

		@Override
		public TextureAtlas getAtlas(String path) {
			return new TextureAtlas(Gdx.files.internal(path));
		}

		@Override
		public Texture getTexture(String path) {
			return new Texture(Gdx.files.internal(path));
		}

		@Override
		public BitmapFont getFont(String path) {
			return null;
		}

		@Override
		public void log(String text) {
			System.out.println(text);
		}
	}
}
