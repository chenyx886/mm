package com.mm.data.entity;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午7:36
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class NodeEntity extends BaseEntity {

    private int NodeID;
    private String NodeName;

    public int getNodeID() {
        return NodeID;
    }

    public void setNodeID(int nodeID) {
        NodeID = nodeID;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }
}
