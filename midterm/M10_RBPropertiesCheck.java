package midterm;

import java.util.*;


public class M10_RBPropertiesCheck {
    
    // 樹節點類別
    static class RBNode {
        int val;
        char color; // 'R' for Red, 'B' for Black
        RBNode left;
        RBNode right;
        
        public RBNode(int val, char color) {
            this.val = val;
            this.color = color;
            this.left = null;
            this.right = null;
        }
    }
    
    // 根據層序遍歷序列建立紅黑樹
    private static RBNode buildRBTree(int[] values, char[] colors, int n) {
        if (n == 0 || values[0] == -1) {
            return null;
        }
        
        // 建立節點陣列
        RBNode[] nodes = new RBNode[n];
        for (int i = 0; i < n; i++) {
            if (values[i] != -1) {
                nodes[i] = new RBNode(values[i], colors[i]);
            }
        }
        
        // 建立父子關係
        for (int i = 0; i < n; i++) {
            if (nodes[i] != null) {
                int leftIndex = 2 * i + 1;
                int rightIndex = 2 * i + 2;
                
                if (leftIndex < n && values[leftIndex] != -1) {
                    nodes[i].left = nodes[leftIndex];
                }
                if (rightIndex < n && values[rightIndex] != -1) {
                    nodes[i].right = nodes[rightIndex];
                }
            }
        }
        
        return nodes[0]; // 回傳根節點
    }
    
    // 檢查性質1：根節點為黑
    private static boolean checkRootBlack(RBNode root) {
        return root == null || root.color == 'B';
    }
    
    // 檢查性質2：不得有相鄰紅節點
    private static String checkRedRedViolation(RBNode root) {
        if (root == null) {
            return null;
        }
        
        // 檢查當前節點與其子節點的紅紅相鄰
        if (root.color == 'R') {
            if ((root.left != null && root.left.color == 'R') ||
                (root.right != null && root.right.color == 'R')) {
                return "RedRedViolation";
            }
        }
        
        // 遞迴檢查左右子樹
        String leftResult = checkRedRedViolation(root.left);
        if (leftResult != null) {
            return leftResult;
        }
        
        return checkRedRedViolation(root.right);
    }
    
    // 檢查性質3：黑高度一致
    private static boolean checkBlackHeight(RBNode root) {
        return getBlackHeight(root) != -1;
    }
    
    private static int getBlackHeight(RBNode root) {
        if (root == null) {
            return 1; // NIL 節點視為黑，貢獻黑高度 1
        }
        
        // 遞迴計算左右子樹的黑高度
        int leftHeight = getBlackHeight(root.left);
        int rightHeight = getBlackHeight(root.right);
        
        // 如果左右子樹黑高度不一致，回傳 -1 表示不合法
        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) {
            return -1;
        }
        
        // 當前節點的黑高度 = 子樹黑高度 + (當前節點為黑 ? 1 : 0)
        return leftHeight + (root.color == 'B' ? 1 : 0);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 讀取節點個數
        int n = scanner.nextInt();
        
        // 讀取節點值和顏色
        int[] values = new int[n];
        char[] colors = new char[n];
        
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            if (values[i] != -1) {
                colors[i] = scanner.next().charAt(0);
            } else {
                colors[i] = 'B'; // 空節點視為黑
            }
        }
        
        // 建立紅黑樹
        RBNode root = buildRBTree(values, colors, n);
        
        // 檢查性質1：根節點為黑
        if (!checkRootBlack(root)) {
            System.out.println("RootNotBlack");
            scanner.close();
            return;
        }
        
        // 檢查性質2：不得有相鄰紅節點
        String redRedResult = checkRedRedViolation(root);
        if (redRedResult != null) {
            System.out.println(redRedResult);
            scanner.close();
            return;
        }
        
        // 檢查性質3：黑高度一致
        if (!checkBlackHeight(root)) {
            System.out.println("BlackHeightMismatch");
            scanner.close();
            return;
        }
        
        // 所有性質都滿足
        System.out.println("RB Valid");
        
        scanner.close();
    }
}
