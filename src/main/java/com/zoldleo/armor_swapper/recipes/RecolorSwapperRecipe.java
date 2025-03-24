package com.zoldleo.armor_swapper.recipes;

import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.init.ItemInit;
import com.zoldleo.armor_swapper.item.ArmorSwapperItem;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecolorSwapperRecipe extends ShapelessRecipe {
    public static final ResourceLocation NAME = new ResourceLocation(ArmorSwapperMod.MOD_ID, "recolor_armor_swapper");
    public static final Serializer SERIALIZER = new Serializer();
    public final int colorId;

    public RecolorSwapperRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> ingredients, int colorIdIn) {
        super(id, group, output, ingredients);
        colorId = colorIdIn;
    }

    @Override
    public ItemStack assemble(@Nonnull CraftingContainer inventory) {
        ItemStack swapper = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ArmorSwapperItem) {
                    swapper = stack.copy();
                } else if (stack.is(Tags.Items.DYES)) {
                    dye = stack;
                }
            }
        }
        DyeColor color = DyeColor.getColor(dye);
        if (color != null) {
            ArmorSwapperItem.setSwapperColor(swapper, color.getId());
        }
        return swapper;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<RecolorSwapperRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = RecolorSwapperRecipe.NAME;

        @Override
        public RecolorSwapperRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            NonNullList<Ingredient> list = NonNullList.create();

            List<ItemStack> swapperIng = new ArrayList<>();
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
                ArmorSwapperItem.setSwapperColor(stack, color.getId());
                swapperIng.add(stack);
            }
            list.add(Ingredient.of(swapperIng.stream()));

            DyeColor color = DyeColor.byId(json.get("color").getAsInt());
            list.add(Ingredient.of(DyeItem.byColor(color)));
            //list.add(Ingredient.of(Tags.Items.DYES));
            ItemStack output = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
            ArmorSwapperItem.setSwapperColor(output, color.getId());

            return new RecolorSwapperRecipe(recipeId, ArmorSwapperMod.MOD_ID + ".item.color", output, list, color.getId());
        }
        
        @Override
        public RecolorSwapperRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            NonNullList<Ingredient> list = NonNullList.create();

            List<ItemStack> swapperIng = new ArrayList<>();
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
                ArmorSwapperItem.setSwapperColor(stack, color.getId());
                swapperIng.add(stack);
            }
            list.add(Ingredient.of(swapperIng.stream()));

            DyeColor color = DyeColor.byId(buffer.readInt());
            //DyeColor color = DyeColor.byId(DyeColor.BLUE.getId());
            list.add(Ingredient.of(DyeItem.byColor(color)));
            //list.add(Ingredient.of(Tags.Items.DYES));
            ItemStack output = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
            ArmorSwapperItem.setSwapperColor(output, color.getId());

            return new RecolorSwapperRecipe(recipeId, ArmorSwapperMod.MOD_ID + ".item.color", output, list, color.getId());
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RecolorSwapperRecipe recipe) {
            ItemStack stack = recipe.getResultItem();
            buffer.writeInt(stack.getOrCreateTag().getInt("SwapperColor"));
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}