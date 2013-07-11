 #include <EntityTypeCustom.h>

EntityTypeCustom::EntityTypeCustom(const size_t id, const EntityType *parent, const IdMap &components, const IdMap &controllers, const size_t viewId, const AnyMemory &defaultComponents, const vector<size_t> &offsets) 
   : EntityType(id, parent, components, controllers, viewId, defaultComponents, offsets) 
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