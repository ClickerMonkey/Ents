
package particles;

public interface Path<T extends Attribute<T>>
{
    public T set( T target, float delta );
}
