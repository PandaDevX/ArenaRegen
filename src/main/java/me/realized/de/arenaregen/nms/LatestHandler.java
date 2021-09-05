package me.realized.de.arenaregen.nms;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Chunk;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

public class LatestHandler implements NMS {


    @Override
    public void sendChunkUpdate(final Player player, final org.bukkit.Chunk chunk) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 65535));
    }

    @Override
    public void setBlockFast(final Block bukkitBlock, final Material material, final int data) {
        final int x = bukkitBlock.getX(), y = bukkitBlock.getY(), z = bukkitBlock.getZ();
        final BlockPosition position = new BlockPosition(x, y, z);
        final Chunk chunk = ((CraftChunk) bukkitBlock.getChunk()).getHandle();
        final net.minecraft.server.v1_16_R3.Block block = CraftMagicNumbers.getBlock(material);
        final IBlockData blockData = block.getBlockData();
        chunk.setType(position, blockData, true);

        if (bukkitBlock.getType() == Material.AIR || isSurrounded(bukkitBlock)) {
            return;
        }

        final World world = ((CraftWorld) bukkitBlock.getWorld()).getHandle();
        world.getChunkProvider().getLightEngine().a(position);
    }

    private boolean isSurrounded(final Block block) {
        final Block east = block.getRelative(BlockFace.EAST);
        final Block west = block.getRelative(BlockFace.WEST);
        final Block south = block.getRelative(BlockFace.SOUTH);
        final Block north = block.getRelative(BlockFace.NORTH);
        final Block up = block.getRelative(BlockFace.UP);
        final Block down = block.getRelative(BlockFace.DOWN);
        return !east.getType().isTransparent()
                && !west.getType().isTransparent()
                && !up.getType().isTransparent()
                && !down.getType().isTransparent()
                && !south.getType().isTransparent()
                && !north.getType().isTransparent();
    }
}
