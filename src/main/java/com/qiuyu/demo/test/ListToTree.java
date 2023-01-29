package com.qiuyu.demo.test;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListToTree {

    public static void main(String[] args) {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu(1, 0, "用户管理", 2));
        list.add(new Menu(2, 0, "租户管理", 1));
        list.add(new Menu(3, 1, "添加用户", 3));
        list.add(new Menu(4, 1, "删除用户", 2));
        list.add(new Menu(5, 2, "添加租户", 1));
        list.add(new Menu(6, 2, "删除租户", 2));
        List<Menu> menus = makeTree(list, x -> x.getParentId() == 0, (x, y) -> x.getId().equals(y.getParentId()), Menu::setChildren);
        System.out.println(JSONUtil.toJsonStr(menus));
    }

    public static <E> List<E> makeTree(List<E> list, Predicate<E> root, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> children) {
        return list.stream().filter(root).sorted().peek(x -> children.accept(x, makeChildren(x, list, parentCheck, children))).collect(Collectors.toList());
    }

    public static <E> List<E> makeChildren(E parent, List<E> allData, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> children) {
        return allData.stream().filter(x -> parentCheck.apply(parent, x)).peek(x -> children.accept(x, makeChildren(x, allData, parentCheck, children))).collect(Collectors.toList());
    }

    @Data
    public static class Menu implements Comparable<Menu>{
        private Integer id;
        private Integer parentId;
        private String name;
        private Integer weight;
        private List<Menu> children;
        public Menu(Integer id, Integer parentId, String name, Integer weight) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.weight = weight;
        }

        @Override
        public int compareTo(Menu menu) {
            return 0;
        }
    }

}