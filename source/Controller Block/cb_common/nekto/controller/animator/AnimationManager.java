package nekto.controller.animator;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nekto.controller.tile.TileEntityAnimator;

public class AnimationManager {
    
    public static final int MAX_FRAMES = 7; //Or however many we want.
    
    private float delay = 0.1F; //Default time? Also, should we do milliseconds, or fractions of a second? E.G. 0.1s or 100ms
    private List<Frame> frames = null;
    private int frameIndex = 1;
    private Iterator iter;
    private Frame previous = null;
    private TileEntityAnimator tile;
    
    Timer timer = new Timer();
    
    private enum Mode {
        LOOP,
        SEQUENCE,
        RANDOM
    }
    
    public Mode currMode = Mode.SEQUENCE;
    
    public AnimationManager(TileEntityAnimator tile)
    {
        this.tile = tile;
     /* this.frames = new ArrayList<Frame>();
        createFrame(1);
        this.iter = this.frames.iterator();*/
    }
    
    public void setTimer(float time)
    {
        this.delay = time;
    }
    
    public boolean createFrame(int id)
    {
        if(frameIndex < MAX_FRAMES)
        {
            frames.add(frameIndex, new Frame());
            frameIndex++;
            return true;
        } else {
            return false;
        }
    }
    
    public void changeMode(Mode par1Mode)
    {
        this.currMode = par1Mode;
    }
    
    public void setActiveFrame(int index)
    {
        
    }
    
    public float getDelay() {
        return this.delay;
    }
    
    public void onPowered()
    {      
        timer.schedule(new TimerTask() {
            public void run() {
                if(iter.hasNext())
                {
                    if(previous != null)
                    {
                        previous.setInactiveBlocks(tile.worldObj);
                    }
                    
                    Frame curr = (Frame)iter.next();
                    curr.setActiveBlocks(tile.worldObj);
                    
                    previous = curr;
                }
            }
        }, (long) this.delay);
    }
}
