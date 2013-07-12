#ifndef ENTITYTYPECUSTOM_H
#define ENTITYTYPECUSTOM_H

#include <Common.h>
#include <EntityType.h>
#include <IdMap.h>
#include <AnyMemory.h>

class EntityTypeCustom : public EntityType 
{
public:

   EntityTypeCustom(const size_t id, const EntityType *parent, const IdMap &components, const IdMap &controllers, const size_t viewId, const AnyMemory &defaultComponents, const std::vector<size_t> &offsets);

   bool isCustom();

   EntityType* addCustomComponent( const size_t componentId );

   EntityType* addCustomController( const size_t behaviorId );

   EntityType* setCustomView( const size_t viewId );

};

#endif