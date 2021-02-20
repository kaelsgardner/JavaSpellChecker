package spell;
import java.util.*;
import java.lang.*;
import java.io.*;



public class Trie implements ITrie
{
    private Node root;
    private int wordCount;
    private int nodeCount;


    public Trie()
    {
        root = new Node();
        wordCount = 0;
        nodeCount = 1;
    }

    public class Node implements INode
    {
        private int count;
        private Node[] children;
        private Node()
        {
            children = new Node[26];
            count = 0;
        }
        @Override
        public int getValue()
        {
            return count;
        }

        @Override
        public void incrementValue()
        {
            count++;
        }

        @Override
        public INode[] getChildren()
        {
            return children;
        }
    }

    @Override
    public void add(String word)
    {
        addInput(word, root);
    }

    public void addInput(String word, Node n)
    {
        if (word.length() > 0)
        {
            word = word.toLowerCase();
            for (int i = 0; i < word.length(); i++)
            {
                int index = word.charAt(i) - 'a';
                if (n.children[index] == null)
                {
                    n.children[index] = new Node();
                    nodeCount++;
                }
                if (i == word.length() - 1)
                {
                    if (n.children[index].getValue() < 1)
                    {
                        n.children[index].incrementValue();
                        wordCount++;
                        return;
                    }
                    else {n.children[index].incrementValue();}
                }
                n = n.children[index];
            }
        }
        //n = n.children[index];
    }

    @Override
    public INode find(String word)
    {
        Node current = root;
        for(int i = 0; i< word.length(); i++)
        {
            int letter = word.charAt(i) - 'a';
            Node child = current.children[letter];
            if (child == null)
            {
                return null;
            }
            if (i == word.length() - 1)
            {
                if(child.count > 0)//child.count same thing?
                {
                    return child;
                }
                // else current = child;
            }
            current = child;
        }
        return null;
    }

    @Override
    public int getWordCount()
    {
        return wordCount;
    }

    @Override
    public int getNodeCount()
    {
        return nodeCount;
    }

    public String toString()
    {
        StringBuilder word = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringHelper(word, output, root);
        return output.toString();
    }

    private void toStringHelper(StringBuilder word, StringBuilder output, INode n)
    {
        if (n.getValue() > 0)
        {
            output.append(word.toString());
            output.append("\n");
        }
        for (int i = 0; i < n.getChildren().length; i++)
        {
            INode child = n.getChildren()[i];
            if (child != null)
            {
                char childLetter = (char)('a' + i);//?
                word.append(childLetter);
                toStringHelper(word, output, child);
                word.deleteCharAt(word.length() - 1);

            }
        }

    }

    public boolean equals(Object o)
    {
        if (this == o) { return true; }
        if (o == null) { return false; }
        if (o.getClass() != this.getClass()) { return false; }
        else {

            Trie obj = (Trie) o;
            if (obj.nodeCount != this.nodeCount) {
                return false;
            }
            if (obj.wordCount != this.wordCount) {
                return false;
            }
            if (!equalsHelper(this.root, obj.root))
            {
                return false;
            }
        }
        return true;
    }

    public boolean equalsHelper(Node n1, Node n2)
    {
        if (n1 != null && n2 != null)
        {
            if (n1.getValue() != n2.getValue())
            {
                return false;
            }
            for (int i = 0; i < 26; i++)
            {
                if ((n1.children[i] != null && n2.children[i] == null)
                        || (n1.children[i] == null && n2.children[i] != null))
                {
                    return false;
                }
                else if (n1.children[i] != null && n2.children[i] != null)
                {
                    if (n1.children[i].getValue() != n2.children[i].getValue())
                    {
                        return false;
                    }
                    if (!equalsHelper(n1.children[i], n2.children[i]))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int hashCode()
    {
        int sum = 0;
        for (int i = 0; i < 26; i++)
        {
            if (root.children[i] != null)
            {
                sum += i;
            }
        }
        return (sum + wordCount * nodeCount);
    }
}
