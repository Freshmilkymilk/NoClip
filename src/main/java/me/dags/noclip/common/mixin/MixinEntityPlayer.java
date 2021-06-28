package me.dags.noclip.common.mixin;

import me.dags.noclip.common.EntityNoClipper;
import me.dags.noclip.common.NoClipData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(PlayerEntity.class)
public abstract class MixinEntityPlayer extends Entity implements EntityNoClipper {

    private final NoClipData noClipData = new NoClipData();
    private boolean noClipping = false;
    private boolean disableDamage = false;

    public MixinEntityPlayer(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    public NoClipData getNoClipData() {
        return noClipData;
    }

    @Inject(method = "onUpdate()V", at = @At("RETURN"))
    public void onUpdate(CallbackInfo callbackInfo) {
        if (getNoClipData().noClip()) {
            noClip = true;
            if (!noClipping) {
                noClipping = true;
                disableDamage = getCapabilities().disableDamage;
                getCapabilities().disableDamage = true;
                sendAbilities();
            }
        } else if (noClipping) {
            noClipping = false;
            getCapabilities().disableDamage = disableDamage;
            sendAbilities();
        }
    }

    public void move(MoverType type, double x, double y, double z) {
        if (getNoClipData().noClip() && getCapabilities().isFlying) {
            this.noClip = true;

        }
        super.move(type, x, y, z);
    }

    private PlayerAbilities getCapabilities() {
        return PlayerEntity.class.cast(this).abilities;
    }

    private void sendAbilities() {
        if (PlayerEntity.class.isInstance(this)) {
            PlayerEntity.class.cast(this).onUpdateAbilities();
        }
    }
}
