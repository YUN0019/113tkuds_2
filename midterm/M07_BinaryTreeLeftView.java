package midterm;

import java.util.*;

public class M07_BinaryTreeLeftView {
    
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
    
    // 使用 BFS 找出左視圖
    private static List<Integer> getLeftView(TreeNode root) {
        List<Integer> leftView = new ArrayList<>();
        
        if (root == null) {
            return leftView;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            // 處理當前層的所有節點
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                
                // 每層第一個節點即為左視圖節點
                if (i == 0) {
                    leftView.add(current.val);
                }
                
                // 加入下一層的節點
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
        }
        
        return leftView;
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
        
        // 找出左視圖
        List<Integer> leftView = getLeftView(root);
        
        // 輸出結果
        System.out.print("LeftView: ");
        for (int i = 0; i < leftView.size(); i++) {
            System.out.print(leftView.get(i));
            if (i < leftView.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}
