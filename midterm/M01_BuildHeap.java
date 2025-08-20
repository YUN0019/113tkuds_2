package midterm;

import java.util.Scanner;

/*
 * Time Complexity: O(n)
 * 說明：使用 Bottom-up 建堆法，從最後一個非葉節點開始向下調整
 * 每個節點最多調整 log n 次，但總體攤銷時間複雜度為 O(n)
 * 因為大部分節點只需要調整很少次數
 */
public class M01_BuildHeap {
    
    private int[] heap;
    private int size;
    private boolean isMaxHeap;
    
    public M01_BuildHeap(int[] arr, boolean isMaxHeap) {
        this.heap = arr.clone();
        this.size = arr.length;
        this.isMaxHeap = isMaxHeap;
        buildHeap();
    }
    
    // 獲取左子節點索引
    private int leftChild(int i) {
        return 2 * i + 1;
    }
    
    // 獲取右子節點索引
    private int rightChild(int i) {
        return 2 * i + 2;
    }
    
    // 向下調整 (heapify down)
    private void heapifyDown(int i) {
        int largest = i;
        int left = leftChild(i);
        int right = rightChild(i);
        
        // 根據堆類型比較子節點
        if (left < size) {
            if (isMaxHeap && heap[left] > heap[largest]) {
                largest = left;
            } else if (!isMaxHeap && heap[left] < heap[largest]) {
                largest = left;
            }
        }
        
        if (right < size) {
            if (isMaxHeap && heap[right] > heap[largest]) {
                largest = right;
            } else if (!isMaxHeap && heap[right] < heap[largest]) {
                largest = right;
            }
        }
        
        // 如果需要交換，則交換並繼續向下調整
        if (largest != i) {
            swap(i, largest);
            heapifyDown(largest);
        }
    }
    
    // 交換兩個元素
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    // 自底向上建堆
    private void buildHeap() {
        // 從最後一個非葉節點開始，自底向上調整
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }
    
    // 獲取建堆後的結果
    public int[] getHeap() {
        return heap;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取堆類型
        String type = scanner.nextLine();
        boolean isMaxHeap = type.equals("max");
        
        // 讀取元素個數
        int n = scanner.nextInt();
        
        // 讀取陣列元素
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        // 建立堆
        M01_BuildHeap heapBuilder = new M01_BuildHeap(arr, isMaxHeap);
        int[] result = heapBuilder.getHeap();
        
        // 輸出結果
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}
