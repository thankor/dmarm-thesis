package dmarm.utilities;

import java.util.ArrayList;

public class Hierarchy {
	private ArrayList<HierarchyNode> nodes = new ArrayList<HierarchyNode>();
	private String hierarchyName;
	private int levels;
	private int nodeNumber;

	public String getHierarchyName() {
		return hierarchyName;
	}

	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public int getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public ArrayList<HierarchyNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<HierarchyNode> nodes) {
		this.nodes = nodes;
	}

	public void addNode(HierarchyNode node) {
		this.nodes.add(node);

	}

}
