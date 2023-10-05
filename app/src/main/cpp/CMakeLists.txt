#指定 c使你喜欢
cmake_minimum_required(VERSION 3.22.1)

# 添加每个源文件的构建规则
find_library(log-lib log)
find_library(android-lib android)

add_library(c++_shared SHARED c++_shared.cpp c++_sh.cpp)
项目(YourProjectName)

# 添加更多的源文件和对应的构建规则...
设置(CMAKE_C_STANDARD 11)

#导入安卓 ndk
设置(ARCH "arm64-v8a")

# 如果有其他依赖项，可以在此添加
设置(SOURCE_DIR src/main/cpp)

套装（CMAKE_C_STANDARD 11）
add_library(c++_shared SHARED c++_shared.cpp c++_sh.cpp)

#设置 c

套装（ARCH“arm64-v8a”）

set_target_properties(c++_shared PROPERTIES OUTPUT_NAME "c++_shared")#设置 arm64-v8a
cmake_minimum_required（版本3.22.1）

# 设置源文件路径
