package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import map.Block;
import map.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;

import data.Coord;
import data.GSB;

public class AI
{
	float middlex = 0;
	boolean[][] map;

	int minY = 1000000000, maxy = -10000000;
	int minx = 1000000000, maxx = -10000000;
	
	public AI(Map m)
	{
		for(Block c : m.getBlocks())
		{
			middlex += c.getX();
		}
		middlex /= m.getBlocks().size();
		middlex *= 256;
		
		for(Block c : m.getBlocks())
		{
			if(c.getY() < minY)
				minY = (int) c.getY();
			if(c.getY() > maxy)
				maxy = (int) c.getY();
			if(c.getX() < minx)
				minx = (int) c.getX();
			if(c.getX() > maxx)
				maxx = (int) c.getX();
		}
		minx -= 20;
		minY -= 20;
		maxx += 20;
		maxy += 20;

		map = new boolean[maxx-minx][maxy-minY];
		
		for(int i = 0 ; i < map.length ; i++)
		{
			for(int j = 0 ; j < map[0].length ; j++)
			{
				map[i][j] = false;
			}
		}
		
		for(Block b : m.getBlocks())
		{
			Coord c = new Coord(b.pos);
			c.add(-minx, -minY);
			map[c.X()][c.Y()] = true;
		}
	}
	
	private Coord convertToGrid(Coord guy)
	{
		guy.setX((int)((guy.getX()+30)/256));
		guy.setY((int)((guy.getY()+45)/256));
		guy.add(-minx, -minY);
		
		return guy;
	}
	
	public void update(Personnage me, Personnage enemy, float delta)
	{
		dumbAI(me, enemy, delta);
		//smartAI(me, enemy, delta);
		
		if(Math.abs(me.y - enemy.y) < 200)
		{
			me.fire();
		}
	}
	
	class Path
	{
		ArrayList<Coord> nodes;
		public Path()
		{
			nodes = new ArrayList<Coord>();
		}
		
		public Path(Path p)
		{
			nodes = new ArrayList<Coord>();
			for(Coord coord : p.getNodes())
			{
				nodes.add(new Coord(coord));
			}
		} 
		
		public void addNode(Coord c)
		{
			nodes.add(c);
		}
		
		public ArrayList<Coord> getNodes()
		{
			return nodes;
		}
	}
	
