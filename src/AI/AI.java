package AI;

import game.Personnage;

import java.util.HashSet;
import java.util.Set;

import map.Block;
import map.Map;
import screens.Game;
import screens.Options;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

import data.Coord;
import data.GSB;

public class AI
{
	float middlex = 0;
	
	Network n;
	
	public AI(Map m)
	{
		generateNetwork();
		
		for(Block b : m.getBlocks())
		{
			n.remove(b.pos);
		}
		Set<Coord> toRemove = new HashSet<Coord>();
		for(Coord c : n.nodes.keySet())
		{
			boolean remove = true;
			for(Block b : m.getBlocks())
			{
				if(b.getX() == c.getX())
					if(c.getY() >= b.getY())
					{
						remove = false;
					}
			}
			if(remove)
				toRemove.add(new Coord(c));
		}
		
		for(Coord c : toRemove)
		{
			n.remove(c);
		}
	}
	
	
	public void generateNetwork()
	{
		int extend = 1;
		n = new Network();
		for(float x = Game.getMinx() - extend ; x <= Game.getMaxx() + extend ; x++)
		{
			for(float y = Game.getMiny() - extend ;y <= Game.getMaxy() + extend + 5 ; y++)
			{
				n.add(new Node(x, y));
			}
		}
		for(float x = Game.getMinx() - extend ; x <= Game.getMaxx() + extend ; x++)
		{
			for(float y = Game.getMiny() - extend ;y <= Game.getMaxy() + extend + 5; y++)
			{
				n.addLink(new Coord(x, y), new Coord(x + 1,  y));
				n.addLink(new Coord(x, y), new Coord(x - 1,  y));
				n.addLink(new Coord(x, y), new Coord(x,  y + 1));
				n.addLink(new Coord(x, y), new Coord(x,  y - 1));
			}
		}
		n.links.clear(n.nodes);
	}
	
	public void update(Personnage me, Personnage enemy, float delta)
	{
		if(Options.get("advancedIA"))
		{
			smartAI(me, enemy, delta);
		}
		else
		{
			dumbAI(me, enemy, delta);
		}
		
		if(Math.abs(me.getY() - enemy.getY()) < 200)
		{
			me.fire();
		}
	}
	boolean once = true;
	private void smartAI(Personnage me, Personnage enemy, float delta)
	{
		Coord mepos = n.getNearest(me.getX()/256, me.getY()/256);
		Coord youpos = n.getNearest(enemy.getX()/256, enemy.getY()/256);
		if(!(mepos.equals(abc) && youpos.equals(def)))
		{
			lastPath = Pathfinding.getPath(mepos, youpos, n.nodes, n.links);	
			abc = mepos;
			def = youpos;
		}
		if(lastPath != null)
		{
			Coord toGo = lastPath.getCoord(1);
			
			if(toGo.getY()*256-128 > me.getY() && !(me.jumps == 1))
				me.jump();
			if(toGo.getX()*256 < me.getX())
				me.move(false, delta);
			if(toGo.getX()*256 > me.getX())
				me.move(true, delta);
			if((!me.onGround() && Math.abs(me.getVy()) < 1 && toGo.getY()*256-128 > me.getY())  || isInTheAir(me))
			{
				me.jump();
			}
		}
		else {
			dumbAI(me, enemy, delta);
		}
	}
	Coord abc, def;
	Path lastPath;
	
	public void renderPath()
	{
		if(lastPath == null)
			return;
		GSB.srCam.begin(ShapeType.Line);
		GSB.srCam.setColor(Color.BLACK);
			n.renderLinks();
		GSB.srCam.end();
		GSB.srCam.begin(ShapeType.Filled);
			n.renderNetwork(abc, def);
			lastPath.renderPath();
		GSB.srCam.end();
	}
	
	float random, randomcount = 1;
	
	private boolean isInTheAir(Personnage pers)
	{
		for(Polygon p : pers.getCollisions())
		{
			float x = pers.getX();
			
			if(x > p.getX() && x < p.getX()+256)
			{
				return false;
			}
		}
		return true;
	}
	
	private void iDontKnowWhatToDo(Personnage me, Personnage enemy, float delta)
	{
		me.jump();
		if(middlex > me.getX())
			me.move(true, delta);
		else
			me.move(false, delta);
	}
	
	private void dumbAI(Personnage me, Personnage enemy, float delta)
	{
		randomcount -= delta;
		if(randomcount < 0)
		{
			random = (float) Math.random();
			randomcount = 1;
		}

		if(isInTheAir(enemy))
		{
			this.iDontKnowWhatToDo(me, enemy, delta);
		}
		else
		{
			if(Math.abs(me.getX() - enemy.getX()) > random*300)
			{
				if(enemy.getX() > me.getX())
				{
					me.move(true, delta);
				}
				if(enemy.getX() < me.getX())
				{
					me.move(false, delta);
				}
			}
			else
			{
				if(enemy.getX() > me.getX())
				{
					me.move(false, delta);
				}
				if(enemy.getX() < me.getX()) 
				{
					me.move(true, delta);
				}
			}
		}
		if(enemy.getY() > me.getY()+10 && !me.jumping || isInTheAir(me))
		{
			me.jump();
		}
		
		if(!me.onGround() && Math.abs(me.getVy()) < 1 && enemy.getY() > me.getY()+10)
		{
			me.jump();
		}
	}
	
}
