package morph.avaritia.item;

import codechicken.lib.util.ItemUtils;
import codechicken.lib.vec.Vector3;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ItemStackWrapper;
import morph.avaritia.util.Lumberjack;
import morph.avaritia.util.ToolHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.Map.Entry;

public class ItemMatterCluster extends Item {

    protected static Random randy = new Random();

    public static final String MAINTAG = "clusteritems";
    public static final String LISTTAG = "items";
    public static final String ITEMTAG = "item";
    public static final String COUNTTAG = "count";
    public static final String MAINCOUNTTAG = "total";

    public static int CAPACITY = 64 * 64;

    public ItemMatterCluster() {
        super(new Properties().stacksTo(1).rarity(ModItems.COSMIC_RARITY));
    }

    @SuppressWarnings({"ConstantConditions"})
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTag() || !stack.getTag().contains(MAINTAG)) {
            return;
        }
        CompoundNBT clustertag = stack.getTag().getCompound(MAINTAG);

        tooltip.add(new StringTextComponent(clustertag.getInt(MAINCOUNTTAG) + "/" + CAPACITY + " ")
                .append(new TranslationTextComponent("tooltip.matter_cluster.counter")));


        if (net.minecraft.client.gui.screen.Screen.hasShiftDown()) {
            ListNBT list = clustertag.getList(LISTTAG, 10);
            for (net.minecraft.nbt.INBT inbt : list) {
                CompoundNBT tag = (CompoundNBT) inbt;
                ItemStack countstack = ItemStack.of(tag.getCompound("item"));
                int count = tag.getInt(COUNTTAG);

//                Lumberjack.log(Level.INFO, countstack.getHoverName());
                tooltip.add(countstack.getHoverName().copy()
                        .withStyle(countstack.getRarity().color)
                        .append(new StringTextComponent(" x " + count)
                                .withStyle(TextFormatting.GRAY)));
            }
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.matter_cluster.desc").withStyle(TextFormatting.DARK_GRAY));
            tooltip.add(new TranslationTextComponent("tooltip.matter_cluster.desc2").withStyle(TextFormatting.DARK_GRAY, TextFormatting.ITALIC));

        }
    }

    public static List<ItemStack> makeClusters(List<ItemStack> input) {
        Map<ItemStackWrapper, Integer> items = ToolHelper.collateMatterCluster(input);
        List<ItemStack> clusters = new ArrayList<>();
        List<Entry<ItemStackWrapper, Integer>> itemlist = new ArrayList<>(items.entrySet());

        int currentTotal = 0;
        Map<ItemStackWrapper, Integer> currentItems = new HashMap<>();

        while (!itemlist.isEmpty()) {
            Entry<ItemStackWrapper, Integer> e = itemlist.get(0);
            ItemStackWrapper wrap = e.getKey();
            int wrapcount = e.getValue();

            int count = Math.min(CAPACITY - currentTotal, wrapcount);

            if (!currentItems.containsKey(e.getKey())) {
                currentItems.put(wrap, count);
            } else {
                currentItems.put(wrap, currentItems.get(wrap) + count);
            }
            currentTotal += count;

            e.setValue(wrapcount - count);
            if (e.getValue() == 0) {
                itemlist.remove(0);
            }

            if (currentTotal == CAPACITY) {
                ItemStack cluster = makeCluster(currentItems);

                clusters.add(cluster);

                currentTotal = 0;
                currentItems = new HashMap<>();
            }
        }

        if (currentTotal > 0) {
            ItemStack cluster = makeCluster(currentItems);

            clusters.add(cluster);
        }

        return clusters;
    }

    public static ItemStack makeCluster(Map<ItemStackWrapper, Integer> input) {
        ItemStack cluster = new ItemStack(ModItems.matter_cluster);
        int total = 0;
        for (int num : input.values()) {
            total += num;
        }
        setClusterData(cluster, input, total);
        return cluster;
    }

    @SuppressWarnings("ConstantConditions")
    public static Map<ItemStackWrapper, Integer> getClusterData(ItemStack cluster) {
        if (!cluster.hasTag() || !cluster.getTag().contains(MAINTAG)) {
            return null;
        }
        CompoundNBT tag = cluster.getTag().getCompound(MAINTAG);
        ListNBT list = tag.getList(LISTTAG, 10);
        Map<ItemStackWrapper, Integer> data = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            CompoundNBT entry = list.getCompound(i);
            ItemStackWrapper wrap = new ItemStackWrapper(ItemStack.of(entry.getCompound(ITEMTAG)));
            int count = entry.getInt(COUNTTAG);
            data.put(wrap, count);
        }
        return data;
    }

    public static int getClusterSize(ItemStack cluster) {
        if (!cluster.hasTag() || !cluster.getTag().contains(MAINTAG)) {
            return 0;
        }
        return cluster.getTag().getCompound(MAINTAG).getInt(MAINCOUNTTAG);
    }

    public static void setClusterData(ItemStack stack, Map<ItemStackWrapper, Integer> data, int count) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        CompoundNBT clustertag = new CompoundNBT();
        ListNBT list = new ListNBT();

        for (Entry<ItemStackWrapper, Integer> entry : data.entrySet()) {
            CompoundNBT itemtag = new CompoundNBT();
            itemtag.put(ITEMTAG, entry.getKey().stack.save(new CompoundNBT()));
            itemtag.putInt(COUNTTAG, entry.getValue());
            list.add(itemtag);
        }
        clustertag.put(LISTTAG, list);
        clustertag.putInt(MAINCOUNTTAG, count);
        stack.getTag().put(MAINTAG, clustertag);
    }

    public static void mergeClusters(ItemStack donor, ItemStack recipient) {
        int donorcount = getClusterSize(donor);
        int recipientcount = getClusterSize(recipient);

        //Lumberjack.log(Level.INFO, donorcount + ", " + recipientcount);
        if (donorcount == 0 || donorcount == CAPACITY || recipientcount == CAPACITY) {
            return;
        }

        Map<ItemStackWrapper, Integer> donordata = getClusterData(donor);
        Map<ItemStackWrapper, Integer> recipientdata = getClusterData(recipient);
        List<Entry<ItemStackWrapper, Integer>> datalist = new ArrayList<>();
        datalist.addAll(donordata.entrySet());

        while (recipientcount < CAPACITY && donorcount > 0) {
            Entry<ItemStackWrapper, Integer> e = datalist.get(0);
            ItemStackWrapper wrap = e.getKey();
            int wrapcount = e.getValue();

            int count = Math.min(CAPACITY - recipientcount, wrapcount);

            if (!recipientdata.containsKey(wrap)) {
                recipientdata.put(wrap, count);
            } else {
                recipientdata.put(wrap, recipientdata.get(wrap) + count);
            }

            donorcount -= count;
            recipientcount += count;

            if (wrapcount - count > 0) {
                e.setValue(wrapcount - count);
            } else {
                donordata.remove(wrap);
                datalist.remove(0);
            }
        }
        setClusterData(recipient, recipientdata, recipientcount);

        if (donorcount > 0) {
            setClusterData(donor, donordata, donorcount);
        } else {
            donor.setTag(null);
            donor.setCount(0);
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            List<ItemStack> drops = ToolHelper.collateMatterClusterContents(ItemMatterCluster.getClusterData(stack));

            for (ItemStack drop : drops) {
                ItemUtils.dropItem(drop, world, new Vector3(player.position()));
            }
        }

        stack.setCount(0);
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

}
