/*
 * Time Complexity: O(log n + k) where k is the number of results
 * 說明：使用BST性質剪枝，只需要遍歷範圍內的節點，加上找到範圍邊界的O(log n)時間
 * 空間複雜度：O(k) 用於存儲結果列表
 */

import java.util.*;

public class AVLRangeQueryExercise {
    
    // AVL樹節點類別
    static class Node {
        int data;
        Node left, right;
        int height;
        
        Node(int data) {
            this.data = data;
            this.height = 1;
        }
    }
    
    // 根節點
    private Node root;
    
    // 建構函數
    public AVLRangeQueryExercise() {
        root = null;
    }
    
    // 獲取節點高度
    private int getHeight(Node node) {
        if (node == null) return 0;
        return node.height;
    }
    
    // 獲取平衡因子
    private int getBalanceFactor(Node node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }
    
    // 更新節點高度
    private void updateHeight(Node node) {
        if (node != null) {
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        }
    }
    
    // 左旋轉
    private Node leftRotate(Node x) {
        if (x == null || x.right == null) {
            return x;
        }
        
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    // 右旋轉
    private Node rightRotate(Node y) {
        if (y == null || y.left == null) {
            return y;
        }
        
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    // 左右旋轉
    private Node leftRightRotate(Node z) {
        if (z == null || z.left == null) {
            return z;
        }
        
        z.left = leftRotate(z.left);
        return rightRotate(z);
    }
    
    // 右左旋轉
    private Node rightLeftRotate(Node z) {
        if (z == null || z.right == null) {
            return z;
        }
        
        z.right = rightRotate(z.right);
        return leftRotate(z);
    }
    
    // 插入節點
    public void insert(int data) {
        root = insertRec(root, data);
    }
    
    private Node insertRec(Node node, int data) {
        if (node == null) {
            return new Node(data);
        }
        
        if (data < node.data) {
            node.left = insertRec(node.left, data);
        } else if (data > node.data) {
            node.right = insertRec(node.right, data);
        } else {
            return node; // 重複值
        }
        
        updateHeight(node);
        
        int balance = getBalanceFactor(node);
        
        if (balance > 1) {
            if (data < node.left.data) {
                return rightRotate(node);
            } else {
                return leftRightRotate(node);
            }
        }
        
        if (balance < -1) {
            if (data > node.right.data) {
                return leftRotate(node);
            } else {
                return rightLeftRotate(node);
            }
        }
        
        return node;
    }
    
    // 範圍查詢 - 主要方法
    public List<Integer> rangeQuery(int min, int max) {
        List<Integer> result = new ArrayList<>();
        rangeQueryRec(root, min, max, result);
        return result;
    }
    
    // 遞迴範圍查詢實現
    private void rangeQueryRec(Node node, int min, int max, List<Integer> result) {
        if (node == null) {
            return;
        }
        
        // 利用BST性質進行剪枝
        // 如果當前節點值小於min，則左子樹都不在範圍內，跳過
        if (node.data >= min) {
            rangeQueryRec(node.left, min, max, result);
        }
        
        // 如果當前節點在範圍內，加入結果
        if (node.data >= min && node.data <= max) {
            result.add(node.data);
        }
        
        // 如果當前節點值大於max，則右子樹都不在範圍內，跳過
        if (node.data <= max) {
            rangeQueryRec(node.right, min, max, result);
        }
    }
    
    // 優化版本：使用迭代實現（可選）
    public List<Integer> rangeQueryIterative(int min, int max) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Stack<Node> stack = new Stack<>();
        Node current = root;
        
        while (current != null || !stack.isEmpty()) {
            // 遍歷到最左邊
            while (current != null) {
                stack.push(current);
                // 剪枝：如果當前節點值小於min，跳過左子樹
                if (current.data < min) {
                    current = null;
                } else {
                    current = current.left;
                }
            }
            
            if (!stack.isEmpty()) {
                current = stack.pop();
                
                // 檢查是否在範圍內
                if (current.data >= min && current.data <= max) {
                    result.add(current.data);
                }
                
                // 剪枝：如果當前節點值大於max，跳過右子樹
                if (current.data <= max) {
                    current = current.right;
                } else {
                    current = null;
                }
            }
        }
        
        return result;
    }
    
    // 統計範圍內的元素個數
    public int countInRange(int min, int max) {
        return countInRangeRec(root, min, max);
    }
    
    private int countInRangeRec(Node node, int min, int max) {
        if (node == null) {
            return 0;
        }
        
        int count = 0;
        
        // 利用BST性質進行剪枝
        if (node.data >= min) {
            count += countInRangeRec(node.left, min, max);
        }
        
        if (node.data >= min && node.data <= max) {
            count++;
        }
        
        if (node.data <= max) {
            count += countInRangeRec(node.right, min, max);
        }
        
        return count;
    }
    
    // 檢查是否為有效的AVL樹
    public boolean isValidAVL() {
        return isValidAVLRec(root);
    }
    
    private boolean isValidAVLRec(Node node) {
        if (node == null) {
            return true;
        }
        
        if (!isValidAVLRec(node.left)) {
            return false;
        }
        
        if (!isValidAVLRec(node.right)) {
            return false;
        }
        
        int balance = getBalanceFactor(node);
        if (balance < -1 || balance > 1) {
            return false;
        }
        
        return true;
    }
    
    // 中序遍歷
    public void inorderTraversal() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(Node node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.print(node.data + " ");
            inorderRec(node.right);
        }
    }
    
