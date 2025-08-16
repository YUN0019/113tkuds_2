/*
 * Time Complexity: O(1) for each rotation operation
 * 說明：每次旋轉操作只需要常數時間來重新連接節點
 * 空間複雜度：O(1) 只使用常數額外空間
 */

public class AVLRotationExercise {
    
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
    public AVLRotationExercise() {
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
    
    // 1. 左旋轉 (Left Rotation)
    public Node leftRotate(Node x) {
        if (x == null || x.right == null) {
            return x; // 無法進行左旋轉
        }
        
        Node y = x.right;
        Node T2 = y.left;
        
        // 執行旋轉
        y.left = x;
        x.right = T2;
        
        // 更新高度
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    // 2. 右旋轉 (Right Rotation)
    public Node rightRotate(Node y) {
        if (y == null || y.left == null) {
            return y; // 無法進行右旋轉
        }
        
        Node x = y.left;
        Node T2 = x.right;
        
        // 執行旋轉
        x.right = y;
        y.left = T2;
        
        // 更新高度
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    // 3. 左右旋轉 (Left-Right Rotation)
    public Node leftRightRotate(Node z) {
        if (z == null || z.left == null) {
            return z;
        }
        
        // 先對左子節點進行左旋轉
        z.left = leftRotate(z.left);
        
        // 再對根節點進行右旋轉
        return rightRotate(z);
    }
    
    // 4. 右左旋轉 (Right-Left Rotation)
    public Node rightLeftRotate(Node z) {
        if (z == null || z.right == null) {
            return z;
        }
        
        // 先對右子節點進行右旋轉
        z.right = rightRotate(z.right);
        
        // 再對根節點進行左旋轉
        return leftRotate(z);
    }
    
    // 插入節點並自動平衡
    public void insert(int data) {
        root = insertRec(root, data);
    }
    
    private Node insertRec(Node node, int data) {
        // 標準BST插入
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
        
        // 更新高度
        updateHeight(node);
        
        // 獲取平衡因子
        int balance = getBalanceFactor(node);
        
        // 根據不平衡情況進行相應旋轉
        if (balance > 1) {
            // Left Left Case
            if (data < node.left.data) {
                return rightRotate(node);
            }
            // Left Right Case
            else {
                return leftRightRotate(node);
            }
        }
        
        if (balance < -1) {
            // Right Right Case
            if (data > node.right.data) {
                return leftRotate(node);
            }
            // Right Left Case
            else {
                return rightLeftRotate(node);
            }
        }
        
        return node;
    }
    
    // 檢查是否為有效的AVL樹
    public boolean isValidAVL() {
        return isValidAVLRec(root);
    }
    
    private boolean isValidAVLRec(Node node) {
        if (node == null) {
            return true;
        }
        
        // 檢查左子樹
        if (!isValidAVLRec(node.left)) {
            return false;
        }
        
        // 檢查右子樹
        if (!isValidAVLRec(node.right)) {
            return false;
        }
        
        // 檢查平衡因子
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
    
    // 前序遍歷（用於顯示樹結構）
    public void preorderTraversal() {
        preorderRec(root);
        System.out.println();
    }
    
    private void preorderRec(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preorderRec(node.left);
            preorderRec(node.right);
        }
    }
    
    // 獲取樹的高度
    public int getTreeHeight() {
        return getHeight(root);
    }
    
    // 測試各種不平衡情況
    public static void main(String[] args) {
        AVLRotationExercise avl = new AVLRotationExercise();
        
        System.out.println("=== AVL樹旋轉操作測試 ===\n");
        
        // 測試1: Left Left Case (需要右旋轉)
        System.out.println("測試1: Left Left Case (右旋轉)");
        System.out.println("插入順序: 30, 20, 10");
        avl.insert(30);
        avl.insert(20);
        avl.insert(10);
        System.out.print("前序遍歷: ");
        avl.preorderTraversal();
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 重置樹
        avl = new AVLRotationExercise();
        
        // 測試2: Right Right Case (需要左旋轉)
        System.out.println("測試2: Right Right Case (左旋轉)");
        System.out.println("插入順序: 10, 20, 30");
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        System.out.print("前序遍歷: ");
        avl.preorderTraversal();
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 重置樹
        avl = new AVLRotationExercise();
        
        // 測試3: Left Right Case (需要左右旋轉)
        System.out.println("測試3: Left Right Case (左右旋轉)");
        System.out.println("插入順序: 30, 10, 20");
        avl.insert(30);
        avl.insert(10);
        avl.insert(20);
        System.out.print("前序遍歷: ");
        avl.preorderTraversal();
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 重置樹
        avl = new AVLRotationExercise();
        
        // 測試4: Right Left Case (需要右左旋轉)
        System.out.println("測試4: Right Left Case (右左旋轉)");
        System.out.println("插入順序: 10, 30, 20");
        avl.insert(10);
        avl.insert(30);
        avl.insert(20);
        System.out.print("前序遍歷: ");
        avl.preorderTraversal();
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 測試5: 複雜情況
        System.out.println("測試5: 複雜情況 - 多種旋轉組合");
        avl = new AVLRotationExercise();
        System.out.println("插入順序: 50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45");
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        for (int val : values) {
            avl.insert(val);
        }
        System.out.print("前序遍歷: ");
        avl.preorderTraversal();
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 測試6: 邊界情況
        System.out.println("測試6: 邊界情況");
        avl = new AVLRotationExercise();
        System.out.println("空樹測試:");
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        
        System.out.println("\n單節點測試:");
        avl.insert(42);
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
        
        System.out.println("\n重複值測試:");
        avl.insert(42);
        avl.insert(42);
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("樹高度: " + avl.getTreeHeight());
        System.out.println("是否平衡: " + avl.isValidAVL());
    }
}
