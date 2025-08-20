package midterm;

import java.util.Scanner;

/*
 * Time Complexity: O(log min(a, b))
 * 說明：使用遞迴歐幾里得算法計算 GCD
 * 每次遞迴調用，較大的數至少減少一半
 * 因此遞迴深度最多為 log min(a, b) 次
 * LCM 計算為 O(1)，所以總時間複雜度為 O(log min(a, b))
 */
public class M05_GCD_LCM_Recursive {
    
    // 遞迴歐幾里得算法計算 GCD
    private static long gcd(long a, long b) {
        // 基礎情況：當 b 為 0 時，a 即為 GCD
        if (b == 0) {
            return a;
        }
        // 遞迴情況：gcd(a, b) = gcd(b, a % b)
        return gcd(b, a % b);
    }
    
    // 計算 LCM：LCM = (a / GCD) * b，避免乘法溢位
    private static long lcm(long a, long b) {
        long gcdValue = gcd(a, b);
        // 先除後乘，避免溢位
        return (a / gcdValue) * b;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取兩個正整數
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        
        // 計算 GCD 和 LCM
        long gcdResult = gcd(a, b);
        long lcmResult = lcm(a, b);
        
        // 輸出結果
        System.out.println("GCD: " + gcdResult);
        System.out.println("LCM: " + lcmResult);
        
        scanner.close();
    }
}
