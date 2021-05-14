package com.qiuyu.demo.utils;

import com.sun.istack.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 自定义栈，满足先进后出的一种数据结构，使用数组加计数器来实现
 * @Author: qiuyu
 * @Date: 2021/3/8
 **/
public class Stack<E> {

    // stack的大小
    private int size;

    // 对象数组
    private Object[] stack;


    public Stack(){
        stack = new Object[10];
    }

    public boolean isEmpty(){
        return size==0;
    }

    // 入栈
    public E push (@NotNull E item){
        expandCapacity(size + 1);
        stack[size] = item;
        size++;
        return item;
    }

    // 数组扩容
    private void expandCapacity(int size) {
        if (size > stack.length) {
            size = size * 3 / 2 + 1;//每次扩大50%
            stack = Arrays.copyOf(stack, size);
        }
    }


    // 返回栈顶元素
    public E peek() {
        E item = null;
        if (size > 0)
            item = (E) stack[size-1];
        return item;
    }


    //出栈
    public E pop() {
        E item = peek();
        if (size > 0) {
            stack[size-1] = null;
            size--;
        }
        return item;
    }


    // 打印栈元素
    public void printElement(){
        Stream.of(stack).filter(item->item!=null).collect(Collectors.toList())
                .forEach(iteam->{
            System.out.println(iteam);
        });

    }






    /*public static void main(String [] args){
        Stack<String> stack = new Stack<>();
        stack.push("nihao");
        stack.push("hello");
        stack.push("qiuyu");
//        System.out.println(stack.pop());
//        System.out.println(stack.isEmpty());
//        System.out.println(stack.peek());
        stack.printElement();
    }

*/


}
