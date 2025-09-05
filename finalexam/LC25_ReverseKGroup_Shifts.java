package finalexam;

import java.util.Scanner;

public class LC25_ReverseKGroup_Shifts {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        sc.nextLine(); // consume the rest of the line
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return;
        String[] parts = line.split("\\s+");
        ListNode dummy = new ListNode(0), curr = dummy;
        for (String s : parts) {
            curr.next = new ListNode(Integer.parseInt(s));
            curr = curr.next;
        }
        ListNode head = reverseKGroup(dummy.next, k);
        printList(head);
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        while (true) {
            ListNode node = prev;
            for (int i = 0; i < k && node != null; i++) node = node.next;
            if (node == null) break;
            ListNode curr = prev.next, next = curr.next;
            for (int i = 1; i < k; i++) {
                curr.next = next.next;
                next.next = prev.next;
                prev.next = next;
                next = curr.next;
            }
            prev = curr;
        }
        return dummy.next;
    }

    private static void printList(ListNode head) {
        boolean first = true;
        while (head != null) {
            if (!first) System.out.print(" ");
            System.out.print(head.val);
            first = false;
            head = head.next;
        }
    }
}
