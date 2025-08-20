package midterm;

import java.util.*;

/*
 * Time Complexity: O(n log K)
 * 說明：使用大小為 K 的 Min-Heap 來維護 Top-K 元素
 * 對於每個商品，最多需要 log K 的時間來插入或更新堆
 * 總共 n 個商品，所以時間複雜度為 O(n log K)
 * 當 K 遠小於 n 時，此方法比排序更有效率
 */
public class M03_TopKConvenience {
    
    // 商品類別，包含名稱和銷量
    static class Item {
        String name;
        int quantity;
        
        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
    
    // 使用 Min-Heap 找出 Top-K 商品
    private static List<Item> findTopKItems(List<Item> items, int k) {
        // 使用 Min-Heap，比較器按銷量排序（相同銷量按字典序）
        PriorityQueue<Item> minHeap = new PriorityQueue<>((a, b) -> {
            if (a.quantity != b.quantity) {
                return Integer.compare(a.quantity, b.quantity); // 銷量升序（Min-Heap）
            } else {
                return a.name.compareTo(b.name); // 相同銷量按字典序
            }
        });
        
        // 遍歷所有商品
        for (Item item : items) {
            if (minHeap.size() < k) {
                // 堆的大小小於 K，直接加入
                minHeap.offer(item);
            } else if (item.quantity > minHeap.peek().quantity) {
                // 當前商品銷量大於堆頂元素，替換堆頂
                minHeap.poll();
                minHeap.offer(item);
            }
            // 如果當前商品銷量小於等於堆頂，則忽略
        }
        
        // 將堆中的元素轉換為列表並排序
        List<Item> result = new ArrayList<>(minHeap);
        // 按銷量降序排序（相同銷量按字典序）
        result.sort((a, b) -> {
            if (a.quantity != b.quantity) {
                return Integer.compare(b.quantity, a.quantity); // 銷量降序
            } else {
                return a.name.compareTo(b.name); // 相同銷量按字典序
            }
        });
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取商品個數和 Top-K 的 K 值
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.nextLine(); // 消費換行符
        
        // 讀取所有商品資訊
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            String name = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            items.add(new Item(name, quantity));
        }
        
        // 找出 Top-K 商品
        List<Item> topKItems = findTopKItems(items, k);
        
        // 輸出結果
        for (Item item : topKItems) {
            System.out.println(item.name + " " + item.quantity);
        }
        
        scanner.close();
    }
}
