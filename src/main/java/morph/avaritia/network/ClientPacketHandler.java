package morph.avaritia.network;

import codechicken.lib.packet.ICustomPacketHandler.IClientPacketHandler;
import codechicken.lib.packet.PacketCustom;
import morph.avaritia.tile.TileMachineBase;
import morph.avaritia.util.Lumberjack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

/**
 * Created by covers1624 on 20/05/2017.
 */
public class ClientPacketHandler implements IClientPacketHandler {

    @Override
    public void handlePacket(PacketCustom packet, Minecraft mc, IClientPlayNetHandler handler) {
        switch (packet.getType()) {
            case NetworkDispatcher.MACHINE_UPDATE: {
                BlockPos pos = packet.readPos();
                TileEntity tile = mc.level.getBlockEntity(pos);
                if (tile instanceof TileMachineBase) {
                    ((TileMachineBase) tile).readUpdatePacket(packet);
                } else {
                    Lumberjack.log(Level.ERROR, "Received Machine update packet for tile that is not a Machine... Pos: " + pos);
                }
                break;
            }
            case NetworkDispatcher.GUI_CHANGES: {
//                BlockPos pos = packet.readPos();
//                TileEntity tile = mc.level.getBlockEntity(pos);
//                if (tile instanceof TileMachineBase) {
//                    ((TileMachineBase) tile).readGuiData(packet, packet.readBoolean());
//                } else {
//                    Lumberjack.log(Level.ERROR, "Received Gui update packet for tile that is not a Machine... Pos: " + pos);
//                }
                break;
            }
        }
    }
}
