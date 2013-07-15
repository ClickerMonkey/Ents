#ifndef ENTITYSYSTEM_H
#define ENTITYSYSTEM_H

#include <map>

#include <Common.h>
#include <Entity.h>
#include <EntityList.h>
#include <IdMap.h>

class EntitySystem
{
protected:

	std::map<size_t,std::vector<EntityList*>> componentListeners;
	std::map<size_t,std::vector<EntityList*>> controllerListeners;
	EntityList entities;

public:

	size_t clean();

	Entity* track(Entity *e);

	void listenForComponents(EntityList *listener, const IdMap &components);

	void listenForControllers(EntityList *listener, const IdMap &controllers);

	size_t removeFromComponents(EntityList *listener);

	size_t removeFromControllers(EntityList* listener);

	inline EntityList& getEntities()
	{
		return entities;
	}

};

#endif