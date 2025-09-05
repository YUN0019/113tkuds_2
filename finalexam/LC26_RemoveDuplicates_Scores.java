package finalexam;

import java.util.Scanner;

public class LC26_RemoveDuplicates_Scores {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }
        int len = removeDuplicates(nums, n);
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(nums[i]);
        }
    }

    public static int removeDuplicates(int[] nums, int n) {
        if (n == 0) return 0;
        int write = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] != nums[write - 1]) {
                nums[write++] = nums[i];
            }
        }
        return write;
    }
}
