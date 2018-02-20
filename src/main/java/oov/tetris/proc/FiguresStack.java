package oov.tetris.proc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;

import java.util.Deque;


public class FiguresStack {
    private final Deque<CompoundObj> stack = Lists.newLinkedList();
    private final CompObjFactory compObjFactory;
    private CompoundObj ongoing;

    public FiguresStack(int deep, CompObjFactory compObjFactory) {
        this.compObjFactory = compObjFactory;
        Preconditions.checkArgument(deep >= 1);
        for (int i = 0; i < deep; i++) {
            genAndAdd();
        }
        initOngoing();
    }


    public CompoundObj next() {
        CompoundObj compoundObj = stack.pop();
        genAndAdd();
        initOngoing();
        return compoundObj;
    }

    public CompoundObj getOngoing() {
        return ongoing;
    }

    private void genAndAdd(){
        CompoundObj obj = compObjFactory.makeRandObj();
        stack.addLast(obj);
    }

    private void initOngoing(){
        ongoing = stack.peek();
//        if(stackManagerListener != null){
//            stackManagerListener.onOngoingObjIsReady(ongoing);
//        }
    }

    public interface ChunksStackManagerOngoingListener {
        void onOngoingObjIsReady(CompoundObj ongoing);
    }
    public interface ChunksStackManagerCurrentListener {
        void onObjIsReady(CompoundObj current);

    }
}
