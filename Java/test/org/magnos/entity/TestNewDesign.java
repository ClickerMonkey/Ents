package org.magnos.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.magnos.entity.helper.Bounds;
import org.magnos.entity.helper.Scalar;
import org.magnos.entity.helper.Vector;



@SuppressWarnings("unchecked")
public class TestNewDesign {
	
	public static class Bag<T> {
		T[] values;
		public Bag(T ... values) {
			this.values = values;
		}
		public static <T> Bag<T> of(T ... values) {
			return new Bag<T>(values);
		}
		public static <T> Bag<T> empty(Class<T> type) {
			return new Bag<T>();
		}
	}

	public static class Id {
		final int index;
		final String name;
		public Id(int index, String name) {
			this.index = index;
			this.name = name;
		}
	}

	public static interface Renderer {
		void draw(Entity e, Object drawState);
	}

	public static class View extends Id {
		Renderer renderer;
		View(int index, String name, Renderer renderer) {
			super(index, name);
			this.renderer = renderer;
		}
	}
	
	public static interface Control {
		void control(Entity e, Object updateState);
	}
	
	public static class Controller extends Id {
		Control control;
		Controller(int index, String name, Control control) {
			super(index, name);
			this.control = control;
		}
	}
	
	public static class Template extends Id {
		public static final int CUSTOM = Integer.MAX_VALUE;
		public static final String CUSTOM_NAME = "custom";
		
		final Template parent;
		
		Component<?>[] components;
		BitSet componentsBitSet;
		Controller[] controllers;
		BitSet controllerBitSet;
		TemplateComponent<?>[] handlers = {};
		ComponentFactory<?>[] factories = {};
		View view;
		int instances;
		
		Template() {
			this(CUSTOM, CUSTOM_NAME, null, new Bag<Component<?>>(), new Bag<Controller>(), null);
		}
		
		Template(int id, String name, Template parent, Bag<Component<?>> component, Bag<Controller> controllers, View view) {
			super(id, name);
			
			this.parent = parent;
			this.components = component.values;
			this.controllers = controllers.values;
			this.view = view;
			
			for (int i = 0; i < components.length; i++) {
				Component<?> c = components[i];
				if (has(c)) {
					throw new RuntimeException( "A template cannot have the same component more than once!" );
				}
				TemplateComponent<?> handler = c.add(this);
				ensureFit(c);
				handlers[c.index] = handler;
			}
		}
		
		Template extend(int id, String name) {
			return new Template( id, name, this, new Bag<Component<?>>(components), new Bag<Controller>(controllers), view );
		}
		
		Object[] createDefaultValues() {
			final int n = factories.length;
			Object[] values = new Object[n];
			for (int i = 0; i < n; i++) {
				values[i] = factories[i].create();
			}
			return values;
		}
		public void newInstance() {
			instances++;
		}
		public void removeInstance() {
			instances--;
		}
		<T> boolean has(Component<T> component) {
			return component.index < handlers.length && handlers[component.index] != null;
		}
		<T> boolean hasExact(Component<T> component) {
			return has(component) && EntityUtility.indexOfSame(components, component) != -1;
		}
		boolean has(Component<?> ... components) {
			for (Component<?> c : components) {
				if (!has(c)) {
					return false;
				}
			}
			return true;
		}
		<T> void add(Component<T> component) {
			ensureFit(component);
			TemplateComponent<?> existing = handlers[component.index];
			if (existing != null) {
				existing.remove(this);
			}
			handlers[component.index] = component.add(this);
			if (existing == null) {
				componentsBitSet.set(components.length);
				components = EntityUtility.append(components, component);
			}
		}
		
