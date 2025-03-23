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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;

public class CraftSwapperRecipe extends ShapedRecipe {
    public static final ResourceLocation NAME = new ResourceLocation(ArmorSwapperMod.MOD_ID, "craft_armor_swapper");
    public static final Serializer SERIALIZER = new Serializer();

    public CraftSwapperRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, ItemStack output) {
        super(id, group, 3, 3, ingredients, output);
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory) {
        ItemStack result = super.assemble(inventory);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<CraftSwapperRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = CraftSwapperRecipe.NAME;

        @Override
        public CraftSwapperRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack stack = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
            stack.getOrCreateTag().putInt(ArmorSwapperItem.NBT_COLOR, DyeColor.WHITE.getId());
            NonNullList<Ingredient> list = NonNullList.create();
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.STRING));
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.of(Items.ARMOR_STAND));
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.EMPTY);
            return new CraftSwapperRecipe(recipeId, ArmorSwapperMod.MOD_ID + ".item.color", list, stack);
        }
        
        @Override
        public CraftSwapperRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ItemStack stack = new ItemStack(ItemInit.ARMOR_SWAPPER.get());
            stack.getOrCreateTag().putInt(ArmorSwapperItem.NBT_COLOR, DyeColor.WHITE.getId());
            NonNullList<Ingredient> list = NonNullList.create();
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.STRING));
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.of(Items.ARMOR_STAND));
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.EMPTY);
            list.add(Ingredient.of(Items.LEATHER));
            list.add(Ingredient.EMPTY);
            return new CraftSwapperRecipe(recipeId, ArmorSwapperMod.MOD_ID + ".item.color", list, stack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CraftSwapperRecipe recipe) {
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