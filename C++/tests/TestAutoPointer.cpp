#include <iostream>

using namespace std;

template<class T>
struct ptr
{
	T *data;
	const bool autoDelete;

	ptr(T &m_data) 
		: data( &m_data ), autoDelete( false )
	{
	}
	ptr(T* m_data) 
		: data( m_data ), autoDelete( true )
	{
	}
	ptr(T* m_data, const bool &m_autoDelete) 
		: data( m_data ), autoDelete( m_autoDelete )
	{
	}
	~ptr()
	{
		reset( nullptr, false );
	}
	T& operator* () 
	{
		return *data;
	}
	const T& operator* () const
	{
		return *data;
	}
	T* operator-> () 
	{
		return data;
	}
	const T* operator-> () const
	{
		return data;
	}
	ptr& operator=(T& m_data)
	{
		reset( &m_data, false );
		return *this;
	}
	ptr& operator=(T* m_data)
	{
		reset( m_data, true );
		return *this;
	}
	void reset(T* m_data, const bool &m_autoDelete)
	{
		if (autoDelete && data != nullptr)
		{
			delete data;
		}

		data = m_data;
		autoDelete = m_autoDelete;
	}

};

int main()
{
	cout << "Hello World!" << endl;
}