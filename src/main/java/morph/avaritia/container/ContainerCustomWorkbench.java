//package morph.avaritia.container;
//
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.container.WorkbenchContainer;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class ContainerCustomWorkbench extends WorkbenchContainer {
//
//    public BlockPos pos;
//    public World worldObj;
//
//    public ContainerCustomWorkbench(PlayerInventory inv, World world, BlockPos pos) {
//        super(inv, world, pos);
//        this.pos = pos;
//        worldObj = world;
//    }
//
//    public boolean canInteractWith(EntityPlayer player) {
//        return !worldObj.isAirBlock(pos) && player.getDistanceSq(pos) <= 64.0D;
//    }
//}
