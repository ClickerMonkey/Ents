PROGRAM_NAME = EntityCore
CXX = g++
LIBS = 
FLAGS_CXX = -O -Wall -Wextra -g -std=c++11 -fPIC -O0
FLAGS_LD = $(FLAGS_CXX) 
FLAGS_LIB = $(FLAGS_CXX) -shared
SOURCES = lib/*.cpp
INCLUDE = include/
OBJECTS = AnyMemory.o EntityCore.o EntityType.o EntityTypeCustom.o Entity.o EntityList.o BitSet.o ComponentType.o 
DEPENDENCIES = $(OBJECTS) IdMap.o VectorIterator.o DynamicComponentType.o View.o Controller.o


# Compile and run Testing
all: $(DEPENDENCIES)
	$(CXX) $(FLAGS_LIB) -o lib$(PROGRAM_NAME).so $(OBJECTS) -I$(INCLUDE)

# Compiling Headers without Source Files
IdMap.o: include/IdMap.h
	$(CXX) $(FLAGS_CXX) -c include/IdMap.h -I$(INCLUDE)

VectorIterator.o: include/VectorIterator.h
	$(CXX) $(FLAGS_CXX) -c include/VectorIterator.h -I$(INCLUDE)

DynamicComponentType.o: include/DynamicComponentType.h
	$(CXX) $(FLAGS_CXX) -c include/DynamicComponentType.h -I$(INCLUDE)

Controller.o: include/Controller.h
	$(CXX) $(FLAGS_CXX) -c include/Controller.h -I$(INCLUDE)

View.o: include/View.h
	$(CXX) $(FLAGS_CXX) -c include/View.h -I$(INCLUDE)

# Compiling Source Files
BitSet.o: lib/BitSet.cpp
	$(CXX) $(FLAGS_CXX) -c lib/BitSet.cpp -I$(INCLUDE)

AnyMemory.o: lib/AnyMemory.cpp
	$(CXX) $(FLAGS_CXX) -c lib/AnyMemory.cpp -I$(INCLUDE)

ComponentType.o: lib/ComponentType.cpp
	$(CXX) $(FLAGS_CXX) -c lib/ComponentType.cpp -I$(INCLUDE)

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

TestEntity.o-fast: tests/TestEntity.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestEntity.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestEntity $(OBJECTS) TestEntity.o -I$(INCLUDE)
	./TestEntity
TestEntity.o: $(DEPENDENCIES) TestEntity.o-fast

TestBitSet.o-fast: tests/TestBitSet.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestBitSet.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestBitSet $(OBJECTS) TestBitSet.o -I$(INCLUDE)
	./TestBitSet
TestBitSet.o: $(DEPENDENCIES) TestBitSet.o-fast

TestAnyMemory.o-fast: tests/TestAnyMemory.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestAnyMemory.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestAnyMemory $(OBJECTS) TestAnyMemory.o -I$(INCLUDE)
	./TestAnyMemory
TestAnyMemory.o: $(DEPENDENCIES) TestAnyMemory.o-fast

TestDynamicComponent.o-fast: tests/TestDynamicComponent.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestDynamicComponent.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestDynamicComponent $(OBJECTS) TestDynamicComponent.o -I$(INCLUDE)
	./TestDynamicComponent
TestDynamicComponent.o: $(DEPENDENCIES) TestDynamicComponent.o-fast

TestComponentType.o-fast: tests/TestComponentType.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestComponentType.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestComponentType $(OBJECTS) TestComponentType.o -I$(INCLUDE)
	./TestComponentType
TestComponentType.o: $(DEPENDENCIES) TestComponentType.o-fast

TestController.o-fast: tests/TestController.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestController.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestController $(OBJECTS) TestController.o -I$(INCLUDE)
	./TestController
TestController.o: $(DEPENDENCIES) TestController.o-fast

TestView.o-fast: tests/TestView.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestView.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestView $(OBJECTS) TestView.o -I$(INCLUDE)
	./TestView
TestView.o: $(DEPENDENCIES) TestView.o-fast

TestIdMap.o-fast: tests/TestIdMap.cpp
	$(CXX) $(FLAGS_CXX) -c tests/TestIdMap.cpp -I$(INCLUDE)
	$(CXX) $(FLAGS_LD) -o TestIdMap $(OBJECTS) TestIdMap.o -I$(INCLUDE)
	./TestIdMap
TestIdMap.o: $(DEPENDENCIES) TestIdMap.o-fast

TestAll-fast: TestIdMap.o-fast TestView.o-fast TestController.o-fast TestComponentType.o-fast \
			  TestDynamicComponent.o-fast TestAnyMemory.o-fast TestBitSet.o-fast TestEntity.o-fast
TestAll: $(DEPENDENCIES) TestAll-fast


# Running
run:
	./$(PROGRAM_NAME)

clean:
	rm -f lib$(PROGRAM_NAME).so *.o include/*.gch Test*