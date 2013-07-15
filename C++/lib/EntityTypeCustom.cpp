#include <EntityTypeCustom.h>
#include <View.h>
#include <Component.h>
#include <EntityCore.h>

using namespace std;

EntityTypeCustom::EntityTypeCustom(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents)
   : EntityType(m_parent->getCore(), m_id, m_parent, m_components, m_controllers, m_viewId, m_defaultComponents) 
{
}

EntityTypeCustom::EntityTypeCustom(const EntityCore *m_core, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId)
   : EntityType(m_core, EntityType::CUSTOM, nullptr, m_components, m_controllers, m_viewId, {})
{
   for (size_t i = 0; i < components.size(); i++) {
      size_t componentId = components.getId(i);
      ComponentBase* component = core->getComponent(componentId);
      components.setIndex( componentId, defaultComponents.append( component->defaultValue ) );
   }
}

EntityTypeCustom::EntityTypeCustom(const EntityCore *m_core)
   : EntityType(m_core, EntityType::CUSTOM, nullptr, {}, {}, View::NONE, {})
{
}