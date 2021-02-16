//package com.test;
//
//import com.zy.Application;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//public class TransTest {
////    /*
////    REQUIRED: 使用当前的事务，如果当前没有事务，则自己新建一个事务，子方法是必须运行在一个事务中的，
////    如果当前存在事务则加入这个事务，成为一个整体
////    例：领导没饭吃，我有钱，我会自己买了自己吃，领导有饭吃，会分给你一起吃
////
////    SUPPORTS: 如果当前有事务，则使用事务，如果当前没有事务，则不使用事务
////    例：领导没饭吃，我也没饭吃；领导有饭吃，我也有饭吃
////
////    MANDATORY: 该传播属性强制必须存在一个事务，如果不存在则抛出异常
////    例：领导必须管饭，没有饭就抛出异常
////
////    REQUIRES_NEW: 如果当前有事务，则挂起该事务，并且自己创建一个新的事务给自己使用
////    例：领导有饭吃，我偏不要，我自己买来吃
////
////    NOT_SUPPORTED: 如果当前有事务，则吧事务挂起，自己不使用事务去操作数据库
////    例：领导有饭吃，分一点给你，我太忙了，放一边 我不吃
////
////    NEVER: 如果当前有事务存在就抛出一场
////    例：领导有饭给你吃，我不想吃，我抛出异常
////
////    NESTED: 如果当前有事务，则开启事务（嵌套事务），嵌套事务是独立提交或者回滚；如果当前没有事务，则同REQUIRED
////    但是如果主事务提交，则会携带子事务一起提交
////    如果主事务回滚，则子事务一起回滚。相反，子事务异常，父事务可以回滚或者不回滚
////    例：领导决策不对，老板怪罪，领导携带小弟一起受罚，小弟出了差错，领导可以选择推卸责任
////     */
//    @Test
//    public void test01(){
//        String str = "asd.sdf.dfg";
//        String[] split = str.split("\\.");
//        System.out.println(Arrays.toString(split));
//    }
//
//
//
//
//}
