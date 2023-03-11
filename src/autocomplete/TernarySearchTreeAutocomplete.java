package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            overallRoot = insertTerm(overallRoot, term);
        }
//        System.out.print("Root: ");
//        printNode(overallRoot, 0);
    }

    private Node insertTerm(Node root, CharSequence term) {
        if (term.length() > 0) {
            if (root == null) {
                root = new Node(term.charAt(0));
                if (term.length() == 1) {
                    root.isTerm = true;
                }
                root.mid = insertTerm(root.mid, term.subSequence(1, term.length()));
            } else {
                int difference = Character.compare(term.charAt(0), root.data);
                if (difference == 0) {
                    root.mid = insertTerm(root.mid, term.subSequence(1, term.length()));
                } else {
                    if (difference > 0) {
                        root.right = insertTerm(root.right, term);
                    } else {
                        root.left = insertTerm(root.left, term);
                    }
                }
            }
        }
        return root;
    }

    private void addIndents(int indents) {
        for (int i = 0; i < indents; i++) {
            System.out.print("\t");
        }
    }

    private void printNode(Node root, int indents) {
        if (root == null) {
            System.out.println("null.");
            return;
        } else {
            System.out.println(root.data + (root.isTerm ? " end of term!" : ""));
        }
        addIndents(indents);
        System.out.print("Left:");
        printNode(root.left, indents + 1);
        addIndents(indents);
        System.out.print("Down:");
        printNode(root.mid, indents + 1);
        addIndents(indents);
        System.out.print("Right:");
        printNode(root.right, indents + 1);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> out = new ArrayList<>();
        //Find prefix subtree
        Node curr = overallRoot;
        char currChar = prefix.charAt(0);
        int i = 0;
        while (i < prefix.length()) {
            int difference = Character.compare(currChar, curr.data);
            if (difference == 0) {
                i++;
                if (i < prefix.length()) {
                    currChar = prefix.charAt(i);
                }
                if (i != prefix.length()) {
                    curr = curr.mid;
                }
            } else if (difference > 0) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }

        Node temp = curr;
        curr = new Node(curr.data);
        curr.mid = temp.mid;
        curr.isTerm = temp.isTerm;
        // Traverse all solutions;
        collectTerms(out, curr, prefix.subSequence(0, prefix.length() - 1), prefix);
        return out;
    }

    public void collectTerms(List<CharSequence> out, Node curr, CharSequence progress, CharSequence prefix) {
        if (curr != null) {
            if (curr.isTerm) {
                out.add(progress.toString() + Character.toString(curr.data));
            }

            collectTerms(out, curr.left, progress, prefix);
            collectTerms(out, curr.right, progress, prefix);
            progress += Character.toString(curr.data);
            collectTerms(out, curr.mid, progress, prefix);
        }
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
