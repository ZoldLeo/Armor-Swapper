package com.zoldleo.armor_swapper.item;

import com.zoldleo.armor_swapper.network.PacketHandler;
import com.zoldleo.armor_swapper.network.SPacketSyncCurios;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import top.theillusivec4.curios.api.CuriosApi;

public class ArmorSwapperInventory extends SimpleContainer {

	private final ItemStack stack;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ArmorSwapperInventory(ItemStack stackTemp, int count) {
		super(count);
		stack = stackTemp;
		readItemStack();
	}

    public ItemStack getStack() {
		return stack;
	}
	
	public void readItemStack() {
		if (stack.getTag() != null) {
			readNBT(stack.getTag());
		}
	}
	
	public void writeItemStack() {
		if (isEmpty()) {
			stack.removeTagKey("Items");
		} else {
			writeNBT(stack.getOrCreateTag());
		}
	}
	
	private void readNBT(CompoundTag compound) {
		final NonNullList<ItemStack> list = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compound, list);
		for (int index = 0; index < list.size(); index++) {
			setItem(index, list.get(index));
		}
	}
	
	private void writeNBT(CompoundTag compound) {
		final NonNullList<ItemStack> list = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		for (int index = 0; index < list.size(); index++) {
			list.set(index, getItem(index));
		}
		ContainerHelper.saveAllItems(compound, list, false);
	}

	public boolean getFlag(String string) {
		return stack.getOrCreateTag().contains(string);
	}

	public void writeCurios(Player player) {
		CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
			ListTag taglist = ((CompoundTag) handler.writeTag()).getList("Curios", Tag.TAG_COMPOUND);
			stack.getOrCreateTag().put("Curios", taglist);
		});
	}

	@SuppressWarnings("deprecation")
	public void applyCurios(Player player, CompoundTag tag) {
		if (tag.contains("Curios")) {
			CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
				handler.readTag(tag);
				PacketHandler.sendToClient(player, new SPacketSyncCurios(player.getId(), tag));
			});
		} else {
			CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
				CompoundTag nbt = new CompoundTag();
				ListTag taglist = new ListTag();
				CuriosApi.getSlotHelper().createSlots().forEach((slotType, stacksHandler) -> {
					CompoundTag compound = new CompoundTag();
					compound.put("StacksHandler", stacksHandler.serializeNBT());
					compound.putString("Identifier", slotType.getIdentifier());
					taglist.add(compound);
				});
				nbt.put("Curios", taglist);
				handler.readTag(nbt);
				PacketHandler.sendToClient(player, new SPacketSyncCurios(player.getId(), nbt));
			});
		}
	}
}