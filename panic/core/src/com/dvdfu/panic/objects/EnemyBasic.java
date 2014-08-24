package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.EnemyState;
import com.dvdfu.panic.visuals.Label;
import com.dvdfu.panic.visuals.Sprites;

public class EnemyBasic extends AbstractEnemy {
	private Label healthBar;
	private boolean movingRight;
	private final int moveSpeed = 1;

	public EnemyBasic() {
		super();
		healthBar = new Label("" + stunnedTimer);
		reset();
		stretched = false;
		sprScale = 2;
		setSize(19 * sprScale, 15 * sprScale);
		xSprOffset = -2;
		setSprite(Sprites.enemyThrow);
	}

	public void move() {
		if (state != EnemyState.GRABBED) {
			ySpeed -= 0.3f;
		}
		if ((state == EnemyState.THROWN || state == EnemyState.STUNNED) && grounded) {
			brake(0, 0.25f);
			if (state == EnemyState.THROWN && xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
		if (getRight() > Consts.BoundsR) {
			setX(Consts.BoundsR - getWidth());
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		if (getX() < Consts.BoundsL) {
			setX(Consts.BoundsL);
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		grounded = false;
	}

	private void brake(float vf, float a) {
		if (xSpeed > vf + a) {
			xSpeed -= a;
		} else if (xSpeed > vf) {
			xSpeed = vf;
		} else if (xSpeed < -vf - a) {
			xSpeed += a;
		} else if (xSpeed < -vf){
			xSpeed  = -vf;
		}
	}

	public void act(float delta) {
		if (state == EnemyState.STUNNED) {
			if (stunnedTimer > 0) {
				stunnedTimer--;
			} else {
				setState(EnemyState.ACTIVE);
			}
		}
		super.act(delta);
	}

	public void collideSolid(Solid other) {
		if (state == EnemyState.GRABBED) { return; }
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(other.bounds)) {
			if (getTop() + ySpeed > other.getY() && bounds.y < other.getY()) {
				setY(other.getY() - getHeight());
			}
			if (getTop() + ySpeed > other.getTop() && bounds.y < other.getTop()) {
				setY(other.getTop());
				grounded = true;
			}
			ySpeed = 0;
			if (state == EnemyState.DEAD) {
				setState(EnemyState.REMOVE);
			}
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(other.bounds)) {
			if (getRight() + xSpeed > other.getX() && bounds.x < other.getX()) {
				setX(other.getX() - getWidth());
			}
			if (getRight() + xSpeed > other.getRight() && bounds.x < other.getRight()) {
				setX(other.getRight());
			}
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		updateBounds();
	}

	public void collideEnemy(AbstractEnemy other) {
		if (other.state == EnemyState.DEAD || other.state == EnemyState.REMOVE || state == EnemyState.REMOVE) { return; }
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(other.bounds)) {
			if (state == EnemyState.THROWN) {
				other.setState(EnemyState.DEAD);
				other.launch(xSpeed / 2, 6);
			} else if (state == EnemyState.GRABBED) {
				other.setState(EnemyState.STUNNED);
				if (xSpeed == 0) {
					if (getX() > other.getX()) {
						other.launch(-1, 6);
					} else {
						other.launch(1, 6);
					}
				} else {
					other.launch(xSpeed / 2, 6);
				}
			}
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(other.bounds)) {
			if (state == EnemyState.ACTIVE && other.state == EnemyState.STUNNED && grounded) {
				if (getRight() + xSpeed > other.getX() && bounds.x < other.getX()) {
					setX(other.getX() - getWidth());
				}
				if (getRight() + xSpeed > other.getRight() && bounds.x < other.getRight()) {
					setX(other.getRight());
				}
				xSpeed = -xSpeed;
				movingRight ^= true;
			}
		}
		updateBounds();
	}

	public void setState(EnemyState state) {
		// STATE EXIT
		switch (this.state) {
		default:
			break;
		}
		// STATE ENTER
		switch (state) {
		case ACTIVE:
			xSpeed = movingRight ? moveSpeed : -moveSpeed;
			setSprite(Sprites.enemyThrow);
			break;
		case STUNNED:
			xSpeed = 0;
			stunnedTimer = 180;
			setSprite(Sprites.enemyThrow);
			break;
		case GRABBED:
			xSpeed = 0;
			ySpeed = 0;
		case THROWN:
			stunnedTimer = 0;
			setSprite(Sprites.enemyThrow);
			break;
		case DEAD:
			setSprite(Sprites.enemyThrow);
			break;
		default:
			setSprite(Sprites.enemyThrow);
			break;
		}
		super.setState(state);
	}

	public void draw(Batch batch, float alpha) {
		switch (state) {
		case ACTIVE:
			batch.setColor(new Color(1, 0, 0, 1));
			break;
		case STUNNED:
			batch.setColor(new Color(1, 1, 0, 1));
			break;
		case GRABBED:
			batch.setColor(new Color(0, 0, 1, 1));
			break;
		case THROWN:
			batch.setColor(new Color(0, 1, 0, 1));
			break;
		case DEAD:
			batch.setColor(new Color(1, 0, 1, 1));
			break;
		default:
			break;
		}
		super.draw(batch, alpha);
		batch.setColor(new Color(1, 1, 1, 1));
		if (state == EnemyState.STUNNED) {
			healthBar.setText("" + (stunnedTimer / 60 + 1));
			healthBar.drawC(batch, getX() + getWidth() / 2, getY() + 40);
		}
	}

	public void reset() {
		state = EnemyState.ACTIVE;
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			setX(1 - getWidth() + MathUtils.random(160));
			xSpeed = moveSpeed;
		} else {
			setX(Consts.ScreenWidth - MathUtils.random(160) - 1);
			xSpeed = -moveSpeed;
		}
		setY(Consts.ScreenHeight);
		ySpeed = 0;
	}
}