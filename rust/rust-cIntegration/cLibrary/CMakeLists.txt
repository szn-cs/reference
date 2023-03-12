cmake_minimum_required(VERSION 3.0) 
project(cLibrary C) 
add_library(library STATIC library.c)
install(TARGETS library DESTINATION .) 
