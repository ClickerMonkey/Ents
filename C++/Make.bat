@ECHO OFF

set PROGRAM_NAME=EntityCore
set CXX=g++
set FLAGS_CXX=-O -Wall -Wextra -g -std=c++0x
set FLAGS_LD=%FLAGS_CXX%
set FLAGS_LIB=%FLAGS_CXX% -shared -fPIC
set INCLUDE=include/
set OBJECTS=AnyMemory.o EntityCore.o EntityType.o EntityTypeCustom.o Entity.o EntityList.o BitSet.o ComponentType.o 
set DEPENDENCIES=%OBJECTS% IdMap.o VectorIterator.o DynamicComponentType.o Controller.o View.o

echo Compiling BitSet...
%CXX% %FLAGS_CXX% -c lib/BitSet.cpp -I%INCLUDE%
echo Compiling IdMap...
%CXX% %FLAGS_CXX% -c include/IdMap.h -I%INCLUDE%
echo Compiling ComponentType...
%CXX% %FLAGS_CXX% -c lib/ComponentType.cpp -I%INCLUDE%
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
) else if "%~1"=="TestDynamicComponent.o" (
	echo Compiling and Running TestDynamicComponent...
	%CXX% %FLAGS_CXX% -c tests/TestDynamicComponent.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestDynamicComponent.exe %OBJECTS% TestDynamicComponent.o -I%INCLUDE%
	TestDynamicComponent.exe
) else if "%~1"=="TestComponentType.o" (
	echo Compiling and Running TestComponentType...
	%CXX% %FLAGS_CXX% -c tests/TestComponentType.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestComponentType.exe %OBJECTS% TestComponentType.o -I%INCLUDE%
	TestComponentType.exe
) else if "%~1"=="TestController.o" (
	echo Compiling and Running TestController...
	%CXX% %FLAGS_CXX% -c tests/TestController.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestController.exe %OBJECTS% TestController.o -I%INCLUDE%
	TestController.exe
) else if "%~1"=="TestView.o" (
	echo Compiling and Running TestView...
	%CXX% %FLAGS_CXX% -c tests/TestView.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestView.exe %OBJECTS% TestView.o -I%INCLUDE%
	TestView.exe
) else if "%~1"=="TestIdMap.o" (
	echo Compiling and Running TestIdMap...
	%CXX% %FLAGS_CXX% -c tests/TestIdMap.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestIdMap.exe %OBJECTS% TestIdMap.o -I%INCLUDE%
	TestIdMap.exe
) else if "%~1"=="TestEntity2.o" (
	echo Compiling and Running TestEntity2...
	%CXX% %FLAGS_CXX% -c tests/TestEntity2.cpp -I%INCLUDE%
	%CXX% %FLAGS_LD% -o TestEntity2.exe %OBJECTS% TestEntity2.o -I%INCLUDE%
	TestEntity2.exe
) else (
	echo No Tests Ran.
)