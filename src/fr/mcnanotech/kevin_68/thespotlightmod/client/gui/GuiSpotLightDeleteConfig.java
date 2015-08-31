package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.ConfigEntry;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class GuiSpotLightDeleteConfig extends GuiContainer implements GuiListBase
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tileSpotLight;
	public World world;
	private GuiList gList;
	private ArrayList<BaseListEntry> list = new ArrayList<BaseListEntry>();
	private ConfigEntry selected;
	private GuiButton deleteButton;
	private GuiBooleanButton helpButton;

	public GuiSpotLightDeleteConfig(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld, ArrayList<BaseListEntry> list)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
		this.invPlayer = playerInventory;
		this.tileSpotLight = tileEntity;
		this.world = wrld;
		this.list = list;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.buttonList.add(0, new GuiButton(0, x + 6, y + 117, 78, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(1, this.deleteButton = new GuiButton(1, x + 90, y + 117, 78, 20, EnumChatFormatting.RED + I18n.format("container.spotlight.delete")));
		this.buttonList.add(2, this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
		this.deleteButton.enabled = false;

		this.gList = new GuiList(this, this.list, x + 6, y + 17, x + 169, y + 115);
		this.gList.addButton(this.buttonList);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 0:
		{
			this.mc.displayGuiScreen(new GuiSpotLightConfigs(this.invPlayer, this.tileSpotLight, this.world));
			break;
		}
		case 1:
		{
			if(this.deleteButton.enabled)
			{
//				PacketSender.send(EnumLaserInformations.COMMANDREMOVECONFIG, this.selected.getId());
			}
			break;
		}
		case 20:
		{
			this.helpButton.toggle();
			break;
		}
		default:
		{
			this.gList.actionPerformed(guibutton, this.buttonList);
		}
		}
	}

	@Override
	public void setSelected(BaseListEntry entry)
	{
		if(entry instanceof ConfigEntry)
		{
			ConfigEntry ent = (ConfigEntry)entry;
			this.selected = ent;
			// PacketSender.send(EnumLaserInformations.COMMANDAPPLYCONFIG,
			// ent.getId());
		}
	}

	@Override
	public void updateScreen()
	{
		this.deleteButton.enabled = this.selected != null;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
	{
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		super.drawScreen(mouseX, mouseY, partialRenderTick);
		this.gList.drawScreen(x, y);

		if(this.helpButton.isActive())
		{
			boolean reversed = mouseX > this.width / 2;
			ArrayList<String> list = new ArrayList<String>();

			if(mouseY > y + 95 && mouseY < y + 115)
			{
				if(mouseX > x + 6 && mouseX < x + 26)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.textures.prevpage"), mouseX, this.width, reversed);
				}

				if(mouseX > x + 149 && mouseX < x + 169)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.textures.nextpage"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x + 6 && mouseX < x + 169 && mouseY > y + 17 && mouseY < y + 90)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.delete.slots"), mouseX, this.width, reversed);
			}

			if(mouseY > y + 117 && mouseY < y + 137)
			{
				if(mouseX > x + 6 && mouseX < x + 84)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, this.width, reversed);
				}

				if(mouseX > x + 90 && mouseX < x + 170)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.delete.del"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, this.width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, this.height, 0x00ff00);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.delete")), x + 5, y + 8, 4210752);
	}
}