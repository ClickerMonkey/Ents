EntityCore
==========

![Development](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_development.png)

A hybrid of the Entity-Component-System and Model-View-Controller patterns with new concepts developed specifically for games. EntityCore is easy to integrate into your game, is developed to be decoupled from a graphics library, and is very memory conscious compared to similar frameworks.

#### What are you talking about?

EntityCore is a library for aiding in creating games where you create your game objects through composition. 
No more creating classes, no more messy update logic, no more rendering coupled to a specific graphics library.
You add components to your Entities (i.e. position, velocity, size, angle, image), controllers (i.e. user input, physics, networking, AI), and a view.
EntityCore is designed to promote modular development, you are no longer creating a sprite which has physics, you are defining Entities that can have position and velocity, and you create a Physics controller that takes velocity and adds it to position.

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

| Type       | Definition                        |
|:---------- |:--------------------------------- |
| Distinct   | On the entity, each entity has it's own value for the given component |
| Shared     | On the entity's template, therefore entities of the same type point to the same component. (think a collision callback that gets invoked when two things collide, so you can have a different algorithm for ships and asteroids while NOT wasting space storing the callback on each entity) |
| Constant   | Shared between all entities |
| Dynamic    | Generated upon request (like the visual bounds of the entity) |
| Alias      | A component that actually takes value from another (useful when you need to have a "center" component but you already store the center as the "position" component) |

##### Alternatives

You can define "alternative" components. You could define a component as a distinct component that stores the Entity's center, then optionally you could create a dynamic alternative which can compute the center upon request but space isn't wasted on storing the center if the Entity stores position by some other means.  
There's a great power that lies in this, using distinct components is like having simple getters and setters on your object, but with alternatives you can override what the setters and getters do without the mess of actually extending a class. Alternatives offer a dynamic and clean way to have fine control on your data. These benefits also apply going the other direction, you could define a component as being shared between all entities of a given template, however you could make one of those entities actually have it's own distinct component value.

#### Entity <a name=entity/>

An entity has a collection of components, a set of controllers that affect it, and can be drawn and updated. An entity is defined by it's template, which it may share or it may have a custom one. Components and Controllers can customly be added to an Entity at any time.

#### Templates <a name=templates/>

Defines the components, controllers, and view for an entity. Every entity has a template it uses to get component values, get controllers from, and get a view from.

#### Containers <a name=containers/>

Containers are Entities that can contain one or more child Entities. The container handles calling the draw and update methods of it's children automatically.

| Container    | Definition                        |
|:------------ |:--------------------------------- |
| EntityList   | A simple list of Entities where all child entities are updated and drawn after the container |
| EntityChain  | An Entity that has a "before" and "after" Entity that are updated and draw before and after the container respectively |
| EntityLayers | A fixed number of layers of EntityList where the layers are drawn and updated in order. This is often used to control the ordering of drawing or which entities are not updated |

#### Filters <a name=filters/>

A filter takes an Entity (typically a container) and traverses all child Entities and returns a set that match the filtering criteria. You can create your own filter but there are existing implementations:

| Filter                 | Definition                        |
|:---------------------- |:--------------------------------- |
| ComponentFilter        | A filter that returns all entities that have a set of components |
| ControllerFilter       | A filter that returns all entities that have a ser of controllers |
| CustomFilter           | A filter that returns all entities which are custoly created (had components, controllers, and views dynamically added to them) |
| DefaultFilter          | A filter that returns all entities |
| EnabledFilter          | A filter that returns all enabled entities (entities that can be updated) |
| ExpiredFilter          | A filter that returns all entities that have expired (are ready for removal) |
| TemplateContainsFilter | A filter that returns all entities that have a given set of components, controllers, and optionally a view |
| TemplateExactFilter    | A filter that returns all entities that exactly have a given Template and have not had any components, controllers, or views dynamically added |
| TemplateRelativeFilter | A filter that returns all entities that have a given template or have one of it's parent templates |
| ValueFilter            | A filter that returns all entities that have a specific component value |
| VisibleFilter          | A filter that returns all visible entities (entities that can be drawn) |
| NotFilter              | A filter that returns the opposite of a given filter |
| AndFilter              | A filter that returns the entities that are valid for filter A and filter B |
| OrFilter               | A filter that returns the entities that are valid for filter A or filter B |
| XorFilter              | A filter that returns the entities that are valid for filter A xor filter B |


