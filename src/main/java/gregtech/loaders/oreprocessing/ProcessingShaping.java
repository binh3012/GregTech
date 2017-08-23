package gregtech.loaders.oreprocessing;

import gregtech.api.GTValues;
import gregtech.api.items.ItemList;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictionaryUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.IOreRegistrationHandler;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.SimpleItemStack;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import ic2.core.item.type.CasingResourceType;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ProcessingShaping implements IOreRegistrationHandler {
    public ProcessingShaping() {
        OrePrefix.ingot.addProcessingHandler(this);
        OrePrefix.dust.addProcessingHandler(this);
    }
    
    public void registerOre(UnificationEntry uEntry, String modName, SimpleItemStack simpleStack) {
        ItemStack stack = simpleStack.asItemStack();
        if (((uEntry.material == Materials.Glass) || (OreDictionaryUnifier.get(OrePrefix.ingot, uEntry.material, 1) != null)) && (!uEntry.material.contains(SubTag.NO_SMELTING))) {
            long materialMass = uEntry.material.getMass();
            int tAmount = (int) (uEntry.orePrefix.mMaterialAmount / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (uEntry.orePrefix.mMaterialAmount % 3628800L == 0L)) {
                int tVoltageMultiplier = uEntry.material.mBlastFurnaceTemp >= 2800 ? 64 : 16;

                if (uEntry.material.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (uEntry.orePrefix.name().startsWith(OrePrefix.dust.name())) {
                    return;
                }

                if (!OrePrefix.block.isIgnored(uEntry.material.mSmeltInto)) {
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, stack), ItemList.Shape_Extruder_Block.get(0L), OreDictionaryUnifier.get(OrePrefix.block, uEntry.material.mSmeltInto, tAmount), 10 * tAmount, 8 * tVoltageMultiplier);
                    GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(9L, stack), ItemList.Shape_Mold_Block.get(0L), OreDictionaryUnifier.get(OrePrefix.block, uEntry.material.mSmeltInto, tAmount), 5 * tAmount, 4 * tVoltageMultiplier);
                }
                if (((uEntry.orePrefix != OrePrefix.ingot) || (uEntry.material != uEntry.material.mSmeltInto))) {
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Ingot.get(0L), OreDictionaryUnifier.get(OrePrefix.ingot, uEntry.material.mSmeltInto, tAmount), 10, 4 * tVoltageMultiplier);
                }

                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L), OreDictionaryUnifier.get(OrePrefix.pipeTiny, uEntry.material.mSmeltInto, tAmount * 2), 4 * tAmount, 8 * tVoltageMultiplier);
                if (!(uEntry.material == Materials.Redstone || uEntry.material == Materials.Glowstone)) {
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Pipe_Small.get(0L), OreDictionaryUnifier.get(OrePrefix.pipeSmall, uEntry.material.mSmeltInto, tAmount), 8 * tAmount, 8 * tVoltageMultiplier);
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(3L, stack), ItemList.Shape_Extruder_Pipe_Medium.get(0L), OreDictionaryUnifier.get(OrePrefix.pipeMedium, uEntry.material.mSmeltInto, tAmount), 24 * tAmount, 8 * tVoltageMultiplier);
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(6L, stack), ItemList.Shape_Extruder_Pipe_Large.get(0L), OreDictionaryUnifier.get(OrePrefix.pipeLarge, uEntry.material.mSmeltInto, tAmount), 48 * tAmount, 8 * tVoltageMultiplier);
                }
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(12L, stack), ItemList.Shape_Extruder_Pipe_Huge.get(0L), OreDictionaryUnifier.get(OrePrefix.pipeHuge, uEntry.material.mSmeltInto, tAmount), 96 * tAmount, 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Plate.get(0L), OreDictionaryUnifier.get(OrePrefix.plate, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
                if (tAmount * 2 <= 64)
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Rod.get(0L), OreDictionaryUnifier.get(OrePrefix.stick, uEntry.material.mSmeltInto, tAmount * 2), (int) Math.max(materialMass * 2L * tAmount, tAmount), 6 * tVoltageMultiplier);
                if (tAmount * 2 <= 64)
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Wire.get(0L), OreDictionaryUnifier.get(OrePrefix.wireGt01, uEntry.material.mSmeltInto, tAmount * 2), (int) Math.max(materialMass * 2L * tAmount, tAmount), 6 * tVoltageMultiplier);
                if (tAmount * 8 <= 64)
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Bolt.get(0L), OreDictionaryUnifier.get(OrePrefix.bolt, uEntry.material.mSmeltInto, tAmount * 8), (int) Math.max(materialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                if (tAmount * 4 <= 64) {
                    GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Ring.get(0L), OreDictionaryUnifier.get(OrePrefix.ring, uEntry.material.mSmeltInto, tAmount * 4), (int) Math.max(materialMass * 2L * tAmount, tAmount), 6 * tVoltageMultiplier);
                    if ((uEntry.material.mUnificatable) && (uEntry.material.mMaterialInto == uEntry.material) && !uEntry.material.contains(SubTag.NO_SMASHING))
                        ModHandler.addCraftingRecipe(OreDictionaryUnifier.get(OrePrefix.ring, uEntry.material, 1L), GT_Proxy.tBits, new Object[]{"h ", " X", Character.valueOf('X'), OrePrefix.stick.get(uEntry.material)});
                }
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Extruder_Sword.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadSword, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(3L, stack), ItemList.Shape_Extruder_Pickaxe.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadPickaxe, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 3L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Shovel.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadShovel, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(3L, stack), ItemList.Shape_Extruder_Axe.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadAxe, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 3L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Extruder_Hoe.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadHoe, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(6L, stack), ItemList.Shape_Extruder_Hammer.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadHammer, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 6L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Extruder_File.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadFile, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Extruder_Saw.get(0L), OreDictionaryUnifier.get(OrePrefix.toolHeadSaw, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(4L, stack), ItemList.Shape_Extruder_Gear.get(0L), OreDictionaryUnifier.get(OrePrefix.gearGt, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 5L * tAmount, tAmount), 8 * tVoltageMultiplier);

                GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Plate.get(0L), OreDictionaryUnifier.get(OrePrefix.plate, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 2L * tAmount, tAmount), 2 * tVoltageMultiplier);
                GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(8L, stack), ItemList.Shape_Mold_Gear.get(0L), OreDictionaryUnifier.get(OrePrefix.gearGt, uEntry.material.mSmeltInto, tAmount), (int) Math.max(materialMass * 10L * tAmount, tAmount), 2 * tVoltageMultiplier);
                switch (uEntry.material.mSmeltInto.toString()) {
                    case "Glass":
                        GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Bottle.get(0L), new ItemStack(Items.GLASS_BOTTLE, 1), tAmount * 32, 16);
                        GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Mold_Bottle.get(0L), new ItemStack(Items.GLASS_BOTTLE, 1), tAmount * 64, 4);
                        break;
                    case "Steel":
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.steel, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.steel, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Iron":
                    case "WroughtIron":
                        GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Cell.get(0L), ModHandler.getIC2Item(ItemName.crafting, CraftingItemType.fuel_rod, tAmount), tAmount * 128, 32);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.iron, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.iron, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        if (tAmount * 31 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(31L, stack), ItemList.Shape_Mold_Anvil.get(0L), new ItemStack(Blocks.ANVIL, 1, 0), tAmount * 512, 4 * tVoltageMultiplier);
                        break;
                    case "Tin":
                        GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Extruder_Cell.get(0L), ItemList.Cell_Empty.get(tAmount), tAmount * 128, 32);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.tin, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.tin, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Lead":
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.lead, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.lead, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Copper":
                    case "AnnealedCopper":
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.copper, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.copper, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Bronze":
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.bronze, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.bronze, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Gold":
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, stack), ItemList.Shape_Extruder_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.gold, tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GTValues.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, stack), ItemList.Shape_Mold_Casing.get(0L), ModHandler.getIC2Item(ItemName.casing, CasingResourceType.gold, tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                }
            }
        }
    }
}
