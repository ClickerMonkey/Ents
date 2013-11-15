package particles;


public class PathTween<T extends Attribute<T>> implements Path<T>
{

    public T start;
    public T end;
    
    public PathTween(T start, T end)
    {
        this.start = start;
        this.end = end;
    }
    
    @Override
    public T set( T target, float delta )
    {
        target.set( start );
        target.scale( 1.0f - delta );
        target.add( end, delta );
        
        return target;
    }
    
}
