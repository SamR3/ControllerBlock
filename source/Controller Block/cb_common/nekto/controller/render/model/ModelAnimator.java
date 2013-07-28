package nekto.controller.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelAnimator extends ModelBase
{
    //fields
    ModelRenderer Top;
    ModelRenderer Base;
    ModelRenderer Bars;
    ModelRenderer Orb;
    
    public ModelAnimator()
    {
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("Bars.Bar1", 0, 0);
        setTextureOffset("Bars.Bar2", 0, 0);
        setTextureOffset("Bars.Bar3", 0, 0);
        setTextureOffset("Bars.Bar4", 0, 0);
        setTextureOffset("Orb.Orb1", 0, 20);
        setTextureOffset("Orb.Orb2", 9, 20);
        setTextureOffset("Orb.Orb3", 0, 28);
        setTextureOffset("Orb.Orb4", 27, 19);
        
        Top = new ModelRenderer(this, 0, 0);
        Top.addBox(0F, 0F, 0F, 16, 3, 16);
        Top.setRotationPoint(-8F, 8F, -8F);
        Top.setTextureSize(64, 32);
        Top.mirror = true;
        setRotation(Top, 0F, 0F, 0F);
        
        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(0F, 0F, 0F, 16, 3, 16);
        Base.setRotationPoint(-8F, 21F, -8F);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        
        Bars = new ModelRenderer(this, "Bars");
        Bars.setRotationPoint(0F, 0F, 0F);
        setRotation(Bars, 0F, 0F, 0F);
        Bars.mirror = true;
            Bars.addBox("Bar1", -8F, 8F, -8F, 2, 14, 2);
            Bars.addBox("Bar2", 6F, 8F, -8F, 2, 14, 2);
            Bars.addBox("Bar3", 6F, 8F, 6F, 2, 14, 2);
            Bars.addBox("Bar4", -8F, 9F, 6F, 2, 14, 2);
        
        Orb = new ModelRenderer(this, "Orb");
        Orb.setRotationPoint(-1F, 13F, -1F);
        setRotation(Orb, 0F, 0F, 0F);
        Orb.mirror = true;
            Orb.addBox("Orb1", 0F, 0F, 0F, 2, 5, 2);
            Orb.addBox("Orb2", -1F, 1F, -1F, 4, 3, 4);
            Orb.addBox("Orb3", -2F, 2F, 0F, 6, 1, 2);
            Orb.addBox("Orb4", 0F, 2F, -2F, 2, 1, 6);
    }
     
    public void render(float f)
    {
        Top.render(f);
        Base.render(f);
        Bars.render(f);
    }
    
    public void renderOrb(float f)
    {
       Orb.render(f);
    }
     
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}