package particles;

import org.magnos.entity.Ents;
import org.magnos.entity.Template;
import org.magnos.entity.util.ComponentSet;
import org.magnos.entity.util.ControllerSet;


public class Templates
{
    public static Template PARTICLE_SYSTEM = Ents.newTemplate( "particle-system", new ComponentSet(), new ControllerSet(), Views.PARTICLE_SYSTEM );
}
