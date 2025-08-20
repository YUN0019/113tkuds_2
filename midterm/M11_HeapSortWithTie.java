package midterm;

import java.util.*;

/*
 * Time Complexity: O(n log n)
 * 說明：使用堆排序算法，建堆時間複雜度為 O(n)
 * 每次取出堆頂元素並調整堆的時間複雜度為 O(log n)
 * 總共需要取出 n 個元素，所以總時間複雜度為 O(n log n)
 * 平手處理使用索引比較，不影響整體時間複雜度
 */
public class M11_HeapSortWithTie {
    
    // 學生分數類別，包含分數和原始索引
    static class Score {
        int score;
        int index;
        
        public Score(int score, int index) {
            this.score = score;
            this.index = index;
        }
    }
    
    // 堆排序主函數
    private static void heapSort(int[] scores) {
        int n = scores.length;
        
        // 建立分數和索引的陣列
        Score[] scoreArray = new Score[n];
        for (int i = 0; i < n; i++) {
            scoreArray[i] = new Score(scores[i], i);
        }
        
        // 建堆（Max-Heap）
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(scoreArray, n, i);
        }
        
        // 逐一取出堆頂元素（最大值）
        for (int i = n - 1; i > 0; i--) {
            // 交換堆頂和最後一個元素
            Score temp = scoreArray[0];
            scoreArray[0] = scoreArray[i];
            scoreArray[i] = temp;
            
            // 調整堆
            heapify(scoreArray, i, 0);
        }
        
        // 將排序結果複製回原陣列
        for (int i = 0; i < n; i++) {
            scores[i] = scoreArray[i].score;
        }
    }
    
    // 調整堆（Max-Heap）
    private static void heapify(Score[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        // 比較左子節點
        if (left < n && compareScores(arr[left], arr[largest]) > 0) {
            largest = left;
        }
        
        // 比較右子節點
        if (right < n && compareScores(arr[right], arr[largest]) > 0) {
            largest = right;
        }
        
        // 如果需要交換
        if (largest != i) {
            Score temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            
            // 遞迴調整子樹
            heapify(arr, n, largest);
        }
    }
    
    // 比較兩個分數（包含平手處理）
    private static int compareScores(Score a, Score b) {
        // 先比較分數
        if (a.score != b.score) {
            return Integer.compare(a.score, b.score);
        }
        // 分數相同時，索引較小的優先（穩定排序）
        return Integer.compare(a.index, b.index);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取學生個數
        int n = scanner.nextInt();
        
        // 讀取分數
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }
        
        // 執行堆排序
        heapSort(scores);
        
        // 輸出排序結果
        for (int i = 0; i < n; i++) {
            System.out.print(scores[i]);
            if (i < n - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}