		boolean has(Controller controller) {
			return controllerBitSet.get(controller.index);
		}
		boolean hasExact(Controller controller) {
			return has(controller) && EntityUtility.indexOfSame(controllers, controller) != -1;
		}
		boolean has(Controller ... controllers) {
			for (Controller c : controllers) {
				if (!has(c)) {
					return false;
				}
			}
			return true;
		}
		void add(Controller controller) {
			if (!has(controller)) {
				controllerBitSet.set(controllers.length);
				controllers = EntityUtility.append(controllers, controller);
			}
		}
		
		boolean has(View view) {
			return (this.view.index == view.index);
		}
		boolean hasExact(View view) {
			return this.view == view;
		}
		void setView(View view) {
			this.view = view;
		}
		
		private void ensureFit(Id id) {
			if (handlers.length <= id.index) {
				handlers = Arrays.copyOf(handlers, id.index + 1);
			}
		}
		
		boolean isCustom() {
			return (index == CUSTOM);
		}
		
		<T> Template addCustomComponent(Component<T> component) {
			if (hasExact(component)) {
				return this;
			}
			Template t = getCustomTemplate();
			t.add(component);
			return t;
		}
		Template addCustomController(Controller controller) {
			if (hasExact(controller)) {
				return this;
			}
			Template t = getCustomTemplate();
			t.add(controller);
			return t;
		}
		Template setCustomView(View view) {
			if (hasExact(view)) {
				return this;
			}
			Template t = getCustomTemplate();
			t.setView(view);
			return t;
		}
		
		Template getCustomTemplate() {
			return isCustom() && instances == 1 ? this : extend(CUSTOM, CUSTOM_NAME);
		}
	}
	
	public static class Entity {
		Template template;
		Object[] values;
		
		public Entity(Template template) {
			this( template, template.createDefaultValues() );
		}
		private Entity(Template template, Object[] values) {
			this.setTemplate(template);
			this.values = values;
		}
		protected boolean setTemplate(Template newTemplate) {
			boolean changed = (template != newTemplate);
			if (changed) {
				if (template != null) {
					template.removeInstance();
				}
				(template = newTemplate).newInstance();
			}
			return changed;
		}
		public <T> T get(Component<T> component) {
			TemplateComponent<T> ch = (TemplateComponent<T>)template.handlers[component.index];
			return ch.get(this);
		}
		public <T> T gets(Component<T> component) {
			if (!template.has(component)) {
				return null;
			}
			return get(component);
		}
		public <T> T gets(Component<T> component, T missingValue) {
			if (!template.has(component)) {
				return missingValue;
			}
			return get(component);
		}
		public <T> void set(Component<T> component, T value) {
			TemplateComponent<T> ch = (TemplateComponent<T>)template.handlers[component.index];
			ch.set(this, value);
		}
		public <T> boolean sets(Component<T> component, T value) {
			boolean has = template.has(component);
			if (has) {
				set(component, value);
			}
			return has;
		}
		
		public Entity clone(boolean deep) {
			final int valueCount = values.length;
			Object[] clonedValues = new Object[valueCount];
			if (deep) {
				for (int i = 0; i < valueCount; i++) {
					ComponentFactory<Object> factory = (ComponentFactory<Object>)template.factories[i]; 
					clonedValues[i] = factory.clone(values[i]);
				}	
			} else {
				System.arraycopy(values, 0, clonedValues, 0, valueCount);
			}
			return new Entity( template, clonedValues );
		}
		
		public <T> void add(Component<T> component) {
			if (setTemplate(template.addCustomComponent(component))) {
				component.postCustomAdd(this);
			}
		}
		public <T> void add(Controller controller) {
			setTemplate(template.addCustomController(controller));
		}
		public <T> void setView(View view) {
			setTemplate(template.setCustomView(view));
		}
	}
	
	public static class EntityCore {
		// Views
		private ArrayList<View> views = new ArrayList<View>( 64 );
		public View newView(String name) {
			return newView( name, null );
		}
		public View newView(String name, Renderer defaultRenderer) {
			View v = new View(views.size(), name, defaultRenderer);
			views.add(v);
			return v;
		}
		public void setViewDefault(View view, Renderer defaultRenderer) {
			views.get( view.index ).renderer = defaultRenderer;
		}
		public View newViewAlternative(View view, Renderer renderer) {
			return new View(view.index, view.name, renderer);
		}
		
