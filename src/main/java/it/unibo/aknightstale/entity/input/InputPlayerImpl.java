package it.unibo.aknightstale.entity.input;

import java.util.ArrayList;
import java.util.List;

import it.unibo.aknightstale.entity.controller.EntityController;
import javafx.scene.layout.Pane;

public class InputPlayerImpl implements InputPlayer {
	
	private final EntityController player;
	private List<String> keyPressed;

	public InputPlayerImpl(EntityController player, Pane scene) {
		super();
		this.player = player;
		this.keyPressed = new ArrayList<>();
		
		scene.setOnKeyPressed( event -> {
			String keyName = event.getCode().toString();
			if (!keyPressed.contains(keyName)) {
				keyPressed.add(keyName);
			}
		}); 

		scene.setOnKeyReleased( event -> {
			String keyName = event.getCode().toString();
			if (keyPressed.contains(keyName)) {
				keyPressed.remove(keyName);
			}
		}); 
	}

	@Override
	public void update() {/*
							 * if (keyPressed.contains("A")) { this.player.moveLeft(); } if
							 * (keyPressed.contains("D")) { this.player.moveRight(); } if
							 * (keyPressed.contains("W")) { this.player.moveUp(); } if
							 * (keyPressed.contains("S")) { this.player.moveDown(); } if
							 * (keyPressed.contains("SPACE")) { this.player.attack(); }
							 */
	}

}