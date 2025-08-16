/*
 * Time Complexity: O(log n) for insert and search operations
 * 說明：AVL樹保持平衡，每次操作最多需要O(log n)時間來遍歷樹的高度
 * 空間複雜度：O(n) 用於存儲n個節點
 */

public class AVLBasicExercise {
    
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
    public AVLBasicExercise() {
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
    
    // 右旋轉
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        // 更新高度
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        
        return x;
    }
    
    // 左旋轉
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        // 更新高度
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        
        return y;
    }
    
    // 插入節點
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
            // 重複值不插入
            return node;
        }
        
        // 更新節點高度
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        
        // 獲取平衡因子
        int balance = getBalanceFactor(node);
        
        // 如果節點不平衡，有4種情況
        
        // Left Left Case
        if (balance > 1 && data < node.left.data) {
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && data > node.right.data) {
            return leftRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && data > node.left.data) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && data < node.right.data) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    // 搜尋節點
    public boolean search(int data) {
        return searchRec(root, data);
    }
    
    private boolean searchRec(Node node, int data) {
        if (node == null) {
            return false;
        }
        
        if (node.data == data) {
            return true;
        }
        
        if (data < node.data) {
            return searchRec(node.left, data);
        } else {
            return searchRec(node.right, data);
        }
    }
    
    // 計算樹的高度
    public int getTreeHeight() {
        return getHeight(root);
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
        
        // 檢查平衡因子是否在[-1,1]範圍內
        int balance = getBalanceFactor(node);
        if (balance < -1 || balance > 1) {
            return false;
        }
        
        return true;
    }
    
    // 中序遍歷（用於測試）
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
    
    // 測試主函數
    public static void main(String[] args) {
        AVLBasicExercise avl = new AVLBasicExercise();
        
        // 測試插入
        System.out.println("插入節點: 10, 20, 30, 40, 50, 25");
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        avl.insert(40);
        avl.insert(50);
        avl.insert(25);
        
        // 測試中序遍歷
        System.out.print("中序遍歷結果: ");
        avl.inorderTraversal();
        
        // 測試搜尋
        System.out.println("搜尋 25: " + avl.search(25));
        System.out.println("搜尋 35: " + avl.search(35));
        
        // 測試樹高度
        System.out.println("樹的高度: " + avl.getTreeHeight());
        
        // 測試AVL有效性
        System.out.println("是否為有效AVL樹: " + avl.isValidAVL());
    }
}
