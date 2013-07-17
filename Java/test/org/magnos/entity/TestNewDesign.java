package org.magnos.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.magnos.entity.factory.ComponentFactoryNull;
import org.magnos.entity.helper.Bounds;
import org.magnos.entity.helper.Scalar;
import org.magnos.entity.helper.Vector;



@SuppressWarnings("unchecked")
public class TestNewDesign {

	
	public static class Id {
		final int index;
		final String name;
		public Id(int index, String name) {
			this.index = index;
			this.name = name;
		}
	}

	public static class IdMap<I extends Id> {
		I[] ids;
		int[] map;
		
		public static <D extends Id> IdMap<D> NEW(D ... ids) {
			return new IdMap<D>(ids);
		}
		
		public IdMap(I ...ids ) {
			this.ids = ids;
			int max = 0;
			for (int i = 0; i < ids.length; i++) {
				max = Math.max(max, ids[i].index );
			}
			map = new int[max + 1];
			Arrays.fill( map, -1 );
			for (int i = 0; i < ids.length; i++) {
				map[ids[i].index] = i;
			}
		}
		public I getId(I id) {
			return ids[indexOf(id)];
		}
		public void refresh(I id) {
			ids[indexOf(id)] = id;
		}
		public boolean has(I id) {
			return map[id.index] != -1;
		}
		public int indexOf(I id) {
			return map[id.index];
		}
		public void add(I id, int index) {
			ensureMapSize(id.index);
			map[id.index] = index;
			ids = EntityUtility.append(ids, id);
		}
		public void add(I id) {
			add(id, ids.length);
		}
		public void put(I id) {
			int i = id.index >= map.length ? -1 : indexOf(id);
			if (i != -1) {
				ids[i] = id;
			} else {
				add( id );
			}
		}
		public int size() {
			return ids.length;
		}
		private void ensureMapSize(int index) {
			int size = map.length;
			if (size <= index) {
				map = Arrays.copyOf(map, index + 1);
				while (size < map.length) {
					map[size++] = -1;
				}
			}
		}
	}	
	
