package particles;

import org.magnos.entity.ComponentValueFactory;


public class PathComponentValueFactory<T extends Attribute<T>> implements ComponentValueFactory<Path<T>>
{

    @Override
    public Path<T> create()
    {
        return null;
    }

    @Override
    public Path<T> clone( Path<T> value )
    {
        return value;
    }

    @Override
    public Path<T> copy( Path<T> from, Path<T> to )
    {
        return to;
    }

}
