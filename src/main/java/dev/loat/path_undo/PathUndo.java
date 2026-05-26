package dev.loat.path_undo;

import dev.loat.path_undo.logging.Logger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;


public class PathUndo implements ModInitializer {

    /**
     * Mod entry point. Registers a callback for when the player uses a block.
     * If the block is a dirt path and the player is holding a shovel,
     * it will turn the dirt path back into regular dirt, play a sound, and damage the shovel.
     */
    @Override
    public void onInitialize() {
        Logger.setLoggerClass(this.getClass());
        Logger.info("Mod initialized!");

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            
            BlockPos blockPosition = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPosition);

            if (blockState.is(Blocks.DIRT_PATH) && player.getItemInHand(hand).getItem() instanceof ShovelItem) {

                world.setBlockAndUpdate(blockPosition, Blocks.DIRT.defaultBlockState());

                world.playSound(null, blockPosition, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);

                player.getItemInHand(hand).hurtAndBreak(1, player, hand);

                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        });
    }
}
