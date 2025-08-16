public class AVLTree {
    private AVLNode root;
    
    // 取得節點高度
    private int getHeight(AVLNode node) {
        return (node != null) ? node.height : 0;
    }
    
    // 插入節點
    // 時間複雜度: O(log n), 空間複雜度: O(log n)
    public void insert(int data) {
        root = insertNode(root, data);
    }
    
    private AVLNode insertNode(AVLNode node, int data) {
        // 1. 標準 BST 插入
        if (node == null) {
            return new AVLNode(data);
        }
        
        if (data < node.data) {
            node.left = insertNode(node.left, data);
        } else if (data > node.data) {
            node.right = insertNode(node.right, data);
        } else {
            return node; // 重複值不插入
        }
        
        // 2. 更新高度
        node.updateHeight();
        
        // 3. 檢查平衡因子
        int balance = node.getBalance();
        
        // 4. 處理不平衡情況
        // Left Left 情況
        if (balance > 1 && data < node.left.data) {
            return AVLRotations.rightRotate(node);
        }
        
        // Right Right 情況
        if (balance < -1 && data > node.right.data) {
            return AVLRotations.leftRotate(node);
        }
        
        // Left Right 情況
        if (balance > 1 && data > node.left.data) {
            node.left = AVLRotations.leftRotate(node.left);
            return AVLRotations.rightRotate(node);
        }
        
        // Right Left 情況
        if (balance < -1 && data < node.right.data) {
            node.right = AVLRotations.rightRotate(node.right);
            return AVLRotations.leftRotate(node);
        }
        
        return node;
    }
    
    // 搜尋節點
    // 時間複雜度: O(log n), 空間複雜度: O(log n)
    public boolean search(int data) {
        return searchNode(root, data);
    }
    
    private boolean searchNode(AVLNode node, int data) {
        if (node == null) return false;
        if (data == node.data) return true;
        if (data < node.data) return searchNode(node.left, data);
        return searchNode(node.right, data);
    }
    
    // 找最小值節點
    private AVLNode findMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    // 刪除節點
    // 時間複雜度: O(log n), 空間複雜度: O(log n)
    public void delete(int data) {
        root = deleteNode(root, data);
    }
    
    private AVLNode deleteNode(AVLNode node, int data) {
        // 1. 標準 BST 刪除
        if (node == null) return null;
        
        if (data < node.data) {
            node.left = deleteNode(node.left, data);
        } else if (data > node.data) {
            node.right = deleteNode(node.right, data);
        } else {
            // 找到要刪除的節點
            if (node.left == null || node.right == null) {
                AVLNode temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    // 複製內容而不是引用
                    node.data = temp.data;
                    node.left = temp.left;
                    node.right = temp.right;
                    node.height = temp.height;
                }
            } else {
                AVLNode temp = findMin(node.right);
                node.data = temp.data;
                node.right = deleteNode(node.right, temp.data);
            }
        }
        
        if (node == null) return node;
        
        // 2. 更新高度
        node.updateHeight();
        
        // 3. 檢查平衡因子並修復
        int balance = node.getBalance();
        
        // Left Left 情況
        if (balance > 1 && node.left.getBalance() >= 0) {
            return AVLRotations.rightRotate(node);
        }
        
        // Left Right 情況
        if (balance > 1 && node.left.getBalance() < 0) {
            node.left = AVLRotations.leftRotate(node.left);
            return AVLRotations.rightRotate(node);
        }
        
        // Right Right 情況
        if (balance < -1 && node.right.getBalance() <= 0) {
            return AVLRotations.leftRotate(node);
        }
        
        // Right Left 情況
        if (balance < -1 && node.right.getBalance() > 0) {
            node.right = AVLRotations.rightRotate(node.right);
            return AVLRotations.leftRotate(node);
        }
        
        return node;
    }
    
    // 驗證是否為有效的 AVL 樹
    public boolean isValidAVL() {
        return checkAVL(root) != -1;
    }
    
    private int checkAVL(AVLNode node) {
        if (node == null) return 0;
        
        int leftHeight = checkAVL(node.left);
        int rightHeight = checkAVL(node.right);
        
        if (leftHeight == -1 || rightHeight == -1) return -1;
        
        if (Math.abs(leftHeight - rightHeight) > 1) return -1;
        
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    // 列印樹狀結構
    public void printTree() {
        printInOrder(root);
        System.out.println();
    }
    
    private void printInOrder(AVLNode node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.print(node.data + "(" + node.getBalance() + ") ");
            printInOrder(node.right);
        }
    }
}
