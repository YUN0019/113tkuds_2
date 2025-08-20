package midterm;

import java.util.Scanner;


public class M02_YouBikeNextArrival {
    
    // 將時間字串轉換為分鐘數
    private static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }
    
    // 將分鐘數轉換為時間字串
    private static String minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
    
    // 二分搜尋找到第一個大於查詢時間的補給時間
    private static String findNextArrival(int[] arrivalTimes, int queryMinutes) {
        int left = 0;
        int right = arrivalTimes.length - 1;
        int result = -1;
        
        // 二分搜尋
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arrivalTimes[mid] > queryMinutes) {
                // 找到一個大於查詢時間的時間，記錄下來並繼續搜尋更小的
                result = arrivalTimes[mid];
                right = mid - 1;
            } else {
                // 當前時間小於等於查詢時間，搜尋右半部分
                left = mid + 1;
            }
        }
        
        // 如果找到結果，轉換為時間格式；否則返回 "No bike"
        return result != -1 ? minutesToTime(result) : "No bike";
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取補給時間個數
        int n = scanner.nextInt();
        scanner.nextLine(); // 消費換行符
        
        // 讀取補給時間並轉換為分鐘數
        int[] arrivalTimes = new int[n];
        for (int i = 0; i < n; i++) {
            String time = scanner.nextLine();
            arrivalTimes[i] = timeToMinutes(time);
        }
        
        // 讀取查詢時間
        String queryTime = scanner.nextLine();
        int queryMinutes = timeToMinutes(queryTime);
        
        // 找到下一批到站時間
        String result = findNextArrival(arrivalTimes, queryMinutes);
        
        // 輸出結果
        System.out.println(result);
        
        scanner.close();
    }
}
