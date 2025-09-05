package finalexam;

import java.util.Scanner;

public class LC04_Median_QuakeFeeds {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        double[] nums1 = new double[n];
        double[] nums2 = new double[m];
        for (int i = 0; i < n; i++) nums1[i] = sc.nextDouble();
        for (int i = 0; i < m; i++) nums2[i] = sc.nextDouble();
        double median = findMedianSortedArrays(nums1, nums2);
        System.out.printf("%.1f\n", median);
    }

    public static double findMedianSortedArrays(double[] nums1, double[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int n = nums1.length, m = nums2.length;
        int left = 0, right = n;
        while (left <= right) {
            int i = (left + right) / 2;
            int j = (n + m + 1) / 2 - i;

            double maxLeftA = (i == 0) ? Double.NEGATIVE_INFINITY : nums1[i - 1];
            double minRightA = (i == n) ? Double.POSITIVE_INFINITY : nums1[i];
            double maxLeftB = (j == 0) ? Double.NEGATIVE_INFINITY : nums2[j - 1];
            double minRightB = (j == m) ? Double.POSITIVE_INFINITY : nums2[j];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                if ((n + m) % 2 == 0) {
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                } else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            } else if (maxLeftA > minRightB) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        }
        return 0.0; // 不會到這裡


}    }    }
}
