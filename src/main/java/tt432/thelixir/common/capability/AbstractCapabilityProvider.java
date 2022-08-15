package tt432.thelixir.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProviderImpl;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author DustW
 **/
public abstract class AbstractCapabilityProvider<B extends ICapabilityProviderImpl<B>>
        extends CapabilityProvider<B> implements INBTSerializable<CompoundTag> {

    protected AbstractCapabilityProvider(Class<B> baseClass) {
        super(baseClass);
    }

    protected AbstractCapabilityProvider(Class<B> baseClass, boolean isLazy) {
        super(baseClass, isLazy);
    }
}
