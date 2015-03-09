package effective.chapter01;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Created by root on 15-3-9.
 */
public class MemoryLeakStack {
    private Object[] elements;
    private static final int DEFAULT_LENGTH=16;
    private int size=0;
    public MemoryLeakStack() {
        this.elements = new Object[DEFAULT_LENGTH];
    }

    public void push(Object obj){
        ensureCapacity();
        elements[size++]=obj;
    }

    public Object pop(){
        if (size==0)
            throw new EmptyStackException();
        Object o=elements[--size];
        elements[size]=null;
        return o;

    }

    private  void ensureCapacity(){
        if(elements.length==size){
            elements= Arrays.copyOf(elements,2*size+1);
        }
    }
}