		// Controllers
		private ArrayList<Controller> controllers = new ArrayList<Controller>( 64 );
		public Controller newController(String name) {
			return newController( name, null );
		}
		public Controller newController(String name, Control defaultControl) {
			Controller v = new Controller(controllers.size(), name, defaultControl);
			controllers.add(v);
			return v;
		}
		public void setControllerDefault(Controller controller, Control defaultControl) {
			controllers.get( controller.index ).control = defaultControl;
		}
		public Controller newControllerAlternative(Controller controller, Control control) {
			return new Controller(controller.index, controller.name, control);
		}
		
		// Components
		private ArrayList<Component<?>> components = new ArrayList<Component<?>>( 64 );
		public <T> Component<T> newComponent(String name) {
			return newComponent( new ComponentUndefined<T>( components.size(), name ) );
		}
		// Entity contains component value
		public <T> Component<T> newComponent(String name, ComponentFactory<T> factory) {
			return newComponent( new ComponentDistinct<T>( components.size(), name, factory ) );
		}
		public <T> Component<T> newComponentAlternative(Component<T> component, ComponentFactory<T> factory) {
			return new ComponentDistinct<T>(component.index, component.name, factory );
		}
		// Template contains component value
		public <T> Component<T> newComponentShared(String name, ComponentFactory<T> factory) {
			return newComponent( new ComponentShared<T>( components.size(), name, factory ) );
		}
		public <T> Component<T> newComponentSharedAlternative(Component<T> component, ComponentFactory<T> factory) {
			return new ComponentShared<T>( component.index, component.name, factory );
		}
		// Nothing contains component value, is calculated upon request.
		public <T> Component<T> newComponentDynamic(String name, Dynamic<T> dynamic) {
			return newComponent( new ComponentDynamic<T>(components.size(), name, dynamic ) );
		}
		public <T> Component<T> newComponentDynamicAlternative(Component<T> component, Dynamic<T> dynamic) {
			return new ComponentDynamic<T>(component.index, component.name, dynamic);
		}
		// All entities share same value
		public <T> Component<T> newComponentConstant(String name, T constant, boolean settable) {
			return newComponent( new ComponentConstant<T>(components.size(), name, constant, settable) );
		}
		public <T> Component<T> newComponentConstantAlternative(Component<T> component, T constant, boolean settable) {
			return new ComponentConstant<T>(component.index, component.name, constant, settable);
		}
		
		private <T> Component<T> newComponent(Component<T> c) {
			components.add(c);
			return c;
		}
		
		// Template
		private ArrayList<Template> templates = new ArrayList<Template>();
		
		public Template newTemplate( String name, Bag<Component<?>> components, Bag<Controller> controllers, View view) {
			Template t = new Template(templates.size(), name, null, components, controllers, view);
			templates.add(t);
			return t;
		}
	}
	
	public static abstract class Component<T> extends Id {
		Component(int index, String name) {
			super(index, name);
		}
		public abstract TemplateComponent<T> add(Template template);
		public abstract void postCustomAdd(Entity e);
	}
	
	public static interface TemplateComponent<T> {
		public void set(Entity e, T value);
		public T get(Entity e);
		public void remove(Template template);
	}
	
	public static interface Dynamic<T>{
		T get(Entity e);
		void set(Entity e, T target);
	}
	
	public static class ComponentUndefined<T> extends Component<T> {
		ComponentUndefined(int index, String name) {
			super(index, name);
		}
		public void postCustomAdd(Entity e) {
			
		}
		public TemplateComponent<T> add(Template template) {
			throw new RuntimeException("An undefined component cannot be added to an Template");
		}
	}
	
