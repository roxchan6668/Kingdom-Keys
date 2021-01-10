package online.kingdomkeys.kingdomkeys.world.dimension;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

@Mod.EventBusSubscriber
public class DiveToTheHeartDimension{
    //Event Listeners//

    //Set the fog density to fade out the bottom of the platform
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.FogDensity event) {
        World world = event.getInfo().getRenderViewEntity().world;
        if (world != null) {
            if (world.getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                event.setDensity(0.06F);
                event.setCanceled(true);
            }
        }
    }

    //Prevent taking damage in this dimension
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            if (!((PlayerEntity)event.getEntityLiving()).isCreative()) {
                if (event.getEntityLiving().world.getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    //Prevent player from falling off the platform
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.isCreative()) {
            if (event.player.world.getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                if (event.player.getPosY() < 10) {
                    event.player.setPositionAndUpdate(0, 25, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getPlayer().world.getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.pedestal.get()) {
                    if (event.getPlayer().isSneaking()) {
                        event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getPlayer().isCreative()) {
            if (event.getWorld().getDimensionKey().equals(ModDimensions.DIVE_TO_THE_HEART)) {
                event.setCanceled(true);
            }
        }
    }
}
