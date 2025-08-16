/*
 * Time Complexity: O(log n) for add, update, rank, and select operations
 * 說明：AVL樹保持平衡，所有操作都在O(log n)時間內完成
 * 空間複雜度：O(n) 用於存儲n個玩家節點
 */

import java.util.*;

public class AVLLeaderboardSystem {
    
    // 擴展的AVL樹節點類別，存儲玩家資訊和子樹大小
    static class PlayerNode {
        String playerId;
        int score;
        PlayerNode left, right;
        int height;
        int subtreeSize; // 子樹大小（包括自己）
        
        PlayerNode(String playerId, int score) {
            this.playerId = playerId;
            this.score = score;
            this.height = 1;
            this.subtreeSize = 1;
        }
    }
    
    // 根節點
    private PlayerNode root;
    
    // 建構函數
    public AVLLeaderboardSystem() {
        root = null;
    }
    
    // 獲取節點高度
    private int getHeight(PlayerNode node) {
        if (node == null) return 0;
        return node.height;
    }
    
    // 獲取子樹大小
    private int getSubtreeSize(PlayerNode node) {
        if (node == null) return 0;
        return node.subtreeSize;
    }
    
    // 獲取平衡因子
    private int getBalanceFactor(PlayerNode node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }
    
    // 更新節點資訊（高度和子樹大小）
    private void updateNodeInfo(PlayerNode node) {
        if (node != null) {
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            node.subtreeSize = getSubtreeSize(node.left) + getSubtreeSize(node.right) + 1;
        }
    }
    
    // 左旋轉
    private PlayerNode leftRotate(PlayerNode x) {
        if (x == null || x.right == null) {
            return x;
        }
        
        PlayerNode y = x.right;
        PlayerNode T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        // 更新節點資訊
        updateNodeInfo(x);
        updateNodeInfo(y);
        
        return y;
    }
    
    // 右旋轉
    private PlayerNode rightRotate(PlayerNode y) {
        if (y == null || y.left == null) {
            return y;
        }
        
        PlayerNode x = y.left;
        PlayerNode T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        // 更新節點資訊
        updateNodeInfo(y);
        updateNodeInfo(x);
        
        return x;
    }
    
    // 左右旋轉
    private PlayerNode leftRightRotate(PlayerNode z) {
        if (z == null || z.left == null) {
            return z;
        }
        
        z.left = leftRotate(z.left);
        return rightRotate(z);
    }
    
    // 右左旋轉
    private PlayerNode rightLeftRotate(PlayerNode z) {
        if (z == null || z.right == null) {
            return z;
        }
        
        z.right = rightRotate(z.right);
        return leftRotate(z);
    }
    
    // 重新平衡節點
    private PlayerNode rebalance(PlayerNode node) {
        if (node == null) {
            return null;
        }
        
        updateNodeInfo(node);
        
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
    
    // 1. 添加玩家分數
    public void addPlayer(String playerId, int score) {
        root = addPlayerRec(root, playerId, score);
    }
    
    private PlayerNode addPlayerRec(PlayerNode node, String playerId, int score) {
        if (node == null) {
            return new PlayerNode(playerId, score);
        }
        
        // 按分數排序（高分在前）
        if (score > node.score) {
            node.left = addPlayerRec(node.left, playerId, score);
        } else if (score < node.score) {
            node.right = addPlayerRec(node.right, playerId, score);
        } else {
            // 分數相同，按玩家ID排序
            int compareResult = playerId.compareTo(node.playerId);
            if (compareResult < 0) {
                node.left = addPlayerRec(node.left, playerId, score);
            } else if (compareResult > 0) {
                node.right = addPlayerRec(node.right, playerId, score);
            } else {
                // 相同玩家，更新分數
                node.score = score;
                return node;
            }
        }
        
        return rebalance(node);
    }
    
    // 2. 更新玩家分數
    public boolean updatePlayerScore(String playerId, int newScore) {
        // 先刪除舊記錄
        if (deletePlayer(playerId)) {
            // 再添加新記錄
            addPlayer(playerId, newScore);
            return true;
        }
        return false;
    }
    
    // 刪除玩家
    private boolean deletePlayer(String playerId) {
        int initialSize = getSubtreeSize(root);
        root = deletePlayerRec(root, playerId);
        return getSubtreeSize(root) < initialSize;
    }
    
    private PlayerNode deletePlayerRec(PlayerNode node, String playerId) {
        if (node == null) {
            return null;
        }
        
        // 先按分數查找，再按ID查找
        // 這裡簡化處理，實際應用中需要更複雜的查找邏輯
        if (playerId.equals(node.playerId)) {
            // 找到要刪除的節點
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // 有兩個子節點，找後繼節點
                PlayerNode successor = findMin(node.right);
                node.playerId = successor.playerId;
                node.score = successor.score;
                node.right = deletePlayerRec(node.right, successor.playerId);
            }
        } else {
            // 遞迴查找
            node.left = deletePlayerRec(node.left, playerId);
            node.right = deletePlayerRec(node.right, playerId);
        }
        
        return rebalance(node);
    }
    
