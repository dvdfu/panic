package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
		stretched = true;
		setSize(44, 32);
		setSprite(Sprites.enemyThrow);
	}

	public void move() {
		if (state != EnemyState.GRABBED) {
			ySpeed -= 0.3f;
		}
		if (state == EnemyState.THROWN && grounded) {
			brake(0, 0.25f);
			if (xSpeed == 0) {
				setState(EnemyState.STUNNED);
			}
		}
		grounded = false;
	}

	private void brake(float vf, float a) {
		if (xSpeed > vf) {
			xSpeed -= a;
		} else if (xSpeed > 0) {
			xSpeed = vf;
		} else if (xSpeed > -vf) {
			xSpeed = -vf;
		} else {
			xSpeed += a;
		}
	}

	public void act(float delta) {
		if (state == EnemyState.STUNNED || state == EnemyState.GRABBED || state == EnemyState.THROWN) {
			if (stunnedTimer > 0) {
				stunnedTimer--;
			} else if (state != EnemyState.THROWN) {
				setState(EnemyState.ACTIVE);
			}
		}
		if (state != EnemyState.GRABBED && getTop() < 0) {
			setState(EnemyState.REMOVE);
		}
		if (getX() > Consts.ScreenWidth) {
			setX(1 - getWidth());
		}
		if (getRight() < 0) {
			setX(Consts.ScreenWidth - 1);
		}
		super.act(delta);
	}

	public void collideSolid(Solid block) {
		if (state == EnemyState.GRABBED) {
			return;
		}
		bounds.setPosition(getX(), getY() + ySpeed);
		if (bounds.overlaps(block.bounds)) {
			if (getTop() + ySpeed > block.getY() && bounds.y < block.getY()) {
				setY(block.getY() - getHeight());
			}
			if (getTop() + ySpeed > block.getTop() && bounds.y < block.getTop()) {
				setY(block.getTop());
				grounded = true;
			}
			ySpeed = 0;
			if (state == EnemyState.DEAD) {
				setState(EnemyState.REMOVE);
			}
		}
		bounds.setPosition(getX() + xSpeed, getY());
		if (bounds.overlaps(block.bounds)) {
			if (getRight() + xSpeed > block.getX() && bounds.x < block.getX()) {
				setX(block.getX() - getWidth());
			}
			if (getRight() + xSpeed > block.getRight() && bounds.x < block.getRight()) {
				setX(block.getRight());
			}
			xSpeed = -xSpeed;
			movingRight ^= true;
		}
		updateBounds();
	}

	public void collideEnemy(AbstractEnemy enemy) {
		if (enemy.state != EnemyState.ACTIVE && enemy.state != EnemyState.STUNNED) {
			return;
		}
		switch (state) {
		case ACTIVE:
			bounds.setPosition(getX() + xSpeed, getY());
			if (bounds.overlaps(enemy.bounds)) {
				if (grounded) {
					if (getX() > enemy.getX()) {
						setX(enemy.getRight());
						xSpeed = moveSpeed;
						movingRight = true;
					} else {
						setX(enemy.getX() - getWidth());
						xSpeed = -moveSpeed;
						movingRight = false;
					}
				}
			}
			break;
		case GRABBED:
		case THROWN:
			bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
			if (bounds.overlaps(enemy.bounds)) {
				enemy.setState(EnemyState.DEAD);
				enemy.launch(xSpeed == 0 ? enemy.xSpeed * 2 : xSpeed / 2, 5);
			}
			break;
		default:
			break;
		}
		updateBounds();
	}

	public void setState(EnemyState state) {
		switch (state) {
		case ACTIVE:
			xSpeed = movingRight ? moveSpeed : -moveSpeed;
			setSprite(Sprites.enemyThrow);
			break;
		case STUNNED:
			xSpeed = 0;
			setSprite(Sprites.enemyThrow);
			break;
		case THROWN:
		case GRABBED:
			setSprite(Sprites.enemyThrow);
			break;
		case DEAD:
			setSprite(Sprites.enemyThrow);
			ySpeed = 6;
			break;
		default:
			setSprite(Sprites.enemyThrow);
			break;
		}
		super.setState(state);
	}

	public void draw(Batch batch, float alpha) {
		/*
		 * switch (state) { case ACTIVE: batch.setColor(new Color(1, 0, 0, 1));
		 * break; case STUNNED: batch.setColor(new Color(1, 1, 0, 1)); break;
		 * case GRABBED: batch.setColor(new Color(0, 0, 1, 1)); break; case
		 * THROWN: batch.setColor(new Color(0, 1, 0, 1)); break; case DEAD:
		 * batch.setColor(new Color(1, 0, 1, 1)); break; default: break; }
		 */
		super.draw(batch, alpha);
		if (state == EnemyState.STUNNED || state == EnemyState.GRABBED || state == EnemyState.THROWN) {
			healthBar.setText("" + stunnedTimer / 10);
			healthBar.drawC(batch, getX() + 16, getY() + 40);
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