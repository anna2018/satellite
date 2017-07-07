package com.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点模型
 * Title: UCAS <br>
 * Description: <br>
 * Date: 2014-6-23 <br>
 * Copyright (c) 2014 AILK <br>
 * 
 * @author fengbo
 */
public class TreeNode implements Comparable<TreeNode> {

    private String name; // 节点名称

    private int count; // 计数

    private TreeNode parent; // 父节点

    private List<TreeNode> children; // 子节点

    private TreeNode nextSameNode; // 下一个同名节点

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
     * 添加一个子节点
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
     * 查找一个子节点
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
     * 打印节点
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
