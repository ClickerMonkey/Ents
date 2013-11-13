#ifndef ID_H
#define ID_H

struct Id
{
	const size_t id;
	const std::string name;

	Id(const size_t m_id, const std::string m_name) 
		: id(m_id), name(m_name)
	{
	}

};

template<typename I>
struct IdContainer
{
protected:
	std::vector<I> definitions;
	std::map<string, I> names;
	std::vector<std::vector<I>> instances;

	IdContainer()
	{
	}

public:
	template<typename D> 
	D& addDefinition( const D &element )
	{
		if (names.count(element.name)) 
		{
			throw exception();
		}
		if (definitions.size() != element.id) 
		{
			throw exception();
		}

		definitions.push_back( element );
		names[ element.name ] = element;
		instances.push_back( std::vector() );
		instances[ element.id ].push_back( element );

		return element;
	}

	template<typename D>
	D& addInstance( const D element )
	{
		if (element.id >= instances.size()) 
		{
			throw exception();
		}
		
		instances[ element.id ].push_back( element );

		return element;
	}

	inline int nextId() 
	{
		return definitions.size();
	}

 	inline int size() 
 	{
 		return definitions.size();
 	}

 	inline std::vector<I>& getDefinitions() 
 	{
 		return definitions;
 	}

 	inline std::vector<std::vector<I>>& getInstances()
 	{
 		return instances;
 	}

 	inline std::map<string, I>& getNameMap()
 	{
 		return names;
 	}	

 	I& getDefinition( const std::string name ) 
 	{
 		return names[ name ];
 	}

 	I* getDefinition( const size_t id )
 	{
 		return (id < definitions.size() ? &definitions[id] : nullptr);
 	}

 	I* getDefinition( const I id )
 	{
 		return (id.id < definitions.size() ? &definitions[id] : nullptr);
 	}

 	std::vector<I>& getInstances( const std::string name )
 	{
 		return instances[ names[name].id ];
 	}

 	std::vector<I>& getInstances( const size_t id )
 	{
 		return instances[ id ];
 	}

 	std::vector<I>& getInstances( const I id )
 	{
 		return instances[ id.id ];
 	}

};

#endif