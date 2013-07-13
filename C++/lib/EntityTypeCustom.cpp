#include <EntityTypeCustom.h>
#include <View.h>
#include <ComponentType.h>
#include <EntityCore.h>

using namespace std;

EntityTypeCustom::EntityTypeCustom(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents)
   : EntityType(m_id, m_parent, m_components, m_controllers, m_viewId, m_defaultComponents) 
{ 
}

EntityTypeCustom::EntityTypeCustom(const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId)
   : EntityType(EntityType::CUSTOM, nullptr, m_components, m_controllers, m_viewId, {})
{
   for (size_t i = 0; i < components.size(); i++) {
      size_t componentId = components.getId(i);
      ComponentType *componentType = EntityCore::getComponent(componentId);
      components.setIndex( componentId, defaultComponents.append( componentType->defaultValue ) );
   }
}

EntityTypeCustom::EntityTypeCustom()
   : EntityType(EntityType::CUSTOM, nullptr, {}, {}, View::NONE, {})
{
}

bool EntityTypeCustom::isCustom() 
{
   return true;
}

EntityType* EntityTypeCustom::addCustomComponent( const size_t componentId ) 
{
   add( componentId );
   return this;
}

EntityType* EntityTypeCustom::addCustomController( const size_t behaviorId ) 
{
   addController( behaviorId );
   return this;
}

EntityType* EntityTypeCustom::setCustomView( const size_t viewId ) 
{
   setView( viewId );
   return this;
}