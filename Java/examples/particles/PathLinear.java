package particles;


public class PathLinear<T extends Attribute<T>> implements Path<T>
{

    public T[] points;
    
    public PathLinear(T ... points)
    {
        this.points = points;
    }
    
    @Override
    public T set( T target, float delta )
    {
        float a = delta * (points.length - 1);
        int index = Math.max( 0, Math.min( (int)a, points.length - 2) );
        float rdelta = a - index;
        
        target.set( points[index] );
        target.add( points[index + 1], rdelta );
        
        return target;
    }
    
}
