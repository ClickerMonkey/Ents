package org.magnos.entity;

import java.util.Arrays;



@SuppressWarnings("unchecked")
public class TestNewDesign {

	
	class Id {
		int index;
		String name;
	}

	class IdMap<I extends Id, V> {
		Id[] ids;
		V[] values;
		int[] map;
		
		public boolean has(Id id) {
			return map[id.index] != -1;
		}
		public int indexOf(Id id) {
			return map[id.index];
		}
		public V get(Id id) {
			return values[map[id.index]];
		}
		public void set(Id id, V value) {
			values[map[id.index]] = value;
		}
		public void add(Id id, V value) {
			ensureMapSize(id.index);
			map[id.index] = ids.length;
			ids = EntityUtility.append(ids, id);
			values = EntityUtility.append(values, value);
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
	
	interface Renderer {
		void draw(Entity e, Object drawState);
	}

	class View extends Id {
		Renderer renderer;
	}
	
	class EntityType {
		IdMap<Component<?>, ComponentHandler<?>> components;
		IdMap<Component<?>, Object> shared;
	}
	
	class Entity {
		EntityType type;
		Object[] components = {};
		
		public <T> T get(Component<T> component) {
			ComponentHandler<T> handler = (ComponentHandler<T>)type.components.get(component);
			return handler.get(component, this);
		}
		
	}
	
	interface ComponentHandler<T> {
		public void set(Component<T> component, Entity e, T value);
		public T get(Component<T> component, Entity e);
		public void addToEntity(Component<T> component, Entity e);
		public void addToType(Component<T> component, EntityType type);
	}
	
	class Component<T> extends Id {
		ComponentHandler<T> handler;
	}
	
	interface Setter<T> {
		void set(Entity e, T target);
	}
	
	interface Getter<T> {
		T get(Entity e);
	}
	
	interface Factory<T> {
		T create();
		T clone(T value);
	}
	
	class ComponentValued<T> implements ComponentHandler<T> {
		Factory<T> factory;
		public ComponentValued(Factory<T> factory) {
			this.factory = factory;
		}
		@Override
		public void set(Component<T> component, Entity e, T value) {
			e.components[e.type.components.indexOf(component)] = value;
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return (T)e.components[e.type.components.indexOf(component)];
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
			e.components = EntityUtility.append(e.components, factory.create());
		}
		@Override
		public void addToType(Component<T> component, EntityType e) {
		}
	}
	
	class ComponentSetter<T> implements ComponentHandler<T> {
		Setter<T> setter;
		public ComponentSetter(Setter<T> setter) {
			this.setter = setter;
		}
		public void set(Component<T> component, Entity e, T value) {
			setter.set(e, value);
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return null;
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
		}
		@Override
		public void addToType(Component<T> component, EntityType e) {
		}
	}
	
	class ComponentShared<T> implements ComponentHandler<T> {
		Factory<T> factory;
		public ComponentShared(Factory<T> factory) {
			this.factory = factory;
		}
		@Override
		public void set(Component<T> component, Entity e, T value) {
			e.type.shared.set(component, value);
		}
		@Override
		public T get(Component<T> component, Entity e) {
			return (T)e.type.shared.get(component);
		}
		@Override
		public void addToEntity(Component<T> component, Entity e) {
		}
		@Override
		public void addToType(Component<T> component, EntityType type) {
			type.shared.add(component, factory.create());
		}
	}
	

	
}
