package gregtech.common.blocks.surfacerock;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TileEntitySurfaceRock extends TileEntity {

    private Material material = Materials.Aluminium;
    private List<Material> undergroundMaterials = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    public Object cachedModel = null;
    @SideOnly(Side.CLIENT)
    public Object cacheKey = null;

    public Material getMaterial() {
        return material;
    }

    public List<Material> getUndergroundMaterials() {
        return undergroundMaterials;
    }

    public void setData(Material material, Collection<Material> undergroundMaterials) {
        this.material = material;
        this.undergroundMaterials = new ArrayList<>(undergroundMaterials);
        markDirty();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        Material material = Material.MATERIAL_REGISTRY.getObject(compound.getString("Material"));
        this.material = material == null ? Materials.Aluminium : material;
        this.undergroundMaterials =
            compound.getTagList("UndergroundMaterials", NBT.TAG_STRING)
            .tagList.stream()
            .map(it -> ((NBTTagString) it).getString())
            .map(Material.MATERIAL_REGISTRY::getObject)
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("Material", Material.MATERIAL_REGISTRY.getNameForObject(material));
        NBTTagList tagList = new NBTTagList();
        this.undergroundMaterials.forEach(it ->
            tagList.appendTag(new NBTTagString(Material.MATERIAL_REGISTRY.getNameForObject(it))));
        compound.setTag("UndergroundMaterials", tagList);
        return compound;
    }
}
