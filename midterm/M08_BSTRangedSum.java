package midterm;

import java.util.*;


public class M08_BSTRangedSum {
    
    // 樹節點類別
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        public TreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
    
    // 根據層序遍歷序列建立二元樹
    private static TreeNode buildTree(int[] levelOrder) {
        if (levelOrder.length == 0 || levelOrder[0] == -1) {
            return null;
        }
        
        TreeNode root = new TreeNode(levelOrder[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int i = 1;
        while (!queue.isEmpty() && i < levelOrder.length) {
            TreeNode current = queue.poll();
            
            // 處理左子節點
            if (i < levelOrder.length && levelOrder[i] != -1) {
                current.left = new TreeNode(levelOrder[i]);
                queue.offer(current.left);
            }
            i++;
            
            // 處理右子節點
            if (i < levelOrder.length && levelOrder[i] != -1) {
                current.right = new TreeNode(levelOrder[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    // 計算 BST 中落在區間 [L, R] 內的節點值總和
    private static int rangeSumBST(TreeNode root, int L, int R) {
        if (root == null) {
            return 0;
        }
        
        int sum = 0;
        
        // 如果當前節點值在區間內，加入總和
        if (root.val >= L && root.val <= R) {
            sum += root.val;
        }
        
        // 利用 BST 性質進行剪枝優化
        // 如果節點值 < L，只需走右子樹
        if (root.val < L) {
            sum += rangeSumBST(root.right, L, R);
        }
        // 如果節點值 > R，只需走左子樹
        else if (root.val > R) {
            sum += rangeSumBST(root.left, L, R);
        }
        // 如果節點值在區間內，需要走左右子樹
        else {
            sum += rangeSumBST(root.left, L, R);
            sum += rangeSumBST(root.right, L, R);
        }
        
        return sum;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取節點個數
        int n = scanner.nextInt();
        
        // 讀取層序遍歷序列
        int[] levelOrder = new int[n];
        for (int i = 0; i < n; i++) {
            levelOrder[i] = scanner.nextInt();
        }
        
        // 讀取區間 [L, R]
        int L = scanner.nextInt();
        int R = scanner.nextInt();
        
        // 建立 BST
        TreeNode root = buildTree(levelOrder);
        
        // 計算區間總和
        int sum = rangeSumBST(root, L, R);
        
        // 輸出結果
        System.out.println("Sum: " + sum);
        
        scanner.close();
    }
}
