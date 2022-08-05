package it.unibo.aknightstale.controllers.entity;

import it.unibo.aknightstale.models.entity.CharacterModel;
import it.unibo.aknightstale.models.entity.Direction;
import it.unibo.aknightstale.utils.EntityManager;
import it.unibo.aknightstale.views.entity.AnimatedEntityView;

public abstract class AbstractController<M extends CharacterModel, V extends AnimatedEntityView> extends EntityControllerImpl<M, V> implements CharacterController<M, V> {
	
	protected final EntityManager manager;
	
	public AbstractController(M model, V view, EntityManager manager) {
		super(model, view);
		this.manager = manager;
	}
	
	protected abstract void move(Direction dir);
	
	private boolean canMove(Direction d) {
		return this.manager.getCollisionManager().canMove(this).contains(d);
	}

	@Override
	public void moveRight() {
		if(canMove(Direction.RIGHT)) {
			this.model.goRight();
			move(Direction.RIGHT);
		}
	}

	@Override
	public void moveLeft() {
		if(canMove(Direction.LEFT)) {
			this.model.goLeft();
			move(Direction.LEFT);
		}
	}

	@Override
	public void moveUp() {
		if(canMove(Direction.UP)) {
			this.model.goUp();
			move(Direction.UP);
		}
	}

	@Override
	public void moveDown() {
		if(canMove(Direction.DOWN)) {
			this.model.goDown();
			move(Direction.DOWN);
		}
	}

	@Override
	public abstract void attack();

}