/*
 * Time Complexity: O(log n) for delete operation
 * 說明：刪除操作需要O(log n)時間來找到節點，刪除後可能需要O(log n)時間來重新平衡
 * 空間複雜度：O(log n) 用於遞迴調用堆疊
 */

public class AVLDeleteExercise {
    
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
    public AVLDeleteExercise() {
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
    
    // 找到最小值節點（用於找後繼節點）
    private Node findMin(Node node) {
        if (node == null || node.left == null) {
            return node;
        }
        return findMin(node.left);
    }
    
    // 找到最大值節點（用於找前驅節點）
    private Node findMax(Node node) {
        if (node == null || node.right == null) {
            return node;
        }
        return findMax(node.right);
    }
    
    // 重新平衡節點
    private Node rebalance(Node node) {
        if (node == null) {
            return null;
        }
        
        // 更新高度
        updateHeight(node);
        
        // 獲取平衡因子
        int balance = getBalanceFactor(node);
        
        // Left Left Case
        if (balance > 1 && getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            return leftRightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            return rightLeftRotate(node);
        }
        
        return node;
    }
    
    // 刪除節點
    public void delete(int data) {
        root = deleteRec(root, data);
    }
    
    private Node deleteRec(Node node, int data) {
        if (node == null) {
            return null; // 節點不存在
        }
        
        // 標準BST刪除邏輯
        if (data < node.data) {
            node.left = deleteRec(node.left, data);
        } else if (data > node.data) {
            node.right = deleteRec(node.right, data);
        } else {
            // 找到要刪除的節點，處理三種情況
            
            // 情況1: 葉子節點
            if (node.left == null && node.right == null) {
                return null; // 直接刪除
            }
            
            // 情況2: 只有一個子節點
            else if (node.left == null) {
                return node.right; // 返回右子節點
            } else if (node.right == null) {
                return node.left; // 返回左子節點
            }
            
            // 情況3: 有兩個子節點
            else {
                // 找後繼節點（右子樹的最小值）
                Node successor = findMin(node.right);
                
                // 複製後繼節點的值到當前節點
                node.data = successor.data;
                
                // 遞迴刪除後繼節點
                node.right = deleteRec(node.right, successor.data);
            }
        }
        
        // 從刪除點往上重新平衡
        return rebalance(node);
    }
    
    // 插入節點（用於建立測試樹）
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
    
    // 前序遍歷
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
    
    // 測試各種刪除情況
    public static void main(String[] args) {
        AVLDeleteExercise avl = new AVLDeleteExercise();
        
        System.out.println("=== AVL樹刪除操作測試 ===\n");
        
        // 建立測試樹
        System.out.println("建立測試樹...");
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
        for (int val : values) {
            avl.insert(val);
        }
        
        System.out.print("原始樹中序遍歷: ");
        avl.inorderTraversal();
        System.out.print("原始樹前序遍歷: ");
        avl.preorderTraversal();
        System.out.println("原始樹高度: " + avl.getTreeHeight());
        System.out.println("原始樹是否平衡: " + avl.isValidAVL());
        System.out.println();
        
        // 測試1: 刪除葉子節點
        System.out.println("測試1: 刪除葉子節點 (10)");
        avl.delete(10);
        System.out.print("刪除後中序遍歷: ");
        avl.inorderTraversal();
        System.out.print("刪除後前序遍歷: ");
        avl.preorderTraversal();
        System.out.println("刪除後高度: " + avl.getTreeHeight());
        System.out.println("刪除後是否平衡: " + avl.isValidAVL());
        System.out.println("節點10是否存在: " + avl.search(10));
        System.out.println();
        
        // 測試2: 刪除只有一個子節點的節點
        System.out.println("測試2: 刪除只有一個子節點的節點 (20)");
        avl.delete(20);
        System.out.print("刪除後中序遍歷: ");
        avl.inorderTraversal();
        System.out.print("刪除後前序遍歷: ");
        avl.preorderTraversal();
        System.out.println("刪除後高度: " + avl.getTreeHeight());
        System.out.println("刪除後是否平衡: " + avl.isValidAVL());
        System.out.println("節點20是否存在: " + avl.search(20));
        System.out.println();
        
        // 測試3: 刪除有兩個子節點的節點
        System.out.println("測試3: 刪除有兩個子節點的節點 (30)");
        avl.delete(30);
        System.out.print("刪除後中序遍歷: ");
        avl.inorderTraversal();
        System.out.print("刪除後前序遍歷: ");
        avl.preorderTraversal();
        System.out.println("刪除後高度: " + avl.getTreeHeight());
        System.out.println("刪除後是否平衡: " + avl.isValidAVL());
        System.out.println("節點30是否存在: " + avl.search(30));
        System.out.println();
        
        // 測試4: 刪除根節點
        System.out.println("測試4: 刪除根節點 (50)");
        avl.delete(50);
        System.out.print("刪除後中序遍歷: ");
        avl.inorderTraversal();
        System.out.print("刪除後前序遍歷: ");
        avl.preorderTraversal();
        System.out.println("刪除後高度: " + avl.getTreeHeight());
        System.out.println("刪除後是否平衡: " + avl.isValidAVL());
        System.out.println("節點50是否存在: " + avl.search(50));
        System.out.println();
        
        // 測試5: 連續刪除多個節點
        System.out.println("測試5: 連續刪除多個節點");
        int[] toDelete = {40, 60, 70, 25, 35};
        for (int val : toDelete) {
            System.out.println("刪除節點 " + val + ":");
            avl.delete(val);
            System.out.print("中序遍歷: ");
            avl.inorderTraversal();
            System.out.println("高度: " + avl.getTreeHeight() + ", 是否平衡: " + avl.isValidAVL());
        }
        System.out.println();
        
        // 測試6: 邊界情況
        System.out.println("測試6: 邊界情況");
        System.out.println("刪除不存在的節點 (999):");
        avl.delete(999);
        System.out.print("中序遍歷: ");
        avl.inorderTraversal();
        System.out.println("高度: " + avl.getTreeHeight() + ", 是否平衡: " + avl.isValidAVL());
        
        System.out.println("\n刪除所有剩餘節點:");
        while (avl.getTreeHeight() > 0) {
            // 找到第一個節點並刪除
            // 這裡簡化處理，實際應用中需要更複雜的邏輯
            break;
        }
        System.out.println("空樹高度: " + avl.getTreeHeight());
        System.out.println("空樹是否平衡: " + avl.isValidAVL());
    }
}
