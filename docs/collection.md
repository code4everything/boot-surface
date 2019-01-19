## 集合的封装

#### 1. 排序列表

在一些项目的开发过程中，我们有时候需要让指定的 `List` 始终保持排序的能力，虽然 `PriorityQueue` 实现这个功能，但是只有通过 `poll` 方法弹出的数据才是有序的，并不完全符合我们的需求，所以这里我封装了一个 `SortedList`

> 该类实现了通过二分法查找数据的索引位置

- 数据容器

    用来存放数据的，你可以用任何实现了 `List` 接口的数据结构来作为数据容器

    ``` java
    List<Integer> list = new ArrayList<>();
    list.add(2);
    list.add(5);
    list.add(1);
    ```
    
- 包装数据容器

    这里示例了一个升序（自然顺序）列表，需要传递一个实现了 `Comparator` 接口的比较器（下方代码的第二个参数，使用 `Lambda` 表达式简化了匿名类）

    ``` java
    SortedList<Integer, List<Integer>> sortedList = SortedList.of(list, Comparator.comparingInt(i -> i));
    ```
    
- 新增数据

    这里请注意：使用SortedList包装后，直接使用 List#add 方法是无效的（无法保持排序的能力），必须使用 SortedList#add 才能让列表保持排序

    ``` java
    sortedList.add(10);
    sortedList.add(3);
    ```
    
- 测试结果

    对上面的数据进行打印，查看结果
    
    ``` java
    System.out.println(list);
    // output --> [1, 2, 3, 5, 10]
    sortedList.addIgnoreNull(7);
    System.out.println(sortedList.getList());
    // output --> [1, 2, 3, 5, 7, 10]
    ```

> 特别说明：`SortedList` 是非线程安全的，在多线程的环境下请使用 `ConcurrentSortedList`