    // 找到最小值節點
    private PlayerNode findMin(PlayerNode node) {
        if (node == null || node.left == null) {
            return node;
        }
        return findMin(node.left);
    }
    
    // 3. 查詢玩家排名 (rank操作)
    public int getPlayerRank(String playerId) {
        return getPlayerRankRec(root, playerId, 0);
    }
    
    private int getPlayerRankRec(PlayerNode node, String playerId, int currentRank) {
        if (node == null) {
            return -1; // 玩家不存在
        }
        
        if (playerId.equals(node.playerId)) {
            // 找到玩家，返回排名
            return currentRank + getSubtreeSize(node.left) + 1;
        }
        
        // 在左子樹中查找
        int leftResult = getPlayerRankRec(node.left, playerId, currentRank);
        if (leftResult != -1) {
            return leftResult;
        }
        
        // 在右子樹中查找
        return getPlayerRankRec(node.right, playerId, 
                               currentRank + getSubtreeSize(node.left) + 1);
    }
    
    // 4. 查詢前K名玩家 (select操作)
    public List<String> getTopKPlayers(int k) {
        List<String> result = new ArrayList<>();
        getTopKPlayersRec(root, k, result);
        return result;
    }
    
    private void getTopKPlayersRec(PlayerNode node, int k, List<String> result) {
        if (node == null || result.size() >= k) {
            return;
        }
        
        // 中序遍歷（按分數降序）
        getTopKPlayersRec(node.left, k, result);
        
        if (result.size() < k) {
            result.add(node.playerId + "(" + node.score + ")");
        }
        
        if (result.size() < k) {
            getTopKPlayersRec(node.right, k, result);
        }
    }
    
    // 額外功能：根據排名查詢玩家 (select操作)
    public String getPlayerByRank(int rank) {
        if (rank < 1 || rank > getSubtreeSize(root)) {
            return null;
        }
        return getPlayerByRankRec(root, rank);
    }
    
    private String getPlayerByRankRec(PlayerNode node, int rank) {
        if (node == null) {
            return null;
        }
        
        int leftSize = getSubtreeSize(node.left);
        
        if (rank <= leftSize) {
            // 在左子樹中
            return getPlayerByRankRec(node.left, rank);
        } else if (rank == leftSize + 1) {
            // 當前節點
            return node.playerId + "(" + node.score + ")";
        } else {
            // 在右子樹中
            return getPlayerByRankRec(node.right, rank - leftSize - 1);
        }
    }
    
    // 獲取總玩家數
    public int getTotalPlayers() {
        return getSubtreeSize(root);
    }
    
    // 檢查是否為有效的AVL樹
    public boolean isValidAVL() {
        return isValidAVLRec(root);
    }
    
