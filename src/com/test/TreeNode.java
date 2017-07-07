package com.test;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ڵ�ģ��
 * Title: UCAS <br>
 * Description: <br>
 * Date: 2014-6-23 <br>
 * Copyright (c) 2014 AILK <br>
 * 
 * @author fengbo
 */
public class TreeNode implements Comparable<TreeNode> {

    private String name; // �ڵ�����

    private int count; // ����

    private TreeNode parent; // ���ڵ�

    private List<TreeNode> children; // �ӽڵ�

    private TreeNode nextSameNode; // ��һ��ͬ���ڵ�

    public TreeNode(String item) {
        
    }
    
    public TreeNode() {
    }

    public void countIncrement(int n) {
        this.count += n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getNextSameNode() {
        return nextSameNode;
    }

    public void setNextSameNode(TreeNode nextSameNode) {
        this.nextSameNode = nextSameNode;
    }

    public int compareTo(TreeNode treeNode) {
        int count0 = treeNode.getCount();
        return count0 - this.count;
    }
    
    /**
     * ���һ���ӽڵ�
     * @param child
     * @author fengbo
     */
    public void addChild(TreeNode child) {
        if (this.getChildren() == null) {
            List<TreeNode> list = new ArrayList<TreeNode>();
            list.add(child);
            this.setChildren(list);
        } else {
            this.getChildren().add(child);
        }
    }
    
    /**
     * ����һ���ӽڵ�
     * @param name
     * @return 
     * @author fengbo
     */
    public TreeNode findChild(String name) {
        List<TreeNode> childName =  this.getChildren();
        if(childName != null)
        {
            for (TreeNode child : childName)
            {
                if(name.equals(child.getName()))
                {
                    return child;
                }
            }
        }
        return null;
    }
    
    /**
     * ��ӡ�ڵ�
     * 
     * @author fengbo
     */
    public void printChildrenName() {
        List<TreeNode> children = this.getChildren();
        if (children != null) {
            for (TreeNode child : children) {
                System.out.print(child.getName() + " ");
            }
        } else {
            System.out.print("null");
        }
    }
}
