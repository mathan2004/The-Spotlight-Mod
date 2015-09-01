package fr.mcnanotech.kevin_68.thespotlightmod.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;

@SideOnly(Side.CLIENT)
public class TileEntitySpotLightRender extends TileEntitySpecialRenderer
{
    private ModelSpotLight model = new ModelSpotLight();
    private static final ResourceLocation tex = new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png");
    private static final ResourceLocation defaultBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    public void renderTileEntitySpotLightAt(TileEntitySpotLight tile, double x, double y, double z, float tick)
    {
        byte b0 = 1;
        float f2 = tile.getWorld().getTotalWorldTime() + tick;
        float timer = getWorld().getTotalWorldTime() * 0.00125F;
        float angleX = tile.beamAutoRotateX ? timer * tile.beamRotationSpeedX * (tile.beamReverseRotateX ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleX);
        float angleY = tile.beamAutoRotateY ? timer * tile.beamRotationSpeedY * (tile.beamReverseRotateY ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleY);
        float angleZ = tile.beamAutoRotateZ ? timer * tile.beamRotationSpeedZ * (tile.beamReverseRotateZ ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleZ);
        if(!tile.isBeam)
        {
            angleX = 0.0F;
            angleY = tile.textAutoRotateY ? timer * tile.textRotationSpeedY * (tile.textReverseRotateY ? -1.0F : 1.0F) : (float)Math.toRadians(tile.textAngleY);
            angleZ = 0.0F;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        bindTexture(tex);
        this.model.setRotation(-angleX, angleY, -angleZ);
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        float f1 = tile.isActive();
        GlStateManager.alphaFunc(516, 0.1F);
        if(f1 > 0.0F)
        {
            if(tile.isBeam)
            {
                Tessellator tess = Tessellator.getInstance();
                WorldRenderer worldrenderer = tess.getWorldRenderer();

                ItemStack s = tile.getStackInSlot(6);
                if(s != null && s.getItem() != null)
                {
                    bindTexture(getResourceLocationStack(s) != null ? getResourceLocationStack(s) : TextureMap.LOCATION_MISSING_TEXTURE);
                }
                else
                {
                    bindTexture(defaultBeam);
                }
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                float f3 = -f2 * 0.2F - MathHelper.floor_float(-f2 * 0.1F);
                double t2 = -1.0F - f3;
                double t3 = tile.bVec[0].getLenVec().norm() * f1 * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.beamSize) / 200.0D), 2) / 2)/* d4 */) + t2;
                double t4 = tile.bVec[1].getLenVec().norm() * f1 * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.beamSize) / 200.0D), 2) / 2)/* d4 */) + t2;
                float r = tile.beamRed / 255.0F;
                float g = tile.beamGreen / 255.0F;
                float b = tile.beamBlue / 255.0F;
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_F(r, g, b, 0.125F);
                drawBeam(worldrenderer, x, y, z, t2, t3, tile.bVec[0]);
                tess.draw();
                if(tile.beamDouble)
                {
                    worldrenderer.startDrawingQuads();
                    worldrenderer.setColorRGBA_F(r, g, b, 0.125F);
                    drawBeam(worldrenderer, x, y, z, t2, t4, tile.bVec[1]);
                    tess.draw();
                }
                ItemStack s2 = tile.getStackInSlot(7);
                if(s2 != null && s2.getItem() != null)
                {
                    bindTexture(getResourceLocationStack(s2) != null ? getResourceLocationStack(s2) : TextureMap.LOCATION_MISSING_TEXTURE);
                }
                else
                {
                    bindTexture(defaultBeam);
                }
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                if(tile.secBeamEnabled)
                {
                    float sR = tile.secBeamRed / 255.0F;
                    float sG = tile.secBeamGreen / 255.0F;
                    float sB = tile.secBeamBlue / 255.0F;
                    worldrenderer.startDrawingQuads();
                    worldrenderer.setColorRGBA_F(sR, sG, sB, 0.125F);
                    drawBeam(worldrenderer, x, y, z, t2, t3, tile.bVec[2]);
                    tess.draw();
                    if(tile.beamDouble)
                    {
                        worldrenderer.startDrawingQuads();
                        worldrenderer.setColorRGBA_F(sR, sG, sB, 0.125F);
                        drawBeam(worldrenderer, x, y, z, t2, t4, tile.bVec[3]);
                        tess.draw();
                    }
                }
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
            }
            else
            {
                GL11.glPushMatrix();
                GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
                GlStateManager.translate(0.0F, -0.4F, 0.0F);
                GlStateManager.scale(0.9D, 0.9D, 0.9D);
                GlStateManager.rotate((float)Math.toDegrees(angleY), 0.0F, 1.0F, 0.0F);
                FontRenderer fontrenderer = getFontRenderer();
                float f21 = 0.016666668F * 0.6666667F;
                GlStateManager.translate(0.0F, 0.5F * 0.6666667F, 0.07F * 0.6666667F);
                GlStateManager.scale(f21 * 5, -f21 * 5, f21 * 5);
                GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
                GlStateManager.depthMask(false);
                GlStateManager.translate(0.0F, 100.0F-tile.textHeight*2.0F, 0.0F);
                GlStateManager.translate(0.0F, tile.textScale*0.8F+1.0F, 0.0F);
                if(tile.textHeight < 50)
                {
                    GlStateManager.translate(0.0F, 25.0F + 1.0F+tile.textScale*0.45F, 0.0F);
                }
                GlStateManager.scale(1.0+tile.textScale/16.0F, 1.0+tile.textScale/16.0F, 1.0+tile.textScale/16.0F);
                fontrenderer.drawString(tile.text, -fontrenderer.getStringWidth(tile.text) / 2, -20, tile.textRed * 65536 + tile.textGreen * 256 + tile.textBlue);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                fontrenderer.drawString(tile.text, -fontrenderer.getStringWidth(tile.text) / 2, -20, tile.textRed * 65536 + tile.textGreen * 256 + tile.textBlue);
                GlStateManager.depthMask(true);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.alphaFunc(516, 0.5F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick, int p_180535_9_)
    {
        renderTileEntitySpotLightAt((TileEntitySpotLight)tileentity, x, y, z, tick);
    }

    public void drawBeam(WorldRenderer worldrenderer, double x, double y, double z, double t2, double t3, BeamVec vec)
    {
        TSMVec3[] v = vec.getVecs();
        TSMVec3 e = vec.getLenVec();
        for(int i = 0; i < v.length; i++)
        {
            worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord, y + 0.5 + v[i].yCoord, z + 0.5 + v[i].zCoord, 1.0F, t3);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord + e.xCoord, y + 0.5 + v[i].yCoord + e.yCoord, z + 0.5 + v[i].zCoord + e.zCoord, 1.0F, t2);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord + e.xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord + e.yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord + e.zCoord, 0.0F, t2);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord, 0.0F, t3);
        }
    }

    private ResourceLocation getResourceLocationStack(ItemStack stack)
    {
        TextureAtlasSprite sprite = null;
        Block b = Block.getBlockFromItem(stack.getItem());
        if(b != null)
        {
            sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(b.getStateFromMeta(stack.getMetadata()));
        }
        else
        {
            sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(stack.getItem());
        }

        if(sprite == null)
        {
            return null;
        }
        String iconName = sprite.getIconName();
        String[] strs = iconName.split(":");
        if(strs.length > 1)
        {
            String resource = strs[0] + ":textures/" + strs[1] + ".png";
            return new ResourceLocation(resource);
        }
        return null;
    }
}