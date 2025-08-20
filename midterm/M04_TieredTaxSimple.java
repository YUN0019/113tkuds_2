package midterm;

import java.util.Scanner;

/*
 * Time Complexity: O(n)
 * 說明：對於每個收入，需要計算其對應的稅額
 * 每個收入最多需要檢查 4 個稅率區間，但區間數量是常數
 * 總共 n 個收入，所以時間複雜度為 O(n)
 */
public class M04_TieredTaxSimple {
    
    // 計算單一收入的稅額
    private static int calculateTax(int income) {
        int tax = 0;
        
        // 第一級距：0 - 120,000，稅率 5%
        if (income > 0) {
            int taxableAmount = Math.min(income, 120000);
            tax += (int) Math.round(taxableAmount * 0.05);
        }
        
        // 第二級距：120,001 - 500,000，稅率 12%
        if (income > 120000) {
            int taxableAmount = Math.min(income - 120000, 500000 - 120000);
            tax += (int) Math.round(taxableAmount * 0.12);
        }
        
        // 第三級距：500,001 - 1,000,000，稅率 20%
        if (income > 500000) {
            int taxableAmount = Math.min(income - 500000, 1000000 - 500000);
            tax += (int) Math.round(taxableAmount * 0.20);
        }
        
        // 第四級距：1,000,001 以上，稅率 30%
        if (income > 1000000) {
            int taxableAmount = income - 1000000;
            tax += (int) Math.round(taxableAmount * 0.30);
        }
        
        return tax;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取收入個數
        int n = scanner.nextInt();
        
        int totalTax = 0;
        
        // 處理每個收入
        for (int i = 0; i < n; i++) {
            int income = scanner.nextInt();
            int tax = calculateTax(income);
            
            // 輸出該收入的稅額
            System.out.println("Tax: " + tax);
            
            // 累計總稅額
            totalTax += tax;
        }
        
        // 計算並輸出平均稅額
        int averageTax = (int) Math.round((double) totalTax / n);
        System.out.println("Average: " + averageTax);
        
        scanner.close();
    }
}
