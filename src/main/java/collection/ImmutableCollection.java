package collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by yao on 15/6/20.
 * JDK 1.8
 */
public class ImmutableCollection {

    public static void main(String[]args){

        // createListA();
        //immutable list
        List<String> list = Lists.newArrayList("a", "b");
        ImmutableList<String> sublist = list
                .stream()
                .filter(s -> s.charAt(0) == 'a')
                .collect(ImmutableCollection.toList());
        System.out.println(Iterables.getOnlyElement(sublist).equals("a"));

        // immutable set
        List<String> set = Lists.newArrayList("a", "b");
        ImmutableSet<String> subset = list
                .stream()
                .filter(s -> s.charAt(0) == 'a')
                .collect(ImmutableCollection.toSet());
        System.out.println(Iterables.getOnlyElement(subset).equals("a"));


    }
    public static void createListA(){
        List<String>list=new ArrayList<String>(){{
            add("one1");
            add("one2");
            add("one3");

            add("two1");
            add("two2");
            add("two3");
        }};
        List<String>subList=list
                .stream()
                .filter(u ->u.contains("one"))
                .collect(Collectors.toList());

        System.out.println(subList.toString());
        List<String> immutableSubList = Collections.unmodifiableList(subList);
        System.out.println(immutableSubList);
        //throw exception
        immutableSubList.add("xx");
    }

    public static <T> Collector<T, ?, ImmutableList<T>> toList() {

        return Collector.of(
                ImmutableList.Builder::new,
                ImmutableList.Builder::add,
                (left, right) -> left.addAll(right.build()),
                (Function<ImmutableList.Builder<T>, ImmutableList<T>>)
                        ImmutableList.Builder::build);

    }

    public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
        return Collector.of(
                ImmutableSet.Builder::new,
                ImmutableSet.Builder::add,
                (left, right) -> left.addAll(right.build()),
                (Function<ImmutableSet.Builder<T>, ImmutableSet<T>>)
                        ImmutableSet.Builder::build,
                Collector.Characteristics.UNORDERED);
    }



}
