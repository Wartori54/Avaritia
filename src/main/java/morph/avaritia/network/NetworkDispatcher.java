package morph.avaritia.network;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustomChannelBuilder;
import morph.avaritia.Avaritia;
import morph.avaritia.tile.TileMachineBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

/**
 * Created by covers1624 on 20/05/2017.
 */
public class NetworkDispatcher {

    public static final ResourceLocation NET_CHANNEL = new ResourceLocation(Avaritia.MOD_ID, "avaritia");
    private static final String PROTOCOL_VERSION = "1";
    public static EventNetworkChannel net_channel;

    // this is purely for readability, we will be grateful in the future when adding new channel types :P
    public static final int MACHINE_UPDATE = 1;
    public static final int GUI_CHANGES = 2;

    public static void init() {
        net_channel = PacketCustomChannelBuilder.named(NET_CHANNEL)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .assignClientHandler(() -> ClientPacketHandler::new)
                .build();
    }
    public static void dispatchMachineUpdate(TileMachineBase machineTile) {
        PacketCustom packet = new PacketCustom(NET_CHANNEL, MACHINE_UPDATE);
        packet.writePos(machineTile.getBlockPos());
        machineTile.writeUpdateData(packet);
        packet.sendToChunk(machineTile);
    }

    public static void dispatchGuiChanges(PlayerEntity player, TileMachineBase machineTile, boolean isFullSync) {
        PacketCustom packet = new PacketCustom(NET_CHANNEL, GUI_CHANGES);
        packet.writePos(machineTile.getBlockPos());
        packet.writeBoolean(isFullSync);
        machineTile.writeGuiData(packet, isFullSync);
        packet.sendToPlayer((ServerPlayerEntity) player);
    }

}
