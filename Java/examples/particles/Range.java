package particles;

import java.util.Random;


public class Range
{
    
    public static Random RANDOM = new Random();
    
    public float min, max;
    
    public Range()
    {
    }
    
    public Range(float x)
    {
        min = max = x;
    }
    
    public Range(float a, float b)
    {
        min = Math.min( a, b );
        max = Math.max( a, b );
    }
    
    public Range(int x)
    {
        min = max = x;
    }
    
    public Range(int a, int b)
    {
        min = Math.min( a, b );
        max = Math.max( a, b );
    }
    
    public void set(Range r)
    {
        min = r.min;
        max = r.max;
    }
    
    public int randomInt()
    {
        return (int)(RANDOM.nextFloat() * (max - min + 1) + min);
    }
    
    public float randomFloat()
    {
        return RANDOM.nextFloat() * (max - min) + min;
    }

}
