PROGRAM_NAME = EntityCore
CXX = g++
LIBS = 
FLAGS_CXX = -O -Wall -Wextra -g -std=c++0x -fPIC -O0
FLAGS_LD = $(FLAGS_CXX) 
FLAGS_LIB = $(FLAGS_CXX) -shared
SOURCES = lib/*.cpp
INCLUDE = include/
OBJECTS = AnyMemory.o EntityCore.o EntityType.o EntityTypeCustom.o Entity.o EntityList.o
DEPENDENCIES = $(OBJECTS) BitSet.o IdMap.o ComponentType.o VectorIterator.o DynamicComponentType.o View.o Controller.o


# Compile and run Testing
all: $(DEPENDENCIES)
	$(CXX) $(FLAGS_LIB) -o lib$(PROGRAM_NAME).so $(OBJECTS) -I$(INCLUDE)

# Compiling Headers without Source Files
BitSet.o: include/BitSet.h
	$(CXX) $(FLAGS_CXX) -c include/BitSet.h -I$(INCLUDE)

IdMap.o: include/IdMap.h
	$(CXX) $(FLAGS_CXX) -c include/IdMap.h -I$(INCLUDE)

ComponentType.o: include/ComponentType.h
	$(CXX) $(FLAGS_CXX) -c include/ComponentType.h -I$(INCLUDE)

VectorIterator.o: include/VectorIterator.h
	$(CXX) $(FLAGS_CXX) -c include/VectorIterator.h -I$(INCLUDE)

DynamicComponentType.o: include/DynamicComponentType.h
	$(CXX) $(FLAGS_CXX) -c include/DynamicComponentType.h -I$(INCLUDE)

Controller.o: include/Controller.h
	$(CXX) $(FLAGS_CXX) -c include/Controller.h -I$(INCLUDE)

View.o: include/View.h
	$(CXX) $(FLAGS_CXX) -c include/View.h -I$(INCLUDE)

# Compiling Source Files
AnyMemory.o: lib/AnyMemory.cpp
	$(CXX) $(FLAGS_CXX) -c lib/AnyMemory.cpp -I$(INCLUDE)

EntityCore.o: lib/EntityCore.cpp
	$(CXX) $(FLAGS_CXX) -c lib/EntityCore.cpp -I$(INCLUDE)

EntityType.o: lib/EntityType.cpp
	$(CXX) $(FLAGS_CXX) -c lib/EntityType.cpp -I$(INCLUDE)

EntityTypeCustom.o: lib/EntityTypeCustom.cpp
	$(CXX) $(FLAGS_CXX) -c lib/EntityTypeCustom.cpp -I$(INCLUDE)

Entity.o: lib/Entity.cpp
	$(CXX) $(FLAGS_CXX) -c lib/Entity.cpp -I$(INCLUDE)

EntityList.o: lib/EntityList.cpp
	$(CXX) $(FLAGS_CXX) -c lib/EntityList.cpp -I$(INCLUDE)

# Compiling Tests

TestEntity.o: $(DEPENDENCIES)
	$(CXX) $(FLAGS_CXX) -c tests/TestEntity.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestEntity $(OBJECTS) TestEntity.o -I$(INCLUDE)
	./TestEntity

TestBitSet.o: $(DEPENDENCIES)
	$(CXX) $(FLAGS_CXX) -c tests/TestBitSet.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestBitSet $(OBJECTS) TestBitSet.o -I$(INCLUDE)
	./TestBitSet

TestAnyMemory.o: $(DEPENDENCIES)
	$(CXX) $(FLAGS_CXX) -c tests/TestAnyMemory.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestAnyMemory $(OBJECTS) TestAnyMemory.o -I$(INCLUDE)
	./TestAnyMemory

TestVectorIterator.o: $(DEPENDENCIES) tests/TestVectorIterator.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestVectorIterator.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestVectorIterator $(OBJECTS) TestVectorIterator.o -I$(INCLUDE)
	./TestVectorIterator

# Running
run:
	./$(PROGRAM_NAME)

clean:
	rm -f lib$(PROGRAM_NAME).so *.o TestEntity TestBitSet TestAnyMemory TestVectorIterator