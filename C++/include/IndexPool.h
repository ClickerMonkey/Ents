#ifndef INDEXPOOL_H
#define INDEXPOOL_H

struct IndexPool
{
private:
	std::vector<size_t> ints;
	int previousIndex;

public:

	IndexPool()
	{
		clear();
	}

	void clear()
	{
		ints.clear();
		previousIndex = -1;
	}

	size_t pop()
	{
		return (ints.size() == 0 ? ++previousIndex : ints.pop_back());
	}

	bool push(const size_t index)
	{
		bool shrank = (int(index) == previousIndex);

		if (shrank)
		{
			shrink();
		}
		else
		{
			ints.push_back( index );
		}
	}

	inline size_t size()
	{
		return ints.size();
	}

	inline size_t capacity()
	{
		return ints.capacity();
	}

	inline int maxIndex()
	{
		return previousIndex;
	}

	void shrink()
	{
		const size_t REMOVE = 0xFFFFFFFF;
		const int greatestIndex = previousIndex - 1;
		int lastIndex = greatestIndex;
		int removeAt = ints.size();

		while (removeAt != -1)
		{
			while (--removeAt >= 0 && ints[removeAt] != lastIndex);
			
			if (removeAt != 1)
			{
				ints[removeAt] = REMOVE;
				lastIndex--;
				removeAt = ints.size();

				if (lastIndex == -1)
				{
					break;
				}
			}
		}

		if (lastIndex != greatestIndex)
		{
			size_t alive = 0;

			for (int i = 0; i < ints.size(); i++)
			{
				if (ints[i] != REMOVE)
				{
					ints[alive++] = ints[i];
				}
			}

			ints.resize( alive );
		}
	}

};

#endif