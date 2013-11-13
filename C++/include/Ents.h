#ifndef ID_H
#define ID_H

class Entity;
class View;
class Controller;
class ComponentBase;
class Template;

class EntityListener 
{
	virtual void onEntityAdd( Entity &e, const size_t &id ) = 0;
	virtual void onEntityRemove( Entity &e );
	virtual void onCoreClear() = 0;
	virtual void onViewAdd( View& view, const bool &definition ) = 0;
	virtual void onControllerAdd( Controller& controller, const bool &definition ) = 0;
	virtual void onComponentAdd( Id component, const bool &definition ) = 0;
	virtual void onTemplateAdd( const Template* temp ) = 0;
};

struct Renderer
{
	virtual Renderer& create(Entity& e) = 0;
	virtual void begin(Entity& e, void *drawState) = 0;
	virtual void end(Entity& e, void *drawState) = 0;
	virtual void destroy(Entity& e) = 0;
};

class Ents
{
protected:
	static EntityListener* listener;

	static size_t register( Entity& e )
	{
		IndexPool indices = getEntityIds();
		size_t id = indices.pop();

		if (listener != nullptr)
		{
			listener->onEntityAdd( e, id );
		}

		return id;
	}

	static void unregister( Entity& e )
	{
		IndexPool indices = getEntityIds();
		indices.push( e.id );

		if (listener != nullptr)
		{
			listener->onEntityRemove( e );
		}
	}

	static View& registerView( const bool &definition, View& view )
	{
		if (listener != nullptr)
		{
			listener->onViewAdd( view, definition );
		}

		return view;
	}

	static Controller& registerController( const bool &definition, Controller& controller )
	{
		if (listener != nullptr)
		{
			listener->onControllerAdd( controller, definition );
		}

		return controller;
	}

	template<typename T>
	static Component<T>& registerComponent( const bool &definition, Component<T>& component )
	{
		if (listener != nullptr)
		{
			listener->onComponentAdd( component, definition );
		}

		return component;
	}

	static Template* registerTemplate( Template *temp )
	{
		if (listener != nullptr)
		{
			listener->onTemplateAdd( temp );
		}

		return temp;
	}

public:

	/*
	 * VIEWS
	 */

	static View& newView( const std::string &name )
	{
		return newView( name, View::NO_RENDERER );
	}

	static View& newView( const std::string &name, Renderer& defaultRenderer )
	{
		IdContainer<View> views = getViews();

		return registerView( true, views.addDefinition( new View( views.nextId(), name, defaultRenderer ) ) );
	}

	static void setViewDefault( View& view, Renderer& renderer )
	{
		IdContainer<View> views = getViews();

		views.getDefinition( view ).renderer = renderer;
	}

	static View& newViewAlternative( View& view, Renderer& renderer )
	{
		IdContainer<View> views = getViews();		

		return  registerView( false, views.addInstance( new View( view.id, view.name, renderer ) ) );
	}


	/*
	 * CONTROLLERS
	 */


	static Controller& newController( const std::string &name )
	{
		return newController( name, Controller::NO_CONTROL );
	}

	static Controller& newController( const std::string &name, std::function<void(Entity&,void*)> &control )
	{
		IdContainer<Controller> controllers = getControllers();

		return registerController( true, controllers.addDefinition( new Controller( controllers.nextId(), name, control ) ) );
	}

	static void setControllerDefault( Controller& controller, std::function<void(Entity&,void*)> &control )
	{
		IdContainer<Controller> controllers = getControllers();

		controllers.getDefinition( controller ).control = control;
	}

	static Controller& newControllerAlternative( Controller& controller, std::function<void(Entity&,void*)> &control )
	{
		IdContainer<Controller> controllers = getControllers();

		return registerController( false, controllers.addInstance( new Controller( controller.id, controller.name, control ) ) );
	}

	/*
	 * COMPONENTS
	 */

	template<typename T>
	static Component<T>& newComponentUndefined( const std::string &name )
	{
		IdContainer<Id> components = getComponents();

		return registerComponent( true, components.addDefinition( ComponentUndefined<T>( components.nextId(), name ) ) );
	}
	
	template<typename T>
	static Component<T>& newComponent( const std::string &name )
	{
		IdContainer<Id> components = getComponents();		

		return registerComponent( true, components.addDefinition( ComponentDistinct<T>( components.nextId(), name ) ) );
	}
	
	template<typename T>
	static Component<T>& newComponentAlternative( const Component<T>& component )
	{
		IdContainer<Id> components = getComponents();		

		return registerComponent( false, components.addInstance( ComponentDistinct<T>( component.id, component.name ) ) );
	}

	static Template* newTemplate()
	{
		return new Template();
	}

	static Template* newTemplate( const std::string &name, std::initializer_list<Id> &components, std::initializer_list<Controller> &controllers, View &view)
	{
		IdContainer<Template*> templates = getTemplates();

		return registerTemplate( templates.addDefinition( new Template( templates.nextId(), name, nullptr, components, controllers, view ) ) );
	}

};

IndexPool& getEntityIds() {
	static IndexPool indices;
	return indices;
}

IdContainer<View>& getViews() {
	static IdContainer<View> views;
	return views;
}

IdContainer<Controller>& getControllers() {
	static IdContainer<Controller> controllers;
	return controllers;
}

IdContainer<Id>& getComponents() {
	static IdContainer<Id> components;
	return components;
}

IdContainer<Template*>& getTemplates() {
	static IdContainer<Template*> templates;
	return templates;
}

#endif