package finalexam;

import java.util.*;

public class LC23_MergeKLists_Hospitals {
    static class Node implements Comparable<Node> {
        int val, row, idx;
        Node(int v, int r, int i) { val = v; row = r; idx = i; }
        public int compareTo(Node o) { return Integer.compare(this.val, o.val); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            List<Integer> list = new ArrayList<>();
            while (true) {
                int x = sc.nextInt();
                if (x == -1) break;
                list.add(x);
            }
            lists.add(list);
        }
        List<Integer> merged = mergeKLists(lists);
        for (int i = 0; i < merged.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(merged.get(i));
        }
    }

    public static List<Integer> mergeKLists(List<List<Integer>> lists) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int k = lists.size();
        for (int i = 0; i < k; i++) {
            if (!lists.get(i).isEmpty()) {
                pq.offer(new Node(lists.get(i).get(0), i, 0));
            }
        }
        List<Integer> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            res.add(node.val);
            int nextIdx = node.idx + 1;
            if (nextIdx < lists.get(node.row).size()) {
                pq.offer(new Node(lists.get(node.row).get(nextIdx), node.row, nextIdx));
            }
        }
        return res;
    }
}
