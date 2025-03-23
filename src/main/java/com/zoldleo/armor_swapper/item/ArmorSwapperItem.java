package com.zoldleo.armor_swapper.item;

import java.util.List;

import javax.annotation.Nullable;

import com.zoldleo.armor_swapper.gui.SwapperOptionsScreen;
import com.zoldleo.armor_swapper.init.EntityInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ArmorSwapperItem extends Item {

    public static final String NBT_COLOR = "SwapperColor";

    public ArmorSwapperItem(Properties prop) {
        super(prop);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.fail(stack);
        }
        if (!level.isClientSide && player instanceof ServerPlayer && !player.isCrouching()) {
            open((ServerPlayer) player, stack);
        } else if (level.isClientSide && player.isCrouching()) {
            Minecraft.getInstance().setScreen(new SwapperOptionsScreen(stack));
        }
        return InteractionResultHolder.success(stack);
    }
	
	public void open(ServerPlayer player, ItemStack stack) {
        boolean haveCuriosMod = ModList.get().isLoaded("curios");

        ArmorSwapperInventory inventory = getInventory(stack);
        if (!inventory.getFlag("DontStoreArmor")) {
            ItemStack headItem = inventory.getItem(0).copy();
            ItemStack chestItem = inventory.getItem(1).copy();
            ItemStack legsItem = inventory.getItem(2).copy();
            ItemStack feetItem = inventory.getItem(3).copy();
            inventory.setItem(0, player.getItemBySlot(EquipmentSlot.HEAD));
            inventory.setItem(1, player.getItemBySlot(EquipmentSlot.CHEST));
            inventory.setItem(2, player.getItemBySlot(EquipmentSlot.LEGS));
            inventory.setItem(3, player.getItemBySlot(EquipmentSlot.FEET));
            player.setItemSlot(EquipmentSlot.HEAD, headItem);
            player.setItemSlot(EquipmentSlot.CHEST, chestItem);
            player.setItemSlot(EquipmentSlot.LEGS, legsItem);
            player.setItemSlot(EquipmentSlot.FEET, feetItem);
        }
        if (!inventory.getFlag("DontStoreOffhand")) {
            ItemStack offhandItem = inventory.getItem(4).copy();
            inventory.setItem(4, player.getItemBySlot(EquipmentSlot.OFFHAND));
            player.setItemSlot(EquipmentSlot.OFFHAND, offhandItem);
        }
        if (haveCuriosMod && !inventory.getFlag("DontStoreCurios")) {
            CompoundTag tag = stack.getOrCreateTag().copy();
            inventory.writeCurios(player);
            inventory.applyCurios(player, tag);
        }

        saveInventory(inventory);
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F));
	}

	public ArmorSwapperInventory getInventory(ItemStack stack) {
		return new ArmorSwapperInventory(stack, 5);
	}
	
	public void saveInventory(Container inventory) {
		if (inventory instanceof ArmorSwapperInventory armorSwapperInventory) {
			armorSwapperInventory.writeItemStack();
		}
    }
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
        Container inventory = getInventory(stack);
        for (int i = 0; i < 5; i++) {
            if (!(inventory.getItem(i).isEmpty()))
                tooltip.add(new TextComponent(inventory.getItem(i).getDisplayName().getString()));
        }
	}

    public static int getSwapperColor(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_COLOR);
    }

    public static void setSwapperColor(ItemStack stack, int color) {
        stack.getOrCreateTag().putInt(NBT_COLOR, color);
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return DyeColor.byId(getSwapperColor(stack)).getFireworkColor();
        }
        return 0xFFFFFF;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (allowdedIn(tab)) {
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                setSwapperColor(stack, color.getId());
                items.add(stack);
            }
        }
    }

    public static float hasItems(ItemStack stack) {
        return hasItemsB(stack) ? 1 : 0;
    }

    public static boolean hasItemsB(ItemStack stack) {
        return stack.getOrCreateTag().contains("Items") || hasCurios(stack);
    }

	private static boolean hasCurios(ItemStack stack) {
		boolean isEmpty = true;
        if (!stack.getOrCreateTag().contains("Curios")) {
            return false;
        }
		ListTag taglist = stack.getOrCreateTag().getList("Curios", Tag.TAG_COMPOUND);
		for (int i = 0; i < taglist.size(); i++) {
			if (taglist.getCompound(i).getCompound("StacksHandler").getCompound("Stacks").getList("Items", Tag.TAG_COMPOUND).isEmpty() || 
                taglist.getCompound(i).getCompound("StacksHandler").getCompound("Cosmetics").getList("Items", Tag.TAG_COMPOUND).isEmpty()) {
					isEmpty = false;
					break;
			}
		}
		return !isEmpty;
	}

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity entity, ItemStack stack) {
        if (!(entity instanceof ItemEntity) || !hasItemsB(stack)) {
            return null;
        }
		ArmorSwapperEntity swapperItemEntity = EntityInit.SWAPPER_ITEM_ENTITY.get().create(world);
		if (swapperItemEntity != null) {
			swapperItemEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
			swapperItemEntity.setItem(stack);
			swapperItemEntity.setPickUpDelay(getPickupDelay((ItemEntity)entity));
			swapperItemEntity.setThrower(((ItemEntity)entity).getThrower());
			swapperItemEntity.setDeltaMovement(entity.getDeltaMovement());
		}
		return swapperItemEntity;
    }

	private int getPickupDelay(ItemEntity itemEntity) {
		Integer result = ObfuscationReflectionHelper.getPrivateValue(ItemEntity.class, itemEntity, "f_31986_");
		if (result == null) {
			System.out.println("Reflection get of pickupDelay (pickupDelay) from ItemEntity returned null");
			return 20;
		}
		return result;
	}
}