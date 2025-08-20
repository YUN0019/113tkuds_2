package midterm;

import java.util.Scanner;


public class M06_PalindromeClean {
    
    // 檢查字符是否為英文字母
    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    
    // 將字符轉換為小寫
    private static char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }
    
    // 使用雙指針法檢查回文
    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // 找到左邊第一個字母
            while (left < right && !isLetter(s.charAt(left))) {
                left++;
            }
            
            // 找到右邊第一個字母
            while (left < right && !isLetter(s.charAt(right))) {
                right--;
            }
            
            // 比較兩個字母（轉換為小寫後比較）
            if (toLowerCase(s.charAt(left)) != toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取字串
        String s = scanner.nextLine();
        
        // 檢查是否為回文
        boolean result = isPalindrome(s);
        
        // 輸出結果
        System.out.println(result ? "Yes" : "No");
        
        scanner.close();
    }
}
