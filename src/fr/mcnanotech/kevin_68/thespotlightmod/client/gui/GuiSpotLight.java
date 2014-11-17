package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;

public class GuiSpotLight extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    public GuiBooleanButton timeButton, textButton;

    public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLightSlotConfig(tileEntity, playerInventory, wrld));
        invPlayer = playerInventory;
        tileSpotLight = tileEntity;
        world = wrld;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        this.buttonList.add(new GuiButton(0, x + 5, y + 20, 80, 20, I18n.format("container.spotlight.color")));
        this.buttonList.add(new GuiButton(1, x + 90, y + 20, 50, 20, I18n.format("container.spotlight.textures")));
        this.buttonList.add(new GuiButton(5, x + 142, y + 20, 28, 20, I18n.format("container.spotlight.more")));
        this.buttonList.add(new GuiButton(2, x + 5, y + 43, 80, 20, I18n.format("container.spotlight.beamspecs")));
        this.buttonList.add(new GuiButton(3, x + 90, y + 43, 80, 20, I18n.format("container.spotlight.timeline")));
        this.buttonList.add(timeButton = new GuiBooleanButton(4, x + 5, y + 66, 166, 20, I18n.format("container.spotlight.timeline") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.timeline") + " " + I18n.format("container.spotlight.off"), tileSpotLight.isTimeLineEnabled()));
        this.buttonList.add(textButton = new GuiBooleanButton(6, x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textEnabled") + " " + I18n.format("container.spotlight.true"), I18n.format("container.spotlight.textEnabled") + " " + I18n.format("container.spotlight.false"), tileSpotLight.isTextEnabled()));
        this.buttonList.add(new GuiButton(7, x + 90, y + 89, 80, 20, I18n.format("container.spotlight.text")));
        this.buttonList.add(new GuiButton(8, x + 90, y + 112, 80, 20, I18n.format("container.spotlight.text")));
        this.buttonList.add(new GuiButton(9, x + 27, y + 112, 58, 20, I18n.format("container.spotlight.configs")));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 0:
            {
                this.mc.displayGuiScreen(new GuiSpotLightColor(invPlayer, tileSpotLight, world));
                break;
            }
            case 1:
            {
                this.mc.displayGuiScreen(new GuiSpotLightTexture(invPlayer, tileSpotLight, world));
                break;
            }
            case 2:
            {
                this.mc.displayGuiScreen(new GuiSpotLightBeamSpec(invPlayer, tileSpotLight, world));
                break;
            }
            case 3:
            {
                this.mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tileSpotLight, world));
                break;
            }
            case 4:
            {
                timeButton.toggle();
                PacketSender.sendSpotLightPacketBoolean(this.tileSpotLight, (byte)22, timeButton.getIsActive());
                break;
            }
            case 5:
            {
                this.mc.displayGuiScreen(new GuiSpotLightAddTexture(invPlayer, tileSpotLight, world));
                break;
            }
            case 6:
            {
                textButton.toggle();
                PacketSender.sendSpotLightPacketBoolean(this.tileSpotLight, (byte)27, textButton.getIsActive());
                break;
            }
            case 7:
            {
                this.mc.displayGuiScreen(new GuiSpotLightText(invPlayer, tileSpotLight, world));
                break;
            }
            case 8:
            {
                this.mc.displayGuiScreen(new GuiSpotLightText2(invPlayer, tileSpotLight, world));
                break;
            }
            case 9:
            {
                this.mc.displayGuiScreen(new GuiSpotLightConfigs(invPlayer, tileSpotLight, world));
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.mc.renderEngine.bindTexture(icons);
        this.drawTexturedModalRect(x + 7, y + 113, 238, 0, 18, 18);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", "").replace("-", ""), x + 6, y + 7, 4210752);
    }
}