	public static class ComponentDistinct<T> extends Component<T> {
		ComponentFactory<T> factory;
		public ComponentDistinct(int index, String name, ComponentFactory<T> factory) {
			super(index, name);
			this.factory = factory;
		}
		public void postCustomAdd(Entity e) {
			e.values = EntityUtility.append(e.values, factory.create());
		}
		public TemplateComponent<T> add(Template template) {
			final ComponentFactory<?>[] factories = template.factories;
			int i = 0;
			while (i < factories.length) {
				if (factories[i] == null) {
					break;
				}
				i++;
			}
			if (i == factories.length) {
				template.factories = EntityUtility.append(factories, factory);	
			} else {
				factories[i] = factory;
			}
			return new ComponentValuedHandler(i);
		}
		
		private class ComponentValuedHandler implements TemplateComponent<T> {
			int componentIndex;
			public ComponentValuedHandler(int index) {
				this.componentIndex = index;
			}
			public void set(Entity e, T value) {
				e.values[componentIndex] = value;
			}
			public T get(Entity e) {
				return (T)e.values[componentIndex];
			}
			public void remove(Template template) {
				template.factories[componentIndex] = null;
			}
		}
	}
	
	public static class ComponentDynamic<T> extends Component<T> {
		Dynamic<T> dynamic;
		ComponentDynamicHandler handler;
		public ComponentDynamic(int index, String name, Dynamic<T> dynamic) {
			super(index, name);
			this.dynamic = dynamic;
			this.handler = new ComponentDynamicHandler();
		}
		public void postCustomAdd(Entity e) {
			
		}
		public TemplateComponent<T> add(Template template) {
			return handler;
		}
		private class ComponentDynamicHandler implements TemplateComponent<T> {
			public void set(Entity e, T value) { 
				dynamic.set(e, value);
			}
			public T get(Entity e) {
				return dynamic.get(e);
			}
			public void remove(Template template) { 
				
			}
		}
	}
	
	public static class ComponentShared<T> extends Component<T> {
		ComponentFactory<T> factory;
		public ComponentShared(int index, String name, ComponentFactory<T> factory) {
			super(index, name);
			this.factory = factory;
		}
		public void postCustomAdd(Entity e) {
			
		}
		public TemplateComponent<T> add(Template template) {
			return new ComponentSharedHandler( factory.create() );
		}
		private class ComponentSharedHandler implements TemplateComponent<T> {
			T value;
			public ComponentSharedHandler(T value) {
				this.value = value;
			}
			public void set(Entity e, T value) {
				this.value = value;
			}
			public T get(Entity e) {
				return value;
			}
			public void remove(Template template) {
				
			}
		}
	}
	
	public static class ComponentConstant<T> extends Component<T> {
		T constant;
		boolean settable;
		ComponentConstantHandler handler;
		public ComponentConstant(int index, String name, T constant, boolean settable) {
			super(index, name);
			this.constant = constant;
			this.settable = settable;
			this.handler = new ComponentConstantHandler();
		}
		public void postCustomAdd(Entity e) {
			
		}
		public TemplateComponent<T> add(Template template) {
			return handler;
		}
		private class ComponentConstantHandler implements TemplateComponent<T> {
			public void set(Entity e, T value) {
				if (settable) {
					constant = value;
				}
			}
			public T get(Entity e) {
				return constant;
			}
			public void remove(Template template) {
				
			}
		}
	}
	
	public static interface CollisionHandler {
		void handleCollision(Entity a, Entity b, float time);
	}
	
	public static class Shape implements ComponentFactory<Shape> {
		public Shape create() {
			return new Shape();
		}
		public Shape clone( Shape value ) {
			return new Shape();
		}
	}
	
	public static class CollisionHandlerDefault implements CollisionHandler, ComponentFactory<CollisionHandler> {
		public CollisionHandler create() {
			return this;
		}
		public CollisionHandler clone( CollisionHandler value ) {
			return this;
		}
		public void handleCollision( Entity a, Entity b, float time ) {
			
		}
	}
	
