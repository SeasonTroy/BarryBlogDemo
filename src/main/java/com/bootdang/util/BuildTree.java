package com.bootdang.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {

	public static List<TreeMenu> build(List<TreeMenu> nodes) {

		if (nodes == null) {
			return null;
		}

		List<TreeMenu> topNodes = new ArrayList<>();


		for (TreeMenu children : nodes) {
			Integer parentid = children.getParentid();
			if(parentid==0&&parentid.equals(0)) {
				Integer pid = children.getId();

				for (TreeMenu parent : nodes) {
					Integer id = parent.getParentid();
					if (id != null && pid.equals(id)) {
						children.getChildren().add(parent);

					}

			}
				topNodes.add(children);
			}
		}


		return topNodes;
	}

	/*public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

		for (Tree<T> children : nodes) {

			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					children.setHasParent(true);
					parent.getChildren().add(children);
					parent.setChildren(true);
					continue;
				}
			}

		}
		//System.out.println("----"+topNodes.toString());
		return topNodes;
	}*/

}