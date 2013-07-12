@ECHO OFF

set PROGRAM_NAME=EntityCore
set CXX=g++
set FLAGS_CXX=-O -Wall -Wextra -g -std=c++0x -fPIC
set FLAGS_LD=%FLAGS_CXX%
set FLAGS_LIB=%FLAGS_CXX% -shared
set SOURCES=lib/*.cpp
set INCLUDE=include/
set OBJECTS=AnyMemory.o EntityCore.o EntityType.o EntityTypeCustom.o Entity.o EntityList.o
set DEPENDENCIES=%OBJECTS% BitSet.o IdMap.o ComponentType.o VectorIterator.o DynamicComponentType.o Controller.o View.o

echo Compiling BitSet...
%CXX% %FLAGS_CXX% -c include/BitSet.h -I%INCLUDE%
echo Compiling IdMap...
%CXX% %FLAGS_CXX% -c include/IdMap.h -I%INCLUDE%
echo Compiling ComponentType...
%CXX% %FLAGS_CXX% -c include/ComponentType.h -I%INCLUDE%
echo Compiling VectorIterator...
%CXX% %FLAGS_CXX% -c include/VectorIterator.h -I%INCLUDE%
echo Compiling DynamicComponentType...
%CXX% %FLAGS_CXX% -c include/DynamicComponentType.h -I%INCLUDE%
echo Compiling Controller...
%CXX% %FLAGS_CXX% -c include/Controller.h -I%INCLUDE%
echo Compiling View...
%CXX% %FLAGS_CXX% -c include/View.h -I%INCLUDE%
echo Compiling AnyMemory...
%CXX% %FLAGS_CXX% -c lib/AnyMemory.cpp -I%INCLUDE%
echo Compiling EntityCore...
%CXX% %FLAGS_CXX% -c lib/EntityCore.cpp -I%INCLUDE%
echo Compiling EntityType...
%CXX% %FLAGS_CXX% -c lib/EntityType.cpp -I%INCLUDE%
echo Compiling EntityTypeCustom...
%CXX% %FLAGS_CXX% -c lib/EntityTypeCustom.cpp -I%INCLUDE%
echo Compiling Entity...
%CXX% %FLAGS_CXX% -c lib/Entity.cpp -I%INCLUDE%
echo Compiling EntityList...
%CXX% %FLAGS_CXX% -c lib/EntityList.cpp -I%INCLUDE%
echo Compiling DLL...
%CXX% %FLAGS_LIB% -o %PROGRAM_NAME%.dll %OBJECTS% -I%INCLUDE%

if "%~1"=="TestEntity.o" (
	echo Compiling and Running TestEntity...
	%CXX% %FLAGS_CXX% -c tests/TestEntity.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestEntity.exe %OBJECTS% TestEntity.o -I%INCLUDE%
	TestEntity.exe
) else if "%~1"=="TestBitSet.o" (
	echo Compiling and Running TestBitSet...
	%CXX% %FLAGS_CXX% -c tests/TestBitSet.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestBitSet.exe %OBJECTS% TestBitSet.o -I%INCLUDE%
	TestBitSet.exe
) else if "%~1"=="TestAnyMemory.o" (
	echo Compiling and Running TestAnyMemory...
	%CXX% %FLAGS_CXX% -c tests/TestAnyMemory.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestAnyMemory.exe %OBJECTS% TestAnyMemory.o -I%INCLUDE%
	TestAnyMemory.exe
) else if "%~1"=="TestVectorIterator.o" (
	echo Compiling and Running TestVectorIterator...
	%CXX% %FLAGS_CXX% -c tests/TestVectorIterator.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestVectorIterator.exe %OBJECTS% TestVectorIterator.o -I%INCLUDE%
	TestVectorIterator.exe
) else (
	echo No Tests Ran.
)