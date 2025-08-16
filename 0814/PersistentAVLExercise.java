/*
 * Time Complexity: O(log n) for insert and query operations
 * 說明：每次操作需要複製路徑上的節點，但AVL樹高度為O(log n)
 * 空間複雜度：O(log n) per operation，版本間共享不變節點
 */

import java.util.*;

public class PersistentAVLExercise {
    
    // 不可變的AVL樹節點類別
    static class ImmutableNode {
        final int data;
        final ImmutableNode left, right;
        final int height;
        
        ImmutableNode(int data, ImmutableNode left, ImmutableNode right, int height) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.height = height;
        }
        
        // 創建新節點（不可變）
        static ImmutableNode create(int data, ImmutableNode left, ImmutableNode right) {
            int height = Math.max(getHeight(left), getHeight(right)) + 1;
            return new ImmutableNode(data, left, right, height);
        }
        
        // 獲取節點高度
        static int getHeight(ImmutableNode node) {
            return node != null ? node.height : 0;
        }
        
        // 獲取平衡因子
        static int getBalanceFactor(ImmutableNode node) {
            if (node == null) return 0;
            return getHeight(node.left) - getHeight(node.right);
        }
    }
    
    // 版本類別
    static class Version {
        final int versionId;
        final ImmutableNode root;
        final int size;
        
        Version(int versionId, ImmutableNode root, int size) {
            this.versionId = versionId;
            this.root = root;
            this.size = size;
        }
    }
    
    // 版本管理器
    private List<Version> versions;
    private int nextVersionId;
    
    // 建構函數
    public PersistentAVLExercise() {
        this.versions = new ArrayList<>();
        this.nextVersionId = 0;
        // 創建初始版本（空樹）
        versions.add(new Version(nextVersionId++, null, 0));
    }
    
    // 獲取當前版本
    public int getCurrentVersion() {
        return versions.size() - 1;
    }
    
    // 獲取指定版本
    public Version getVersion(int versionId) {
        if (versionId < 0 || versionId >= versions.size()) {
            return null;
        }
        return versions.get(versionId);
    }
    
    // 插入操作 - 產生新版本
    public int insert(int data) {
        Version currentVersion = versions.get(versions.size() - 1);
        ImmutableNode newRoot = insertRec(currentVersion.root, data);
        int newSize = currentVersion.size + 1;
        
        Version newVersion = new Version(nextVersionId++, newRoot, newSize);
        versions.add(newVersion);
        
        return newVersion.versionId;
    }
    
    // 遞迴插入實現（路徑複製）
    private ImmutableNode insertRec(ImmutableNode node, int data) {
        if (node == null) {
            return ImmutableNode.create(data, null, null);
        }
        
        ImmutableNode newLeft = node.left;
        ImmutableNode newRight = node.right;
        
        if (data < node.data) {
            newLeft = insertRec(node.left, data);
        } else if (data > node.data) {
            newRight = insertRec(node.right, data);
        } else {
            // 重複值，返回原節點
            return node;
        }
        
        // 創建新節點（路徑複製）
        ImmutableNode newNode = ImmutableNode.create(node.data, newLeft, newRight);
        
        // 檢查平衡並進行必要的旋轉
        return rebalance(newNode);
    }
    
    // 重新平衡節點
    private ImmutableNode rebalance(ImmutableNode node) {
        if (node == null) {
            return null;
        }
        
        int balance = ImmutableNode.getBalanceFactor(node);
        
        // Left Left Case
        if (balance > 1 && ImmutableNode.getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && ImmutableNode.getBalanceFactor(node.left) < 0) {
            return leftRightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && ImmutableNode.getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && ImmutableNode.getBalanceFactor(node.right) > 0) {
            return rightLeftRotate(node);
        }
        
        return node;
    }
    
    // 左旋轉（創建新節點）
    private ImmutableNode leftRotate(ImmutableNode x) {
        if (x == null || x.right == null) {
            return x;
        }
        
        ImmutableNode y = x.right;
        ImmutableNode T2 = y.left;
        
        // 創建新的節點結構
        ImmutableNode newX = ImmutableNode.create(x.data, x.left, T2);
        ImmutableNode newY = ImmutableNode.create(y.data, newX, y.right);
        
        return newY;
    }
    
    // 右旋轉（創建新節點）
    private ImmutableNode rightRotate(ImmutableNode y) {
        if (y == null || y.left == null) {
            return y;
        }
        
        ImmutableNode x = y.left;
        ImmutableNode T2 = x.right;
        
        // 創建新的節點結構
        ImmutableNode newY = ImmutableNode.create(y.data, T2, y.right);
        ImmutableNode newX = ImmutableNode.create(x.data, x.left, newY);
        
        return newX;
    }
    
    // 左右旋轉
    private ImmutableNode leftRightRotate(ImmutableNode z) {
        if (z == null || z.left == null) {
            return z;
        }
        
        // 先對左子節點進行左旋轉
        ImmutableNode newLeft = leftRotate(z.left);
        ImmutableNode newZ = ImmutableNode.create(z.data, newLeft, z.right);
        
        // 再對根節點進行右旋轉
        return rightRotate(newZ);
    }
    
    // 右左旋轉
    private ImmutableNode rightLeftRotate(ImmutableNode z) {
        if (z == null || z.right == null) {
            return z;
        }
        
        // 先對右子節點進行右旋轉
        ImmutableNode newRight = rightRotate(z.right);
        ImmutableNode newZ = ImmutableNode.create(z.data, z.left, newRight);
        
        // 再對根節點進行左旋轉
        return leftRotate(newZ);
    }
    
    // 查詢操作（不產生新版本）
    public boolean search(int versionId, int data) {
        Version version = getVersion(versionId);
        if (version == null) {
            return false;
        }
        return searchRec(version.root, data);
    }
    
    private boolean searchRec(ImmutableNode node, int data) {
        if (node == null) {
            return false;
        }
        
        if (data == node.data) {
            return true;
        } else if (data < node.data) {
            return searchRec(node.left, data);
        } else {
            return searchRec(node.right, data);
        }
    }
    
    // 獲取指定版本的中序遍歷
    public List<Integer> inorderTraversal(int versionId) {
        List<Integer> result = new ArrayList<>();
        Version version = getVersion(versionId);
        if (version != null) {
            inorderRec(version.root, result);
        }
        return result;
    }
    
    private void inorderRec(ImmutableNode node, List<Integer> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.data);
            inorderRec(node.right, result);
        }
    }
    
    // 獲取指定版本的樹高度
    public int getTreeHeight(int versionId) {
        Version version = getVersion(versionId);
        return version != null ? ImmutableNode.getHeight(version.root) : 0;
    }
    
    // 獲取指定版本的大小
    public int getSize(int versionId) {
        Version version = getVersion(versionId);
        return version != null ? version.size : 0;
    }
    
    // 檢查指定版本是否為有效的AVL樹
    public boolean isValidAVL(int versionId) {
        Version version = getVersion(versionId);
        return version != null && isValidAVLRec(version.root);
    }
    
    private boolean isValidAVLRec(ImmutableNode node) {
        if (node == null) {
            return true;
        }
        
        if (!isValidAVLRec(node.left)) {
            return false;
        }
        
        if (!isValidAVLRec(node.right)) {
            return false;
        }
        
        int balance = ImmutableNode.getBalanceFactor(node);
        if (balance < -1 || balance > 1) {
            return false;
        }
        
        return true;
    }
    
    // 顯示指定版本的樹結構
    public void displayVersion(int versionId) {
        Version version = getVersion(versionId);
        if (version == null) {
            System.out.println("版本 " + versionId + " 不存在");
            return;
        }
        
        System.out.println("=== 版本 " + versionId + " ===");
        System.out.println("大小: " + version.size);
        System.out.println("高度: " + getTreeHeight(versionId));
        System.out.println("是否平衡: " + isValidAVL(versionId));
        System.out.print("中序遍歷: ");
        List<Integer> traversal = inorderTraversal(versionId);
        System.out.println(traversal);
        System.out.println();
    }
    
    // 比較兩個版本
    public void compareVersions(int version1, int version2) {
        Version v1 = getVersion(version1);
        Version v2 = getVersion(version2);
        
        if (v1 == null || v2 == null) {
            System.out.println("版本不存在");
            return;
        }
        
        System.out.println("=== 版本比較: " + version1 + " vs " + version2 + " ===");
        System.out.println("版本 " + version1 + " 大小: " + v1.size);
        System.out.println("版本 " + version2 + " 大小: " + v2.size);
        System.out.println("版本 " + version1 + " 高度: " + getTreeHeight(version1));
        System.out.println("版本 " + version2 + " 高度: " + getTreeHeight(version2));
        
        List<Integer> traversal1 = inorderTraversal(version1);
        List<Integer> traversal2 = inorderTraversal(version2);
        
        System.out.println("版本 " + version1 + " 中序遍歷: " + traversal1);
        System.out.println("版本 " + version2 + " 中序遍歷: " + traversal2);
        System.out.println();
    }
    
    // 測試持久化AVL樹
    public static void main(String[] args) {
        PersistentAVLExercise persistentAVL = new PersistentAVLExercise();
        
        System.out.println("=== 持久化AVL樹測試 ===\n");
        
        // 測試1: 基本插入操作
        System.out.println("測試1: 基本插入操作");
        System.out.println("插入 50");
        int v1 = persistentAVL.insert(50);
        persistentAVL.displayVersion(v1);
        
        System.out.println("插入 30");
        int v2 = persistentAVL.insert(30);
        persistentAVL.displayVersion(v2);
        
        System.out.println("插入 70");
        int v3 = persistentAVL.insert(70);
        persistentAVL.displayVersion(v3);
        
        System.out.println("插入 20");
        int v4 = persistentAVL.insert(20);
        persistentAVL.displayVersion(v4);
        
        System.out.println("插入 40");
        int v5 = persistentAVL.insert(40);
        persistentAVL.displayVersion(v5);
        
        System.out.println("插入 60");
        int v6 = persistentAVL.insert(60);
        persistentAVL.displayVersion(v6);
        
        System.out.println("插入 80");
        int v7 = persistentAVL.insert(80);
        persistentAVL.displayVersion(v7);
        
        // 測試2: 版本查詢
        System.out.println("測試2: 版本查詢");
        System.out.println("在版本 " + v3 + " 中搜尋 30: " + persistentAVL.search(v3, 30));
        System.out.println("在版本 " + v3 + " 中搜尋 80: " + persistentAVL.search(v3, 80));
        System.out.println("在版本 " + v7 + " 中搜尋 80: " + persistentAVL.search(v7, 80));
        System.out.println();
        
        // 測試3: 版本比較
        System.out.println("測試3: 版本比較");
        persistentAVL.compareVersions(v3, v7);
        persistentAVL.compareVersions(v1, v5);
        System.out.println();
        
        // 測試4: 歷史版本查詢
        System.out.println("測試4: 歷史版本查詢");
        System.out.println("查詢版本 " + v3 + " 的內容:");
        persistentAVL.displayVersion(v3);
        
        System.out.println("查詢版本 " + v5 + " 的內容:");
        persistentAVL.displayVersion(v5);
        System.out.println();
        
        // 測試5: 平衡性測試
        System.out.println("測試5: 平衡性測試");
        System.out.println("插入 10 (可能觸發旋轉)");
        int v8 = persistentAVL.insert(10);
        persistentAVL.displayVersion(v8);
        
        System.out.println("插入 15");
        int v9 = persistentAVL.insert(15);
        persistentAVL.displayVersion(v9);
        
        System.out.println("插入 5");
        int v10 = persistentAVL.insert(5);
        persistentAVL.displayVersion(v10);
        System.out.println();
        
        // 測試6: 大量操作測試
        System.out.println("測試6: 大量操作測試");
        System.out.println("進行大量插入操作...");
        
        long startTime = System.nanoTime();
        int currentVersion = v10;
        for (int i = 100; i < 200; i += 10) {
            currentVersion = persistentAVL.insert(i);
        }
        long endTime = System.nanoTime();
        
        System.out.println("插入100個元素耗時: " + (endTime - startTime) / 1000000 + " 毫秒");
        System.out.println("最終版本: " + currentVersion);
        System.out.println("最終版本大小: " + persistentAVL.getSize(currentVersion));
        System.out.println("最終版本高度: " + persistentAVL.getTreeHeight(currentVersion));
        System.out.println();
        
        // 測試7: 空間效率測試
        System.out.println("測試7: 空間效率測試");
        System.out.println("總版本數: " + persistentAVL.versions.size());
        System.out.println("檢查所有版本的平衡性:");
        
        for (int i = 0; i < persistentAVL.versions.size(); i += 5) {
            if (persistentAVL.isValidAVL(i)) {
                System.out.println("版本 " + i + " 是平衡的");
            } else {
                System.out.println("版本 " + i + " 不平衡");
            }
        }
        System.out.println();
        
        // 測試8: 邊界情況
        System.out.println("測試8: 邊界情況");
        System.out.println("查詢不存在的版本: " + persistentAVL.getVersion(999));
        System.out.println("在版本 0 中搜尋不存在的元素: " + persistentAVL.search(0, 999));
        System.out.println("版本 0 的大小: " + persistentAVL.getSize(0));
        System.out.println("版本 0 的高度: " + persistentAVL.getTreeHeight(0));
    }
}
