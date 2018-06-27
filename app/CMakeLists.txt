# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

#ADD_DEFINITIONS(-DCOMPILER_IS_ARM_LINUX_GCC)

#set(CMAKE_ANDROID_STL_TYPE gnustl_static)

#SET(CMAKE_CXX_FLAGS "-Wno-error=format-security -Wno-error=pointer-sign")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -g -Wall -Wno-unused-variable -pthread")


#设置生成的so动态库最后输出的路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# so输出路径 CMAKE_LIBRARY_OUTPUT_DIRECTORY
# .a 静态库输出路径 CMAKE_ARCHIVE_OUTPUT_DIRECTORY

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#libprotobuf.a
add_library(protobuf STATIC IMPORTED)
set_target_properties(protobuf
                      PROPERTIES IMPORTED_LOCATION
                      ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include/libs/${ANDROID_ABI}/libprotobuf.a)

add_library( # Sets the name of the library.
             platform-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
            src/main/cpp/native-api.cpp

            #protobuf
            src/main/cpp/probuf/src/adsimulate.pb.cpp

             )

INCLUDE_DIRECTORIES(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include/)
INCLUDE_DIRECTORIES(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/probuf/inc/)


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       platform-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       protobuf
                       ${log-lib} )