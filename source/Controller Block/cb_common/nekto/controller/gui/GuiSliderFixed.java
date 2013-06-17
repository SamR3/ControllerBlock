package nekto.controller.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSliderFixed extends GuiButton
{
    /** The value of this slider control. */
    public float sliderValue = 0.1F;

    /** Is this slider control being dragged. */
    public boolean dragging = false;
    
    private String sliderLabel;

    public GuiSliderFixed(int id, int x, int y, int width, int height, String label, float value)
    {
        super(id, x, y, width, height, label);
        this.sliderValue = value;
        this.sliderLabel = label;
    }

    protected int getHoverState(boolean par1)
    {
        return 0;
    }

    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.sliderValue < 0.1F)
                {
                    this.sliderValue = 0.1F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }
            }

            if(round(sliderValue, 2) == 1.0)
            {
                this.displayString = this.sliderLabel + ": 1 second";
            } else {
                this.displayString = this.sliderLabel + ": " + Float.toString(round(sliderValue, 2)) + " seconds";
            }
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
    
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!this.dragging)
        {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mouseReleased(int par1, int par2)
    {
        if(this.dragging)
        {
            this.dragging = false;
        }
    }
    
    public float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }
}
