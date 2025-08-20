package midterm;

import java.util.*;

public class M12_MergeKTimeTables {
    
    // 時刻表元素類別，包含時間、時刻表編號和索引
    static class TimeEntry {
        int time;
        int listIndex;
        int elementIndex;
        
        public TimeEntry(int time, int listIndex, int elementIndex) {
            this.time = time;
            this.listIndex = listIndex;
            this.elementIndex = elementIndex;
        }
    }
    
    // 合併 K 個時刻表
    private static List<Integer> mergeKTimeTables(List<List<Integer>> timeTables) {
        List<Integer> result = new ArrayList<>();
        
        if (timeTables.isEmpty()) {
            return result;
        }
        
        // 使用 Min-Heap 來合併時刻表
        PriorityQueue<TimeEntry> minHeap = new PriorityQueue<>((a, b) -> {
            // 按時間排序，時間相同時按時刻表編號排序
            if (a.time != b.time) {
                return Integer.compare(a.time, b.time);
            }
            return Integer.compare(a.listIndex, b.listIndex);
        });
        
        // 將每個時刻表的第一個元素加入堆中
        for (int i = 0; i < timeTables.size(); i++) {
            List<Integer> timeTable = timeTables.get(i);
            if (!timeTable.isEmpty()) {
                minHeap.offer(new TimeEntry(timeTable.get(0), i, 0));
            }
        }
        
        // 逐一取出堆頂元素並加入下一個元素
        while (!minHeap.isEmpty()) {
            TimeEntry current = minHeap.poll();
            result.add(current.time);
            
            // 將該時刻表的下一個元素加入堆中
            List<Integer> currentTable = timeTables.get(current.listIndex);
            int nextIndex = current.elementIndex + 1;
            
            if (nextIndex < currentTable.size()) {
                minHeap.offer(new TimeEntry(currentTable.get(nextIndex), 
                                          current.listIndex, nextIndex));
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取時刻表數量
        int K = scanner.nextInt();
        
        // 讀取每個時刻表
        List<List<Integer>> timeTables = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            int len = scanner.nextInt();
            List<Integer> timeTable = new ArrayList<>();
            
            for (int j = 0; j < len; j++) {
                timeTable.add(scanner.nextInt());
            }
            
            timeTables.add(timeTable);
        }
        
        // 合併時刻表
        List<Integer> mergedSchedule = mergeKTimeTables(timeTables);
        
        // 輸出合併後的時刻表
        for (int i = 0; i < mergedSchedule.size(); i++) {
            System.out.print(mergedSchedule.get(i));
            if (i < mergedSchedule.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}
