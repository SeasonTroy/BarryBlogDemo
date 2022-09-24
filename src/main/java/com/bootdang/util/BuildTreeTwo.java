package com.bootdang.util;

import com.bootdang.system.entity.Dept;
import com.bootdang.system.entity.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTreeTwo {

	public static List<Tree<Dept>> build(List<Tree<Dept>> nodes) {

		if (nodes == null) {
			return null;
		}
		List<Tree<Dept>> topNodes = new ArrayList<Tree<Dept>>();

		for (Tree<Dept> children : nodes) {

			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);

				continue;
			}

			for (Tree<Dept> parent : nodes) {
				Integer id = parent.getId();
				if (id != null && id.equals(Integer.parseInt(pid))) {
					parent.getChildren().add(children);
					continue;
				}
			}

		}


		return topNodes;
	}

	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
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
				Integer id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);


					continue;
				}
			}

		}
		return topNodes;
	}

}