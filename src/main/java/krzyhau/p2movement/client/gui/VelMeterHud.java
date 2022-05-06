package krzyhau.p2movement.client.gui;

import krzyhau.p2movement.ModMain;
import krzyhau.p2movement.Portal2Movement;
import krzyhau.p2movement.config.P2MovementConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.util.Locale;

public class VelMeterHud {
    private static final MinecraftClient MC_INSTANCE = MinecraftClient.getInstance();

    public static boolean noDraw = false;

    public static void draw(MatrixStack matrices) {
        if (!canDraw()) return;

        String[] lines = getVelString();
        for (int i = 0; i < lines.length; i++) {
            MC_INSTANCE.textRenderer.draw(matrices, lines[i], 5, 5 + i * 10, -1);
        }
    }

    private static boolean canDraw() {
        ClientPlayerEntity player = MC_INSTANCE.player;
        if (player == null)
            return false;

        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        if (!(boots.getItem() == (ModMain.LONG_FALL_BOOTS)) || EnchantmentHelper.getLevel(ModMain.CL_SHOWPOS_ENCHANTMENT, boots) != 0) {
            return false;
        }

        return P2MovementConfig.get().velMeter;
    }

    private static String[] getVelString() {
        ClientPlayerEntity player = MC_INSTANCE.player;
        if (player == null)
            return new String[]{};


        Vec3d vel = player.getVelocity().multiply(1 / Portal2Movement.MOVE_SCALAR);

        Vec3d vel2d = vel.add(0, -vel.y, 0);

        return new String[]{
                String.format(Locale.ROOT, "vel: %.2f %.2f", vel2d.length(), vel.y)
        };
    }
}
