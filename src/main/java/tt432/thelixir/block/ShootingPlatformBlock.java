package tt432.thelixir.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.List;

/**
 * @author DustW
 **/
public class ShootingPlatformBlock extends AbstractGlassBlock {
    public static final VoxelShape SHAPE = Shapes.box(0, 0.4375, 0, 1, 0.5625, 1);

    public ShootingPlatformBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        return Collections.singletonList(new ItemStack(this));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pContext instanceof EntityCollisionContext ecc &&
                ecc.getEntity() instanceof Player player &&
                !player.isShiftKeyDown() &&
                player.position().y >= pPos.getY()
                ? SHAPE : Shapes.empty();
    }
}
