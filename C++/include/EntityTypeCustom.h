#ifndef ENTITYTYPECUSTOM_H
#define ENTITYTYPECUSTOM_H

#include <AnyMemory.h>
#include <IdMap.h>
#include <EntityType.h>

class EntityTypeCustom : public EntityType 
{
public:

   EntityTypeCustom(const EntityCore *m_core);

   EntityTypeCustom(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents);

   EntityTypeCustom(const EntityCore *m_core, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId);
};

#endif