	private Path getPath(Coord start, Coord end)
	{
		int[][] path = new int[map.length][map[0].length];
		for(int i = 0 ; i < path.length ; i++)
		{
			for(int j = 0 ; j < path[0].length ; j++)
			{
				path[i][j] = -1;
			}
		}
		ArrayList<Coord> current = new ArrayList<Coord>();
		ArrayList<Coord> toCopy = new ArrayList<Coord>();
		current.add(start);
		int maxLoops = 20;
		int k = 0;
		
		while(--maxLoops > 0 && !current.isEmpty())
		{
			toCopy.clear();
			boolean stop = false;
			for(Coord c : current)
			{
				if(c.equals(end))
				{
					stop = true;
					break;
				}
			}
			if(stop)
			{
				break;
			}
			for(Coord c : current)
			{
				path[c.X()][c.Y()] = k;
				if(check(c.X()-1, c.Y(), path))
				{
					toCopy.add(new Coord(c.X()-1, c.Y()));
				}
				if(check(c.X()+1, c.Y(), path))
				{
					toCopy.add(new Coord(c.X()+1, c.Y()));
				}
				if(check(c.X(), c.Y()-1, path))
				{
					toCopy.add(new Coord(c.X(), c.Y()-1));
				}
				if(check(c.X(), c.Y()+1, path))
				{
					toCopy.add(new Coord(c.X(), c.Y()+1));
				}
			}
			for(int i = 0 ; i < toCopy.size() ; i++)
			{
				Coord a = toCopy.get(i);
				int count = 0;
				for(int j = 0 ; j < toCopy.size() ; j++)
				{
					if(a.equals(toCopy.get(j)))
					{
						count++;
					}
				}
				if(count >= 2)
				{
					toCopy.remove(i);
				}
			}
			
			current.clear();
			current.addAll(toCopy);
			
			k++;
		}
		
		ArrayList<Coord> pathfinded = new ArrayList<Coord>();
		Coord cur = new Coord(end);
		pathfinded.add(cur);
		System.out.println(maxLoops);
		if(maxLoops == 0)
		{
			return null;
		}
		else 
		{
			while(!cur.equals(start))
			{
				int min = 200;
				Coord mem = new Coord(0, 0);
				
				if(cur.X() > 0)
				{
					if(path[cur.X()-1][cur.Y()] < min && path[cur.X()-1][cur.Y()] != -1)
					{
						min = path[cur.X()-1][cur.Y()];
						mem = new Coord(cur.X()-1, cur.Y());
					}
				}
				
				if(cur.X() < path.length-1)
				{
					if(path[cur.X()+1][cur.Y()] < min && path[cur.X()+1][cur.Y()] != -1)
					{
						min = path[cur.X()+1][cur.Y()];
						mem = new Coord(cur.X()+1, cur.Y());
					}
				}
				
				if(cur.Y() > 0)
				{
					if(path[cur.X()][cur.Y()-1] < min && path[cur.X()][cur.Y()-1] != -1)
					{
						min = path[cur.X()][cur.Y()-1];
						mem = new Coord(cur.X(), cur.Y()-1);
					}
				}
				
				if(cur.Y() < path[0].length-1)
				{
					if(path[cur.X()][cur.Y()+1] < min && path[cur.X()][cur.Y()+1] != -1)
					{
						min = path[cur.X()][cur.Y()+1];
						mem = new Coord(cur.X(), cur.Y()+1);
					}
				}
				
				pathfinded.add(mem);
				cur = new Coord(mem);
			}
		}
		Path p = new Path();
		for(Coord coord : pathfinded)
		{
			p.addNode(coord);
		}
		return p;
	}
	
	private void smartAI(Personnage me, Personnage enemy, float delta)
	{
		lastPath = getPath(convertToGrid(new Coord(me.getX(), me.getY())), convertToGrid(new Coord(enemy.getX(), enemy.getY())));
	}
	Path lastPath;
	
	public void renderPath()
	{
		if(lastPath == null)
			return;
		GSB.srCam.begin(ShapeType.Line);
		GSB.srCam.setColor(Color.BLACK);
		ArrayList<Coord> nodes = lastPath.getNodes();
		for(Coord coord : nodes)
		{
			coord.add(minx, minY);
		}
		for(int i = 1 ; i < nodes.size() ; i++)
		{
			GSB.srCam.line(nodes.get(i-1).X()*256, nodes.get(i-1).Y()*256, nodes.get(i).X()*256, nodes.get(i).Y()*256);
		}
		GSB.srCam.end();
	}
	
	private boolean check(int x, int y, int[][] path)
	{
		if(isEmpty(x, y))
		{
			if(path[x][y] == -1)
				return true;
		}
		return false; 
	}
	
	private boolean isEmpty(int x, int y)
	{
		if(x < 0 || x > map.length-1 || y < 0 || y > map[0].length-1)
		{
			return false;
		}
		return map[x][y] == false;
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
		if(middlex > me.x)
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
			if(Math.abs(me.x - enemy.x) > random*300)
			{
				if(enemy.x > me.x)
				{
					me.move(true, delta);
				}
				if(enemy.x < me.x)
				{
					me.move(false, delta);
				}
			}
			else
			{
				if(enemy.x > me.x)
				{
					me.move(false, delta);
				}
				if(enemy.x < me.x) 
				{
					me.move(true, delta);
				}
			}
		}
		if(enemy.y > me.y+10 && !me.jumping || isInTheAir(me))
		{
			me.jump();
		}
		
		if(!me.onGround() && Math.abs(me.vy) < 1 && enemy.y > me.y+10)
		{
			me.jump();
		}
	}
	
}
