#ifndef ENTITYTYPECUSTOM_H
#define ENTITYTYPECUSTOM_H

#include <Common.h>
#include <EntityType.h>
#include <IdMap.h>
#include <AnyMemory.h>

class EntityTypeCustom : public EntityType 
{
public:

   EntityTypeCustom();

   EntityTypeCustom(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents);

   EntityTypeCustom(const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId);

   bool isCustom();

   EntityType* addCustomComponent( const size_t componentId );

   EntityType* addCustomController( const size_t behaviorId );

   EntityType* setCustomView( const size_t viewId );

};

#endif