EntityCore
==========

A hybrid of the Entity-Component-System and Model-View-Controller patterns with new concepts developed specifically for games. EntityCore is easy to integrate into your game, is developed to be decoupled from a graphics library, and is very memory conscious compared to similar frameworks.

### Concepts
* [View](#views-)
* [Controller](#controllers-)
* [Component](#components-)
* [Entity](#entity-)
* [Template](#templates-)
* [Container](#containers-)
* [Filter](#filters-)

#### Views <a name=views/>

A view is responsible for drawing an entity when the Entity.draw method is called. A view can be created and linked at a later time, enabling graphics library decoupling. In other words you can make an OpenGL renderer for your game and a DirectX renderer which are entirely decoupled therefore easily interchangeable.

#### Controllers <a name=controllers/>

Controllers are invoked on an Entity that has them when the Entity.update method is called. The update state is passed to the Controller where the controller may update the state of the Entity (think physics or networking code).

#### Components  <a name=components/>

Components are values on an Entity. Components in EntityCore can be one of many types:
| Type       |                                   |
|:========== |:================================= |
| Distinct   |  |
| Distinct   |  |
| Distinct   |  |
| Distinct   |  |
| Distinct   |  |
* __Distinct__ = on the entity, each entity has it's own value for the given component.
* __Shared__ = on the entity's template, therefore entities of the same type point to the same component. (think a collision callback that gets invoked when two things collide, so you can have a different algorithm for ships and asteroids while NOT wasting space storing the callback on each entity)
* __Constant__ = shared between all entities.
* __Dynamic__ = generated upon request. (like the visual bounds of the entity)
* __Alias__ = a component that actually takes value from another. (useful when you need to have a "center" component but you already store the center as the "position" component)

#### Entity <a name=entity/>

An entity has a collection of components, a set of controllers that affect it, and can be drawn and updated. An entity is defined by it's template, which it may share or it may have a custom one. Components and Controllers can customly be added to an Entity at any time.

#### Templates <a name=templates/>

Defines the components, controllers, and view for an entity. Every entity has a template it uses to get component values, get controllers from, and get a view from.

#### Containers <a name=containers/>

Containers are Entities that can contain one or more child Entities. The container handles calling the draw and update methods of it's children automatically.
* __EntityList__ = A simple list of Entities where all child entities are updated and drawn after the container.
* __EntityChain__ = An Entity that has a "before" and "after" Entity that are updated and draw before and after the container respectively.
* __EntityLayers__ = A fixed number of layers of EntityList where the layers are drawn and updated in order. This is often used to control the ordering of drawing or which entities are not updated.

#### Filters <a name=filters/>

A filter takes an Entity (typically a container) and traverses all child Entities and returns a set that match the filtering criteria. You can create your own filter but there are existing implementations:
* __ComponentFilter__ = A filter that returns all entities that have a set of components.
* __ControllerFilter__ = A filter that returns all entities that have a ser of controllers.
* __CustomFilter__ = A filter that returns all entities which are custoly created (had components, controllers, and views dynamically added to them).
* __DefaultFilter__ = A filter that returns all entities.
* __EnabledFilter__ = A filter that returns all enabled entities (entities that can be updated).
* __ExpiredFilter__ = A filter that returns all entities that have expired (are ready for removal).
* __NegativeFilter__ = A filter that returns the opposite of a given filter.
* __TemplateContainsFilter__ = A filter that returns all entities that have a given set of components, controllers, and optionally a view.
* __TemplateExactFilter__ = A filter that returns all entities that exactly have a given Template and have not had any components, controllers, or views dynamically added.
* __TemplateRelativeFilter__ = A filter that returns all entities that have a given template or have one of it's parent templates.
* __ValueFilter__ = A filter that returns all entities that have a specific component value.
* __VisibleFilter__ = A filter that returns all visible entities (entities that can be drawn).