    // 獲取樹的高度
    public int getTreeHeight() {
        return getHeight(root);
    }
    
    // 測試範圍查詢功能
    public static void main(String[] args) {
        AVLRangeQueryExercise avl = new AVLRangeQueryExercise();
        
        System.out.println("=== AVL樹範圍查詢測試 ===\n");
        
        // 建立測試樹
        System.out.println("建立測試樹...");
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
        for (int val : values) {
            avl.insert(val);
        }
        
        System.out.print("樹的中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否為有效AVL樹: " + avl.isValidAVL());
        System.out.println();
        
        // 測試1: 基本範圍查詢
        System.out.println("測試1: 基本範圍查詢");
        System.out.println("查詢範圍 [25, 60]:");
        List<Integer> result1 = avl.rangeQuery(25, 60);
        System.out.println("結果: " + result1);
        System.out.println("結果數量: " + result1.size());
        System.out.println();
        
        // 測試2: 邊界範圍查詢
        System.out.println("測試2: 邊界範圍查詢");
        System.out.println("查詢範圍 [10, 85]:");
        List<Integer> result2 = avl.rangeQuery(10, 85);
        System.out.println("結果: " + result2);
        System.out.println("結果數量: " + result2.size());
        System.out.println();
        
        // 測試3: 小範圍查詢
        System.out.println("測試3: 小範圍查詢");
        System.out.println("查詢範圍 [35, 45]:");
        List<Integer> result3 = avl.rangeQuery(35, 45);
        System.out.println("結果: " + result3);
        System.out.println("結果數量: " + result3.size());
        System.out.println();
        
        // 測試4: 空範圍查詢
        System.out.println("測試4: 空範圍查詢");
        System.out.println("查詢範圍 [90, 100]:");
        List<Integer> result4 = avl.rangeQuery(90, 100);
        System.out.println("結果: " + result4);
        System.out.println("結果數量: " + result4.size());
        System.out.println();
        
        // 測試5: 單點範圍查詢
        System.out.println("測試5: 單點範圍查詢");
        System.out.println("查詢範圍 [50, 50]:");
        List<Integer> result5 = avl.rangeQuery(50, 50);
        System.out.println("結果: " + result5);
        System.out.println("結果數量: " + result5.size());
        System.out.println();
        
        // 測試6: 統計範圍內元素個數
        System.out.println("測試6: 統計範圍內元素個數");
        System.out.println("範圍 [20, 70] 內的元素個數: " + avl.countInRange(20, 70));
        System.out.println("範圍 [0, 100] 內的元素個數: " + avl.countInRange(0, 100));
        System.out.println("範圍 [100, 200] 內的元素個數: " + avl.countInRange(100, 200));
        System.out.println();
        
        // 測試7: 迭代版本與遞迴版本比較
        System.out.println("測試7: 迭代版本與遞迴版本比較");
        System.out.println("查詢範圍 [30, 60]:");
        List<Integer> recursiveResult = avl.rangeQuery(30, 60);
        List<Integer> iterativeResult = avl.rangeQueryIterative(30, 60);
        System.out.println("遞迴版本結果: " + recursiveResult);
        System.out.println("迭代版本結果: " + iterativeResult);
        System.out.println("結果是否相同: " + recursiveResult.equals(iterativeResult));
        System.out.println();
        
        // 測試8: 性能測試
        System.out.println("測試8: 性能測試");
        System.out.println("測試大範圍查詢的性能...");
        long startTime = System.nanoTime();
        List<Integer> largeRange = avl.rangeQuery(0, 100);
        long endTime = System.nanoTime();
        System.out.println("查詢範圍 [0, 100] 耗時: " + (endTime - startTime) + " 納秒");
        System.out.println("結果數量: " + largeRange.size());
        
        startTime = System.nanoTime();
        List<Integer> smallRange = avl.rangeQuery(40, 45);
        endTime = System.nanoTime();
        System.out.println("查詢範圍 [40, 45] 耗時: " + (endTime - startTime) + " 納秒");
        System.out.println("結果數量: " + smallRange.size());
    }
}
