EntityCore
==========

An extension to the Entity-Component pattern with new concepts developed specifically for games. EntityCore is easy to integrate into your game, is developed to be decoupled from a graphics library, and is very memory conscious compared to similar frameworks.

### Concepts
* [View](#views-)
* [Controller](#controllers-)
* [Component](#components-)
* [Entity](#entity-)
* [Template](#templates-)

#### Views <a name=views/>

A view is responsible for drawing an entity when the Entity.draw method is called. A view can be created and linked at a later time, enabling graphics library decoupling. In other words you can make an OpenGL renderer for your game and a DirectX renderer which are entirely decoupled therefore easily interchangeable.

#### Controllers <a name=controllers/>

Controllers are invoked on an Entity that has them when the Entity.update method is called. The update state is passed to the Controller where the controller may update the state of the Entity (think physics or networking code).

#### Components  <a name=components/>

Components are values on an Entity. Components in EntityCore can be one of many types:  
* _Distinct_ = on the entity, each entity has it's own value for the given component.
* _Shared_ = on the entity's template, therefore entities of the same type point to the same component. (think a collision callback that gets invoked when two things collide, so you can have a different algorithm for ships and asteroids while NOT wasting space storing the callback on each entity)
* _Constant_ = shared between all entities.
* _Dynamic_ = generated upon request. (like the visual bounds of the entity)
* _Alias_ = a component that actually takes value from another. (useful when you need to have a "center" component but you already store the center as the "position" component)

#### Entity <a name=entity/>

An entity has a collection of components, a set of controllers that affect it, and can be drawn and updated. An entity is defined by it's template, which it may share or it may have a custom one. Components and Controllers can customly be added to an Entity at any time.

#### Templates <a name=templates/>

Defines the components, controllers, and view for an entity. Every entity has a template it uses to get component values, get controllers from, and get a view from.


