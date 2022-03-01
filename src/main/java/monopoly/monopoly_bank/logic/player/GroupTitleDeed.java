package monopoly.monopoly_bank.logic.player;

import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;

import java.util.ArrayList;
import java.util.List;

public class GroupTitleDeed{
    private List<TitleDeed> group;
    private boolean isFull;

    public GroupTitleDeed() {
        this.group = new ArrayList();
        this.setFull(false);
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public List<TitleDeed> getGroup() {
        return group;
    }

    public void add(TitleDeed titleDeed) {
        this.group.add(titleDeed);
        if (this.group.size() == titleDeed.getFullGroup())
            setFull(true);
    }

}
