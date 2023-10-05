#include <iostream>
#include <vector>

class MemoryPool {
private:
    std::vector<char*> memoryBlocks; 
     // 存储内存块
    std::vector<char*> freeBlocks;  
      // 存储空闲内存块

    const size_t blockSize;  
    // 内存块大小
    const size_t poolSize;   
    // 内存池大小

public:
    MemoryPool(size_t blockSize, size_t poolSize)
        : blockSize(blockSize), poolSize(poolSize) {
        // 分配内存池
        char* pool = new char[blockSize * poolSize];
        memoryBlocks.push_back(pool);

        // 初始化空闲内存块列表
        for (size_t i = 0; i < poolSize; ++i) {
            freeBlocks.push_back(pool + i * blockSize);
        }
    }

    ~MemoryPool() {
        // 释放内存池
        for (char* block : memoryBlocks) {
            delete[] block;
        }
    }

    void* allocate() {
        if (freeBlocks.empty()) {
            std::cout << "Memory pool is full. Unable to allocate memory." << std::endl;
            return nullptr;
        }

        // 从空闲内存块列表中获取一个内存块
        void* block = freeBlocks.back();
        freeBlocks.pop_back();
        return block;
    }

    void deallocate(void* block) {
        // 将释放的内存块添加到空闲内存块列表中
        freeBlocks.push_back(static_cast<char*>(block));
    }
};

// 示例使用
struct ExampleStruct {
    int data1;
    float data2;
};

int main() {
    MemoryPool memoryPool(sizeof(ExampleStruct), 10);

    // 从内存池中分配内存
    ExampleStruct* obj1 = static_cast<ExampleStruct*>(memoryPool.allocate());
    if (obj1 != nullptr) {
        obj1->data1 = 1;
        obj1->data2 = 2.0f;
        std::cout << "Allocated memory: " << obj1->data1 << ", " << obj1->data2 << std::endl;
    }

    // 从内存池中分配另一个内存
    ExampleStruct* obj2 = static_cast<ExampleStruct*>(memoryPool.allocate());
    if (obj2 != nullptr) {
        obj2->data1 = 3;
        obj2->data2 = 4.0f;
        std::cout << "Allocated memory: " << obj2->data1 << ", " << obj2->data2 << std::endl;
    }

    // 释放内存到内存池中
    memoryPool.deallocate(obj1);
    memoryPool.deallocate(obj2);

    return 0;
}