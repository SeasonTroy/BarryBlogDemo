package com.bootdang.util;

import com.bootdang.system.entity.Comment;

import java.util.ArrayList;
import java.util.List;

public class buildTrweeThree <T extends Comment>{


        public List<T> build (List<T> nodes) {

            if (nodes == null) {
                return null;
            }

            List<T> topNodes = new ArrayList<>();


            for (T children : nodes) {
                Integer parentid = children.getParentid();
                if(parentid==0&&parentid.equals(0)) {
                    Integer pid = children.getCommentId();

                    for (T parent : nodes) {
                        Integer id = parent.getParentid();
                        if (id != null && pid.equals(id)) {
                            children.getReplyBody().add(parent);

                        }


                    }
                    topNodes.add(children);
                }
            }


            return topNodes;
        }


    public static List<Comment> treeMenu(List<Comment> list,Integer pid){
        List<Comment> childList = new ArrayList<Comment>();
        for (Comment item : list) {
            if (item != null) {
                // 判断当前节点的父节点是否是pid
                if (pid.equals(item.getParentid())) {
                    List<Comment> child = treeMenu(list, item.getCommentId());
                    item.setReplyBody(child);
                    childList.add(item);
                }
            }
        }
        return ergodicTrees(childList);
    }
    public static  List<Comment> ergodicTrees(List<Comment> root)  {
        for (int i = 0; i < root.size(); i++) {
            // 查询某节点的子节点（获取的是list）
            List<Comment> children = new ArrayList<Comment>();
            if (null != root.get(i).getReplyBody()) {
                children = root.get(i).getReplyBody();
            }
            ergodicTrees(children);
        }
        return root;
    }


    }