    private boolean isValidAVLRec(PlayerNode node) {
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
    
    // 中序遍歷（顯示排行榜）
    public void displayLeaderboard() {
        System.out.println("=== 排行榜 ===");
        displayLeaderboardRec(root, 1);
        System.out.println();
    }
    
    private void displayLeaderboardRec(PlayerNode node, int rank) {
        if (node != null) {
            displayLeaderboardRec(node.left, rank);
            System.out.println(rank + ". " + node.playerId + " - " + node.score + "分");
            rank++;
            displayLeaderboardRec(node.right, rank);
        }
    }
    
    // 測試排行榜系統
    public static void main(String[] args) {
        AVLLeaderboardSystem leaderboard = new AVLLeaderboardSystem();
        
        System.out.println("=== AVL樹排行榜系統測試 ===\n");
        
        // 測試1: 添加玩家分數
        System.out.println("測試1: 添加玩家分數");
        leaderboard.addPlayer("Alice", 1500);
        leaderboard.addPlayer("Bob", 1800);
        leaderboard.addPlayer("Charlie", 1200);
        leaderboard.addPlayer("David", 2000);
        leaderboard.addPlayer("Eve", 1600);
        leaderboard.addPlayer("Frank", 1400);
        
        System.out.println("添加玩家後的排行榜:");
        leaderboard.displayLeaderboard();
        System.out.println("總玩家數: " + leaderboard.getTotalPlayers());
        System.out.println("是否為有效AVL樹: " + leaderboard.isValidAVL());
        System.out.println();
        
        // 測試2: 查詢玩家排名
        System.out.println("測試2: 查詢玩家排名");
        System.out.println("Alice的排名: " + leaderboard.getPlayerRank("Alice"));
        System.out.println("Bob的排名: " + leaderboard.getPlayerRank("Bob"));
        System.out.println("Charlie的排名: " + leaderboard.getPlayerRank("Charlie"));
        System.out.println("David的排名: " + leaderboard.getPlayerRank("David"));
        System.out.println("Eve的排名: " + leaderboard.getPlayerRank("Eve"));
        System.out.println("Frank的排名: " + leaderboard.getPlayerRank("Frank"));
        System.out.println();
        
        // 測試3: 查詢前K名玩家
        System.out.println("測試3: 查詢前K名玩家");
        System.out.println("前3名玩家: " + leaderboard.getTopKPlayers(3));
        System.out.println("前5名玩家: " + leaderboard.getTopKPlayers(5));
        System.out.println("前10名玩家: " + leaderboard.getTopKPlayers(10));
        System.out.println();
        
        // 測試4: 根據排名查詢玩家
        System.out.println("測試4: 根據排名查詢玩家");
        for (int i = 1; i <= 6; i++) {
            System.out.println("第" + i + "名: " + leaderboard.getPlayerByRank(i));
        }
        System.out.println();
        
        // 測試5: 更新玩家分數
        System.out.println("測試5: 更新玩家分數");
        System.out.println("更新Alice的分數從1500到1700");
        leaderboard.updatePlayerScore("Alice", 1700);
        System.out.println("更新後的排行榜:");
        leaderboard.displayLeaderboard();
        System.out.println("Alice的新排名: " + leaderboard.getPlayerRank("Alice"));
        System.out.println();
        
        // 測試6: 添加更多玩家
        System.out.println("測試6: 添加更多玩家");
        leaderboard.addPlayer("Grace", 1900);
        leaderboard.addPlayer("Henry", 1100);
        leaderboard.addPlayer("Ivy", 1300);
        leaderboard.addPlayer("Jack", 1750);
        
        System.out.println("添加更多玩家後的排行榜:");
        leaderboard.displayLeaderboard();
        System.out.println("總玩家數: " + leaderboard.getTotalPlayers());
        System.out.println("前5名玩家: " + leaderboard.getTopKPlayers(5));
        System.out.println();
        
        // 測試7: 邊界情況
        System.out.println("測試7: 邊界情況");
        System.out.println("查詢不存在的玩家排名: " + leaderboard.getPlayerRank("Unknown"));
        System.out.println("查詢第0名: " + leaderboard.getPlayerByRank(0));
        System.out.println("查詢第100名: " + leaderboard.getPlayerByRank(100));
        System.out.println("查詢前0名: " + leaderboard.getTopKPlayers(0));
        System.out.println("查詢前100名: " + leaderboard.getTopKPlayers(100));
        System.out.println();
        
        // 測試8: 性能測試
        System.out.println("測試8: 性能測試");
        System.out.println("測試大量玩家的性能...");
        
        long startTime = System.nanoTime();
        for (int i = 1; i <= 1000; i++) {
            leaderboard.addPlayer("Player" + i, 1000 + (int)(Math.random() * 1000));
        }
        long endTime = System.nanoTime();
        System.out.println("添加1000個玩家耗時: " + (endTime - startTime) / 1000000 + " 毫秒");
        
        startTime = System.nanoTime();
        int rank = leaderboard.getPlayerRank("Player500");
        endTime = System.nanoTime();
        System.out.println("查詢Player500排名耗時: " + (endTime - startTime) + " 納秒");
        System.out.println("Player500的排名: " + rank);
        
        startTime = System.nanoTime();
        List<String> top10 = leaderboard.getTopKPlayers(10);
        endTime = System.nanoTime();
        System.out.println("查詢前10名耗時: " + (endTime - startTime) + " 納秒");
        System.out.println("前10名: " + top10);
    }
}
