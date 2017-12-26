package dsa.upc.edu.talesofeetacclient.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * The GameImage class represents an image used in the game.
 * 
 * Each instance contains a Bitmap image, a width / height
 * and an on-screen position.
 * 
 * @author Dan Ruscoe (ruscoe.org)
 * @version 1.0
 */
public class GameImage 
{
	protected Bitmap bitmap = null;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;

	public GameImage(Context context)
	{
	}
	
	public GameImage(Context context, int drawable)
	{
		this.setDrawable(context, drawable);
	}

	public void setDrawable(Context context, int drawable)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);

		width = this.bitmap.getWidth();
		height = this.bitmap.getHeight();
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			this.bitmap = bitmap;
			this.width = bitmap.getWidth();
			this.height = bitmap.getHeight();
		}
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmap;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}
	
	public void setX(int x)
	{
        this.x = x;
    }

	public int getX()
	{
		return this.x;
	}

	public void setY(int y)
	{
        this.y = y;
	}

	public int getY()
	{
		return this.y;
	}

	public void setCenterX(int centerX)
	{
		this.x = (centerX - (this.getWidth() / 2));
	}
	
	public int getCenterX()
	{
		return (x + (this.getWidth() / 2));
	}
	
	public void setCenterY(int centerY)
	{
		this.y = (centerY - (this.getHeight() / 2));
	}
	
	public int getCenterY()
	{
		return (y + (this.getHeight() / 2));
	}
}
