package finalexam;

import java.util.Scanner;

public class LC27_RemoveElement_Recycle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int val = sc.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }
        int len = removeElement(nums, n, val);
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(nums[i]);
        }
    }

    public static int removeElement(int[] nums, int n, int val) {
        int write = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] != val) {
                nums[write++] = nums[i];
            }
        }
        return write;
    }
}
