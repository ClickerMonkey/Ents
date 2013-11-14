@ECHO OFF

set CXX=g++
set FLAGS_CXX=-O -Wall -Wextra -g -std=c++0x
set FLAGS_LD=%FLAGS_CXX%
set FLAGS_LIB=%FLAGS_CXX% -shared -fPIC

echo Compiling and Running TestAutoPointer...
%CXX% %FLAGS_CXX% -c tests/TestAutoPointer.cpp
%CXX% %FLAGS_LD% -o TestAutoPointer.exe TestAutoPointer.o
TestAutoPointer.exe
