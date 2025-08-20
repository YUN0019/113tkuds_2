package midterm;

import java.util.*;

/*
 * Time Complexity: O(n)
 * 說明：需要遍歷層序序列一次來建立樹，時間複雜度 O(n)
 * 然後使用遞迴遍歷樹一次檢查 BST 性質，每個節點最多被訪問一次
 * 同時使用後序遍歷檢查 AVL 平衡因子，每個節點最多被訪問一次
 * 總時間複雜度為 O(n)，其中 n 為節點個數
 */
public class M09_AVLValidate {
    
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
    
    // 檢查是否為有效的 BST（使用上下界遞迴）
    private static boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private static boolean isValidBSTHelper(TreeNode root, long min, long max) {
        if (root == null) {
            return true;
        }
        
        // 檢查當前節點值是否在有效範圍內
        if (root.val <= min || root.val >= max) {
            return false;
        }
        
        // 遞迴檢查左右子樹
        return isValidBSTHelper(root.left, min, root.val) && 
               isValidBSTHelper(root.right, root.val, max);
    }
    
    // 檢查是否為有效的 AVL（後序遍歷回傳高度）
    private static boolean isValidAVL(TreeNode root) {
        return checkAVLHelper(root) != Integer.MIN_VALUE;
    }
    
    private static int checkAVLHelper(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 後序遍歷：先檢查左右子樹
        int leftHeight = checkAVLHelper(root.left);
        int rightHeight = checkAVLHelper(root.right);
        
        // 如果子樹不合法，回傳特殊值
        if (leftHeight == Integer.MIN_VALUE || rightHeight == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        
        // 計算平衡因子
        int balanceFactor = leftHeight - rightHeight;
        
        // 檢查平衡因子是否在 [-1, 1] 範圍內
        if (Math.abs(balanceFactor) > 1) {
            return Integer.MIN_VALUE; // 不合法
        }
        
        // 回傳當前節點的高度
        return Math.max(leftHeight, rightHeight) + 1;
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
        
        // 建立樹
        TreeNode root = buildTree(levelOrder);
        
        // 檢查 BST 性質
        boolean isBST = isValidBST(root);
        
        if (!isBST) {
            System.out.println("Invalid BST");
        } else {
            // 檢查 AVL 性質
            boolean isAVL = isValidAVL(root);
            
            if (isAVL) {
                System.out.println("Valid");
            } else {
                System.out.println("Invalid AVL");
            }
        }
        
        scanner.close();
    }
}
