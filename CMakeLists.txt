# 指定 CMake 最低版本要求
cmake_minimum_required(VERSION 3.22.1)

# 导入 Android NDK 的默认库
find_library(log-lib log)
find_library(android-lib android)

# 设置项目名称
project(YourProjectName)

# 设置 C 标准
set(CMAKE_C_STANDARD 11)

# 设置 arm64-v8a 架构
set(ARCH "arm64-v8a")

# 设置源文件路径
set(SOURCE_DIR src/main/cpp)

# 添加每个源文件的构建规则
add_library(c++_shared SHARED c++_shared.cpp c++_sh.cpp)

# 添加更多的源文件和对应的构建规则...

# 如果有其他依赖项，可以在此添加

# 指定生成的共享库的名称
set_target_properties(c++_shared PROPERTIES OUTPUT_NAME "c++_shared")

# 指定更多生成共享库的属性...