	public static class ViewDefault implements Renderer {
		public void draw( Entity e, Object drawState ) {
			
		}
	}
	
	@Test
	public void test()
	{
		final EntityCore core = new EntityCore();
		final Component<Vector> POSITION = core.newComponent( "position", new Vector() );
		final Component<Scalar> RADIUS = core.newComponent( "radius", new Scalar() );
		final Component<CollisionHandler> COLLISION_HANDLER = core.newComponent( "collision-callback" ); // by default, there is no collision handler
		final Component<Shape> SHAPE = core.newComponent( "collision-shape" ); // by default, there is no collision shape on an entity
		final Component<Bounds> BOUNDS = core.newComponent( "bounds" ); // by default, an entity has a bounds component 
		
		final Component<CollisionHandler> COLLISION_HANDLER_SHIP = core.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() ); 
		final Component<CollisionHandler> COLLISION_HANDLER_ASTEROID = core.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() );
		
		final Component<Shape> SHAPE_SHIP = core.newComponentSharedAlternative( SHAPE, new Shape() );
		final Component<Shape> SHAPE_ASTEROID = core.newComponentSharedAlternative( SHAPE, new Shape() );
		
		final Component<Bounds> BOUNDS_DYNAMIC = core.newComponentDynamicAlternative( BOUNDS, new Dynamic<Bounds>() {
			public Bounds get( Entity e ) {
				Bounds b = new Bounds();
				set( e, b );
				return b;
			}
			public void set( Entity e, Bounds target ) {
				Vector p = e.get( POSITION );
				float r = e.get( RADIUS ).x;
				target.left = p.x - r;
				target.right = p.x + r;
				target.top = p.y - r;
				target.bottom = p.y + r;
			}
		});
		
		final View SHIP_VIEW = core.newView( "ship", new ViewDefault() );
		final View ASTEROID_VIEW = core.newView( "asteroid", new ViewDefault() );
		
		final Template SHIP = core.newTemplate( "ship", Bag.of(POSITION, RADIUS, COLLISION_HANDLER_SHIP, SHAPE_SHIP, BOUNDS_DYNAMIC), new Bag<Controller>(), SHIP_VIEW );
		final Template ASTEROID = core.newTemplate( "asteroid", Bag.of(POSITION, RADIUS, COLLISION_HANDLER_ASTEROID, SHAPE_ASTEROID, BOUNDS_DYNAMIC), new Bag<Controller>(), ASTEROID_VIEW );

		assertTrue( SHIP.has(SHAPE) );
		assertTrue( SHIP.has(BOUNDS) );
		assertTrue( SHIP.has(COLLISION_HANDLER) );
		assertTrue( SHIP.has(POSITION) );
		
		assertTrue( ASTEROID.has(SHAPE) );
		assertTrue( ASTEROID.has(BOUNDS) );
		assertTrue( ASTEROID.has(COLLISION_HANDLER) );
		assertTrue( ASTEROID.has(POSITION) );
		
		Entity e = new Entity( SHIP );
		e.get( POSITION ).x = 1.0f;
		e.get( POSITION ).y = 2.0f;
		
		Bounds b = e.get( BOUNDS );

		assertEquals( 1.0f, b.left, 0.00001f );
		assertEquals( 1.0f, b.right, 0.00001f );
		assertEquals( 2.0f, b.top, 0.00001f );
		assertEquals( 2.0f, b.bottom, 0.00001f );
		
		e.get( RADIUS ).x = 2.0f;
		e.set( BOUNDS, b );
		
		assertEquals(-1.0f, b.left, 0.00001f );
		assertEquals( 3.0f, b.right, 0.00001f );
		assertEquals( 0.0f, b.top, 0.00001f );
		assertEquals( 4.0f, b.bottom, 0.00001f );
	}
	
}
