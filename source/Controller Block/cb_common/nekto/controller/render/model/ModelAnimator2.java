package nekto.controller.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAnimator2 extends ModelBase
{
  //fields
    ModelRenderer Frame;
    ModelRenderer Cage;
    ModelRenderer Orb;
  
  public ModelAnimator2()
  {
    textureWidth = 64;
    textureHeight = 64;
    
    setTextureOffset("Frame.Top", 0, 0);
    setTextureOffset("Frame.Top2", 0, 37);
    setTextureOffset("Frame.Base", 0, 0);
    setTextureOffset("Frame.Base2", 0, 37);
    
    setTextureOffset("Cage.Bar1", 18, 19);
    setTextureOffset("Cage.Bar2", 25, 19);
    setTextureOffset("Cage.Bar3", 18, 19);
    setTextureOffset("Cage.Bar4", 25, 19);
    setTextureOffset("Cage.Bar5", 18, 19);
    setTextureOffset("Cage.Bar6", 25, 19);
    setTextureOffset("Cage.Bar7", 25, 19);
    setTextureOffset("Cage.Bar8", 18, 19);
    
    setTextureOffset("Orb.Orb1", 0, 28);
    setTextureOffset("Orb.Orb2", 0, 20);
    
    Frame = new ModelRenderer(this, "Frame");
    Frame.setRotationPoint(0F, 0F, 0F);
    setRotation(Frame, 0F, 0F, 0F);
    Frame.mirror = true;
      Frame.addBox("Top", -7F, 8F, -8F, 14, 3, 16);
      Frame.addBox("Top2", -8F, 8F, -7F, 16, 3, 14);
      Frame.addBox("Base", -7F, 21F, -8F, 14, 3, 16);
      Frame.addBox("Base2", -8F, 21F, -7F, 16, 3, 14);
      
    Cage = new ModelRenderer(this, "Cage");
    Cage.setRotationPoint(0F, 0F, 0F);
    setRotation(Cage, 0F, 0F, 0F);
    Cage.mirror = true;
      Cage.addBox("Bar1", 5F, 8F, 7F, 2, 16, 1);
      Cage.addBox("Bar2", 7F, 8F, 5F, 1, 16, 2);
      Cage.addBox("Bar3", -7F, 8F, 7F, 2, 16, 1);
      Cage.addBox("Bar4", -8F, 8F, 5F, 1, 16, 2);
      Cage.addBox("Bar5", -7F, 8F, -8F, 2, 16, 1);
      Cage.addBox("Bar6", -8F, 8F, -7F, 1, 16, 2);
      Cage.addBox("Bar7", 7F, 8F, -7F, 1, 16, 2);
      Cage.addBox("Bar8", 5F, 8F, -8F, 2, 16, 1);
      
    Orb = new ModelRenderer(this, "Orb");
    Orb.setRotationPoint(-1F, 13F, -1F);
    setRotation(Orb, 0F, 0F, 0F);
    Orb.mirror = true;
      Orb.addBox("Orb1", 0F, 0F, 0F, 2, 5, 2);
      Orb.addBox("Orb2", -1F, 1F, -1F, 4, 3, 4);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Frame.render(f5);
    Cage.render(f5);
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
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
