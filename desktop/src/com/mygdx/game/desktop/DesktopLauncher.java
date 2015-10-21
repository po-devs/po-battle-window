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
import com.mygdx.game.battlewindow.ContinuousGameFrame;
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

		ParticleEffect effect = new ParticleEffect();
		public void specialplay(Batch batch, float delta) {
			//effect.draw(batch, delta);
		}

		@Override
		public void unpause() {

		}

		@Override
		public void finished() {
			effect.load(Gdx.files.internal("sandstorm"), Gdx.files.internal(""));
			effect.setPosition(Gdx.graphics.getWidth()/2 + 100, Gdx.graphics.getHeight());
		}

		Random random = new Random();
		@Override
		public TaskService setGame(ContinuousGameFrame game) {
			this.game = game;
			TaskService service = new DesktopService();
			int randomNum = random.nextInt(37);
			service.offer(new Events.BackgroundChange(randomNum));
			Poke me = new Poke((byte) (random.nextInt(95) + 5), "Test", (short) random.nextInt(600), (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			//mebattle = new Poke((byte) 100, "test", (short) 4, (byte)1,(byte) 100,(short) 0,(short) 100,(short) 100);
			Poke opp = new Poke((byte) (random.nextInt(100) + 5), "Test", (short) 49, (byte) random.nextInt(3), (byte) (random.nextInt(80) + 20), random.nextInt(7));
			service.offer(new Events.SpriteChange(me, this.me, true));
			service.offer(new Events.SpriteChange(opp, this.opp, false));
			//service.offer(new Events.HUDChangeBattling(mebattle));
			service.offer(new Events.HUDChange(me, this.me));
			service.offer(new Events.HUDChange(opp, this.opp));
			return service;
		}
		Poke mebattle;

		@Override
		public void alert(String message) {
			int status = random.nextInt(7);
			if (message == "true") {
				//game.service.offer(new Events.KO(true));
				game.service.offer(new Events.StatusChange(1, status));
			} else if (message == "false") {
				//game.service.offer(new Events.KO(false));
				game.service.offer(new Events.StatusChange(0, status));
			} else {
				log(message);
			}
			log(status + "");
		}

        @Override
        public  void onHover(int spot) {
            log("hover " + spot);
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
