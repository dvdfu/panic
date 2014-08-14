package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
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
		setSize(32, 32);
		setSprite(Sprites.atlas.createSprite("enemy_walk"), 24, 32);
	}

	public void move() {
		if (state != EnemyState.GRABBED) {
			dy -= 0.3f;
		}
		if (state == EnemyState.STUNNED || state == EnemyState.THROWN) {
			float dxt = grounded ? 0.3f : 0.1f;
			if (dx > dxt) {
				dx -= dxt;
			} else if (dx < -dxt) {
				dx += dxt;
			} else {
				dx = 0;
				if (state == EnemyState.THROWN && grounded) {
					setState(EnemyState.STUNNED);
				}
			}
		}
		grounded = false;
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
			x = 1 - getWidth();
		}
		if (getRight() < 0) {
			x = Consts.ScreenWidth - 1;
		}
		super.act(delta);
	}

	public void collideSolid(Solid block) {
		if (state == EnemyState.GRABBED) {
			return;
		}
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + dy > block.getY() && myRect.y < block.getY()) {
				y = block.getY() - getHeight();
			}
			if (getTop() + dy > block.getTop() && myRect.y < block.getTop()) {
				y = block.getTop();
				grounded = true;
			}
			dy = 0;
			if (state == EnemyState.THROWN && dx == 0) {
				setState(EnemyState.STUNNED);
			}
			if (state == EnemyState.DEAD) {
				setState(EnemyState.REMOVE);
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + dx > block.getX() && myRect.x < block.getX()) {
				x = block.getX() - getWidth();
				if (state == EnemyState.THROWN) {
					dx = -dx;
				} else {
					dx = 0;
					dy = 0;
				}
			}
			if (getRight() + dx > block.getRight() && myRect.x < block.getRight()) {
				x = block.getRight();
				if (state == EnemyState.THROWN) {
					dx = -dx;
				} else {
					dx = 0;
					dy = 0;
				}
			}
		}
	}

	public void collideEnemy(AbstractEnemy enemy) {
		switch (state) {
		case ACTIVE:
			if (bounds.overlaps(enemy.bounds)) {
				if (enemy.state == EnemyState.STUNNED && grounded) {
					if (dx > 0) {
						x = enemy.getX() - getWidth();
						enemy.launch(3, 0);
					}
					if (dx < 0) {
						x = enemy.getRight();
						enemy.launch(-3, 0);
					}
					dx = -dx;
					movingRight ^= true;
				}
			}
			break;
		case GRABBED:
			if (bounds.overlaps(enemy.bounds)) {
				if (enemy.state == EnemyState.ACTIVE || enemy.state == EnemyState.STUNNED) {
					enemy.setState(EnemyState.DEAD);
					enemy.launch(dx == 0 ? enemy.dx * 2 : dx / 2, 5);
				}
			}
			break;
		case THROWN:
			if (bounds.overlaps(enemy.bounds)) {
				if (enemy.state == EnemyState.ACTIVE || enemy.state == EnemyState.STUNNED) {
					enemy.setState(EnemyState.DEAD);
					enemy.launch(dx == 0 ? enemy.dx * 2 : dx / 2, 5);
				}
			}
			break;
		default:
			break;
		}
	}

	public void setState(EnemyState state) {
		switch (state) {
		case ACTIVE:
			dx = movingRight ? moveSpeed : -moveSpeed;
			setSprite(Sprites.atlas.createSprite("enemy_walk"), 24, 32);
			break;
		case STUNNED:
			dx = 0;
			setSprite(Sprites.atlas.createSprite("enemy_throw"), 22, 16);
			break;
		case THROWN:
		case GRABBED:
			setSprite(Sprites.atlas.createSprite("enemy_throw"), 22, 16);
			break;
		case DEAD:
			setSprite(Sprites.atlas.createSprite("enemy_throw"), 22, 16);
			dy = 6;
			break;
		default:
			setSprite(Sprites.atlas.createSprite("enemy_walk"), 24, 32);
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
			healthBar.drawC(batch, x + 16, y + 40);
		}
	}

	public void reset() {
		state = EnemyState.ACTIVE;
		movingRight = MathUtils.randomBoolean();
		if (movingRight) {
			x = 1 - getWidth() + MathUtils.random(160);
			dx = moveSpeed;
		} else {
			x = Consts.ScreenWidth - MathUtils.random(160) - 1;
			dx = -moveSpeed;
		}
		y = Consts.ScreenHeight;
		dy = 0;
		setPosition(x, y);
	}
}