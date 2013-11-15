package particles;

import org.magnos.entity.Entity;

public interface EmitterPosition
{
    public void setPosition(Entity e, Vector suggestDirection);
}
