package oov.tetris.proc;

import oov.tetris.draw.item.CompObjFactory;
import oov.tetris.draw.item.CompoundObj;
import oov.tetris.draw.menu.Cells;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by olegdelone on 23.08.2015.
 */
public class ChunksStackManager {
    private Deque<CompoundObj> chunks = new LinkedList<>();
    private Cells previewMenu;

    public ChunksStackManager(Cells previewMenu, int deep) {
        this.previewMenu = previewMenu;
        for (int i = 0; i < deep; i++) {
            CompoundObj obj = CompObjFactory.makeRandObj(2, 3, previewMenu.getCellW(), previewMenu.getCellH());
            chunks.push(obj);
        }
    }

    public CompoundObj pop() {
        CompoundObj compoundObj = chunks.pop();
        CompoundObj newLastObj = CompObjFactory.makeRandObj(2, 3, previewMenu.getCellW(), previewMenu.getCellH());
        chunks.addLast(newLastObj);

        CompoundObj ongoing = chunks.peek();
        previewMenu.addNextCurrentObject(ongoing);
        return compoundObj;
    }

}
