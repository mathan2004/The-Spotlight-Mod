package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;

public class GuiSpotlightTimelineKeyButton extends GuiButton
{
    protected static final ResourceLocation textures = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public GuiSpotlightTimelineKeyButton(int par1, int par2, int par3)
    {
        super(par1, par2, par3, 3, 3, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if(this.visible)
        {
            mc.getTextureManager().bindTexture(textures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 102, 3, 3);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    @Override
    public int getHoverState(boolean isHover)
    {
        byte b0 = 1;

        if(!this.enabled)
        {
            b0 = 0;
        }
        else if(isHover)
        {
            b0 = 2;
        }

        return b0;
    }
}