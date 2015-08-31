package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketData implements IMessage
{
    public int x, y, z;
    public String data;

    public PacketData()
    {}

    public PacketData(int x, int y, int z, String data)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.data = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, this.data);
    }

    public static class Handler implements IMessageHandler<PacketData, IMessage>
    {
        @Override
        public IMessage onMessage(PacketData message, MessageContext ctx)
        {
            TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(te instanceof TileEntitySpotLight)
            {
                TileEntitySpotLight tile = (TileEntitySpotLight)te;
                tile.updated = TSMJsonManager.updateTileData(tile, message.data);
                tile.updating = false;
            }
            return null;
        }
    }
}