package krzyhau.p2movement.mixin;

import krzyhau.p2movement.client.gui.VelMeterHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class VelMeterMixin {
    @Inject(method = "render", at = @At("RETURN"))
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (!VelMeterHud.noDraw)
            VelMeterHud.draw(matrices);
    }
}
