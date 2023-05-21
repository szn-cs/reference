cmake_minimum_required(VERSION 3.0.0)
project(cppProject VERSION 0.1.0)

include(CTest)
enable_testing()

add_executable(cppProject main.cpp)
target_compile_features(cppProject PUBLIC cxx_std_20)

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)


# list of functions
# https://cmake.org/cmake/help/latest/manual/cmake-properties.7.html#properties-on-targets
# https://stackoverflow.com/questions/8774593/cmake-link-to-external-library