	public static class IdValueMap<I extends Id, V> extends IdMap<I> {
		V[] values;
		public IdValueMap(V[] values)	{
			this.values = values;
		}
		public V get(I id) {
			return values[map[id.index]];
		}
		public void set(I id, V value) {
			values[map[id.index]] = value;
		}
		public void add(I id, V value) {
			add( id, value, ids.length );
		}
		public void add(I id, V value, int index) {
			super.add( id, index );
			values = EntityUtility.append(values, value);
		}
		public void put(I id, V value) {
			int i = id.index >= map.length ? -1 : indexOf(id);
			if (i != -1) {
				ids[i] = id;
				values[i] = value;
			} else {
				add(id, value);
			}
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
	
	public static class EntityType {
		IdMap<Component<?>> components;
		IdValueMap<Component<?>, Object> sharedValues;
		IdMap<Component<?>> entityValues;
		View view;
		EntityType(IdMap<Component<?>> components, View view) {
			
			this.components = components;
			this.sharedValues = new IdValueMap<Component<?>, Object>( new Object[0] );
			this.entityValues = new IdMap<Component<?>>();
			this.view = view;
			
			for (int i = 0; i < components.ids.length; i++) {
				Component<Object> c = (Component<Object>)components.ids[i];
				c.handler.addToType( c, this );
			}
		}
		<T> boolean has(Component<T> component) {
			return components.has( component );
		}
		<T> boolean hasExact(Component<T> component) {
			return components.getId( component ) == component;
		}
	}
	
	public static class Entity {
		EntityType type;
		Object[] components = {};
		
		public Entity(EntityType type) {
			this.type = type;
			for (int i = 0; i < type.components.size(); i++) {
				Component<Object> c = (Component<Object>) type.components.ids[i];
				c.handler.addToEntity( c, this );
			}
		}
		public <T> T get(Component<T> component) {
			Component<T> c = (Component<T>)type.components.getId(component);
			return c.handler.get(c, this);
		}
		public <T> void set(Component<T> component, T value) {
			Component<T> c = (Component<T>)type.components.getId(component);
			c.handler.set( c, this, value );
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
			return newComponent( name, new ComponentFactoryNull<T>() );
		}
		// Entity contains component value
		public <T> Component<T> newComponent(String name, ComponentFactory<T> factory) {
			return newComponent( name, new ComponentValued<T>( factory ) );
		}
		public <T> Component<T> newComponentAlternative(Component<T> component, ComponentFactory<T> factory) {
			return newComponentAlternative( component, new ComponentValued<T>( factory ) );
		}
		// EntityType contains component value
		public <T> Component<T> newComponentShared(String name, ComponentFactory<T> factory) {
			return newComponent( name, new ComponentShared<T>( factory ) );
		}
		public <T> Component<T> newComponentSharedAlternative(Component<T> component, ComponentFactory<T> factory) {
			return newComponentAlternative( component, new ComponentShared<T>( factory ) );
		}
		// Nothing contains component value, is calculated upon request.
		public <T> Component<T> newComponentDynamic(String name, Dynamic<T> setter) {
			return newComponent( name, new ComponentDynamic<T>( setter ) );
		}
		public <T> Component<T> newComponentDynamicAlternative(Component<T> component, Dynamic<T> setter) {
			return newComponentAlternative( component, new ComponentDynamic<T>( setter ) );
		}
		
		private <T> Component<T> newComponent(String name, ComponentHandler<T> handler	) {
			Component<T> c = new Component<T>( components.size(), name, handler );
			components.add( c );
			return c;
		}
		private <T> Component<T> newComponentAlternative(Component<T> component, ComponentHandler<T> handler) {
			return new Component<T>( component.index, component.name, handler );
		}
		
		// Entity Type
		public EntityType newEntityType( IdMap<Component<?>> components, View view) {
			return new EntityType(components, view);
		}
	}
	
	public static interface ComponentHandler<T> {
		public void set(Component<T> component, Entity e, T value);
		public T get(Component<T> component, Entity e);
		public void addToEntity(Component<T> component, Entity e);
		public void addToType(Component<T> component, EntityType type);
		public void removeFromType(Component<T> component, EntityType type);
	}
	
	public static class Component<T> extends Id {
		ComponentHandler<T> handler;
		Component(int index, String name, ComponentHandler<T> handler) {
			super(index, name);
			this.handler = handler;
		}
	}
	
	public static interface Dynamic<T>{
		T get(Entity e);
		void set(Entity e, T target);
	}
	
	public static class ComponentValued<T> implements ComponentHandler<T> {
		ComponentFactory<T> factory;
		public ComponentValued(ComponentFactory<T> factory) {
			this.factory = factory;
		}
		@Override
		public void set(Component<T> component, Entity e, T value) {
			e.components[e.type.entityValues.indexOf(component)] = value;
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return (T)e.components[e.type.entityValues.indexOf(component)];
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
			e.components = EntityUtility.append(e.components, factory.create());
		}
		@Override
		public void addToType(Component<T> component, EntityType type) {
			type.entityValues.put( component );
		}
		@Override
		public void removeFromType( Component<T> component, EntityType type ) {

		}
	}
	
	public static class ComponentDynamic<T> implements ComponentHandler<T> {
		Dynamic<T> dynamic;
		public ComponentDynamic(Dynamic<T> dynamic) {
			this.dynamic = dynamic;
		}
		public void set(Component<T> component, Entity e, T value) {
			dynamic.set(e, value);
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return dynamic.get( e );
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
		}
		@Override
		public void addToType(Component<T> component, EntityType e) {
		}
		@Override
		public void removeFromType( Component<T> component, EntityType type ) {
		}
	}
	
	public static class ComponentShared<T> implements ComponentHandler<T> {
		ComponentFactory<T> factory;
		public ComponentShared(ComponentFactory<T> factory) {
			this.factory = factory;
		}
		@Override
		public void set(Component<T> component, Entity e, T value) {
			e.type.sharedValues.set(component, value);
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return (T)e.type.sharedValues.get(component);
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
		}
		@Override
		public void addToType(Component<T> component, EntityType type) {
			type.sharedValues.put(component, factory.create());
		}
		@Override
		public void removeFromType( Component<T> component, EntityType type ) {
			
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
		
		final EntityType SHIP = core.newEntityType( IdMap.NEW(POSITION, RADIUS, COLLISION_HANDLER_SHIP, SHAPE_SHIP, BOUNDS_DYNAMIC), SHIP_VIEW );
		final EntityType ASTEROID = core.newEntityType( IdMap.NEW(POSITION, RADIUS, COLLISION_HANDLER_ASTEROID, SHAPE_ASTEROID, BOUNDS_DYNAMIC), ASTEROID_VIEW );

		assertTrue( SHIP.has(SHAPE) );
		assertTrue( SHIP.has(BOUNDS) );
		assertTrue( SHIP.has(COLLISION_HANDLER) );
		assertTrue( SHIP.has(POSITION) );
		
		assertFalse( SHIP.hasExact(SHAPE) );
		assertFalse( SHIP.hasExact(BOUNDS) );
		assertFalse( SHIP.hasExact(COLLISION_HANDLER) );
		assertTrue( SHIP.hasExact(POSITION) );
		
		assertTrue( ASTEROID.has(SHAPE) );
		assertTrue( ASTEROID.has(BOUNDS) );
		assertTrue( ASTEROID.has(COLLISION_HANDLER) );
		assertTrue( ASTEROID.has(POSITION) );
		
		assertFalse( ASTEROID.hasExact(SHAPE) );
		assertFalse( ASTEROID.hasExact(BOUNDS) );
		assertFalse( ASTEROID.hasExact(COLLISION_HANDLER) );
		assertTrue( ASTEROID.hasExact(POSITION) );
		
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
